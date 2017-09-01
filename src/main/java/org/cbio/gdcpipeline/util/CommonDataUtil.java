package org.cbio.gdcpipeline.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.rest.response.Hits;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dixit Patel
 */
public class CommonDataUtil {
    public static final String NORMAL_SAMPLE_SUFFIX = "-10";
    private static Log LOG = LogFactory.getLog(CommonDataUtil.class);
    public enum CLINICAL_TYPE{PATIENT,SAMPLE}
    public enum CLINICAL_OS_STATUS {LIVING, DECEASED}

    public enum GDC_DATAFORMAT {
        BCR_XML("BCR XML"),
        MAF("MAF");

        private final String format;
        private GDC_DATAFORMAT(String format){
            this.format=format;
        }

        @Override
        public String toString(){
            return this.format;
        }
    }

    public enum GDC_TYPE{
        BIOSPECIMEN("biospecimen_supplement"),
        CLINICAL("clinical_supplement"),
        MUTATION("masked_somatic_mutation"),;

        private final String type;

        GDC_TYPE(String type){
            this.type=type;
        }

        @Override
        public String toString(){
            return this.type;
        }
    }

    public static List<File> getFileList(List<Hits> gdcFileMetadatas, CommonDataUtil.GDC_TYPE type, String sourceDir) {
        List<File> fileList = new ArrayList<>();
        if (!gdcFileMetadatas.isEmpty()) {
            for (Hits data : gdcFileMetadatas) {
                if (data.getType().equals(type.toString())) {
                    File file = new File(sourceDir, data.getFile_name());
                    if (file.exists()) {
                        fileList.add(file);
                    } else {
                        if (LOG.isInfoEnabled()) {
                            LOG.info(type.toString() + " file : " + file.getAbsolutePath() + " not found.\nSkipping File");
                        }
                    }
                }
            }
        }
        return fileList;
    }
}
