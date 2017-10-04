package br.ufc.quixada.musicplayer.views;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import br.ufc.quixada.musicplayer.R;
import br.ufc.quixada.musicplayer.controllers.PlayerController;

import static android.content.ClipData.Item;


public class MainActivity extends AppCompatActivity {
    private PlayerController playerController;

    private Button btnPlay = null;
    private Button btnNext = null;
    private Button btnPrev = null;
    private SeekBar seekbarProgress = null;

    private TextView txtViewName = null;
    private ImageView imageAlbum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.playerController = new PlayerController(this);

        setReferences();

        btnNext.setOnClickListener((View v) -> {
            try {
                playerController.next();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnPrev.setOnClickListener((View v) -> {
            try {
                playerController.prev();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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

                this.playerController.play(0);

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

    private void setReferences() {
        this.btnPlay = findViewById(R.id.btnPlay);
        this.btnPrev = findViewById(R.id.btnPrev);
        this.btnNext = findViewById(R.id.btnNext);
        this.seekbarProgress = findViewById(R.id.progressSeekbar);

        this.txtViewName = findViewById(R.id.nameTxtView);
        this.imageAlbum = findViewById(R.id.imageAlbum);
    }

}
