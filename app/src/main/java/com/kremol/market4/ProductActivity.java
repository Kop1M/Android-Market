package com.kremol.market4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.order;
import static android.R.string.ok;
import static com.kremol.market4.MainActivity.INIT_PRODUCT_OK;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int ADDIN_SHOPCART = 0;
    public static final int  ADD_FAILED = 1;

    Product product;
    User user;
    TextView productDescripotin,productPrice;
    ImageView productImage;
    Button sellerInfo,addinShopcart,buyNow;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADDIN_SHOPCART:
                    String id = String.valueOf(msg.arg1);
                    createDialog("加入购物车","已加入购物车，订单号为\n" + id);
                    break;
                case ADD_FAILED:
                    createDialog("加入购物车","加入失败");
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (Product) getIntent().getSerializableExtra("product");
        user= (User) getIntent().getSerializableExtra("user");

        /*找到控件*/
        productDescripotin = (TextView) findViewById(R.id.product_description);
        productPrice = (TextView) findViewById(R.id.product_price);
        productImage = (ImageView) findViewById(R.id.product_img);
        sellerInfo = (Button) findViewById(R.id.seller_information);
        addinShopcart = (Button) findViewById(R.id.add_in_shopcart);
        buyNow = (Button) findViewById(R.id.buy_now);

        /*设置product属性*/
        productDescripotin.setText(product.getAbout());
        productImage.setImageResource(R.drawable.changongzi);
       productPrice.setText(String.valueOf(product.getProductprice()));

        /*设置监听*/
        sellerInfo.setOnClickListener(this);
        addinShopcart.setOnClickListener(this);
        buyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*显示卖家信息*/
            case R.id.seller_information:
                createDialog("卖家信息",product.getUserforsale());
                break;

            /*生成一个订单发送给服务器*/
            case R.id.add_in_shopcart:
                /*构建订单*/
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time = df.format(new Date());
                Orders  order = new Orders();
                order.setUserforbuyer(user.getUser_name());
                order.setTotal_price(product.getProductprice());
                order.setProduct_id(product.getProduct_id());
                order.setOrder_time(time);
                order.setOrder_state(1);
                order.setUserforsaler(product.getUserforsale());
                order.setNumber(1);

                Gson gson = new Gson();
                String orderJson = gson.toJson(order);
                try {
                    String address = "http://47.93.249.197:8080/secondary/addOrderServlet?"+"order="+ URLEncoder
                            .encode(orderJson,"utf8");
                    HttpUtil.sendHttpRequest(address, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = ADD_FAILED;
                            handler.sendMessage(message);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Message message = new Message();
                            message.what = ADDIN_SHOPCART;
                            message.arg1 = Integer.valueOf(responseData);
                            handler.sendMessage(message);
                        }
                    });
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = ADD_FAILED;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
                break;

            /*调到订单结算界面*/
            case R.id.buy_now:
                  /*构建订单*/
                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String time2 = df2.format(new Date());
                Orders  order2 = new Orders();
                order2.setUserforbuyer(user.getUser_name());
                order2.setTotal_price(product.getProductprice());
                order2.setProduct_id(product.getProduct_id());
                order2.setOrder_time(time2);
                order2.setOrder_state(1);
                order2.setUserforsaler(product.getUserforsale());
                order2.setNumber(1);

                Gson gson2 = new Gson();
                String orderJson2 = gson2.toJson(order2);
                try {
                    String address = "http://47.93.249.197:8080/secondary/addOrderServlet?"+"order="+ URLEncoder
                            .encode(orderJson2,"utf8");
                    HttpUtil.sendHttpRequest(address, new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = ADD_FAILED;
                            handler.sendMessage(message);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Message message = new Message();
                            message.what = ADDIN_SHOPCART;
                            message.arg1 = Integer.valueOf(responseData);
                            handler.sendMessage(message);
                        }
                    });
                } catch (Exception e) {
                    Message message = new Message();
                    message.what = ADD_FAILED;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
                Intent a = new Intent(ProductActivity.this,OrderActivity.class);
                a.putExtra("user",user);
                startActivity(a);
                break;
            default:
                break;
        }
    }

    public void createDialog(String title, String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle(title);
        builder.setMessage(content);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
