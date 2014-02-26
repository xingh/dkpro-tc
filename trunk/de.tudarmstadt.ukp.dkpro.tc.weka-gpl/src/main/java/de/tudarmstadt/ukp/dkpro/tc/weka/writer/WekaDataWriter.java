package de.tudarmstadt.ukp.dkpro.tc.weka.writer;

import java.io.File;

import de.tudarmstadt.ukp.dkpro.tc.api.features.FeatureStore;
import de.tudarmstadt.ukp.dkpro.tc.core.Constants;
import de.tudarmstadt.ukp.dkpro.tc.core.io.DataWriter;
import de.tudarmstadt.ukp.dkpro.tc.weka.util.WekaUtils;

/**
 * {@link DataWriter} for the Meka machine learning tool.
 * 
 * @author zesch
 * 
 */
public class WekaDataWriter
    implements DataWriter, Constants
{

    @Override
    public void write(File outputDirectory, FeatureStore featureStore, boolean useDenseInstances,
            String learningMode)
        throws Exception
    {
        boolean isRegression = learningMode.equals(Constants.LM_REGRESSION);
        WekaUtils.instanceListToArffFile(new File(outputDirectory, ARFF_FILENAME), featureStore,
                useDenseInstances, isRegression);
    }
}
