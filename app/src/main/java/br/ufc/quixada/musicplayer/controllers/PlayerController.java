package br.ufc.quixada.musicplayer.controllers;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by darkbyte on 07/09/17.
 */

public class PlayerController implements MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private List <Uri> playlist;

    public PlayerController(){
        this.mediaPlayer = new MediaPlayer();
        this.playlist = new ArrayList<Uri>();
    }
    //nao funciona ainda
    public void play() throws IOException {
        for(Uri uri:playlist){

            mediaPlayer.setDataSource(uri.getPath());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        }

        return;
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


    public void onPrepared(MediaPlayer player) {
        player.start();
    }

}
