package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.fragment.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(WelcomeActivity.this,"cd6ace4e1908e4ddac3f2fd44dc26208");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startLoginActivity();
    }

    private void startLoginActivity(){

        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Intent Intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(Intent);
                WelcomeActivity.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
    }
}
