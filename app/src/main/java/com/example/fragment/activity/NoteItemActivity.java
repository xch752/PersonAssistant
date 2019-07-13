package com.example.fragment.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class NoteItemActivity extends AppCompatActivity {
    private String objectIDS;
    private EditText noteUpdateTitle,noteUpdateContent;
    private TextView noteUpdateDate;
    private Button noteUpdateBtn,noteDeleteBtn;

    private Date noteUpdateDateD=new Date();
    private String noteUpdateTitleS="无",noteUpdateContentS="无",noteUpdateDateS=noteUpdateDateD.toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_item);
        Intent intent = getIntent();
        objectIDS =  intent.getStringExtra("objectID");

        findById();
        queryID();
        initDate();
        initUpdateBtn();
        initDeleteBtn();
    }

    private void queryID(){
        BmobQuery<Note> query = new BmobQuery<>();
        query.getObject(objectIDS, new QueryListener<Note>() {
            @Override
            public void done(Note note, BmobException e) {
                if(e==null){
                    noteUpdateTitleS=note.getTitle();
                    noteUpdateContentS=note.getContent();
                    noteUpdateDateS=note.getDate().getDate();
                    noteUpdateTitle.setText(noteUpdateTitleS);
                    noteUpdateContent.setText(noteUpdateContentS);
                    noteUpdateDate.setText(noteUpdateDateS);
                    Toast.makeText(NoteItemActivity.this,"Sucess:"+objectIDS,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(NoteItemActivity.this,"Fail QueryID",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initDate(){
        noteUpdateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDate();
            }
        });
    }

    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                noteUpdateDate.setText(time);
                noteUpdateDateD = date_1;
                noteUpdateDateS=time;
                Toast.makeText(NoteItemActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
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

    private void initUpdateBtn(){
        noteUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUpdate();
            }
        });
    }

    private void initUpdate(){
        noteUpdateTitleS= noteUpdateTitle.getText().toString();
        noteUpdateContentS=noteUpdateContent.getText().toString();
        BmobDate bmobDate=new BmobDate(noteUpdateDateD);
        Note note = new Note();
        note.setTitle(noteUpdateTitleS);
        note.setContent(noteUpdateContentS);
        note.setDate(bmobDate);

        note.update(objectIDS, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Snackbar sb = Snackbar.make(noteUpdateBtn, "sucess"+";"+"标题："+noteUpdateTitleS+";"+"日期："+noteUpdateDateS, Snackbar.LENGTH_LONG);
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
                }else{
                    Toast.makeText(NoteItemActivity.this,"更新失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private  void deleteID(){
        Note note = new Note();
        note.setObjectId(objectIDS);
        note.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(NoteItemActivity.this,"Sucess delete"+objectIDS,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NoteItemActivity.this,"Fail delete",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initDeleteBtn(){
        noteDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNormalDialog();
            }
        });
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(NoteItemActivity.this);
        normalDialog.setIcon(R.drawable.delete);
        normalDialog.setTitle("删除确认");
        normalDialog.setMessage("确定删除?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteID();
                        finish();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    private void findById(){
        noteUpdateTitle = (EditText)findViewById(R.id.noteUpdateTitle);
        noteUpdateContent = (EditText)findViewById(R.id.noteUpdateContent);
        noteUpdateDate = (TextView)findViewById(R.id.noteUpdateDate);
        noteUpdateBtn = (Button)findViewById(R.id.noteUpdateBtn);
        noteDeleteBtn = (Button)findViewById(R.id.noteDeleteBtn);

    }
}
