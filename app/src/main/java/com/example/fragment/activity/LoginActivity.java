package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.R;
import com.example.fragment.model.User;
import com.google.android.material.snackbar.Snackbar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText userpassword;
    private CheckBox remember;
    private CheckBox autologin;
    private Button login;
    private SharedPreferences sp;
    private String userNameValue,passwordValue;
    private TextView tv_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // 初始化用户名、密码、记住密码、自动登录、登录按钮
        username = (EditText) findViewById(R.id.userNameLogin);
        userpassword = (EditText) findViewById(R.id.userPasswordLogin);
        remember = (CheckBox) findViewById(R.id.remember);
        autologin = (CheckBox) findViewById(R.id.autologin);
        login = (Button) findViewById(R.id.btn_login);
        tv_register=(TextView)findViewById(R.id.tv_register);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        sp = getSharedPreferences("userInfo", 0);
        String name=sp.getString("USER_NAME", "");
        String pass =sp.getString("PASSWORD", "");


        boolean choseRemember =sp.getBoolean("remember", false);
        boolean choseAutoLogin =sp.getBoolean("autologin", false);
        //      Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            username.setText(name);
            userpassword.setText(pass);
            remember.setChecked(true);
        }
        //如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if(choseAutoLogin){
            autologin.setChecked(true);
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }



        login.setOnClickListener(new View.OnClickListener() {

            // 默认可登录帐号tinyphp,密码123
            @Override
            public void onClick(View arg0) {
                userNameValue = username.getText().toString();
                passwordValue = userpassword.getText().toString();
                final SharedPreferences.Editor editor =sp.edit();

                User user = new User();
                user.setUsername(userNameValue);
                user.setPassword(passwordValue);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User bmobUser, BmobException e) {
                        if (e == null) {
                            User user = BmobUser.getCurrentUser(User.class);
                            Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                            //保存用户名和密码
                            editor.putString("USER_NAME", userNameValue);
                            editor.putString("PASSWORD", passwordValue);

                            //是否记住密码
                            if(remember.isChecked()){
                                editor.putBoolean("remember", true);
                            }else{
                                editor.putBoolean("remember", false);
                            }


                            //是否自动登录
                            if(autologin.isChecked()){
                                editor.putBoolean("autologin", true);
                            }else{
                                editor.putBoolean("autologin", false);
                            }
                            editor.commit();

                            //跳转
                            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                        }
                    }
                });

               /* // TODO Auto-generated method stub
                if (userNameValue.equals("tinyphp") && passwordValue.equals("123"))
                {
                    Toast.makeText(LoginActivity.this, "登录成功",
                            Toast.LENGTH_SHORT).show();

                    //保存用户名和密码
                    editor.putString("USER_NAME", userNameValue);
                    editor.putString("PASSWORD", passwordValue);

                    //是否记住密码
                    if(remember.isChecked()){
                        editor.putBoolean("remember", true);
                    }else{
                        editor.putBoolean("remember", false);
                    }


                    //是否自动登录
                    if(autologin.isChecked()){
                        editor.putBoolean("autologin", true);
                    }else{
                        editor.putBoolean("autologin", false);
                    }
                    editor.commit();

                    //跳转
                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录!",
                            Toast.LENGTH_SHORT).show();
                }*/

            }

        });

    }

}