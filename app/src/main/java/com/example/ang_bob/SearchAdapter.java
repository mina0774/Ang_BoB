package com.example.ang_bob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<String> shop_list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<String> list,Context context){
        this.shop_list=list;
        this.context=context;
        this.inflate=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return shop_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.shop_listview,null);

            viewHolder = new ViewHolder();
            viewHolder.shop = (TextView) convertView.findViewById(R.id.shop);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        // 리스트에 있는 음식점 목록을 리스트뷰에 보이도록 함
        viewHolder.shop.setText(shop_list.get(position));

        return convertView;
    }

    class ViewHolder{
        public TextView shop;
    }

}
