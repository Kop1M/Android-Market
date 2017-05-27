package com.kremol.market4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.kremol.market4.R.id.other;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    //五种商品类别
    enum TYPE{NEWTHING,ELECTRONICS,CLOTHES,BOOK,OHTER,};

    //一个适配器
    private EntryAdapter entryAdapter;

    TextView newthingText,electronicsText,bookText,clothesText,otherText;
    ListView listView;
    ImageView headImg;

    User user;      //用户


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();           //隐藏ActionBar




    }

    @Override
    public void onClick(View v) {

    }


}
