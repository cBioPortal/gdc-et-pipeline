package org.cbio.gdcpipeline.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Dixit on 24/06/17.
 */
@Component
public class ClinicalPatientFileHeaders {

    @Value("#{'${clinical.patient.header.row1}'.split(',')}")
    private String[] row1;
    @Value("#{'${clinical.patient.header.row2}'.split(',')}")
    private String[] row2;
    @Value("#{'${clinical.patient.header.row3}'.split(',')}")
    private String[] row3;
    @Value("#{'${clinical.patient.header.row4}'.split(',')}")
    private String[] row4;
    @Value("#{'${clinical.patient.header.row5}'.split(',')}")
    private String[] row5;

    public String[] getRow1() {
        return row1;
    }

    public void setRow1(String[] row1) {
        this.row1 = row1;
    }

    public String[] getRow2() {
        return row2;
    }

    public void setRow2(String[] row2) {
        this.row2 = row2;
    }

    public String[] getRow3() {
        return row3;
    }

    public void setRow3(String[] row3) {
        this.row3 = row3;
    }

    public String[] getRow4() {
        return row4;
    }

    public void setRow4(String[] row4) {
        this.row4 = row4;
    }

    public String[] getRow5() {
        return row5;
    }

    public void setRow5(String[] row5) {
        this.row5 = row5;
    }
}
