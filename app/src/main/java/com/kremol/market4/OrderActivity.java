package com.kremol.market4;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kremol.market4.MainActivity;
import com.kremol.market4.Orders;
import com.kremol.market4.Product;
import com.kremol.market4.R;
import com.kremol.market4.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    private String username;
    private String password;
    private String address;
    private String telephone;
    private String nikename;
    private int image;
    private ListView orderListView;
    private String orderResponseText;
    private String productResponsetext;
    private List<Orders> ordersList = new ArrayList<Orders>();
    private List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();//订单列表
    Map<String,Object> map;
    private List<Product> productsList = new ArrayList<Product>();
    private Product product;
    private Orders order;
    private int orderId;
    private User user;
    private SimpleAdapter adapter;
    private Bundle bundle = new Bundle();
    private int count = 0;
    private String getAllOrdersURL = "http://47.93.249.197:8080/secondary/getAllOrderServlet?userforbuyer=";
    private String getProductURL = "http://47.93.249.197:8080/secondary/getProductByIdServlet?product_id=";
    private String deleteOrderURL = "http://47.93.249.197:8080/secondary/deleteOrderServlet?order_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderListView = (ListView)findViewById(R.id.orderListView) ;



        user = (User) getIntent().getSerializableExtra("user");
        username = user.getUser_name();
        password = user.getPassword();
        address =user.getAddress();
        telephone = user.getTelephonenumber();
        nikename = user.getNickname();
        //image = bundle.getInt("image");
        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mToolbar.setNavigationIcon(R.drawable.icback);
        mToolbar.setTitle("我的订单");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(OrderActivity.this,MainActivity.class);
                a.putExtras(bundle);
                startActivity(a);
            }
        });
        new Thread(new GetOrderRunner()).start();




    }

    private class GetOrderRunner implements Runnable {
        @Override
        public void run(){
            //System.out.println("0000000000000000000000000");
            String url = getAllOrdersURL + username;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    orderResponseText = EntityUtils.toString(response.getEntity());//返回的参数
                    orderHandler.sendEmptyMessage(1);
                    //System.out.print("1111111111111111111111111111111111111111111");
                }else{
                    orderHandler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Handler orderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                orderList = new ArrayList<Map<String,Object>>();
                productsList = new ArrayList<Product>();
                ordersList = new ArrayList<Orders>();

                ordersList = gson.fromJson(orderResponseText,new TypeToken<List<Orders>>(){}.getType());
                System.out.println("2222222222222222222222222222222222222222222"+ ordersList.toString());
                // Toast.makeText(OrderActivity.this,ordersList.toString(),Toast.LENGTH_SHORT).show();

                adapter = new SimpleAdapter(OrderActivity.this,orderList,R.layout.activity_orderlist_iitem,
                        new String[]{"orderTime","orderState","orderPrice","orderAbout","orderType","orderTitle"},
                        new int[]{R.id.txtTime,R.id.txtState,R.id.txtPrice,R.id.txtAbout,R.id.txtType,R.id.txtTitle});

                System.out.println("+++++++++++++++++++++++++" + ordersList.size());

                if(ordersList.size() != 0){
                    new Thread(new GetProductRunner()).start();
                }else {
                    orderListView.setAdapter(adapter);
                }
            }
        }
    };

    //得到订单对应的货物的信息
    private class GetProductRunner implements Runnable {
        @Override
        public void run(){

            String url = getProductURL+ordersList.get(count).getProduct_id();
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    productResponsetext = EntityUtils.toString(response.getEntity());//返回的参数
                    productHandler.sendEmptyMessage(1);
                }else{
                    productHandler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //得到订单对应的货物信息
    private Handler productHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                product = gson.fromJson(productResponsetext,new TypeToken<Product>(){}.getType());

                if(product.getAbout() != ""){
                    productsList.add(product);
                }
                count++;
                if(count < ordersList.size()){
                    new Thread(new GetProductRunner()).start();
                }else{

                    System.out.println("-----------------------------" + productsList.toString());

                    for(int j = 0;j<productsList.size();j++){
                        order = ordersList.get(j);
                        product = productsList.get(j);

                        map = new HashMap<String,Object>();
                        map.put("orderTitle", "订单名称" + product.getTitle());
                        map.put("orderTime","发布时间" + order.getOrder_time());
                        map.put("orderStata",order.getOrder_state());
                        map.put("orderPrice","总价" + order.getTotal_price());
                        map.put("orderAbout", product.getAbout());
                        map.put("orderType","商品类型" + product.getType());
                        orderList.add(map);
                    }
                    orderListView.setAdapter(adapter);

                    orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            orderId = ordersList.get(position).getOrder_id();
                            AlertDialog.Builder builder=new AlertDialog.Builder(OrderActivity.this);
                            builder.setTitle("删除订单");
                            builder.setMessage("是否确认删除订单");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new Thread(new DeleteOrderRunner()).start();
                                        }
                                    }
                            );
                            builder.setNegativeButton("取消", null);
                            builder.show();

                            //Toast.makeText(OrderActivity.this,"-----------------" + orderId,Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }
    };



    //删除订单
    private class DeleteOrderRunner implements Runnable {
        @Override
        public void run(){

            String url = deleteOrderURL+ orderId;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String responsetext = EntityUtils.toString(response.getEntity());//返回的参数
                    Gson gson = new Gson();
                    Boolean a =gson.fromJson(responsetext,Boolean.class);
                    if(a){
                        deleteHandler.sendEmptyMessage(1);
                    }else
                        deleteHandler.sendEmptyMessage(0);
                }else{
                    deleteHandler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //重绘界面
    private Handler deleteHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                count = 0;
                new Thread(new GetOrderRunner()).start();
            }
        }
    };



}
