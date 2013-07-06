package de.tudarmstadt.ukp.dkpro.tc.experiments.regression;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createDescription;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.opennlp.OpenNlpPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask.ExecutionPolicy;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.TaskBase;
import de.tudarmstadt.ukp.dkpro.tc.core.extractor.SingleLabelInstanceExtractorPair;
import de.tudarmstadt.ukp.dkpro.tc.core.task.ExtractFeaturesTask;
import de.tudarmstadt.ukp.dkpro.tc.core.task.MetaInfoTask;
import de.tudarmstadt.ukp.dkpro.tc.core.task.PreprocessTask;
import de.tudarmstadt.ukp.dkpro.tc.experiments.regression.io.STSReader;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.CVBatchReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.report.CVRegressionReport;
import de.tudarmstadt.ukp.dkpro.tc.weka.task.CrossValidationTask;
import de.tudarmstadt.ukp.dkpro.tc.weka.writer.WekaDataWriter;

public class RegressionExperiment
{

    private static final String nameCV = "-RegressionExampleCV";

    static String jsonPath;
    static JSONObject json;

    public static String languageCode;
    public static String inputFile;
    public static String goldFile;

    public static void main(String[] args)
        throws Exception
    {

        jsonPath = FileUtils.readFileToString(new File("src/main/resources/config/train.json"));
        json = (JSONObject) JSONSerializer.toJSON(jsonPath);

        languageCode = json.getString("languageCode");
        inputFile = json.getString("inputFile");
        goldFile = json.getString("goldFile");

        runCrossValidation(ParameterSpaceParser.createParamSpaceFromJson(json));

        // runTrainTest(ParameterSpaceParser.createParamSpaceFromJson(json));
    }

    // ##### CV #####
    private static void runCrossValidation(ParameterSpace pSpace)
        throws Exception
    {
        PreprocessTask preprocessTask = new PreprocessTask();
        preprocessTask.setReader(getReaderDesc(inputFile, goldFile));
        preprocessTask.setAggregate(getPreprocessing());
        preprocessTask.setType(preprocessTask.getType() + "-RegressionExampleCV");

        // get some meta data depending on the whole document collection that we need for training
        MetaInfoTask metaTask = new MetaInfoTask();
        metaTask.setType(metaTask.getType() + nameCV);
        metaTask.addImportLatest(MetaInfoTask.INPUT_KEY, PreprocessTask.OUTPUT_KEY,
                preprocessTask.getType());

        // Define the base task which generates an arff instances file
        ExtractFeaturesTask trainTask = new ExtractFeaturesTask();
        trainTask.setInstanceExtractor(SingleLabelInstanceExtractorPair.class);
        trainTask.setDataWriter(WekaDataWriter.class.getName());
        trainTask.setRegressionExperiment(true);
        trainTask.setType(trainTask.getType() + nameCV);
        trainTask.addImportLatest(MetaInfoTask.INPUT_KEY, PreprocessTask.OUTPUT_KEY,
                preprocessTask.getType());
        trainTask.addImportLatest(MetaInfoTask.META_KEY, MetaInfoTask.META_KEY, metaTask.getType());

        // Define the cross-validation task which operates on the results of the the train task
        TaskBase cvTask = new CrossValidationTask();
        cvTask.setType(cvTask.getType() + nameCV);
        cvTask.addImportLatest(MetaInfoTask.META_KEY, MetaInfoTask.META_KEY, metaTask.getType());
        cvTask.addImportLatest(CrossValidationTask.INPUT_KEY, ExtractFeaturesTask.OUTPUT_KEY,
                trainTask.getType());
        cvTask.addReport(CVRegressionReport.class);

        // Define the overall task scenario
        BatchTask batch = new BatchTask();
        batch.setType("Evaluation" + nameCV);
        batch.setParameterSpace(pSpace);
        batch.addTask(preprocessTask);
        batch.addTask(metaTask);
        batch.addTask(trainTask);
        batch.addTask(cvTask);
        batch.setExecutionPolicy(ExecutionPolicy.RUN_AGAIN);
        batch.addReport(CVBatchReport.class);

        // Run
        Lab.getInstance().run(batch);
    }

    private static CollectionReaderDescription getReaderDesc(String inputFile, String goldFile)
        throws ResourceInitializationException, IOException
    {

        return createDescription(STSReader.class, STSReader.PARAM_INPUT_FILE, inputFile,
                STSReader.PARAM_GOLD_FILE, goldFile);
    }

    public static AnalysisEngineDescription getPreprocessing()
        throws ResourceInitializationException
    {

        return createAggregateDescription(
                createPrimitiveDescription(BreakIteratorSegmenter.class),
                createPrimitiveDescription(OpenNlpPosTagger.class,
                        OpenNlpPosTagger.PARAM_LANGUAGE, languageCode));
    }
}