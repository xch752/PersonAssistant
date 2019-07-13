package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fragment.R;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.fragment.model.IncomeTable;
import com.example.fragment.model.PayTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class QueryDateActivity extends AppCompatActivity {
    private TextView startDate,endDate;
    private Date startDateD=new Date(),endDateD=new Date();
    private Button queryDateActivity;
    private ListView listView;
    private ListView listView1;
    private SimpleAdapter adapter;
    private SimpleAdapter adapter1;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();
    public List<String> ObjectID = new ArrayList<>();
    public List<String> ObjectID1 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_date);
        findById();
        initDateStart();
        ininQueryMoneyButton();
    }

    private void showDateStart() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                startDate.setText(time);
                startDateD = date_1;
                Toast.makeText(QueryDateActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
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

    private void showDateEnd() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                endDate.setText(time);
                endDateD = date_1;
                Toast.makeText(QueryDateActivity.this,date_1.toString(),Toast.LENGTH_LONG).show();
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

    private void initDateStart(){
        startDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.payStartDate:
                        showDateStart();
                        break;
                }
            }
        });

        endDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.payEndDate:
                        showDateEnd();
                        break;
                }
            }
        });
    }

    private void ininQueryMoneyButton(){
        queryDateActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryDatePay();
                queryDateIncome();
            }
        });
    }

    private void queryDatePay(){
        ObjectID.clear();
        data.clear();
        BmobDate bmobCreatedAtDateStart = new BmobDate(startDateD);
        BmobDate bmobCreatedAtDateEnd = new BmobDate(endDateD);
        BmobQuery<PayTable> BmobQueryStart = new BmobQuery<>();
        BmobQueryStart.addWhereGreaterThanOrEqualTo("date", bmobCreatedAtDateStart);
        BmobQuery<PayTable> BmobQueryEnd = new BmobQuery<>();
        BmobQueryEnd.addWhereLessThanOrEqualTo("date", bmobCreatedAtDateEnd);
        List<BmobQuery<PayTable>> queries = new ArrayList<>();
        queries.add(BmobQueryStart);
        queries.add(BmobQueryEnd);
        BmobQuery<PayTable> BmobQuery = new BmobQuery<>();
        BmobQuery.and(queries);
        BmobQuery.findObjects(new FindListener<PayTable>() {
            @Override
            public void done(List<PayTable> list, BmobException e) {
                if (e == null) {
                    int size = data.size();
                    if(size>0){
                        data.removeAll(data);
                        adapter.notifyDataSetChanged();
                    }
                    for(int i=0;i<list.size();i++){
                        Map<String, Object> item = new HashMap<String, Object>();
                        switch (list.get(i).getType()){
                            case "餐饮食品":
                                item.put("image", R.drawable.food);
                                break;
                            case "衣服饰品":
                                item.put("image", R.drawable.clothes);
                                break;
                            case "居家生活":
                                item.put("image", R.drawable.home);
                                break;
                            case "行车交通":
                                item.put("image", R.drawable.trans);
                                break;
                            case "休闲娱乐":
                                item.put("image", R.drawable.game);
                                break;
                            case "文化教育":
                                item.put("image", R.drawable.book);
                                break;
                            case "健康医疗":
                                item.put("image", R.drawable.medic);
                                break;
                            case "投资支出":
                                item.put("image", R.drawable.invest);
                                break;
                            case "其他支出":
                                item.put("image", R.drawable.other);
                                break;
                            default:
                                item.put("image", R.drawable.other);
                                break;
                        }
                        ObjectID.add(list.get(i).getObjectId());
                        item.put("type",list.get(i).getType());
                        item.put("money", "+"+ list.get(i).getMoney());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("free", list.get(i).getFree());
                        data.add(item);
                    }
                    adapter = new SimpleAdapter(QueryDateActivity.this, data,
                            R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                            new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            Intent intent=new Intent(QueryDateActivity.this, PayItemActivity.class);
                            intent.putExtra("objectID",ObjectID.get(i));
                            startActivity(intent);
                        }
                    });
                    /*Toast.makeText(QueryDateActivity.this,"sucess:查询到"+list.size()+"条记录",Toast.LENGTH_LONG).show();*/
                } else {
                    Toast.makeText(QueryDateActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void queryDateIncome(){
        ObjectID1.clear();
        data1.clear();
        BmobDate bmobCreatedAtDateStart = new BmobDate(startDateD);
        BmobDate bmobCreatedAtDateEnd = new BmobDate(endDateD);
        BmobQuery<IncomeTable> BmobQueryStart = new BmobQuery<>();
        BmobQueryStart.addWhereGreaterThanOrEqualTo("date", bmobCreatedAtDateStart);
        BmobQuery<IncomeTable> BmobQueryEnd = new BmobQuery<>();
        BmobQueryEnd.addWhereLessThanOrEqualTo("date", bmobCreatedAtDateEnd);
        List<BmobQuery<IncomeTable>> queries = new ArrayList<>();
        queries.add(BmobQueryStart);
        queries.add(BmobQueryEnd);
        BmobQuery<IncomeTable> BmobQuery = new BmobQuery<>();
        BmobQuery.and(queries);
        BmobQuery.findObjects(new FindListener<IncomeTable>() {
            @Override
            public void done(List<IncomeTable> list, BmobException e) {
                if (e == null) {
                    int size = data1.size();
                    if(size>0){
                        data1.removeAll(data1);
                        adapter1.notifyDataSetChanged();
                    }
                    for(int i=0;i<list.size();i++){
                        Map<String, Object> item = new HashMap<String, Object>();
                        switch (list.get(i).getType()){
                            case "工资薪水":
                                item.put("image", R.drawable.money1);
                                break;
                            case "礼金":
                                item.put("image", R.drawable.money2);
                                break;
                            case "贷款":
                                item.put("image", R.drawable.money3);
                                break;
                            case "提成":
                                item.put("image", R.drawable.money4);
                                break;
                            case "赞助费":
                                item.put("image", R.drawable.money5);
                                break;
                            case "公司奖金":
                                item.put("image", R.drawable.money6);
                                break;
                            case "兼职":
                                item.put("image", R.drawable.money7);
                                break;
                            case "投资收益":
                                item.put("image", R.drawable.money8);
                                break;
                            case "其他收入":
                                item.put("image", R.drawable.money9);
                                break;
                            default:
                                item.put("image", R.drawable.money9);
                                break;
                        }
                        ObjectID1.add(list.get(i).getObjectId());
                        item.put("type",list.get(i).getType());
                        item.put("money", "-"+ list.get(i).getMoney());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("free", list.get(i).getFree());
                        data1.add(item);
                    }
                    adapter1 = new SimpleAdapter(QueryDateActivity.this, data1,
                            R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                            new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                    listView1.setAdapter(adapter1);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            Intent intent=new Intent(QueryDateActivity.this, IncomeItemActivity.class);
                            intent.putExtra("objectID1",ObjectID1.get(i));
                            startActivity(intent);
                        }
                    });
                    /*Toast.makeText(QueryDateActivity.this,"sucess:查询到"+list.size()+"条记录",Toast.LENGTH_LONG).show();*/
                } else {
                    Toast.makeText(QueryDateActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findById(){
        startDate = (TextView)findViewById(R.id.payStartDate);
        endDate = (TextView)findViewById(R.id.payEndDate);
        queryDateActivity = (Button)findViewById(R.id.payQueryDateActivity);
        listView = (ListView) findViewById(R.id.payListViewDate);
        listView1 = (ListView) findViewById(R.id.incomeListViewDate);
    }
}

