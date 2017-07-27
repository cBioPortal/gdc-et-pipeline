package org.cbio.gdcpipeline.util;

/**
 * @author Dixit Patel
 */
public class CommonDataUtil {
    public static final String NORMAL_SAMPLE_SUFFIX= "-10";

    public enum CLINICAL_TYPE{PATIENT,SAMPLE}
    public enum CLINICAL_OS_STATUS {LIVING, DECEASED}

    public enum STEP {
        ALL, CLINICAL
    }

    public enum GDC_DATAFORMAT{
        BCR_XML("BCR XML");

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
        CLINICAL("clinical_supplement");

        private final String type;

        private GDC_TYPE(String type){
            this.type=type;
        }

        @Override
        public String toString(){
            return this.type;
        }
    }
}
