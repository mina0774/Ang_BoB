package com.example.ang_bob;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private List<RoomListItem> items;
    Context context;
    int item_layout;
    public RoomAdapter(Context context, List<RoomListItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_listview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, final int position) {
        final RoomListItem item = items.get(position);
        holder.tv_title.setText(item.getRoom_title());
        holder.tv_total_num.setText(item.getTotal_people_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_total_num;
        private TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_total_num = (TextView) itemView.findViewById(R.id.tv_total_num);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

}
