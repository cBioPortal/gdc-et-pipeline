package org.cbio.gdcpipeline.decider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.rest.response.GdcFileMetadata;
import org.cbio.gdcpipeline.util.CommonDataUtil;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Dixit Patel
 */
public class ClinicalFileTypeDecider implements JobExecutionDecider {
    private static Log LOG = LogFactory.getLog(ClinicalFileTypeDecider.class);

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        List<GdcFileMetadata> gdcFileMetadatas = (List<GdcFileMetadata>) jobExecution.getExecutionContext().get("gdcFileMetadatas");
        String data_format = getClinicalFileFormat(gdcFileMetadatas);
        if (!data_format.isEmpty()) {
            if (data_format.equals(CommonDataUtil.GDC_DATAFORMAT.BCR_XML.toString())) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(" Found Clinical files of XML type");
                }
                return new FlowExecutionStatus(CommonDataUtil.GDC_DATAFORMAT.BCR_XML.toString());
            }
        }
        if (LOG.isErrorEnabled()) {
            LOG.error(" Error in deciding Clinical File type.");
        }
        return new FlowExecutionStatus("FAIL");
    }

    private String getClinicalFileFormat(List<GdcFileMetadata> gdcFileMetadatas) {
        for (GdcFileMetadata data : gdcFileMetadatas) {
            if (data.getType().equals(CommonDataUtil.GDC_TYPE.CLINICAL.toString())) {
                return data.getData_format();
            }
        }
        return "";
    }
}
