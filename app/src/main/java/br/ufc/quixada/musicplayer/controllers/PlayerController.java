package br.ufc.quixada.musicplayer.controllers;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darkbyte on 07/09/17.
 */

public class PlayerController implements MediaPlayer.OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private List <Uri> playlist; //currently playlist
    private Context context;
    private int numberMusic; // the music of the playlist that is playing in the time


    public PlayerController(Context context){
        this.numberMusic = 0;
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
        this.playlist = new ArrayList<>();
    }

   //may have changes
    public void play() throws IOException {
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(context, playlist.get(0));
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    public void pause(){
        return;
    }

    public void next(){
        return;
    }
    public void prev(){
        return;
    }

    public void addInPlaylist(Uri uri){
        playlist.add(uri);
    }

    public List<Uri> getPlayList(){
        return playlist;
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        numberMusic ++;
        if (numberMusic < playlist.size()){
            mediaPlayer = MediaPlayer.create(context,playlist.get(numberMusic));
            mediaPlayer.start();
        }
    }

}
