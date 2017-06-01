package com.kremol.market4;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.attr.order;
import static android.R.id.list;
import static android.R.id.message;
import static com.kremol.market4.MainActivity.INIT_PRODUCT_OK;
import static java.security.AccessController.getContext;

public class MyshopActivity extends AppCompatActivity implements MyshopAdapter.Callback {

    public final static int INIT_ORDER_OK = 2;

    List<Product> ProductList;
    private ListView listView;

    User user;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_ORDER_OK:
                   // MyshopAdapter myshopAdapter = new MyshopAdapter(MyshopActivity.this, R.layout.thing, ProductList,this);
                  //  listView.setAdapter(myshopAdapter);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshop);



        //user = (User) getIntent().getSerializableExtra("user");
        user = new User();
        user.setUser_name("张三");
        user.setHeadPortraits(R.drawable.changongzi);
        user.setNickname("cannon");
        user.setUser_id(1);
        listView = (ListView) findViewById(R.id.myshop_listview);


        myshopInit();

    }


    public void myshopInit() {
        try {
            String address = "http://47.93.249.197:8080/secondary/getProductByNameServlet?" + "userforsale=" + user.getUser_name();
            HttpUtil.sendHttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    //获得productList
                    ProductList = gson.fromJson(responseData, new TypeToken<List<Product>>() {
                    }.getType());
                    Message message = new Message();
                    message.what = INIT_ORDER_OK;
                    handler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    @Override
//    public void deleteorder(int orderId) {
//        String address = "http://47.93.249.197:8080/secondary/deleteOrderServlet?" + "order_id=" + orderId;
//        HttpUtil.sendHttpRequest(address, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                if(responseData.equals("true")) {
//                    Message message = new Message();
//                    message.what = INIT_ORDER_OK;
//                    handler.sendMessage(message);
//                }
//            }
//        });
//    }

    @Override
    public void click(View v) {
        Toast.makeText(MyshopActivity.this, "delete", Toast.LENGTH_SHORT).show();
    }
}
