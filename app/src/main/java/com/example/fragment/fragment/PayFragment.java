package com.example.fragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fragment.R;
import com.example.fragment.activity.AddPayActivity;
import com.example.fragment.activity.PayItemActivity;
import com.example.fragment.model.PayTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment {
    public List<String> ObjectID = new ArrayList<>();
    private ListView listView;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private SearchView mSearchView;
    private SimpleAdapter adapter;
    private Button queryMoney,queryDate;
    private FloatingActionButton payAddFAB,payRefreshFAB;

    public PayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        Bmob.initialize(this.getActivity(),"cd6ace4e1908e4ddac3f2fd44dc26208");
        listView =view.findViewById(R.id.listView1);
        mSearchView = view.findViewById(R.id.paySearchView);
        /*queryMoney = view.findViewById(R.id.payQueryMoney);
        queryDate = view.findViewById(R.id.payQueryDate);*/
        payAddFAB = view.findViewById(R.id.payAddFAB);
        payRefreshFAB = view.findViewById(R.id.payRefreshFAB);

        /*initButtonQuery();*/
        initFloatActButton();
        queryAll();
        query();
        // Inflate the layout for this fragment

        return view;
    }



    private void queryAll(){
        data.clear();
        ObjectID.clear();
        BmobQuery<PayTable> query = new BmobQuery<>();
        query.findObjects(new FindListener<PayTable>() {
            @Override
            public void done(List<PayTable> list, BmobException e) {
                if(e==null){
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
                        item.put("money","-"+ list.get(i).getMoney());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("free", list.get(i).getFree());
                        data.add(item);
                    }
                    adapter = new SimpleAdapter(getContext(), data,
                            R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                            new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            Intent intent=new Intent(getActivity().getApplicationContext(), PayItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("objectID", ObjectID.get(i));
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),"Fail QueryAll",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void query(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                data.clear();
                ObjectID.clear();
                BmobQuery<PayTable> query = new BmobQuery<>();
                query.addWhereEqualTo("type", s);
                query.findObjects(new FindListener<PayTable>() {
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
                                item.put("money", "-"+ list.get(i).getMoney());
                                item.put("date", list.get(i).getDate().getDate());
                                item.put("free", list.get(i).getFree());
                                data.add(item);
                            }
                            adapter = new SimpleAdapter(getActivity(), data,
                                    R.layout.listview_item, new String[] { "image", "type","money","date","free"},
                                    new int[] { R.id.imageView1, R.id.textView1 , R.id.textView2,R.id.textView3,R.id.textView4});
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int i = new Long(id).intValue();
                                    Intent intent=new Intent(getActivity().getApplicationContext(), PayItemActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("objectID", ObjectID.get(i));
                                    intent.putExtras(bundle);
                                    getActivity().startActivity(intent);
                                }
                            });
                            Toast.makeText(getActivity(),"SUCESS QUERY:"+list.size()+"条记录",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(),"FAIL QUERY",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    /*private void initButtonQuery(){
        queryMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), QueryMoneyActivity.class);
                getActivity().startActivity(intent);
            }
        });
        queryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), QueryDateActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }*/

    private void initFloatActButton(){
        payAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), AddPayActivity.class);
                getActivity().startActivity(intent);
            }
        });
        payRefreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_LONG).show();
                queryAll();
            }
        });
    }
}
