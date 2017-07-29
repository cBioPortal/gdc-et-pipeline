package org.cbio.gdcpipeline.tasklet;

import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Dixit on 26/06/17.
 */

@PrepareForTest({BiospecimenXmlDataTasklet.class, LogFactory.class})
@RunWith(PowerMockRunner.class)
public class BiospecimenXmlDataTaskletTest {
    private File sourceDir;
    private String cancer_study_id;
    private String GDC_API_ENDPOINT;
    private int MAX_RESPONSE_SIZE;

    @Mock
    StepContribution stepContext;
    @Mock
    ChunkContext chunkContext;
    @Mock
    RestTemplate restTemplate;

    private BiospecimenXmlDataTasklet tasklet;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        setProperties();
        tasklet = new BiospecimenXmlDataTasklet();
    }

    public void setProperties() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Properties p = new Properties();
        File file = new File(classLoader.getResource("data").getFile());
        sourceDir = new File(file.getAbsolutePath() + File.separator + "GDC");
        p.load(new FileReader(new File(classLoader.getResource("application.properties").getFile())));
        cancer_study_id = p.getProperty("test.cancer.study.id");
        GDC_API_ENDPOINT = p.getProperty("gdc.api.files.endpoint");
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

}