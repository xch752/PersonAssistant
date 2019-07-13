package com.example.fragment.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.fragment.R;
import com.example.fragment.model.PayTable;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class PayItemActivity extends AppCompatActivity {
    private RadioButton rb_pay1_update,rb_pay2_update,rb_pay3_update,rb_pay4_update,rb_pay5_update,rb_pay6_update,rb_pay7_update,rb_pay8_update,rb_pay9_update;
    private Button btn_write_pay_delete;
    private Button btn_write_pay_update;
    private EditText et_money_pay_update,et_free_pay_update;
    private TextView et_date_pay_update;

    private String objectIDS;
    private String typeS_update = "餐饮食品";
    private double moneyD_update = 0.0;
    private String freeS_update = "无";
    private Date dateD_update = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_item);
        Intent intent = getIntent();
        objectIDS =  intent.getStringExtra("objectID");
        findById();
        initEditText();
        initDate();
        queryID();
        initUpdateButton();
        initDelete();
    }

    private void queryID(){
        BmobQuery<PayTable> query = new BmobQuery<>();
        query.getObject(objectIDS, new QueryListener<PayTable>() {
            @Override
            public void done(PayTable payTable, BmobException e) {
                if(e==null){
                    switch (payTable.getType()){
                        case"餐饮食品":
                            rb_pay1_update.setChecked(true);
                            break;
                        case"衣服饰品":
                            rb_pay2_update.setChecked(true);
                            break;
                        case"居家生活":
                            rb_pay3_update.setChecked(true);
                            break;
                        case"行车交通":
                            rb_pay4_update.setChecked(true);
                            break;
                        case"休闲娱乐":
                            rb_pay5_update.setChecked(true);
                            break;
                        case"文化教育":
                            rb_pay6_update.setChecked(true);
                            break;
                        case"健康医疗":
                            rb_pay7_update.setChecked(true);
                            break;
                        case"投资支出":
                            rb_pay8_update.setChecked(true);
                            break;
                        case"其他支出":
                            rb_pay9_update.setChecked(true);
                            break;
                        default:
                            rb_pay1_update.setChecked(true);
                            break;
                    }
                    typeS_update=payTable.getType();
                    freeS_update=payTable.getFree();
                    moneyD_update= payTable.getMoney();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try{
                        dateD_update= sdf.parse(payTable.getDate().getDate());
                    }catch(ParseException ex){
                        Toast.makeText(PayItemActivity.this,"日期转换失败",Toast.LENGTH_LONG).show();
                    }
                    et_date_pay_update.setText(payTable.getDate().getDate());
                    et_free_pay_update.setHint(payTable.getFree());
                    et_money_pay_update.setHint(payTable.getMoney().toString());
                    Toast.makeText(PayItemActivity.this,"sucess:"+objectIDS,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(PayItemActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void deleteID(){
        PayTable p1 = new PayTable();
        p1.setObjectId(objectIDS);
        p1.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(PayItemActivity.this,"Sucess delete"+objectIDS,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PayItemActivity.this,"Fail delete",Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void initDelete(){
        btn_write_pay_delete.setOnClickListener(new View.OnClickListener() {
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
                new AlertDialog.Builder(PayItemActivity.this);
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

    private void initType(){
        if(rb_pay1_update.isChecked()){
            typeS_update = "餐饮食品";
        }
        else if(rb_pay2_update.isChecked()){
            typeS_update = "衣服饰品";
        }
        else if(rb_pay3_update.isChecked()){
            typeS_update = "居家生活";
        }
        else if(rb_pay4_update.isChecked()){
            typeS_update = "行车交通";
        }
        else if(rb_pay5_update.isChecked()){
            typeS_update = "休闲娱乐";
        }
        else if(rb_pay6_update.isChecked()){
            typeS_update = "文化教育";
        }
        else if(rb_pay7_update.isChecked()){
            typeS_update = "健康医疗";
        }
        else if(rb_pay8_update.isChecked()){
            typeS_update = "投资支出";
        }
        else if(rb_pay9_update.isChecked()){
            typeS_update = "其他支出";
        }
    }

    private void initEditText(){
        et_money_pay_update.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_money_pay_update.setFilters(new InputFilter[]{new InputFilter() {
            int decimalNumber = 2;//小数点后保留位数

            @Override
            //source:即将输入的内容 dest：原来输入的内容
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String sourceContent = source.toString();
                String lastInputContent = dest.toString();

                //验证删除等按键
                if (TextUtils.isEmpty(sourceContent)) {
                    return "";
                }
                //以小数点"."开头，默认为设置为“0.”开头
                if (sourceContent.equals(".") && lastInputContent.length() == 0) {
                    return "0.";
                }
                //输入“0”，默认设置为以"0."开头
                if (sourceContent.equals("0") && lastInputContent.length() == 0) {
                    return "0.";
                }
                //小数点后保留两位
                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= decimalNumber + 1) {
                        return "";
                    }
                }
                return null;
            }
        }});
    }

    private void initEditMoney(){
        if(TextUtils.isEmpty(et_money_pay_update.getText())){

        }
        else {
            moneyD_update=Double.valueOf(et_money_pay_update.getText().toString());
        }
    }

    private void initEditFree() {
        if(TextUtils.isEmpty(et_free_pay_update.getText())){

        }
        else {
            freeS_update=et_free_pay_update.getText().toString();
        }
    }

    private void initDate(){
        et_date_pay_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.et_date_pay_update:
                        showDate();
                        break;
                }
            }
        });
    }

    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                et_date_pay_update.setText(time);
                dateD_update = date_1;
                Toast.makeText(PayItemActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
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

    private void initUpdate(){
        PayTable payTable = new PayTable();
        payTable.setType(typeS_update);
        payTable.setMoney(moneyD_update);
        BmobDate bmobDate=new BmobDate(dateD_update);
        payTable.setDate(bmobDate);
        payTable.setFree(freeS_update);

        payTable.update(objectIDS, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Snackbar sb = Snackbar.make(btn_write_pay_update, "sucess"+";"+"类型："+typeS_update+";"+"金额："+moneyD_update+";"+"日期："+dateD_update+";"+"备注："+freeS_update, Snackbar.LENGTH_LONG);
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
                    Toast.makeText(PayItemActivity.this,"更新失败",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initUpdateButton(){
        btn_write_pay_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initType();
                initDate();
                initEditFree();
                initEditMoney();
                initUpdate();
            }
        });
    }

    private void findById(){
        btn_write_pay_delete = (Button) findViewById(R.id.btn_write_pay_delete);
        btn_write_pay_update = (Button)findViewById(R.id.btn_write_pay_update);

        rb_pay1_update=(RadioButton) findViewById(R.id.rb_pay1_update);
        rb_pay2_update=(RadioButton) findViewById(R.id.rb_pay2_update);
        rb_pay3_update=(RadioButton) findViewById(R.id.rb_pay3_update);
        rb_pay4_update=(RadioButton) findViewById(R.id.rb_pay4_update);
        rb_pay5_update=(RadioButton) findViewById(R.id.rb_pay5_update);
        rb_pay6_update=(RadioButton) findViewById(R.id.rb_pay6_update);
        rb_pay7_update=(RadioButton) findViewById(R.id.rb_pay7_update);
        rb_pay8_update=(RadioButton) findViewById(R.id.rb_pay8_update);
        rb_pay9_update=(RadioButton) findViewById(R.id.rb_pay9_update);

        et_money_pay_update=(EditText)findViewById(R.id.et_money_pay_update);
        et_free_pay_update=(EditText)findViewById(R.id.et_free_pay_update);
        et_date_pay_update=(TextView)findViewById(R.id.et_date_pay_update);
    }
}
