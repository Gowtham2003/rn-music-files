package com.rnmusicfiles.Utils;

import com.rnmusicfiles.C;

public class OrderByGenerator {
    public static String generateSortOrder(C.SortBy sortBy, C.SortOrder sortOrder) {
        return (sortBy.getValue() + " " + sortOrder.getValue());
    }
}
