package org.cbio.gdcpipeline.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dixit on 26/06/17.
 */

@PrepareForTest(ClinicalLineAggregator.class)
@RunWith(PowerMockRunner.class)
public class ClinicalLineAggregatorTest {

    Object[] fields;
    ArrayList<String> sampleList = new ArrayList<>();
    ClinicalLineAggregator<Object> clinicalLineAggregator;

    @Before
    public void setUp() throws Exception {

        // there should be exactly one ArrayList Type object in fields

        fields = new Object[4];
        fields[0] = "string 1";
        fields[1] = 5;
        fields[2] = sampleList;
        fields[3] = "string 2";

        clinicalLineAggregator = new ClinicalLineAggregator<>();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDoAggregateNoArrayListTypeFound() throws Exception {

        Object[] test = fields.clone();
        test[2] = "";
        String expectedResponse = StringUtils.arrayToDelimitedString(test, "\t");

        String actualResponse = clinicalLineAggregator.doAggregate(fields);

        assertEquals(expectedResponse, actualResponse);


    }

    @Test
    public void testDoAggregateEmptySampleListFound() throws Exception {

        String expectedResponse = fields[0] + "\t" + fields[1] + "\t" + "" + "\t" + fields[3];

        String actualResponse = clinicalLineAggregator.doAggregate(fields);

        assertEquals(expectedResponse, actualResponse);


    }

    @Test
    public void testDoAggregateOneSampleInsideSampleListFound() throws Exception {

        Object[] test = fields.clone();
        sampleList.add("sample_1");
        test[2] = sampleList;

        String expectedResponse = fields[0] + "\t" + fields[1] + "\t" + sampleList.get(0) + "\t" + fields[3];

        String actualResponse = clinicalLineAggregator.doAggregate(test);

        assertEquals(expectedResponse, actualResponse);


    }

    @Test
    public void testDoAggregateManySampleInsideSampleListFound() throws Exception {

        Object[] test = fields.clone();
        sampleList.add("sample_1");
        sampleList.add("sample_2");
        sampleList.add("sample_3");
        test[2] = sampleList;

        String expectedResponse =
                fields[0] + "\t" + fields[1] + "\t" + sampleList.get(0) + "\t" + fields[3] + "\n" +
                        fields[0] + "\t" + fields[1] + "\t" + sampleList.get(1) + "\t" + fields[3] + "\n" +
                        fields[0] + "\t" + fields[1] + "\t" + sampleList.get(2) + "\t" + fields[3];

        String actualResponse = clinicalLineAggregator.doAggregate(test);

        assertEquals(expectedResponse, actualResponse);


    }
}