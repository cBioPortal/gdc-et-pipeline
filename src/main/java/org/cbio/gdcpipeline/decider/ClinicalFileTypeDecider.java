package org.cbio.gdcpipeline.decider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * Created by Dixit
 */
public class ClinicalFileTypeDecider implements JobExecutionDecider {
    @Value("#{jobParameters[sourceDirectory]}")
    private String sourceDir;
    @Value("#{jobParameters[study]}")
    private String cancer_study_id;

    private static Log LOG = LogFactory.getLog(ClinicalFileTypeDecider.class);

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        File clinical = new File(sourceDir);
        for (String name : clinical.list()) {
            if (name.contains("biospecimen")) {
                if (name.endsWith(".xml")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info(" Found Clinical files of XML type");
                    }
                    return new FlowExecutionStatus("XML");
                } else if (name.endsWith(".xlsx")) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error(" XLSX Clinical File type is currently unsupported. Terminating Job");
                    }
                    return new FlowExecutionStatus("XLSX");
                }
            }
        }
        if (LOG.isErrorEnabled()) {
            LOG.error(" Error in deciding Clinical File type.");
        }
        return new FlowExecutionStatus("FAIL");
    }
}
