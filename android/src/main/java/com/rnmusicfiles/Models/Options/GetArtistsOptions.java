package com.rnmusicfiles.Models.Options;

import com.rnmusicfiles.C;
import com.facebook.react.bridge.ReadableMap;

public class GetArtistsOptions {
    public int batchSize;
    public int batchNumber;
    public C.SortBy sortBy;
    public C.SortOrder sortOrder;

    public GetArtistsOptions(ReadableMap options) {
        this.batchSize = options.hasKey("batchSize") ? options.getInt("batchSize") : 0;
        this.batchNumber = options.hasKey("batchNumber") ? options.getInt("batchNumber") : 0;
        this.sortBy = options.hasKey("sortBy") ? C.SortBy.valueOf(options.getString("sortBy")) : null;
        this.sortOrder = options.hasKey("sortOrder") ? C.SortOrder.valueOf(options.getString("sortOrder")) : C.SortOrder.ASC;
    }
}
