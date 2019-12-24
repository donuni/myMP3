package com.example.user.mymp3_2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {
    Context context;
    int layout;
    private ArrayList<MainData> arrayList;
    String selectFileNameString;
    View starView;
    float star;
    public MainAdapter(Context context, int layout, ArrayList<MainData> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    //처음생성될때와같은생명주기와같다고생각할것
    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    //실제추가되었을때
    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder customViewHolder, final int i) {

        customViewHolder.tvFileName.setText(arrayList.get(i).getFileName());
        customViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(customViewHolder.getAdapterPosition());
            }
        });

        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),arrayList.get(i).getFileName()+"재생",Toast.LENGTH_SHORT).show();

                //
                selectFileNameString = arrayList.get(i).getFileName();
                MainActivity.btnStop.callOnClick();
                MainActivity.btnPlay.callOnClick();

                //
            }
        });

        customViewHolder.tvAddMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFileNameString = arrayList.get(i).getFileName();
                starView=View.inflate(context, R.layout.addmylist, null);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("별점 주기");
                dialog.setView(starView);
                final RatingBar ratingBar =starView.findViewById(R.id.ratingBar);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        star=v;
                    }
                });
                dialog.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context,star+"점으로 My List 등록", Toast.LENGTH_SHORT).show();
                        MainActivity.addMyListData();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return   ((arrayList != null) ? arrayList.size() : 0 );
    }

    public void remove(int position){
        try {

            MainData removeMainData=arrayList.get(position);
            MainActivity.myDBHelper.delete(removeMainData.getFileName());
            arrayList.remove(position);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvFileName;
        protected TextView tvPlay;
        protected TextView tvDelete;
        protected TextView tvAddMyList;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvFileName = itemView.findViewById(R.id.tvFileName);
            this.tvPlay = itemView.findViewById(R.id.tvPlay);
            this.tvDelete = itemView.findViewById(R.id.tvDelete);
            this.tvAddMyList = itemView.findViewById(R.id.tvAddMyList);
        }
    }
}
