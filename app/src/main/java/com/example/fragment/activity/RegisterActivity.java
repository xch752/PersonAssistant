package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fragment.R;
import com.example.fragment.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameRegister,userPasswordRegister,userPasswordRegister1;
    private Button btn_register;

    private String userName,userPassword,userPassword1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findById();
        initButtonRegister();


    }

    private void initButtonRegister(){
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName=userNameRegister.getText().toString();
                userPassword=userPasswordRegister.getText().toString();
                userPassword1=userPasswordRegister1.getText().toString();
                BmobQuery<User> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("username",userName);
                bmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if(e==null){
                            if(list.size()==0){
                                if(userPassword.equals(userPassword1)){
                                    User user = new User();
                                    user.setUsername(userName);
                                    user.setPassword(userPassword);
                                    user.signUp(new SaveListener<User>() {
                                        @Override
                                        public void done(User user, BmobException e) {
                                            if (e == null) {
                                                userNameRegister.setText("");
                                                userPasswordRegister.setText("");
                                                userPasswordRegister1.setText("");
                                                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this,"密码输入不一致",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,"用户已存在",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{

                        }
                    }
                });
            }
        });
    }

    private void findById(){
        userNameRegister=(EditText)findViewById(R.id.userNameRegister);
        userPasswordRegister=(EditText)findViewById(R.id.userPasswordRegister);
        userPasswordRegister1=(EditText)findViewById(R.id.userPasswordRegister1);
        btn_register=(Button)findViewById(R.id.btn_register);
    }
}
