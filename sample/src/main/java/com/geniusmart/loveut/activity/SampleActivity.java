package com.geniusmart.loveut.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusmart.loveut.R;

public class SampleActivity extends FragmentActivity {

    private TextView lifecycleTextView;
    private CheckBox inverseCheckBox;
    public boolean isTaskFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        lifecycleTextView = (TextView) findViewById(R.id.tv_lifecycle_value);
        inverseCheckBox = (CheckBox) findViewById(R.id.checkbox);

        lifecycleTextView.setText(R.string.sample_lifecycle_oncreate);
    }

    public void forward(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void showDialog(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(R.string.sample_dialog_message)
                .setTitle(R.string.sample_dialog_title).create();
        alertDialog.show();
    }

    public void showToast(View view){
        Toast.makeText(this,"we love UT",Toast.LENGTH_LONG).show();
    }

    public void inverse(View view) {
        inverseCheckBox.setChecked(!inverseCheckBox.isChecked());
    }

    public void showDemo(View view){
        forward(view);
    }

    public void executeDelayedTask(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isTaskFinish = true;
            }
        }, 2000);
    }

    public void callback(View view) {
        startActivity(new Intent(this,CallbackActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleTextView.setText(R.string.sample_lifecycle_onResume);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleTextView.setText(R.string.sample_lifecycle_onDestroy);
    }
}
