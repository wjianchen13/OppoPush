package com.heytap.push.demo.component.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.heytap.push.demo.R;
import com.heytap.push.demo.util.AppUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv_demo_version = (TextView) findViewById(R.id.tv_demo_version);
        TextView tv_demo_name = (TextView) findViewById(R.id.tv_demo_name);
        tv_demo_name.setText(R.string.app_name);
        tv_demo_version.setText(AppUtils.getVersionName(this));
    }
}
