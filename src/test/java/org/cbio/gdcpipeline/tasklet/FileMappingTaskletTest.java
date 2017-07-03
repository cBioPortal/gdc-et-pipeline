package org.cbio.gdcpipeline.tasklet;

import com.google.gson.Gson;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Dixit on 26/06/17.
 */

@PrepareForTest({FileMappingTasklet.class, LogFactory.class})
//@RunWith(PowerMockRunner.class)
public class FileMappingTaskletTest {

    File sourceDir;
    String cancer_study_id;
    String GDC_API_ENDPOINT;
    int MAX_RESPONSE_SIZE;
    HashMap<String, List<String>> barcodeToSamplesMap = new HashMap<>();

    @Mock
    StepContribution stepContext;
    @Mock
    ChunkContext chunkContext;
    @Mock
    RestTemplate restTemplate;

    private FileMappingTasklet tasklet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setProperties();
        tasklet = new FileMappingTasklet();
    }

    public void setProperties() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties p = new Properties();

        File file = new File(classLoader.getResource("data").getFile());
        sourceDir = new File(file.getAbsolutePath() + File.separator + "GDC");

        p.load(new FileReader(new File(classLoader.getResource("application.properties").getFile())));

        cancer_study_id = p.getProperty("test.cancer.study.id");
        GDC_API_ENDPOINT = p.getProperty("gdc.api.endpoint");
        MAX_RESPONSE_SIZE = Integer.parseInt(p.getProperty("gdc.max.response.size"));
    }


    //@Test(expected = java.lang.Exception.class)
    public void testExecuteEmptyBiospecimenFileList() throws Exception {
        ReflectionTestUtils.setField(tasklet, "sourceDir", sourceDir.getAbsolutePath());
        ReflectionTestUtils.setField(tasklet, "cancer_study_id", cancer_study_id);

        File empty = new File(sourceDir.getAbsolutePath());
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(empty);

        tasklet.execute(stepContext, chunkContext);

    }

    @Test//(expected = java.lang.Exception.class)
    public void testCallGdcApiNullResponse() throws Exception {

        ReflectionTestUtils.setField(tasklet, "GDC_API_ENDPOINT", GDC_API_ENDPOINT);
        ReflectionTestUtils.setField(tasklet, "MAX_RESPONSE_SIZE", MAX_RESPONSE_SIZE);
        HashMap<String, List<String>> uuidToFilesMap = new HashMap<>();
        uuidToFilesMap.put("sample", new ArrayList<>());
        ReflectionTestUtils.setField(tasklet, "uuidToFilesMap", uuidToFilesMap);
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

        RestTemplate restTemplate = mock(RestTemplate.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>("", httpHeaders);


        when(restTemplate.exchange(GDC_API_ENDPOINT, HttpMethod.POST, entity, String.class)).thenThrow(new IOException());


        System.out.print("ahdladlaskdasdmasdasd");

        tasklet.callGdcApi(GDC_API_ENDPOINT, "");




    }

    //@Test
    public void testBuildJsonRequestIsValidJson() {
        Gson gson = new Gson();
        HashMap<String, List<String>> uuidToFilesMap = new HashMap<>();
        uuidToFilesMap.put("sample_case_id", new ArrayList<>());
        ReflectionTestUtils.setField(tasklet, "uuidToFilesMap", uuidToFilesMap);

        String expectedPayload = "{\"filters\":{\"op\":\"in\",\"content\":{\"field\":\"cases.case_id\",\"value\":[\"sample_case_id\"]}}," +
                "\"format\":\"JSON\",\"fields\":\"file_name,cases.case_id,data_format\"}";

        String actualPayload = tasklet.buildJsonRequest();

        assertEquals(expectedPayload, actualPayload);


    }

    // @Test
    public void testGdcApiServiceUnavailable() throws Exception {

//        HashMap<String, List<String>> uuidToFilesMap = new HashMap<>();
//        PowerMockito.mockStatic(LogFactory.class);
//        Log LOG = PowerMockito.mock(Log.class);
//        //when(LogFactory.getLog(any(Class.class))).thenReturn(LOG);
//        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        //FileMappingTasklet tasklet = PowerMockito.mock(FileMappingTasklet.class);
//        //RestTemplate restTemplate = PowerMockito.mock( RestTemplate.class);
//
//        ReflectionTestUtils.setField(tasklet, "GDC_API_ENDPOINT", GDC_API_ENDPOINT);
//        ReflectionTestUtils.setField(tasklet, "MAX_RESPONSE_SIZE", MAX_RESPONSE_SIZE);
//        uuidToFilesMap.put("sample_case_id", new ArrayList<>());
//        ReflectionTestUtils.setField(tasklet, "uuidToFilesMap", uuidToFilesMap);
//
//        HttpEntity<String> entity = new HttpEntity<String>("", new HttpHeaders());
//
//
//        String url = GDC_API_ENDPOINT+"?from=1&size=1";
//
//        doReturn(response).when(restTemplate).exchange(url,HttpMethod.POST, entity,String.class);
//        PowerMockito.whenNew(RestTemplate.class).withNoArguments().thenReturn(restTemplate);
//        //ReflectionTestUtils.setField(tasklet, "restTemplate", restTemplate);
//
//        //PowerMockito.when(rest,"exchange").thenReturn(response);
//
//
//        tasklet.callGdcApi(url,"");
//
//        verify(LOG).error("Error calling GDC API. Response code is : 500 Response Message is " + response.getStatusCode().getReasonPhrase());
//        RepeatStatus status = tasklet.gdcApiRequest("");
//
//
    }

}