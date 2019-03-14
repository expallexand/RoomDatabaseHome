package com.example.roomdatabaseapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbum(Album albums);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSongs(List<AlbumSong> albumSongs);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumSong(AlbumSong albumSong);

    @Query("select * from album")
    List<Album> getAlbums();
    @Query("select id from album where id = :albumId")
    int getAlbumIdWithId(int albumId);

    @Query("select id from albumsong where id =:albumsongId")
    int getAlbumSongWithId(int albumsongId);

    @Query("select * from album")
    Cursor getAlbumsCursor();
    @Query("select * from album where id = :albumId")
    Cursor getAlbumWithIdCursor(int albumId);

    @Query("select * from song")
    List<Song> getSongs();
    @Query("select id from song where id=:songId")
    int getSongIdWithId(int songId);

    @Query("select * from song")
    Cursor getSongCursor();

    @Query("select *from song where id=:songId")
    Cursor getSongWithIdCursor(int songId);


    @Query("select * from AlbumSong")
    List<AlbumSong> getAlbumSongs();



    @Query("select * from AlbumSong")
    Cursor getAlbumSongCursor();

//    @Query("Select * from AlbumSong where album_id=:albumId and song_id= :songId")
//    Cursor getAlbumSongWithAlbumIdAndSongId(int albumId,int songId);
        @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
        Cursor getSongsFromAlbumCursor(int albumId);

        @Query("SELECT * FROM albumsong inner join song on song.id = albumsong.song_id where song_id = :songId")
        Cursor getSongsFromAlbumCursorSong(int songId);


    //получить список песен переданного id альбома
    @Query("select * from song inner join albumsong on song.id = albumsong.song_id where album_id = :albumId")
    List<Song> getSongsFromAlbum(int albumId);

    //обновить информацию об альбоме
    @Update
    int updateAlbumInfo(Album album);
    //обновить информацию о песне
    @Update
    int updateSongInfo(Song song);
    //обновить информацию о песне в AlbumSongs
    @Update
    int updateAlbumSonginfo(AlbumSong albumSong);


    @Delete
    void deleteAlbum(Album album);
    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    int deleteAlbumById(int albumId);

    @Delete
    void deleteSong(Song song);
    //удалить песни по id
    @Query("DELETE FROM song where id=:songId")
    int deleteSongById(int songId);
    //удалить песни альбомов по id
    @Query("DELETE FROM albumsong where id=:albumsongId")
    int deleteAlbumSongById(int albumsongId );
}
