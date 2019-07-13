package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fragment.R;

import android.graphics.Color;
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
import com.example.fragment.model.IncomeTable;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class AddIncomeActivity extends AppCompatActivity {
    private  EditText et_money_income,et_free_income;
    private RadioButton rb_income1,rb_income2,rb_income3,rb_income4,rb_income5,rb_income6,rb_income7,rb_income8,rb_income9;
    private Button btn_write_income;
    private TextView et_date_income;

    private String typeS = "餐饮食品";
    private double moneyD = 0.0;
    private String freeS = "无";
    private Date dateD = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        Bmob.initialize(this,"cd6ace4e1908e4ddac3f2fd44dc26208");

        findById();
        initEditText();
        initButton();
        initDate();
    }

    private void initRadioGroup(){
        if(rb_income1.isChecked()){
            typeS = "工资薪水";
        }
        else if(rb_income2.isChecked()){
            typeS = "礼金";
        }
        else if(rb_income3.isChecked()){
            typeS = "贷款";
        }
        else if(rb_income4.isChecked()){
            typeS = "提成";
        }
        else if(rb_income5.isChecked()){
            typeS = "赞助费";
        }
        else if(rb_income6.isChecked()){
            typeS = "公司奖金";
        }
        else if(rb_income7.isChecked()){
            typeS = "兼职";
        }
        else if(rb_income8.isChecked()){
            typeS = "投资收益";
        }
        else if(rb_income9.isChecked()){
            typeS = "其他收入";
        }
    }

    private void initEditText(){
        et_money_income.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_money_income.setFilters(new InputFilter[]{new InputFilter() {
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
        if(TextUtils.isEmpty(et_money_income.getText())){
            moneyD=0.00;
        }
        else {
            moneyD=Double.valueOf(et_money_income.getText().toString());
        }
    }

    private void initEditFree() {
        if(TextUtils.isEmpty(et_free_income.getText())){
            freeS="无";
        }
        else {
            freeS=et_free_income.getText().toString();
        }
    }

    private void initDate(){
        et_date_income.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.et_date_income:
                        showDate();
                        break;
                }
            }
        });
    }

    private void initButton() {
        btn_write_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRadioGroup();
                initEditMoney();
                initEditFree();
                initIncome();
                clearText();
                /*Toast.makeText(AddPayActivity.this,String.valueOf(moneyD)+typeS+freeS+dateD,Toast.LENGTH_LONG).show();*/
            }
        });
    }

    private void findById(){
        et_money_income=(EditText)findViewById(R.id.et_money_income);
        et_date_income=(TextView)findViewById(R.id.et_date_income);
        et_free_income=(EditText)findViewById(R.id.et_free_income);
        btn_write_income=(Button)findViewById(R.id.btn_write_income);

        rb_income1=(RadioButton) findViewById(R.id.rb_income1);
        rb_income2=(RadioButton) findViewById(R.id.rb_income2);
        rb_income3=(RadioButton) findViewById(R.id.rb_income3);
        rb_income4=(RadioButton) findViewById(R.id.rb_income4);
        rb_income5=(RadioButton) findViewById(R.id.rb_income5);
        rb_income6=(RadioButton) findViewById(R.id.rb_income6);
        rb_income7=(RadioButton) findViewById(R.id.rb_income7);
        rb_income8=(RadioButton) findViewById(R.id.rb_income8);
        rb_income9=(RadioButton) findViewById(R.id.rb_income9);
    }

    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                et_date_income.setText(time);
                dateD = date_1;
                Toast.makeText(AddIncomeActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
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

    private void initIncome(){
        IncomeTable incomeTable = new IncomeTable();
        incomeTable.setType(typeS);
        incomeTable.setMoney(moneyD);
        BmobDate bmobDate=new BmobDate(dateD);
        incomeTable.setDate(bmobDate);
        incomeTable.setFree(freeS);

        incomeTable.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Snackbar sb = Snackbar.make(btn_write_income, "sucess"+";"+"类型："+typeS+";"+"金额："+moneyD+";"+"日期："+dateD+";"+"备注："+freeS, Snackbar.LENGTH_LONG);
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
                }
                else{
                    Toast.makeText(AddIncomeActivity.this,"Fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearText(){
        rb_income1.setChecked(true);
        et_money_income.setText(null);
        et_free_income.setText(null);
        et_date_income.setText("请选择时间");
    }
}


