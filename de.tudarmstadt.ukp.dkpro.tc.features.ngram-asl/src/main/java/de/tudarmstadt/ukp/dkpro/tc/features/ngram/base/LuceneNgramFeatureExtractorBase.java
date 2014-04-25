package de.tudarmstadt.ukp.dkpro.tc.features.ngram.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import com.google.common.collect.MinMaxPriorityQueue;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.util.FrequencyDistribution;
import de.tudarmstadt.ukp.dkpro.tc.api.features.meta.MetaCollector;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.meta.LuceneNGramMetaCollector;
import de.tudarmstadt.ukp.dkpro.tc.features.ngram.util.TermFreqTuple;

public abstract class LuceneNgramFeatureExtractorBase
    extends NGramFeatureExtractorBase
{
    public static final String PARAM_LUCENE_DIR = "luceneDir";
    @ConfigurationParameter(name = PARAM_LUCENE_DIR, mandatory = true)
    protected File luceneDir;

    public static final String LUCENE_NGRAM_FIELD = "ngram";

    @Override
    public List<Class<? extends MetaCollector>> getMetaCollectorClasses()
    {
        List<Class<? extends MetaCollector>> metaCollectorClasses = new ArrayList<Class<? extends MetaCollector>>();
        metaCollectorClasses.add(LuceneNGramMetaCollector.class);

        return metaCollectorClasses;
    }

    @Override
    protected String getFieldName()
    {
        return LUCENE_NGRAM_FIELD;
    }

    @Override
    protected String getFeaturePrefix()
    {
        return "ngram";
    }

    @Override
    protected int getTopN()
    {
        return ngramUseTopK;
    }

    @Override
    protected FrequencyDistribution<String> getTopNgrams()
        throws ResourceInitializationException
    {

        FrequencyDistribution<String> topNGrams = new FrequencyDistribution<String>();

        MinMaxPriorityQueue<TermFreqTuple> topN = MinMaxPriorityQueue.maximumSize(getTopN())
                .create();

        IndexReader reader;
        try {
            reader = DirectoryReader.open(FSDirectory.open(luceneDir));
            Fields fields = MultiFields.getFields(reader);
            if (fields != null) {
                Terms terms = fields.terms(getFieldName());
                if (terms != null) {
                    TermsEnum termsEnum = terms.iterator(null);
                    BytesRef text = null;
                    while ((text = termsEnum.next()) != null) {
                        String term = text.utf8ToString();
                        long freq = termsEnum.totalTermFreq();
                        topN.add(new TermFreqTuple(term, freq));
                    }
                }
            }
        }
        catch (Exception e) {
            throw new ResourceInitializationException(e);
        }

        int size = topN.size();
        for (int i = 0; i < size; i++) {
            TermFreqTuple tuple = topN.poll();
            topNGrams.addSample(tuple.getTerm(), tuple.getFreq());
        }

        getLogger().log(Level.INFO, "+++ TAKING " + topNGrams.getB() + " NGRAMS");

        return topNGrams;
    }
}