package com.example.alessandrolanna.audiovideoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {
    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_CODE_PICTURE = 2;
    private Bitmap bitmap;
    private Bitmap scaled;
    private ImageView imageView;
    private Uri picAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picAddress == null) return;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(picAddress);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switchActivity) {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        }
        if (id == R.id.toAudio) {
            Intent intent = new Intent(this, AudioActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            switch (requestCode) {
                case REQUEST_CODE_GALLERY:
                    manageGallery(data);
                    break;
                case REQUEST_CODE_PICTURE:
                    managePicture(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void managePicture(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        picAddress = data.getData();
        imageView.setImageBitmap(bitmap);

    }

    private void manageGallery(Intent data) {
        try {
            picAddress = data.getData();
            InputStream stream = getContentResolver().openInputStream(data.getData().normalizeScheme());
            bitmap = BitmapFactory.decodeStream(stream);

            if (bitmap.getWidth() >= 2048 || bitmap.getHeight() >= 2048) {
               // bitmap = bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), true);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                bitmap = bitmap.createScaledBitmap(bitmap, width, height, true);
            }

            stream.close();
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pickImage (View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.pickImage:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
                break;
            case R.id.takeImage:
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_PICTURE);
                break;
        }
    }

}
