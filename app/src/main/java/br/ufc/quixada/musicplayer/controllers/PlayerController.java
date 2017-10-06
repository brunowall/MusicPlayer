package br.ufc.quixada.musicplayer.controllers;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

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
    private int currentPosition; // the music of the playlist that is playing in the time

    public PlayerController(Context context){
        this.context = context;

        this.currentPosition = 0;
        this.mediaPlayer = new MediaPlayer();
        this.playlist = new ArrayList<>();
    }

    public void playOrPause() {
        if (mediaPlayer.isPlaying()) 
            mediaPlayer.pause();
        else 
            mediaPlayer.start();
    }

    //may have changes
    public void play(int position) throws IOException {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(context, playlist.get(position));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
    }

    public void next() throws IOException {
        currentPosition = (currentPosition + 1) % playlist.size();
        play(currentPosition);
    }
    public void prev() throws IOException {
        currentPosition = ((currentPosition - 1) + playlist.size()) % playlist.size();
        play(currentPosition);
    }

    public void addInPlaylist(Uri uri){
        playlist.add(uri);
    }

    public List<Uri> getPlayList(){
        return playlist;
    }


    public int getCurrentPosition () {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        currentPosition ++;
        if (currentPosition < playlist.size()){
            mediaPlayer = MediaPlayer.create(context, playlist.get(currentPosition));
            mediaPlayer.start();
        }
    }

}
