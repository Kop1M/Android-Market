package com.kremol.market4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by flexible on 2017/5/27.
 */

public class EntryAdapter extends ArrayAdapter<Entry> {

    private int resourceId;

    public EntryAdapter(Context context, int resource, List<Entry> objects) {
        super(context, resource,objects);
        resourceId =resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entry entry = getItem(position);

        ViewHolder viewHolder;
        View view;
        if(convertView == null){
            view  = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.headImage = (ImageView) view.findViewById(R.id.head_portrait);
            viewHolder.nickName = (TextView) view.findViewById(R.id.nick_name);
            viewHolder.productName = (TextView) view.findViewById(R.id.product_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        ImageView headImage = viewHolder.headImage;
        TextView nickName = viewHolder.nickName;
        TextView productName = viewHolder.productName;
        headImage.setImageResource(entry.getHeadImage());
        nickName.setText(entry.getNickName());
        productName.setText(entry.getProductName());
        return view;
    }


    //同ViewHolder加速listview
    class ViewHolder{
        ImageView headImage;
        TextView nickName;
        TextView productName;
    }
}
