package org.cbio.gdcpipeline.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dixit Patel
 */
public class ManifestFileData {
    private String id;
    private String filename;
    private String md5;
    private String size;
    private String state;

    public List<String> getHeader(){
        List<String> header = new ArrayList<>();
        header.add("Id");
        header.add("Filename");
        header.add("Md5");
        header.add("Size");
        header.add("State");
        return header;
    }
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
