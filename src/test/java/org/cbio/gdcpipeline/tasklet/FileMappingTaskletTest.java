package org.cbio.gdcpipeline.tasklet;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

/**
 * Created by Dixit on 26/06/17.
 */

@PrepareForTest(FileMappingTasklet.class)
@RunWith(PowerMockRunner.class)
public class FileMappingTaskletTest {

    File sourceDir;
    String cancer_study_id;
    String GDC_API_ENDPOINT;
    int MAX_RESPONSE_SIZE;
    HashMap<String, List<String>> barcodeToSamplesMap = new HashMap<>();
    HashMap<String, List<String>> uuidToFilesMap = new HashMap<>();
    RestTemplate restTemplate = new RestTemplate();

    @Mock
    StepContribution stepContext;
    @Mock
    ChunkContext chunkContext;

    private FileMappingTasklet tasklet;


    @Rule
    public ExpectedException thrown = ExpectedException.none();

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


    @Test(expected = java.lang.Exception.class)
    public void testExecuteEmptyBiospecimenFileList() throws Exception {
        ReflectionTestUtils.setField(tasklet, "sourceDir", sourceDir.getAbsolutePath());
        ReflectionTestUtils.setField(tasklet, "cancer_study_id", cancer_study_id);

        File empty = new File(sourceDir.getAbsolutePath());
        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(empty);

        tasklet.execute(stepContext, chunkContext);

    }

    @Test
    public void testGdcApiRequestNullResponse() throws Exception {
        ReflectionTestUtils.setField(tasklet, "GDC_API_ENDPOINT", GDC_API_ENDPOINT);
        ReflectionTestUtils.setField(tasklet, "MAX_RESPONSE_SIZE", MAX_RESPONSE_SIZE);

        FileMappingTasklet tasklet = PowerMockito.spy(new FileMappingTasklet());
        PowerMockito.when(tasklet, method(FileMappingTasklet.class, "callGdcApi", String.class, String.class))
                .withArguments(anyString(), anyString())
                .thenReturn(null);

        RepeatStatus status = tasklet.gdcApiRequest("");
        assertEquals(status, RepeatStatus.FINISHED);

    }

    @Test
    public void testBuildJsonRequestIsValidJson() {
        Gson gson = new Gson();
        uuidToFilesMap.put("sample_case_id", new ArrayList<>());
        ReflectionTestUtils.setField(tasklet, "uuidToFilesMap", uuidToFilesMap);

        String expectedPayload = "{\"filters\":{\"op\":\"in\",\"content\":{\"field\":\"cases.case_id\",\"value\":[\"sample_case_id\"]}}," +
                "\"format\":\"JSON\",\"fields\":\"file_name,cases.case_id,data_format\"}";

        String actualPayload = tasklet.buildJsonRequest();

        assertEquals(expectedPayload, actualPayload);


    }

}