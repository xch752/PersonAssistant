package com.example.fragment.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fragment.R;
import com.example.fragment.model.IncomeTable;
import com.example.fragment.model.PayTable;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class QueryMoneyActivity extends AppCompatActivity {
    private EditText minMoney,maxMoney;
    private Button queryMoneyActivity;
    private Double moneyD1=0.00,moneyD2=0.00;
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
        setContentView(R.layout.activity_query_money);
        findByID();
        initEditText();
        ininQueryMoneyButton();
    }

    private void initEditText(){
        maxMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        maxMoney.setFilters(new InputFilter[]{new InputFilter() {
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
        minMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        minMoney.setFilters(new InputFilter[]{new InputFilter() {
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
        if(TextUtils.isEmpty(minMoney.getText())){
            moneyD1=0.00;
        }
        else {
            moneyD1=Double.valueOf(minMoney.getText().toString());
        }

        if(TextUtils.isEmpty(maxMoney.getText())){
            moneyD2=0.00;
        }
        else {
            moneyD2=Double.valueOf(maxMoney.getText().toString());
        }

        if(moneyD1>moneyD2){
            Double t = moneyD1;
            moneyD1 = moneyD2;
            moneyD2 = t;
        }
    }

    private void ininQueryMoneyButton(){
        queryMoneyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEditMoney();
                queryMoneyPay();
                queryMoneyIncome();
            }
        });
    }

    private void queryMoneyPay(){
        ObjectID.clear();
        data.clear();
        BmobQuery<PayTable> query1 = new BmobQuery<>();
        query1.addWhereGreaterThanOrEqualTo("money",moneyD1);
        BmobQuery<PayTable> query2 = new BmobQuery<>();
        query2.addWhereLessThanOrEqualTo("money",moneyD2);
        List<BmobQuery<PayTable>> andQuerys = new ArrayList<BmobQuery<PayTable>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        BmobQuery<PayTable> query = new BmobQuery<PayTable>();
        query.and(andQuerys);
        query.findObjects(new FindListener<PayTable>() {
            @Override
            public void done(List<PayTable> list, BmobException e) {
                if(e==null){
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
                        item.put("money", "-"+ list.get(i).getMoney());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("free", list.get(i).getFree());
                        data.add(item);
                    }
                    adapter = new SimpleAdapter(QueryMoneyActivity.this, data,
                            R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                            new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            /*Toast.makeText(QueryMoneyActivity.this, ObjectID.get(i), Toast.LENGTH_LONG).show();*/
                            Intent intent=new Intent(QueryMoneyActivity.this, PayItemActivity.class);
                            intent.putExtra("objectID",ObjectID.get(i));
                            startActivity(intent);
                        }
                    });
                    /*Toast.makeText(QueryMoneyActivity.this,"sucess:查询到"+list.size()+"条记录",Toast.LENGTH_LONG).show();*/
                }else{
                    Toast.makeText(QueryMoneyActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void queryMoneyIncome(){
        ObjectID1.clear();
        data1.clear();
        BmobQuery<IncomeTable> query1 = new BmobQuery<>();
        query1.addWhereGreaterThanOrEqualTo("money",moneyD1);
        BmobQuery<IncomeTable> query2 = new BmobQuery<>();
        query2.addWhereLessThanOrEqualTo("money",moneyD2);
        List<BmobQuery<IncomeTable>> andQuerys = new ArrayList<BmobQuery<IncomeTable>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        BmobQuery<IncomeTable> query = new BmobQuery<IncomeTable>();
        query.and(andQuerys);
        query.findObjects(new FindListener<IncomeTable>() {
            @Override
            public void done(List<IncomeTable> list, BmobException e) {
                if(e==null){
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
                        item.put("money", "+"+ list.get(i).getMoney());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("free", list.get(i).getFree());
                        data1.add(item);
                    }
                    adapter1 = new SimpleAdapter(QueryMoneyActivity.this, data1,
                            R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                            new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                    listView1.setAdapter(adapter1);
                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            /*Toast.makeText(QueryMoneyActivity.this, ObjectID1.get(i), Toast.LENGTH_LONG).show();*/
                            Intent intent=new Intent(QueryMoneyActivity.this, IncomeItemActivity.class);
                            intent.putExtra("objectID1",ObjectID1.get(i));
                            startActivity(intent);
                        }
                    });
                    /*Toast.makeText(QueryMoneyActivity.this,"sucess:查询到"+list.size()+"条记录",Toast.LENGTH_LONG).show();*/
                }else{
                    Toast.makeText(QueryMoneyActivity.this,"fail",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void findByID(){
        minMoney = (EditText) findViewById(R.id.payMinMoney);
        maxMoney = (EditText) findViewById(R.id.payMaxMoney);
        queryMoneyActivity = (Button) findViewById(R.id.payQueryMoneyActivity);
        listView = (ListView) findViewById(R.id.payListViewMoney);
        listView1 = (ListView) findViewById(R.id.incomeListViewMoney);
    }
}

