package org.cbio.gdcpipeline.util;

import org.apache.commons.collections.map.MultiKeyMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Dixit
 */
public class DataFileUtils {
    public static String METADATA_PREFIX = "#";
    public static String DELIMITER = "\t";

    public static MultiKeyMap loadDataFileMetadata(File dataFile) throws IOException {
        String[] columnNames;
        int numRecords = 0;

        // get the file header and record count
        try (FileReader reader = new FileReader(dataFile)) {
            BufferedReader buff = new BufferedReader(reader);
            String line = buff.readLine();

            // keep reading until line does not start with meta data prefix
            while (line.startsWith(DataFileUtils.METADATA_PREFIX)) {
                line = buff.readLine();
            }
            // extract the file header
            columnNames = DataFileUtils.splitDataFields(line);

            // keep reading file to get count of records
            while (buff.readLine() != null) {
                numRecords++;
            }
            reader.close();
        }
        MultiKeyMap metadata = new MultiKeyMap();
        metadata.put(dataFile.getName(), "header", columnNames);
        metadata.put(dataFile.getName(), "numRecords", numRecords);

        return metadata;
    }

    public static String[] splitDataFields(String line) {
        line = line.replaceAll("^" + METADATA_PREFIX + "+", "");
        String[] fields = line.split(DELIMITER, -1);

        return fields;
    }
}
