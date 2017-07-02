package org.cbio.gdcpipeline.reader;

import org.cbio.gdcpipeline.model.cbio.ClinicalDataModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertNull;

/**
 * Created by Dixit on 26/06/17.
 */
public class ClinicalReaderTest {

    private ClinicalReader clinicalReader;
    @Mock
    private Resource resource;
    @Mock
    private List<ClinicalDataModel> clinicalDataModelList = new ArrayList<>();
    @Mock
    private HashMap<String, List<String>> barcodeToSamplesMap;
    @Mock
    ExecutionContext executionContext;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        clinicalReader = new ClinicalReader();

    }

    @Test
    public void testEmptyRead() throws Exception {
        assertNull(clinicalReader.read());
    }


    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void read() throws Exception {

    }

    @Test
    public void open() throws Exception {

    }

}