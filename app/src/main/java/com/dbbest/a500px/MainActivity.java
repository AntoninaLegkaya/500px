package com.dbbest.a500px;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.dbbest.a500px.ui.PhotosGalleryActivity;

//@SuppressFBWarnings(value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getPhotosBtn = (Button) findViewById(R.id.login_btn);

        getPhotosBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                App.processor().repository().photo().removeAll();
                App.processor().repository().user().removeAll();
                App.processor().repository().avatars().removeAll();
                moveToGallery();
            }
        });
    }

    public void moveToGallery() {
        Intent intent = new Intent(this, PhotosGalleryActivity.class);
        startActivity(intent);
        finish();
    }

}
