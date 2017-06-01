package com.kremol.market4;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GoodsDesActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    private User user;
    private String username;
    private String password;
    private String address;
    private String telephone;
    private String nikename;
    private EditText et_proudctname;
    private EditText et_productprice;
    private EditText et_productscr;
    private Spinner spinner;
    private Button desButton;
    private String productName;
    private int productPrice;
    private String productScr;
    private String productType;
    private String responseText;
    private String desGoodsURL = "http://47.93.249.197:8088/scondary/addProductServlet?product=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goods_des);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mToolbar.setNavigationIcon(R.drawable.icback);
        mToolbar.setTitle("发布商品");
        //mToolbar.setLogo(R.drawable.market);//设计图片
       setSupportActionBar(mToolbar);

        //实体化
        et_proudctname = (EditText)findViewById(R.id.et_proudctname) ;
        et_productprice = (EditText)findViewById(R.id.et_productprice) ;
        et_productscr = (EditText)findViewById(R.id.et_productscr) ;
        spinner = (Spinner)findViewById(R.id.spinner);
        desButton = (Button)findViewById(R.id.butDes);

       user = (User) getIntent().getSerializableExtra("user");
        username = user.getUser_name();
        password = user.getPassword();
        address =user.getAddress();
        telephone = user.getTelephonenumber();
        nikename = user.getNickname();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsDesActivity.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }
        });
/*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.goodsType);
                productType = type[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        desButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productName = et_proudctname.getText().toString();
                productPrice = Integer.parseInt(et_productprice.getText().toString());
                productScr = et_productscr.getText().toString();
                new Thread(new GoodsDesRunner()).start();

            }
        });

    }

    private class GoodsDesRunner implements Runnable{
        @Override
        public void run(){
            Gson gson = new Gson();
            SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
            Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
            String    publishtime    =    formatter.format(curDate);

            Product product = new Product();
            product.setAbout(productScr);
            product.setProductprice(productPrice);
            product.setType(productType);
            product.setTitle(productName);
            product.setPublishtime(publishtime);
            product.setType("电子");
            product.setUserforsale(username);

            String desGoodtoJson = gson.toJson(product);
            String url = desGoodsURL + desGoodtoJson;
            try {
                url = URLEncoder.encode(url,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                System.out.println( " +++++++++++++++++++++++++++++++++++++++++++++" + url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    responseText = EntityUtils.toString(response.getEntity());//返回的参数

                    System.out.println( " +++++++++++++++++++++++++++++++++++++++++++++" + responseText.toString());

                    desGoodsHandler.sendEmptyMessage(1);
                }else{
                    desGoodsHandler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private Handler desGoodsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == 1){
                Gson gson = new Gson();
                Boolean a = gson.fromJson(responseText,Boolean.class);
                if(a){
                    Toast.makeText(GoodsDesActivity.this,"发布商品成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(GoodsDesActivity.this,"发布商品失败",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

}

