package org.cbio.gdcpipeline.model.rest.response;

import java.util.List;

/**
 * Created by Dixit on 21/06/17.
 */
public class Data {

    private List<Hits> hits;
    private Pagination pagination;

    public List<Hits> getHits() {
        return hits;
    }

    public void setHits(List<Hits> hits) {
        this.hits = hits;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
