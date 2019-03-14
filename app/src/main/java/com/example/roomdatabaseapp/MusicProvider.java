package com.example.roomdatabaseapp;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roomdatabaseapp.database.Album;
import com.example.roomdatabaseapp.database.AlbumSong;
import com.example.roomdatabaseapp.database.MusicDao;
import com.example.roomdatabaseapp.database.MusicDatabase;
import com.example.roomdatabaseapp.database.Song;

public class MusicProvider extends ContentProvider {

    private static final String TAG = "MPL";

    private static final String AUTHORITY = "com.elegion.roomdatabase.musicprovider";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_SONG ="song";
    private static final String TABLE_ALBUMSONG="albumsong";


    private static final int ALBUM_TABLE_CODE = 100;
    private static final int ALBUM_ROW_CODE = 101;
    private static final int SONG_TABLE_CODE=200;
    private static final int SONG_ROW_CODE=201;
    private static final int ALBUMSONG_TABLE_CODE=300;
    private static final int ALBUMSONG_ROW_CODE=301;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM, ALBUM_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY, TABLE_ALBUM + "/*", ALBUM_ROW_CODE);
        URI_MATCHER.addURI(AUTHORITY,TABLE_SONG,SONG_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY,TABLE_SONG+"/*",SONG_ROW_CODE);
        URI_MATCHER.addURI(AUTHORITY,TABLE_ALBUMSONG,ALBUMSONG_TABLE_CODE);
        URI_MATCHER.addURI(AUTHORITY,TABLE_ALBUMSONG+"/*",ALBUMSONG_ROW_CODE);
    }

    private MusicDao mMusicDao;
    private Cursor cursor;

    public MusicProvider() {
    }

    @Override
    public boolean onCreate() {
        if (getContext() != null) {
            mMusicDao = Room.databaseBuilder(getContext().getApplicationContext(), MusicDatabase.class, "music_database")
                    .build()
                    .getMusicDao();
            return true;
        }

        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ALBUM_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_ALBUM;
            case ALBUM_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_ALBUM;
            case SONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_SONG;
            case SONG_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_SONG;
            case ALBUMSONG_TABLE_CODE:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_ALBUMSONG;
            case ALBUMSONG_ROW_CODE:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_ALBUMSONG;
            default:
                throw new UnsupportedOperationException("not yet implemented");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int code = URI_MATCHER.match(uri);
        if(code==ALBUM_TABLE_CODE) {
            cursor = mMusicDao.getAlbumsCursor();
            Log.d(TAG, "ALBUM_TABLE_CODE=" + ALBUM_TABLE_CODE + " cursor=" + cursor);
        }
        if(code== ALBUM_ROW_CODE) {
            cursor = mMusicDao.getAlbumWithIdCursor((int) ContentUris.parseId(uri));
            Log.d(TAG, "ALBUM_ROW_CODE=" + ALBUM_ROW_CODE + " cursor=" + cursor);
        }
        if(code==SONG_TABLE_CODE) {
            cursor = mMusicDao.getSongCursor();
            Log.d(TAG,"SONG_TABLE_CODE="+SONG_TABLE_CODE+" cursor="+cursor);
        }
        if(code==SONG_ROW_CODE) {
            cursor = mMusicDao.getSongWithIdCursor((int) ContentUris.parseId(uri));
            Log.d(TAG, "SONG_ROW_CODE=" + SONG_ROW_CODE + " cursor=" + cursor);
        }
        if(code== ALBUMSONG_TABLE_CODE) {
            cursor = mMusicDao.getAlbumSongCursor();
            Log.d(TAG, "ALBUMSONG_TABLE_CODE=" + ALBUMSONG_TABLE_CODE + " cursor=" + cursor);
        }
        if(code== ALBUMSONG_ROW_CODE) {
            cursor = mMusicDao.getAlbumSongCursor();
            Log.d(TAG, "ALBUMSONG_ROW_CODE=" + ALBUMSONG_ROW_CODE + " cursor=" + cursor);
        }

        Log.d(TAG,"cursor="+cursor);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) == ALBUM_TABLE_CODE && isAlbumValuesValid(values)) {

            Log.d(TAG,"Music Provider insert method for Albums called with uri="+uri);
            Log.d(TAG,"Music Provider insert method for Albums called with urimatcher="+URI_MATCHER.match(uri));

            Album album = new Album();
            Integer id = values.getAsInteger("id");
            album.setId(id);
            album.setName(values.getAsString("name"));
            album.setReleaseDate(values.getAsString("release"));
            mMusicDao.insertAlbum(album);  //добавляем одиночную запись
            return ContentUris.withAppendedId(uri, id);
        }
        if(URI_MATCHER.match(uri) == SONG_TABLE_CODE && isSongValuesValid(values)){
            Log.d(TAG,"Music Provider insert method for Songs called with uri="+uri);
            Log.d(TAG,"Music Provider insert method for Songs called with urimatcher="+URI_MATCHER.match(uri));

            Song song = new Song();
            Integer id = values.getAsInteger("id");
            song.setId(id);
            song.setName(values.getAsString("name"));
            song.setDuration(values.getAsString("duration"));

            mMusicDao.insertSong(song);

            return ContentUris.withAppendedId(uri, id);
        }
        if(URI_MATCHER.match(uri) == ALBUMSONG_TABLE_CODE && isAlbumSongValuesValid(values)) {
//            Log.d("Roomdatabaseapp","Music Provider insert method for AlbumSongs called with uri="+uri);
//            Log.d("Roomdatabaseapp","Music Provider insert method for AlbumSongs called with urimatcher="+URI_MATCHER.match(uri));
            Log.d(TAG, "insert: Check LOG for this method = true");

            AlbumSong albumSong = new AlbumSong();
            Integer id = values.getAsInteger("id");
            albumSong.setId(id);
            albumSong.setAlbumId(values.getAsInteger("album_id"));
            albumSong.setSongId(values.getAsInteger("song_id"));
            mMusicDao.insertAlbumSong(albumSong);

            return ContentUris.withAppendedId(uri,id);

        }

        else {
            throw new IllegalArgumentException("cant add multiple items");
        }
    }

    private boolean isAlbumValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("release");
    }
    private boolean isSongValuesValid(ContentValues values) {
        return values.containsKey("id") && values.containsKey("name") && values.containsKey("duration");
    }

    private boolean isAlbumSongValuesValid(ContentValues values) {
        return values.containsKey("id")&& values.containsKey("song_id") && values.containsKey("album_id");
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (URI_MATCHER.match(uri) == ALBUM_ROW_CODE && isAlbumValuesValid(values)) {

            Log.d(TAG,"Music Provider update method for Albums called with uri="+uri);
            Log.d(TAG,"Music Provider update method for Albums called with uriMatcher="+URI_MATCHER.match(uri));


            Album album = new Album();
            int id = (int) ContentUris.parseId(uri);
            album.setId(id);
            album.setName(values.getAsString("name"));
            album.setReleaseDate(values.getAsString("release"));
            int updatedRows = mMusicDao.updateAlbumInfo(album);
            return updatedRows;
        }
        if(URI_MATCHER.match(uri) == SONG_ROW_CODE && isSongValuesValid(values)){
            Log.d(TAG,"Music Provider update method for Songs called with uri="+uri);
            Log.d(TAG,"Music Provider update method for Songs called with uriMatcher="+URI_MATCHER.match(uri));

            Song song = new Song();
            int id =(int)ContentUris.parseId(uri);
            song.setId(id);
            song.setName(values.getAsString("name"));
            song.setDuration(values.getAsString("duration"));
            int updatedRows = mMusicDao.updateSongInfo(song);

            return updatedRows;
        }
        if(URI_MATCHER.match(uri) == ALBUMSONG_ROW_CODE && isAlbumSongValuesValid(values)){

//            Log.d("Roomdatabaseapp","Music Provider update method for AlbumSongs called with uri="+uri);
//            Log.d("Roomdatabaseapp","Music Provider update method for AlbumSongs called with uriMatcher="+URI_MATCHER.match(uri));
            Log.d(TAG, "update: Check LOG for this method = true");

            AlbumSong albumSong = new AlbumSong();
            int id =(int)ContentUris.parseId(uri);
            albumSong.setId(id);
            albumSong.setSongId(values.getAsInteger("song_id"));
            albumSong.setAlbumId(values.getAsInteger("album_id"));
            int updateRows = mMusicDao.updateAlbumSonginfo(albumSong);
            return updateRows;

        }
        else {
            throw new IllegalArgumentException("cant update multiple items");
        }

    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d(TAG,"Music Provider DELETE method for Songs called with uri="+uri);



        if (URI_MATCHER.match(uri) == ALBUM_ROW_CODE) {
            int id = (int) ContentUris.parseId(uri);
            return mMusicDao.deleteAlbumById(id);
        }
        if(URI_MATCHER.match(uri) == SONG_ROW_CODE){
            int id =(int) ContentUris.parseId(uri);
            return mMusicDao.deleteSongById(id);
        }

        if(URI_MATCHER.match(uri) == ALBUMSONG_ROW_CODE){
            Log.d(TAG, "delete: Check LOG this is method = true");
            int id =(int) ContentUris.parseId(uri);
            return mMusicDao.deleteAlbumSongById(id);
        }
        else {
            throw new IllegalArgumentException("cant delete multiple items");
        }

    }
}