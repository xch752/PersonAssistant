package com.example.fragment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fragment.R;
import android.content.Intent;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.fragment.activity.AddIncomeActivity;
import com.example.fragment.activity.IncomeItemActivity;
import com.example.fragment.model.IncomeTable;
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
public class IncomeFragment extends Fragment {
    public List<String> ObjectID = new ArrayList<>();
    private ListView listView;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private SearchView mSearchView;
    private SimpleAdapter adapter;
    private Button queryMoney,queryDate;
    private FloatingActionButton incomeAddFAB,incomeRefreshFAB;

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        Bmob.initialize(this.getActivity(),"cd6ace4e1908e4ddac3f2fd44dc26208");
        listView =view.findViewById(R.id.listView2);
        mSearchView = view.findViewById(R.id.incomeSearchView);
        /*queryMoney = view.findViewById(R.id.incomeQueryMoney);
        queryDate = view.findViewById(R.id.incomeQueryDate);*/
        incomeAddFAB = view.findViewById(R.id.incomeAddFAB);
        incomeRefreshFAB = view.findViewById(R.id.incomeRefreshFAB);

        //initButtonQuery();
        initFloatActButton();
        queryAll();
        query();
        // Inflate the layout for this fragment

        return view;
    }

    private void queryAll(){
        data.clear();
        ObjectID.clear();
        BmobQuery<IncomeTable> query = new BmobQuery<>();
        query.findObjects(new FindListener<IncomeTable>() {
            @Override
            public void done(List<IncomeTable> list, BmobException e) {
                if(e==null){
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
                        ObjectID.add(list.get(i).getObjectId());
                        item.put("type",list.get(i).getType());
                        item.put("money","+"+ list.get(i).getMoney());
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
                            Intent intent=new Intent(getActivity().getApplicationContext(), IncomeItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("objectID1", ObjectID.get(i));
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
                BmobQuery<IncomeTable> query = new BmobQuery<>();
                query.addWhereEqualTo("type", s);
                query.findObjects(new FindListener<IncomeTable>() {
                    @Override
                    public void done(List<IncomeTable> list, BmobException e) {
                        if (e == null) {
                            int size = data.size();
                            if(size>0){
                                data.removeAll(data);
                                adapter.notifyDataSetChanged();
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
                                ObjectID.add(list.get(i).getObjectId());
                                item.put("type",list.get(i).getType());
                                item.put("money", "+"+list.get(i).getMoney());
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
                                    Intent intent=new Intent(getActivity().getApplicationContext(), IncomeItemActivity.class);
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
        incomeAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), AddIncomeActivity.class);
                getActivity().startActivity(intent);
            }
        });
        incomeRefreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_LONG).show();
                queryAll();
            }
        });
    }
}

