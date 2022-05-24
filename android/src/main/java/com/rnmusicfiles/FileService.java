package com.rnmusicfiles;

import static com.rnmusicfiles.Utils.GeneralUtils.LOG;
import static com.rnmusicfiles.Utils.OrderByGenerator.generateSortOrder;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.rnmusicfiles.Models.Options.GetAlbumsOptions;
import com.rnmusicfiles.Models.Options.GetAllOptions;
import com.rnmusicfiles.Models.Options.GetArtistsOptions;
import com.rnmusicfiles.Models.Options.GetSongsOptions;
import com.rnmusicfiles.Models.Options.SearchOptions;
import com.rnmusicfiles.Utils.FS;
import com.rnmusicfiles.Utils.MetaDataExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileService {

  public static WritableMap getAllSongs(GetAllOptions options, ContentResolver contentResolver,
                                        ReactApplicationContext reactContext) throws Exception {

    WritableArray jsonArray = new WritableNativeArray();
    String[] projection = new String[] { MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media._ID };

    String Selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

    if (options.minimumSongDuration > 0) {
      Selection += " AND " + MediaStore.Audio.Media.DURATION + " >= " + options.minimumSongDuration;
    }

    String orderBy = null;

    if (options.sortBy != null) {
      orderBy = generateSortOrder(options.sortBy, options.sortOrder);
    }

    Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
      projection, Selection, null, orderBy);

    int cursorCount = Objects.requireNonNull(cursor).getCount();

    if (cursorCount > (options.batchSize * options.batchNumber)) {
      cursor.moveToPosition(options.batchSize * options.batchNumber);
      do {
        String path = cursor.getString(4);

        String CoverPath = options.coverFolder;

        WritableMap item = new WritableNativeMap();
        item.putString("title", cursor.getString(0));
        item.putString("artist", cursor.getString(1));
        item.putString("album", cursor.getString(2));
        item.putString("duration", cursor.getString(3));
        item.putString("path", cursor.getString(4));
        item.putString("id", cursor.getString(5));

        if (options.cover) {

          try {
            byte[] albumImageData = MetaDataExtractor.getEmbededPicture(path);
            String coverPath = FS.saveToStorage(reactContext.getCacheDir().getAbsolutePath(),
              albumImageData);
            Log.e(LOG, "File saved");
            item.putString("cover", coverPath);
          } catch (Exception e) {
            Log.e(LOG, String.valueOf(e));
            item.putString("cover", "");
          }
        }
        jsonArray.pushMap(item);
      } while ((options.batchSize == 0
        || cursor.getPosition() + 1 < options.batchSize * (options.batchNumber + 1)) & cursor.moveToNext());
    } else {

      cursor.close();
      throw new Exception(C.ErrorEnum.EMPTY_CURSOR.toString());
    }
    cursor.close();
    WritableMap results = new WritableNativeMap();
    results.putInt("length", cursorCount);
    results.putArray("results", jsonArray);
    return results;
  }

  public static WritableMap getAlbums(GetAlbumsOptions options, ContentResolver contentResolver) throws Exception {

    WritableArray jsonArray = new WritableNativeArray();
    String[] projection = new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM,
      MediaStore.Audio.Albums.ARTIST,
      MediaStore.Audio.Albums.NUMBER_OF_SONGS,
      MediaStore.Audio.Albums.FIRST_YEAR,
      MediaStore.Audio.Albums.LAST_YEAR,
    };


    String Selection = null;
    String searchParam = null;
    if (options.artist != null) {
      searchParam = "%" + options.artist + "%";
      Selection = MediaStore.Audio.Albums.ARTIST + " Like ?";
    }

    String orderBy = null;

    if (options.sortBy != null) {
      orderBy = generateSortOrder(options.sortBy, options.sortOrder);
    }

    Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
      projection, Selection, searchParam == null ? null : new String[]{searchParam}, orderBy);

    int cursorCount = Objects.requireNonNull(cursor).getCount();


    if (cursorCount > (options.batchSize * options.batchNumber)) {
      cursor.moveToPosition(options.batchSize * options.batchNumber);
      do {
        WritableMap item = new WritableNativeMap();
        item.putString("id", String.valueOf(cursor.getString(0)));
        item.putString("album", String.valueOf(cursor.getString(1)));
        item.putString("artist", String.valueOf(cursor.getString(2)));
        item.putString("numberOfSongs", String.valueOf(cursor.getString(3)));
        item.putString("firstYear", String.valueOf(cursor.getString(4)));
        item.putString("lastYear", String.valueOf(cursor.getString(5)));

        jsonArray.pushMap(item);
      } while ((options.batchSize == 0 || cursor.getPosition() + 1 < options.batchSize * (options.batchNumber + 1)) & cursor.moveToNext());
    } else {

      cursor.close();
      throw new Exception(C.ErrorEnum.EMPTY_CURSOR.toString());
    }
    cursor.close();
    WritableMap results = new WritableNativeMap();
    results.putInt("length", cursorCount);
    results.putArray("results", jsonArray);
    return results;
  }

  public static WritableMap getArtists(GetArtistsOptions options, ContentResolver contentResolver) throws Exception {

    WritableArray jsonArray = new WritableNativeArray();
    String[] projection = new String[]{MediaStore.Audio.Artists.ARTIST,
      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};

    String orderBy = null;

    if (options.sortBy != null) {
      orderBy = generateSortOrder(options.sortBy, options.sortOrder);
    }

    Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
      projection, null, null, orderBy);

    int cursorCount = Objects.requireNonNull(cursor).getCount();


    if (cursorCount > (options.batchSize * options.batchNumber)) {
      cursor.moveToPosition(options.batchSize * options.batchNumber);
      do {
        WritableMap item = new WritableNativeMap();
        item.putString("artist", String.valueOf(cursor.getString(0)));
        item.putString("numberOfAlbums", String.valueOf(cursor.getString(1)));
        item.putString("numberOfSongs", String.valueOf(cursor.getString(2)));

        jsonArray.pushMap(item);
      } while ((options.batchSize == 0 || cursor.getPosition() + 1 < options.batchSize * (options.batchNumber + 1)) & cursor.moveToNext());
    } else {

      cursor.close();
      throw new Exception(C.ErrorEnum.EMPTY_CURSOR.toString());
    }
    cursor.close();
    WritableMap results = new WritableNativeMap();
    results.putInt("length", cursorCount);
    results.putArray("results", jsonArray);
    return results;
  }

  public static WritableMap extractMetaDataFromFile(String path) {

    WritableMap results = new WritableNativeMap();
    HashMap<String, String> MetaMap = MetaDataExtractor.getMetaData(path);
    for (Map.Entry<String, String> entry : MetaMap.entrySet()) {
      results.putString(entry.getKey(), entry.getValue());
    }

    return results;
  }

  public static WritableArray extractMetaDataFromDirectory(ReactApplicationContext reactContext, String uri,
                                                           int minFileSize, int maxFileSize, String extensionFilter, boolean cover) {
    WritableArray results = new WritableNativeArray();
    File file = new File(uri);
    if (file.isDirectory()) {
      List<String> filesPaths = new ArrayList<>();
      FS.listFilesForFolder(new File(uri), minFileSize, maxFileSize, extensionFilter, filesPaths);
      for (String s : filesPaths) {
        WritableMap result = new WritableNativeMap();
        HashMap<String, String> MetaMap = MetaDataExtractor.getMetaData(s);
        for (Map.Entry<String, String> entry : MetaMap.entrySet()) {
          result.putString(entry.getKey(), entry.getValue());
        }
        if (cover) {
          try {
            byte[] albumImageData = MetaDataExtractor.getEmbededPicture(s);
            String coverPath = FS
              .saveToStorage(
                reactContext.getCacheDir().getAbsolutePath(),
                albumImageData);
            Log.e(LOG, "File saved");
            result.putString("cover", coverPath);
          } catch (Exception e) {
            Log.e(LOG, String.valueOf(e));
            result.putString("cover", "");
          }
        }
        results.pushMap(result);
      }
    }

    return results;
  }

  public static String getCoverFromFile(String CoverPath, String path) throws IOException {
    byte[] albumImageData = MetaDataExtractor.getEmbededPicture(path);
    String coverPath = FS.saveToStorage(CoverPath, albumImageData);
    Log.e(LOG, "File saved");
    return coverPath;
  }

  public static WritableMap getSongs(GetSongsOptions options, ContentResolver contentResolver) throws Exception {

    WritableArray jsonArray = new WritableNativeArray();
    String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
      MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
      MediaStore.Audio.Media._ID};


    String Selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
    String artistSearchParam ;
    String albumSearchParam ;
    List<String> selectionArgs = new ArrayList<>();
    if (options.artist != null) {
      Selection += " AND " + MediaStore.Audio.Media.ARTIST + " Like ?";
      artistSearchParam = "%" + options.artist + "%";
      selectionArgs.add(artistSearchParam);
    }

    if (options.album != null) {
      Selection += " AND " + MediaStore.Audio.Media.ALBUM + " Like ?";
      albumSearchParam = "%" + options.album + "%";
      selectionArgs.add(albumSearchParam);
    }


    String orderBy = null;

    if (options.sortBy != null) {
      orderBy = generateSortOrder(options.sortBy, options.sortOrder);
    }

    Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
      projection, Selection, selectionArgs.size() != 0 ? selectionArgs.toArray(new String[0]) : null, orderBy);

    int cursorCount = Objects.requireNonNull(cursor).getCount();


    if (cursorCount > (options.batchSize * options.batchNumber)) {
      cursor.moveToPosition(options.batchSize * options.batchNumber);
      do {

        WritableMap item = new WritableNativeMap();
        item.putString("title", cursor.getString(0));
        item.putString("artist", cursor.getString(1));
        item.putString("album", cursor.getString(2));
        item.putString("duration", cursor.getString(3));
        item.putString("path", cursor.getString(4));
        item.putString("id", cursor.getString(5));

        jsonArray.pushMap(item);
      } while ((options.batchSize == 0 || cursor.getPosition() + 1 < options.batchSize * (options.batchNumber + 1)) & cursor.moveToNext());
    } else {

      cursor.close();
      throw new Exception(C.ErrorEnum.EMPTY_CURSOR.toString());
    }
    cursor.close();
    WritableMap results = new WritableNativeMap();
    results.putInt("length", cursorCount);
    results.putArray("results", jsonArray);
    return results;
  }
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
