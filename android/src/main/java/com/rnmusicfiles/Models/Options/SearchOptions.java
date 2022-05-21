package com.rnmusicfiles.Models.Options;

import com.rnmusicfiles.C;
import com.facebook.react.bridge.ReadableMap;

public class SearchOptions {

    public String searchParam;
    public int batchNumber;
    public int batchSize;
    public C.SortBy sortBy;
    public C.SortOrder sortOrder;

    public SearchOptions(ReadableMap options) {
        this.searchParam = options.hasKey("searchParam") ? options.getString("searchParam") : null;
        this.batchSize = options.hasKey("batchSize") ? options.getInt("batchSize") : 0;
        this.batchNumber = options.hasKey("batchNumber") ? options.getInt("batchNumber") : 0;
        this.sortBy = options.hasKey("sortBy") ? C.SortBy.valueOf(options.getString("sortBy")) : null;
        this.sortOrder = options.hasKey("sortOrder") ? C.SortOrder.valueOf(options.getString("sortOrder")) : C.SortOrder.ASC;
    }

}
