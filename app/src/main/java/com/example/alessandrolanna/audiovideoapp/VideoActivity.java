package com.example.alessandrolanna.audiovideoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class VideoActivity extends Activity {
    private VideoView videoView;
    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private Uri videoUri;
    private ImageButton playButton;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = (VideoView) findViewById(R.id.video);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        playButton = (ImageButton) findViewById(R.id.playVideo);
        playButton.setVisibility(View.INVISIBLE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!videoView.isPlaying()) {
                    videoView.start();
                    v.setVisibility(View.INVISIBLE);
                }
                else {
                    videoView.pause();
                }
            }
        });
        videoView.setMediaController(mediaController);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
                else {
                    videoView.start();
                    playButton.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.switchActivity) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public void pickVideo (View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.pickVideo:
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
                break;
            case R.id.takeVideo:
                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Log.i("LOG", intent.resolveActivity(getPackageManager()).toString());
                    startActivityForResult(intent, REQUEST_CODE_VIDEO);
                }
                else
                    Toast.makeText(this, "Non puoi fare video!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    manageGallery(data);
                    break;
                case REQUEST_CODE_VIDEO:
                    manageVideo(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void manageGallery(Intent data) {
            videoView.setVideoURI(data.getData());
            playButton.setVisibility(View.VISIBLE);
    }
    private void manageVideo(Intent data) {
        videoUri = data.getData();
        videoView.setVideoURI(videoUri);

    }
}
