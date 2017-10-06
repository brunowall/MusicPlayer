package br.ufc.quixada.musicplayer.views;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import static android.widget.SeekBar.*;


public class MainActivity extends AppCompatActivity {
    // Constants
    private final int UPDATE_FREQUENCY = 500;
    private final int STEP_VALUE = 4000;
    // Booleans
    private boolean isMoveSeekBar = false;
    // Controllers
    private PlayerController playerController;
    // Buttons
    private Button btnPlay = null;
    private Button btnNext = null;
    private Button btnPrev = null;
    private SeekBar seekBarProgress = null;
    // Views
    private TextView txtViewName = null;
    private ImageView imageAlbum = null;
    // Others
    private final Handler handler = new Handler();
    private final Runnable updatePositionRunnable = () -> updatePosition();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.playerController = new PlayerController(this);

        setReferences(); // Get all the references from xml file

        btnNext.setOnClickListener(onButtonClick);
        btnPrev.setOnClickListener(onButtonClick);
        btnPlay.setOnClickListener(onButtonClick);
        seekBarProgress.setOnSeekBarChangeListener(seekBarChanged);
    }
    
    private void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);

        seekBarProgress.setProgress(playerController.getCurrentPosition());

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }

    private View.OnClickListener onButtonClick = (View v) -> {
        try {
            switch (v.getId()) {
                case R.id.btnPlay:
                    playerController.playOrPause();
                    updatePosition();
                 break;
                case R.id.btnNext: playerController.next();
                 break;
                case R.id.btnPrev: playerController.prev();
                 break;
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    private boolean addMusic(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("audio/*");
        chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(chooseFile, "Choose a file") , 1);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1){
            try {
                ClipData cdata = data.getClipData();
                if(cdata != null)
                    for (int i = 0; i < cdata.getItemCount(); i++)
                        this.playerController.addInPlaylist(cdata.getItemAt(i).getUri());
                else
                    this.playerController.addInPlaylist(data.getData());

                this.playerController.play(0);
                seekBarProgress.setProgress(0);
                seekBarProgress.setMax(playerController.getDuration());
                updatePosition();
            } catch (IOException e){
                e.printStackTrace();
                Log.d("ERROR", "IO Exception in Activity Result");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                return addMusic();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private OnSeekBarChangeListener seekBarChanged = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) { isMoveSeekBar = false; }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) { isMoveSeekBar = true; }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isMoveSeekBar) {
                playerController.seekTo(progress);
                Log.i("OnSeekBarChangeListener", "onProgressChanged");
            }
        }
    };

    private void setReferences() {
        this.btnPlay = findViewById(R.id.btnPlay);
        this.btnPrev = findViewById(R.id.btnPrev);
        this.btnNext = findViewById(R.id.btnNext);
        this.seekBarProgress = findViewById(R.id.progressSeekbar);

        this.txtViewName = findViewById(R.id.nameTxtView);
        this.imageAlbum = findViewById(R.id.imageAlbum);
    }

}
