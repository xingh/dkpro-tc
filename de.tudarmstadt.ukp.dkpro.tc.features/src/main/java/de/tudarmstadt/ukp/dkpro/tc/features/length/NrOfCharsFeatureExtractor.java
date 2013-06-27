package de.tudarmstadt.ukp.dkpro.tc.features.length;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.tc.api.features.Feature;
import de.tudarmstadt.ukp.dkpro.tc.api.features.FeatureExtractor;
import de.tudarmstadt.ukp.dkpro.tc.exception.TextClassificationException;

//TODO: adapt for specified focus annotation
public class NrOfCharsFeatureExtractor
    implements FeatureExtractor
{

    public static final String FN_NR_OF_CHARS = "NrofChars";
    public static final String FN_NR_OF_CHARS_PER_SENTENCE = "NrofCharsPerSentence";
    public static final String FN_NR_OF_CHARS_PER_TOKEN = "NrofCharsPerToken";

    @Override
    public List<Feature> extract(JCas jcas, Annotation focusAnnotation)
        throws TextClassificationException
    {
        int nrOfChars = jcas.getDocumentText().length();
        int nrOfSentences = JCasUtil.select(jcas, Sentence.class).size();
        int nrOfTokens = JCasUtil.select(jcas, Token.class).size();
        
        List<Feature> featList = new ArrayList<Feature>();
        featList.addAll(Arrays.asList(new Feature(FN_NR_OF_CHARS, nrOfChars)));

        double charPerSentence = 0.0;
        if (nrOfSentences > 0) {
            charPerSentence = (double) nrOfChars / nrOfSentences;
        }
        featList.addAll(Arrays.asList(new Feature(FN_NR_OF_CHARS_PER_SENTENCE, charPerSentence)));

        double charPerToken = 0.0;
        if (nrOfTokens > 0) {
            charPerToken = (double) nrOfChars / nrOfTokens;
        }
        featList.addAll(Arrays.asList(new Feature(FN_NR_OF_CHARS_PER_TOKEN, charPerToken)));
        
        return featList;
    }
}