package org.cbio.gdcpipeline.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dixit on 25/06/17.
 */
public class ClinicalLineAggregator<T> extends ExtractorLineAggregator<T> {

    private String delimiter = "\t";
    private static Log LOG = LogFactory.getLog(ClinicalLineAggregator.class);

    public ClinicalLineAggregator() {
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String doAggregate(Object[] fields) {

        // exactly one ArrayList Type (i.e SampleList) is expected in fields object

        int key = -1;
        List<String> sampleList = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            Object obj = fields[i];
            if (obj instanceof ArrayList) {
                key = i;
                sampleList = (List<String>) obj;
                break;
            }
        }

        if (key == -1) {
            //no list type found. assume empty
            return StringUtils.arrayToDelimitedString(fields, this.delimiter);
        }
        if (sampleList.isEmpty()) {
            //empty list; after processor cleanup
            Object[] sample = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                if (i == key) {
                    sample[i] = "";
                } else {
                    sample[i] = fields[i];
                }
            }
            return StringUtils.arrayToDelimitedString(sample, this.delimiter);
        }

        int numberOfLines = sampleList.size();
        Object[][] newSampleFields = new Object[numberOfLines][fields.length];

        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (j == key) {
                    newSampleFields[i][j] = sampleList.remove(0);
                } else {
                    newSampleFields[i][j] = fields[j];
                }
            }
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < numberOfLines; i++) {
            Object[] sample = newSampleFields[i];
            if (i == 0) {
                output.append(StringUtils.arrayToDelimitedString(sample, this.delimiter));
            } else {
                output.append(System.lineSeparator());
                output.append(StringUtils.arrayToDelimitedString(sample, this.delimiter));
            }
        }

        return output.toString();

    }
}
