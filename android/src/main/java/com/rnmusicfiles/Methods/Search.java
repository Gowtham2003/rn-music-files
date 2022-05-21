package com.rnmusicfiles.Methods;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.rnmusicfiles.C;
import com.rnmusicfiles.Models.Options.SearchOptions;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Objects;

import static com.rnmusicfiles.Utils.GeneralUtils.LOG;
import static com.rnmusicfiles.Utils.OrderByGenerator.generateSortOrder;

public class Search {
    public static WritableMap searchDB(SearchOptions options, ContentResolver contentResolver) throws Exception {

        WritableArray jsonArray = new WritableNativeArray();
        String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID};

        String searchParam = "%" + options.searchParam + "%";

        Log.d(LOG, searchParam);

        String Selection = MediaStore.Audio.Albums.ARTIST + " Like ? OR " + MediaStore.Audio.Albums.ALBUM
                + " Like ? OR " + MediaStore.Audio.Media.TITLE + " Like ? OR " + MediaStore.Audio.Media.DATA
                + " Like ?";
        String orderBy = null;
        if (options.sortBy != null) {
            orderBy = generateSortOrder(options.sortBy, options.sortOrder);
        }

        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, Selection, new String[]{searchParam, searchParam, searchParam, searchParam}, orderBy);

        int cursorCount = Objects.requireNonNull(cursor).getCount();

        if (cursorCount > (options.batchSize * options.batchNumber)) {
            cursor.moveToPosition(options.batchSize * options.batchNumber);
            do {
                WritableMap item = new WritableNativeMap();
                item.putString("title", String.valueOf(cursor.getString(0)));
                item.putString("artist", String.valueOf(cursor.getString(1)));
                item.putString("album", String.valueOf(cursor.getString(2)));
                item.putString("duration", String.valueOf(cursor.getString(3)));
                item.putString("path", String.valueOf(cursor.getString(4)));
                item.putString("id", String.valueOf(cursor.getString(5)));
                jsonArray.pushMap(item);
            } while ((options.batchSize == 0 || cursor.getPosition() + 1 < options.batchSize * (options.batchNumber + 1)) & cursor.moveToNext());
        } else {
            if (cursor != null) {
                cursor.close();
            }
            if (cursorCount == 0) {
                throw new Exception(C.ErrorEnum.EMPTY_CURSOR.toString());
            }
            String msg = "cursor is either null or empty ";
            Log.e(LOG, msg);
            throw new Exception(C.ErrorEnum.INDEX_OUT_OF_BOUND.toString());
        }
        cursor.close();
        WritableMap results = new WritableNativeMap();
        results.putInt("length", cursorCount);
        results.putArray("results", jsonArray);
        return results;
    }
}
