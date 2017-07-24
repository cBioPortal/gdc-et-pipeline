package org.cbio.gdcpipeline.util;

import org.apache.commons.collections.map.MultiKeyMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Dixit Patel
 */
public class MutationDataFileUtils {
    public static String METADATA_PREFIX = "#";
    public static String DELIMITER = "\t";

    public static MultiKeyMap loadDataFileMetadata(File dataFile) throws IOException {
        String[] columnNames;
        int metadataCount = 0;

        // get the file header and header count
        try (FileReader reader = new FileReader(dataFile)) {
            BufferedReader buff = new BufferedReader(reader);
            String line = buff.readLine();

            // keep reading until line does not start with meta data prefix
            while (line.startsWith(MutationDataFileUtils.METADATA_PREFIX)) {
                metadataCount++;
                line = buff.readLine();
            }
            // extract the file header
            columnNames = MutationDataFileUtils.splitDataFields(line);
            reader.close();
        }
        MultiKeyMap metadata = new MultiKeyMap();
        metadata.put(dataFile.getName(), "header", columnNames);
        metadata.put(dataFile.getName(), "metadataCount", metadataCount);

        return metadata;
    }

    public static String[] splitDataFields(String line) {
        line = line.replaceAll("^" + METADATA_PREFIX + "+", "");
        String[] fields = line.split(DELIMITER, -1);
        return fields;
    }
}
