package org.cbio.gdcpipeline.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dixit Patel
 */
public class CommonDataUtil {
    public static final String NORMAL_SAMPLE_SUFFIX = "-10";
    public static List<String> ignoreList = initIgnoreList();

    private static List<String> initIgnoreList() {
        List<String> ignoreList = new ArrayList<>();
        ignoreList.add("NA");
        ignoreList.add("N/A");
        ignoreList.add("N/a");
        ignoreList.add("n/A");
        ignoreList.add("Unknown");
        ignoreList.add("not available");
        return ignoreList;
    }

    public static boolean isIgnore(String check) {
        for (String ignore : ignoreList) {
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
}
