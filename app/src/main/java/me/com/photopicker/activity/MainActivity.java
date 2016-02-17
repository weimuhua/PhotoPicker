package me.com.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.com.photopicker.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhotoPickerMain";

    private Context mContext;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.pick_photo_button);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mButton) {
            intent = new Intent(mContext, PickPhotoActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
