package org.cbio.gdcpipeline.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Dixit on 01/07/17.
 */
public interface MetadataManager {

    Map<String, List<String>> getFullHeader();

}
