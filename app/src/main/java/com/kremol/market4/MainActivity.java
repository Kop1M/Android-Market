package com.kremol.market4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.id;
import static android.R.attr.type;
import static android.R.id.list;
import static android.R.string.ok;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;
import static com.kremol.market4.R.id.new_thing;
import static com.kremol.market4.R.id.other;
import static com.kremol.market4.R.layout.entry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    //五种商品类别
    enum TYPE{NEWTHING,ELECTRONICS,CLOTHES,BOOK,OHTER,};

    //一个适配器
    private EntryAdapter entryAdapter;

    TextView newthingText,electronicsText,bookText,clothesText,otherText;
    ListView listView;
    ImageView headImg;

    User user;      //用户

    int typeNow = 0;   //当前选择的类别
    List<Product> productList;      //所有商品
    List<List<Entry>>   entryLists;     //5个entryList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();           //隐藏ActionBar

        /*从别的Activity得到user*/
        user = (User) getIntent().getSerializableExtra("user");

        /*得到控件*/
            listView = (ListView) findViewById(R.id.list_view);
//        newthingText = (TextView) findViewById(R.id.new_thing);
//        electronicsText = (TextView) findViewById(R.id.electronics);
//        bookText = (TextView) findViewById(R.id.book);
//        clothesText = (TextView) findViewById(R.id.clothes);
//        otherText = (TextView) findViewById(R.id.other);
            headImg = (ImageView) findViewById(R.id.head_information);


        /*设置头像*/
        headImg.setImageResource(user.getHeadPortraits());

        /*点头像到头像界面*/
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        /*点击进入商品详情*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Entry entry = entryLists.get(typeNow).get(position);
                Intent intent = new Intent(MainActivity.this,ProductActivity.class);
                intent.putExtra("product",entry.getProduct());
                startActivity(intent);
            }
        });

        /*将product转成entry并分成5个类别，最终得到entryLists*/
        initProduct();
        List<Entry> entryList  = changeToEntryList(productList);
        entryLists = classifyEntry(entryList);

        /*初始时用"typeNow(最新")构造adpater并设置*/
        entryAdapter = new EntryAdapter(MainActivity.this,R.layout.entry,entryLists.get(typeNow));
        listView.setAdapter(entryAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*重新设置adpater*/
            case R.id.new_thing:
                typeNow = 0;
                break;
            case R.id.electronics:
                typeNow = 1;
                break;
            case R.id.book:
                typeNow = 2;
                break;
            case R.id.clothes:
                typeNow = 3;
                break;
            case R.id.other:
                typeNow = 4;
                break;
            default:
                break;
        }
        entryAdapter = new EntryAdapter(MainActivity.this,R.layout.entry,entryLists.get(typeNow));
        listView.setAdapter(entryAdapter);
    }



    //初始化商品
    public void initProduct(){
        try {
           String address = "http://localhost:8088/secondary/ GetAllProductsServlet";
            HttpUtil.sendHttpRequest(address, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    //获得productList
                    productList = gson.fromJson(responseData,new TypeToken<List<Product>>(){}.getType());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //将product转成entryList
    public List<Entry> changeToEntryList(List<Product> productList){
        List<Entry>entryList = new ArrayList<>();
        for(int i=0;i<productList.size();i++){
            Product product = productList.get(i);
            String nickName  = product.getUserforsale();
            String productName = product.getTitle();

            //第一个应该用商品user的head
            Entry entry = new Entry(0,nickName,product,productName);
            entryList.add(entry);
        }
        return entryList;
    }

    //把entryList分类成5个entryList
    public List<List<Entry>> classifyEntry(List<Entry> entryList){
        List<List<Entry>> entryLists = new ArrayList<>(5);
        int group = -1;
        for(int i=0;i<entryList.size();i++){
            Entry entry = entryList.get(i);
            String type = entry.getProduct().getType();
            if( type.equals("最新")){
                group = 0;
            }
            else if(type.equals("电子")){
                group = 1;
            }
            else if(type.equals("书籍")){
                group = 2;
            }
            else if(type.equals("衣物")){
                group = 3;
            }
            else if(type.equals("其他")){
                group = 4;
            }
            else {
                group = -1;
            }
            if(group>=0 && group<=4)
                entryLists.get(group).add(entry);
        }

        return entryLists;
    }

}
