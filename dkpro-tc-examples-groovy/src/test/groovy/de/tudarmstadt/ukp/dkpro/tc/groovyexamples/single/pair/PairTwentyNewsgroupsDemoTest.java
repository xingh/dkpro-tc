/**
 * Copyright 2014
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package de.tudarmstadt.ukp.dkpro.tc.groovyexamples.single.pair;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import de.tudarmstadt.ukp.dkpro.tc.groovyexamples.single.pair.PairTwentyNewsgroupsDemo;

/** 
 * This test only checks to see if the experiment runs without exceptions.
 * 
 * @author jamison
 *
 */

public class PairTwentyNewsgroupsDemoTest
{
    PairTwentyNewsgroupsDemo groovyExperiment;

    @Before
    public void setup()
        throws Exception
    {
        String path = "target/repository/" + getClass().getSimpleName() + "/"
                + name.getMethodName();
        System.setProperty("DKPRO_HOME", new File(path).getAbsolutePath());

        groovyExperiment = new PairTwentyNewsgroupsDemo();
    }

    @Test
    public void testGroovyTrainTest()
        throws Exception
    {
        // Groovy setup with automatic task wiring
        groovyExperiment.runTrainTest();
    }



    @Rule
    public TestName name = new TestName();

    @Before
    public void printSeparator()
    {
        System.out.println("\n=== " + name.getMethodName() + " =====================");
    }
}
