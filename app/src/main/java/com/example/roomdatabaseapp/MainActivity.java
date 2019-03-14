package com.example.roomdatabaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.roomdatabaseapp.database.Album;
import com.example.roomdatabaseapp.database.AlbumSong;
import com.example.roomdatabaseapp.database.MusicDao;
import com.example.roomdatabaseapp.database.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtnAdd;
    private Button mBtnGet;
    public static final String TAG = "MainActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MusicDao musicDao = ((AppDelegate) getApplicationContext()).getMusicDatabase().getMusicDao();

        mBtnAdd = (findViewById(R.id.btn_add));
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Roomdatabaseapp","MainActivity insertAlbums(createAlbums) called");

                musicDao.insertAlbums(createAlbums());
                musicDao.insertSongs(createSongs());
                musicDao.insertAlbumSongs(createAlbumSongs());

            }
        });

        mBtnGet = findViewById(R.id.btn_get);
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(musicDao.getAlbums(),musicDao.getSongs(),musicDao.getAlbumSongs());

                Log.d(TAG, "onClick: Check LOG this method = true" + musicDao.getAlbumSongs());
            }
        });

    }
    private List<Album> createAlbums() {
        List<Album> albums = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            albums.add(new Album(i, "album " + i, "release" + System.currentTimeMillis()));
            Log.d(TAG, "createAlbums: " + i);
        }

        return albums;
    }
    private List<Song> createSongs(){

        List<Song> songs =new ArrayList<>(3);
        for (int i=0;i<3;i++){
            songs.add(new Song(i,"song "+i,"duration"+System.currentTimeMillis()));
            Log.d(TAG, "createSongs: " + i);
        }
        return songs;
    }

    private List<AlbumSong> createAlbumSongs() {
        List<AlbumSong> albumSongs = new ArrayList<>(3);
        final MusicDao musicDao = ((AppDelegate) getApplicationContext()).getMusicDatabase().getMusicDao();

        for (int i = 0; i < 3; i++) {


            int albumId = musicDao.getAlbumIdWithId(i);
            int songId = musicDao.getSongIdWithId(i);

            albumSongs.add(new AlbumSong(albumId, songId));

        }
            return albumSongs;

    }


    private void showToast(List<Album> albums,List<Song> songs,List<AlbumSong> albumSongs) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = albums.size(); i < size; i++) {
            builder.append(albums.get(i).toString()).append("\n");
        }
        for (int i=0,size=songs.size();i<size;i++)  {
            builder.append(songs.get(i).toString()).append("\n");
        }
        for (int i = 0, size = albumSongs.size(); i < size; i++) {
            builder.append(albumSongs.get(i).toString()).append("\n");
        }

        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
    }
}