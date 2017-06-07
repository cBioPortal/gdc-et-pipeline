package org.cbio.gdcpipeline.reader;

import org.cbio.gdcpipeline.model.gdc.clinical.GDCClinicalDataModel;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomStaxEventReader implements ItemReader<GDCClinicalDataModel> {
    @Override
    public GDCClinicalDataModel read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }
}
