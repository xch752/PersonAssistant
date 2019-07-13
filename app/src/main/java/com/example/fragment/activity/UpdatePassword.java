package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragment.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePassword extends AppCompatActivity {
    private String userName="无";

    private String oldPasswordS,newPasswordS,newPassword1S;
    private EditText oldPassword,newPassword,newPassword1;
    private Button btn_update_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        oldPassword =(EditText)findViewById(R.id.oldPassword);
        newPassword =(EditText)findViewById(R.id.newPassword);
        newPassword1 =(EditText)findViewById(R.id.newPassword1);
        btn_update_password =(Button) findViewById(R.id.btn_update_password);



        btn_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPasswordS=oldPassword.getText().toString();
                newPasswordS=newPassword.getText().toString();
                newPassword1S=newPassword1.getText().toString();
                if(newPasswordS.equals(newPassword1S)){
                    BmobUser.updateCurrentUserPassword(oldPasswordS, newPasswordS, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(UpdatePassword.this,"修改成功",Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(UpdatePassword.this,"原密码输入错误",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(UpdatePassword.this,"密码输入不一致",Toast.LENGTH_LONG).show();
                }
            }
        });



        Intent intent = getIntent();
        userName =  intent.getStringExtra("username");

        Toast.makeText(UpdatePassword.this,userName,Toast.LENGTH_LONG).show();


    }


}
