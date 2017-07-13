package org.cbio.gdcpipeline.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 13/07/17.
 */
public class ClinicalDataSourceImpl implements ClinicalDataSource {

    private Map<String, String> clinicalFieldMap = new HashMap<>();

    public enum OS_STATUS {LIVING, DECEASED}

    ;

    public ClinicalDataSourceImpl() {
        addAcceptedClinicalFieldValue("alive", OS_STATUS.LIVING.toString());
        addAcceptedClinicalFieldValue("dead", OS_STATUS.DECEASED.toString());
    }

    private void addAcceptedClinicalFieldValue(String key, String value) {
        this.clinicalFieldMap.put(key.toLowerCase(), value);
    }

    @Override
    public List<Map<String, String>> getClinicalData() {
        return null;
    }

    @Override
    public List<String> getSampleHeader() {
        return null;
    }

    @Override
    public List<String> getPatientHeader() {
        return null;
    }

    @Override
    public List<String> getTimelineHeader() {
        return null;
    }

    @Override
    public List<Map<String, String>> getTimelineData() {
        return null;
    }

    @Override
    public String getNextClinicalStudyId() {
        return null;
    }

    @Override
    public String getNextTimelineStudyId() {
        return null;
    }

    @Override
    public boolean hasMoreTimelineData() {
        return false;
    }

    @Override
    public boolean hasMoreClinicalData() {
        return false;
    }

    @Override
    public Map<String, List<String>> getFullPatientHeader(Map<String, List<String>> fullHeader) {
        return null;
    }

    @Override
    public Map<String, List<String>> getFullSampleHeader(Map<String, List<String>> fullHeader) {
        return null;
    }

    @Override
    public String getAcceptableClinicalFieldValue(String key) {
        if (!key.isEmpty()) {
            return this.clinicalFieldMap.get(key.toLowerCase());
        }
        return null;
    }
}
