package org.cbio.gdcpipeline.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbio.gdcpipeline.model.rest.response.GdcFileMetadata;
import org.cbio.gdcpipeline.reader.ClinicalReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dixit Patel
 */
public class CommonDataUtil {
    public static final String NORMAL_SAMPLE_SUFFIX = "-10";
    public static List<String> missingValueList = initMissingValueList();
    private static Log LOG = LogFactory.getLog(CommonDataUtil.class);

    private static List<String> initMissingValueList() {
        List<String> missingValueList = new ArrayList<>();
        missingValueList.add("NA");
        missingValueList.add("N/A");
        missingValueList.add("N/a");
        missingValueList.add("n/A");
        missingValueList.add("Unknown");
        missingValueList.add("not available");
        return missingValueList;
    }

    public static boolean hasMissingKeys(String check) {
        for (String ignore : missingValueList) {
            if (check.equalsIgnoreCase(ignore)) {
                return true;
            }
        }
        return false;
    }

    public enum CLINICAL_TYPE {PATIENT, SAMPLE}

    public enum CLINICAL_OS_STATUS {LIVING, DECEASED}

    public enum STEP {
        ALL, CLINICAL, MUTATION
    }

    public enum GDC_DATAFORMAT {
        BCR_XML("BCR XML"),
        MAF("MAF");

        private final String format;

        GDC_DATAFORMAT(String format) {
            this.format = format;
        }

        @Override
        public String toString() {
            return this.format;
        }
    }

    public enum GDC_TYPE {
        BIOSPECIMEN("biospecimen_supplement"),
        CLINICAL("clinical_supplement"),
        MUTATION("masked_somatic_mutation"),;

        private final String type;

        GDC_TYPE(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static List<File> getFileList(List<GdcFileMetadata> gdcFileMetadatas, CommonDataUtil.GDC_TYPE type, String sourceDir) {
        List<File> fileList = new ArrayList<>();
        if (!gdcFileMetadatas.isEmpty()) {
            for (GdcFileMetadata data : gdcFileMetadatas) {
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
