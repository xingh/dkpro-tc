/*******************************************************************************
 * Copyright 2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.tc.api.features.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils;

/**
 * Utils for feature extractors
 */
public class FeatureUtil
{
    /**
     * Escapes the names, as Weka does not seem to like special characters in attribute names.
     * @param name
     * @return
     */
    public static String escapeFeatureName(String name) {
        
        // TODO Issue 120: improve the escaping
        // the fix was necessary due to Issue 32
        // http://code.google.com/p/dkpro-tc/issues/detail?id=32
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.length(); i++) {
            String c = name.substring(i, i+1);
            if (StringUtils.isAlphanumeric(c) || c.equals("_")) {
                sb.append(c);
            }
            else {
                sb.append("u");
                sb.append(c.codePointAt(0));
            }
        }
        return sb.toString();
    }
    
    /**
     * @param inputFile Location of the file that contains the stopwords. One stopword per line.
     * @param toLowerCase Whether the stopwords should be converted to lowercase or not.
     * @return A set of stopwords.
     * @throws IOException
     */
    public static Set<String> getStopwords(String inputFile, boolean toLowerCase)
        throws IOException
    {
        Set<String> stopwords = new HashSet<String>();
        if (inputFile != null) {
            URL stopUrl = ResourceUtils.resolveLocation(inputFile, null);
            InputStream is = stopUrl.openStream();
            for (String stopword : IOUtils.readLines(is, "UTF-8")) {
                if (toLowerCase) {
                    stopwords.add(stopword.toLowerCase());
                }
                else {
                    stopwords.add(stopword);
                }
            }
        }
        
        return stopwords;
    }
}