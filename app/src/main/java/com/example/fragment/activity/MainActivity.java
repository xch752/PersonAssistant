package com.example.fragment.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragment.fragment.PayFragment;
import com.example.fragment.fragment.IncomeFragment;
import com.example.fragment.fragment.NoteFragment;
import com.example.fragment.fragment.MineFragment;
import com.example.fragment.R;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //底部菜单栏4个TextView
    private TextView bottom_pay,bottom_income,bottom_note,bottom_mine;

    //4个Fragment
    private Fragment payFragment,incomeFragment,noteFragment,mineFragment;

    //标记当前显示的Fragment
    private int fragmentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        //根据传入的Bundle对象判断Activity是正常启动还是销毁重建
        if(savedInstanceState == null){
            //设置第一个Fragment默认选中
            setFragment(0);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case (R.id.menu1):
                Intent intent = new Intent(MainActivity.this, QueryMoneyActivity.class);
                startActivity(intent);
                Toast.makeText(this, "金额查询", Toast.LENGTH_LONG).show();
                break;
            case (R.id.menu2):
                 intent = new Intent(MainActivity.this, QueryDateActivity.class);
                startActivity(intent);
                Toast.makeText(this, "日期查询", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "click menu item3", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //通过onSaveInstanceState方法保存当前显示的fragment
        outState.putInt("fragment_id",fragmentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //通过FragmentManager获取保存在FragmentTransaction中的Fragment实例
        payFragment = (PayFragment)mFragmentManager
                .findFragmentByTag("pay_fragment");
        incomeFragment = (IncomeFragment)mFragmentManager
                .findFragmentByTag("income_fragment");
        noteFragment = (NoteFragment)mFragmentManager
                .findFragmentByTag("note_fragment");
        mineFragment = (MineFragment)mFragmentManager
                .findFragmentByTag("mine_fragment");
        //恢复销毁前显示的Fragment
        setFragment(savedInstanceState.getInt("fragment_id"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
            case R.id.bottom_pay:
                setFragment(0);
                break;
            case R.id.bottom_income:
                setFragment(1);
                break;
            case R.id.bottom_note:
                setFragment(2);
                break;
            case R.id.bottom_mine:
                setFragment(3);
                break;
        }
    }

    private void init(){
        //初始化控件
        bottom_pay = (TextView)findViewById(R.id.bottom_pay);
        bottom_income = (TextView)findViewById(R.id.bottom_income);
        bottom_note = (TextView)findViewById(R.id.bottom_note);
        bottom_mine = (TextView)findViewById(R.id.bottom_mine);

        //设置监听
        bottom_pay.setOnClickListener(this);
        bottom_income.setOnClickListener(this);
        bottom_note.setOnClickListener(this);
        bottom_mine.setOnClickListener(this);
    }

    private void setFragment(int index){
        //获取Fragment管理器
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        //隐藏所有Fragment
        hideFragments(mTransaction);
        switch (index){
            default:
                break;
            case 0:
                fragmentId = 0;
                //设置菜单栏为选中状态（修改文字和图片颜色）
                bottom_pay.setTextColor(getResources()
                        .getColor(R.color.colorTextPressed));
                bottom_pay.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.bottom_pay_check,0,0);
                //显示对应Fragment
                if(payFragment == null){
                    payFragment = new PayFragment();
                    mTransaction.add(R.id.container, payFragment,
                            "pay_fragment");
                }else {
                    mTransaction.show(payFragment);
                }
                break;
            case 1:
                fragmentId = 1;
                bottom_income.setTextColor(getResources()
                        .getColor(R.color.colorTextPressed));
                bottom_income.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.bottom_income_check,0,0);
                if(incomeFragment == null){
                    incomeFragment = new IncomeFragment();
                    mTransaction.add(R.id.container, incomeFragment,
                            "income_fragment");
                }else {
                    mTransaction.show(incomeFragment);
                }
                break;
            case 2:
                fragmentId = 2;
                bottom_note.setTextColor(getResources()
                        .getColor(R.color.colorTextPressed));
                bottom_note.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.bottom_note_check,0,0);
                if(noteFragment == null){
                    noteFragment = new NoteFragment();
                    mTransaction.add(R.id.container, noteFragment,
                            "note_fragment");
                }else {
                    mTransaction.show(noteFragment);
                }
                break;
            case 3:
                fragmentId = 3;
                bottom_mine.setTextColor(getResources()
                        .getColor(R.color.colorTextPressed));
                bottom_mine.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.bottom_mine_check,0,0);
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    mTransaction.add(R.id.container, mineFragment,
                            "mine_fragment");
                }else {
                    mTransaction.show(mineFragment);
                }
                break;
        }
        //提交事务
        mTransaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(payFragment != null){
            //隐藏Fragment
            transaction.hide(payFragment);
            //将对应菜单栏设置为默认状态
            bottom_pay.setTextColor(getResources()
                    .getColor(R.color.colorText));
            bottom_pay.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.bottom_pay,0,0);
        }
        if(incomeFragment != null){
            transaction.hide(incomeFragment);
            bottom_income.setTextColor(getResources()
                    .getColor(R.color.colorText));
            bottom_income.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.bottom_income,0,0);
        }
        if(noteFragment != null){
            transaction.hide(noteFragment);
            bottom_note.setTextColor(getResources()
                    .getColor(R.color.colorText));
            bottom_note.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.bottom_note,0,0);
        }
        if(mineFragment != null){
            transaction.hide(mineFragment);
            bottom_mine.setTextColor(getResources()
                    .getColor(R.color.colorText));
            bottom_mine.setCompoundDrawablesWithIntrinsicBounds(0,
                    R.drawable.bottom_mine,0,0);
        }
    }
}
