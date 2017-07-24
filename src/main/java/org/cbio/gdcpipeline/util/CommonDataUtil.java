package org.cbio.gdcpipeline.util;

/**
 * @author Dixit Patel
 */
public class CommonDataUtil {
    public static final String NORMAL_SAMPLE_SUFFIX= "-10";

    public enum CLINICAL_TYPE{PATIENT,SAMPLE}
    public enum CLINICAL_OS_STATUS {LIVING, DECEASED}

    public enum STEP {
        ALL, CLINICAL, MUTATION
    }

    public enum GDC_DATAFORMAT{
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

        private GDC_TYPE(String type){
            this.type=type;
        }

        @Override
        public String toString(){
            return this.type;
        }
    }
}
