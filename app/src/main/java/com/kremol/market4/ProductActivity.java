package com.kremol.market4;

import android.content.DialogInterface;
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

import okhttp3.Call;
import okhttp3.Response;

import static android.R.string.ok;
import static com.kremol.market4.MainActivity.INIT_PRODUCT_OK;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int ADDIN_SHOPCART = 0;

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
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().hide();           //隐藏ActionBar
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
       productPrice.setText(String.valueOf(product.getProductprize()));

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
                Orders  order = new Orders();
                order.setUserforbuyer(user.getNickname());
                order.setTotal_price(product.getProductprize());
                order.setPrudut_id(product.getProduct_id());
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
                            createDialog("加入购物车","加入失败");
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
                    createDialog("加入购物车","加入失败");
                    e.printStackTrace();
                }
                break;

            /*调到订单结算界面*/
            case R.id.buy_now:
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
