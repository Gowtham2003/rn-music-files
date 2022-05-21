package com.rnmusicfiles;

import android.provider.MediaStore;

public class C {
    public enum SortBy {
        ARTIST(MediaStore.Audio.Media.ARTIST),
        ALBUM(MediaStore.Audio.Media.ALBUM),
        TITLE(MediaStore.Audio.Media.TITLE);

        private final String sort;

        SortBy(String sort) {
            this.sort = sort;
        }

        public String getValue() {
            return sort;
        }

    }

    public enum SortOrder {
        ASC("ASC"),
        DESC("DESC");

        private final String order;

        SortOrder(String order) {
            this.order = order;
        }

        public String getValue() {
            return order;
        }
    }

    public enum ErrorEnum {
        DATABASE(0, "A database error has occurred."),
        EMPTY_CURSOR(404, "Either the cursor is null or there are no results for this query"),
        INDEX_OUT_OF_BOUND(400, "Index out of bound");

        private final int code;
        private final String description;

        ErrorEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return code + ": " + description;
        }
    }

}
