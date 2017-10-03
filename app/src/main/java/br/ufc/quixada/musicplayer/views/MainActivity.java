package br.ufc.quixada.musicplayer.views;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.IOException;

import br.ufc.quixada.musicplayer.R;
import br.ufc.quixada.musicplayer.controllers.PlayerController;

import static android.content.ClipData.*;


public class MainActivity extends AppCompatActivity {
    private PlayerController playerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.playerController = new PlayerController(this);
    }

    public void addMusic(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("audio/*");
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(chooseFile, "Choose a file") , 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1){
            try {
                ClipData cdata = data.getClipData();
                if(cdata != null)
                    for (int i = 0; i < cdata.getItemCount(); i++) {
                        Item item = cdata.getItemAt(i);
                        this.playerController.addInPlaylist(item.getUri());
                    }
                else
                    this.playerController.addInPlaylist(data.getData());

                this.playerController.play();

            }catch (IOException e){
                e.printStackTrace();
                Log.d("ERROR","IO_EXCEPTION in Activity Result");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                addMusic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
