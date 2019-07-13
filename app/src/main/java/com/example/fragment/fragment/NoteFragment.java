package com.example.fragment.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.TimePickerView;
import com.example.fragment.R;
import com.example.fragment.activity.AddNoteActivity;
import com.example.fragment.activity.NoteItemActivity;
import com.example.fragment.activity.PayItemActivity;
import com.example.fragment.activity.QueryDateActivity;
import com.example.fragment.model.Note;
import com.example.fragment.model.PayTable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

    public List<String> ObjectID = new ArrayList<>();
    private ListView listView;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private SimpleAdapter adapter;
    private Date noteStartDateD=new Date(),noteEndDateD=new Date();

    private Button noteSearch;
    private TextView noteStartDate,noteEndDate;
    private FloatingActionButton noteAddFAB,noteRefreshFAB;

    public NoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        noteAddFAB=view.findViewById(R.id.noteAddFAB);
        listView=view.findViewById(R.id.noteListView);
        noteRefreshFAB=view.findViewById(R.id.noteRefreshFAB);
        noteStartDate=view.findViewById(R.id.noteStartDate);
        noteEndDate=view.findViewById(R.id.noteEndDate);
        noteSearch=view.findViewById(R.id.noteSearch);

        initFAB();
        queryAll();
        initDateStart();
        initButton();



        // Inflate the layout for this fragment
        return view;
    }

    private void queryAll(){
        data.clear();
        ObjectID.clear();
        BmobQuery<Note> query = new BmobQuery<>();
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                if(e==null){
                    for(int i=0;i<list.size();i++){
                        Map<String, Object> item = new HashMap<String, Object>();
                        ObjectID.add(list.get(i).getObjectId());
                        item.put("title",list.get(i).getTitle());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("content","内容:"+list.get(i).getContent());
                        data.add(item);
                    }
                    adapter = new SimpleAdapter(getContext(), data,
                            R.layout.listview_item_note, new String[] { "title", "date","content"},
                            new int[] { R.id.textViewNote1, R.id.textViewNote2, R.id.textViewNote3});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            Intent intent=new Intent(getActivity().getApplicationContext(), NoteItemActivity.class);
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
        ObjectID.clear();
        data.clear();
        BmobDate bmobCreatedAtDateStart = new BmobDate(noteStartDateD);
        BmobDate bmobCreatedAtDateEnd = new BmobDate(noteEndDateD);
        BmobQuery<Note> BmobQueryStart = new BmobQuery<>();
        BmobQueryStart.addWhereGreaterThanOrEqualTo("date", bmobCreatedAtDateStart);
        BmobQuery<Note> BmobQueryEnd = new BmobQuery<>();
        BmobQueryEnd.addWhereLessThanOrEqualTo("date", bmobCreatedAtDateEnd);
        List<BmobQuery<Note>> queries = new ArrayList<>();
        queries.add(BmobQueryStart);
        queries.add(BmobQueryEnd);
        BmobQuery<Note> BmobQuery = new BmobQuery<>();
        BmobQuery.and(queries);
        BmobQuery.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                if(e==null){
                    int size = data.size();
                    if(size>0){
                        data.removeAll(data);
                        adapter.notifyDataSetChanged();
                    }
                    for(int i=0;i<list.size();i++){
                        Map<String, Object> item = new HashMap<String, Object>();
                        ObjectID.add(list.get(i).getObjectId());
                        item.put("title",list.get(i).getTitle());
                        item.put("date", list.get(i).getDate().getDate());
                        item.put("content","内容:"+list.get(i).getContent());
                        data.add(item);
                    }
                    adapter = new SimpleAdapter(getContext(), data,
                            R.layout.listview_item_note, new String[] { "title", "date","content"},
                            new int[] { R.id.textViewNote1, R.id.textViewNote2, R.id.textViewNote3});
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = new Long(id).intValue();
                            Intent intent=new Intent(getActivity().getApplicationContext(), NoteItemActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("objectID", ObjectID.get(i));
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }
                    });
                    Toast.makeText(getActivity(),"SUCESS QUERY:"+list.size()+"条记录",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"Fail Query",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initButton(){
        noteSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query();
            }
        });
    }

    private void showDateStart() {
        TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                noteStartDate.setText(time);
                noteStartDateD = date_1;
                Toast.makeText(getActivity(),date_1.toString(),Toast.LENGTH_LONG).show();
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
        TimePickerView pvTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date_1, View v) {
                String time = getTime(date_1);
                noteEndDate.setText(time);
                noteEndDateD = date_1;
                Toast.makeText(getActivity(),date_1.toString(),Toast.LENGTH_LONG).show();
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
        noteStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.noteStartDate:
                        showDateStart();
                        break;
                }
            }
        });

        noteEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.noteEndDate:
                        showDateEnd();
                        break;
                }
            }
        });
    }

    private void initFAB(){
        noteAddFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), AddNoteActivity.class);
                getActivity().startActivity(intent);
            }
        });

        noteRefreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryAll();
                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_LONG).show();
            }
        });
    }

}
