/*******************************************************************************
 * Copyright 2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universit�t Darmstadt
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
package de.tudarmstadt.ukp.dkpro.tc.evaluation.evaluator.multi;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.tc.evaluation.evaluator.EvaluatorBase;
import de.tudarmstadt.ukp.dkpro.tc.evaluation.evaluator.EvaluatorFactory;
import de.tudarmstadt.ukp.dkpro.tc.evaluation.evaluator.EvaluatorFactory.EvaluationMode;


/**
 * @author Andriy Nadolskyy
 * 
 */
public class MultiEvaluatorTest {
	
	static File file = new File("src/test/resources/datasets/multi/id2outcome.txt");
	EvaluationMode mode = EvaluationMode.MULTI;
	boolean softEvaluation = false;
	HashMap<String, String> results;
	
	@Before
    public void setup() throws IOException
    {
		EvaluatorFactory evalFactory = new EvaluatorFactory(file, mode, softEvaluation);
		evalFactory.readDataFile();
		EvaluatorBase evaluator = evalFactory.makeEvaluator();
		results = evaluator.calculateEvaluationMeasures();
    }
	
	@Test
    public void testCalculateEvaluationMeasures() {
		for (String key : results.keySet()) {
			System.out.println(key + "\t" + results.get(key));
		}
		
		assertTrue(true);
	}
}
