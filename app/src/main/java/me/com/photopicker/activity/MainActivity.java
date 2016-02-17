package me.com.photopicker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.com.photopicker.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PhotoPickerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
