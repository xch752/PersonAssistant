package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.fragment.R;
import com.example.fragment.model.Note;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddNoteActivity extends AppCompatActivity {
    private EditText noteAddTitle,noteAddContent;
    private TextView noteAddDate;
    private Button noteAddBtn;

    private String noteAddTitleS="无",noteAddContentS="无";
    private Date noteAddDateD = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        noteAddTitle=(EditText)findViewById(R.id.noteAddTitle);
        noteAddContent=(EditText)findViewById(R.id.noteAddContent);
        noteAddDate=(TextView)findViewById(R.id.noteAddDate);
        noteAddBtn=(Button)findViewById(R.id.noteAddBtn);

        initDate();
        initButton();

    }

    private void addNote(){
        Note note = new Note();
        noteAddTitleS=noteAddTitle.getText().toString();
        noteAddContentS=noteAddContent.getText().toString();
        note.setTitle(noteAddTitleS);
        note.setContent(noteAddContentS);
        BmobDate bmobDate=new BmobDate(noteAddDateD);
        note.setDate(bmobDate);

        note.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Snackbar sb = Snackbar.make(noteAddBtn, "sucess"+";"+"标题："+noteAddTitleS+";"+"内容："+noteAddContentS+";"+"日期："+noteAddDateD, Snackbar.LENGTH_LONG);
                    sb.setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            // Snackbar关闭时回调
                        }
                        @Override
                        public void onShown(Snackbar snackbar) {
                            super.onShown(snackbar);
                            // Snackbar打开时回调
                        }
                    });
                    sb.show();
                    textClear();
                }
                else{
                    Toast.makeText(AddNoteActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void  initButton(){
        noteAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(noteAddTitle.getText())){
                    Toast.makeText(AddNoteActivity.this,"请输入标题",Toast.LENGTH_LONG).show();
                }
                else {
                    addNote();
                }

            }
        });
    }

    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                noteAddDate.setText(time);
                noteAddDateD = date_1;
                Toast.makeText(AddNoteActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
//                .setTitleSize(20)//标题文字大小
//                //.setTitleText("Title")//标题文字
//                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//                .isCyclic(true)//是否循环滚动
//                //.setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
//                //.setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
////                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
////                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                //.isDialog(true)//是否显示为对话框样式
                .build();

        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        //"YYYY-MM-DD HH:MM:SS"        "yyyy-MM-dd"
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:SS");
        return format.format(date);
    }

    private void initDate(){
        noteAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.noteAddDate:
                        showDate();
                        break;
                }
            }
        });
    }

    private void textClear(){
        noteAddTitle.setText("");
        noteAddContent.setText("");
        noteAddDate.setText("请选择时间");
    }
}
