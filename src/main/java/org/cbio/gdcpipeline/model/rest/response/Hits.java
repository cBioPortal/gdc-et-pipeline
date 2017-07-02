package org.cbio.gdcpipeline.model.rest.response;

import java.util.List;

/**
 * Created by Dixit on 21/06/17.
 */
public class Hits {
    private String file_name;
    private List<Case> cases;
    private String data_format;

    public String getData_format() {
        return data_format;
    }

    public void setData_format(String data_format) {
        this.data_format = data_format;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }
}
