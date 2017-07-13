package org.cbio.gdcpipeline.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Map;

/**
 * Created by Dixit on 08/07/17.
 */
public class MetaFileWriter {

    private static String header;

    private static Log LOG = LogFactory.getLog(MetaFileWriter.class);

    public static void makeHeaders(Map<String, String> headers) {
        if (!headers.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append("\n").append(entry.getKey()).append(": ").append(entry.getValue());
            }
            //remove first line break
            sb.delete(0, 1);
            header = sb.toString();
        }

    }

    public static void writeHeaders(Map<String, String> headers, String file_path) throws Exception {

        makeHeaders(headers);

        File filename = new File(file_path);
        if (header.isEmpty() || header == null) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Meta file Headers are not set");
            }
            throw new Exception();
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "utf-8"))) {
            writer.write(header);
        }

    }
}
