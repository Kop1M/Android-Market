package com.kremol.market4;



/**
 * Created by flexible on 2017/6/1.
 */

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.order;
import static com.kremol.market4.MyshopActivity.INIT_ORDER_OK;


public class MyshopAdapter extends ArrayAdapter<Product> {

    private int resourceId;
    public Callback mCallback;

    public MyshopAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource,objects);
        resourceId =resource;
    }

    public interface  Callback{
         void click(View v,Product p);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final Product product = getItem(position);

        ViewHolder viewHolder;
        View view;
        if(convertView == null){
            view  = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.productName = (TextView) view.findViewById(R.id.myshop_product_name);
            viewHolder.deleteBtn = (Button) view.findViewById(R.id.delete);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        TextView productName = viewHolder.productName;
        Button deleteBtn = viewHolder.deleteBtn;
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,product);
            }
        });
        productName.setText(product.getTitle());
        return view;
    }


    //同ViewHolder加速listview
    class ViewHolder{
        TextView productName;
        Button deleteBtn;
    }
}

