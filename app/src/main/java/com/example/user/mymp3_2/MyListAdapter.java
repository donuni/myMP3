package com.example.user.mymp3_2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MainData> list;
    // 인플레이트를 진행하기 위한한
    private LayoutInflater layoutInflater;
    String selectMyData;

    public MyListAdapter(Context context, int layout, ArrayList<MainData> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //데이터 자료갯수를 리턴해줘야 한다.

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null){
            view = layoutInflater.inflate(layout,null);
        }
        final TextView myTvName = view.findViewById(R.id.myTvName);
        final TextView myTvStar = view.findViewById(R.id.myTvStar);
        final LinearLayout linear = view.findViewById(R.id.linear);
        MainData mainData=list.get(position);
        selectMyData= mainData.getFileName();
        myTvName.setText(mainData.getFileName());
        myTvStar.setText(mainData.getStar());


        return view;
    }

}
