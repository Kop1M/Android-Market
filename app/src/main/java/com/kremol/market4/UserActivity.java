package com.kremol.market4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.debuggable;
import static android.R.attr.start;
import static android.R.attr.switchMinWidth;
import static android.R.attr.y;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    User user;
    ImageView headImage;
    TextView nickName,returnLook,shopCart,indent,release,myshop,signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();           //隐藏ActionBar

        user = (User) getIntent().getSerializableExtra("user");


        /*获得控件*/
        headImage = (ImageView) findViewById(R.id.user_head_information);
        nickName = (TextView) findViewById(R.id.user_nick_name);
        returnLook = (TextView) findViewById(R.id.user_return_look);
        shopCart = (TextView) findViewById(R.id.shopcart);
        indent = (TextView) findViewById(R.id.indent);
        release = (TextView) findViewById(R.id.release);
        myshop = (TextView) findViewById(R.id.myshop);
        signOut = (TextView) findViewById(R.id.sign_out);
        /*
         设置用户属性
         */
        headImage.setImageResource(user.getHeadPortraits());
        nickName.setText(user.getNickname());

        /*设置监听*/
        returnLook.setOnClickListener(this);
        shopCart.setOnClickListener(this);
        indent.setOnClickListener(this);
        release.setOnClickListener(this);
        myshop.setOnClickListener(this);
        signOut.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //进入模块接口
            case R.id.shopcart:
                break;
            case R.id.indent:
                break;
            case R.id.release:
                break;
            case R.id.myshop:
                break;
            case R.id.user_return_look:
                Intent intent = new Intent(UserActivity.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                break;
            case R.id.sign_out:
                break;
            default:
                break;
        }
    }
}
