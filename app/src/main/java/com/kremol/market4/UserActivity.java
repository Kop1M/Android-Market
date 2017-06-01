package com.kremol.market4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.debuggable;
import static android.R.attr.start;
import static android.R.attr.switchMinWidth;
import static android.R.attr.y;
import static com.kremol.market4.R.id.indent;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    User user;
    ImageView headImage;
    TextView nickName;
    Button returnLook,shopCart,indent,release,myshop,signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        user = (User) getIntent().getSerializableExtra("user");


        /*获得控件*/
        headImage = (ImageView) findViewById(R.id.user_head_information);
        nickName = (TextView) findViewById(R.id.user_nick_name);
        returnLook = (Button) findViewById(R.id.user_return_look);
        shopCart = (Button) findViewById(R.id.shopcart);
        indent = (Button) findViewById(R.id.indent);
        release = (Button) findViewById(R.id.release);
        myshop = (Button) findViewById(R.id.myshop);
        signOut = (Button) findViewById(R.id.sign_out);
        /*
         设置用户属性
         */
        headImage.setImageResource(user.getHeadPortraits());
        nickName.setText(user.getNickname());

        /*设置监听*/
        headImage.setOnClickListener(this);
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
            case R.id.user_head_information:
                Intent imgIntent = new Intent(UserActivity.this,UserMsgActivity.class);
                imgIntent.putExtra("user",user);
                startActivity(imgIntent);
                break;
            case R.id.indent:
                Intent intentIndent = new Intent(UserActivity.this,OrderActivity.class);
                intentIndent.putExtra("user",user);
                startActivity(intentIndent);
                break;
            case R.id.release:
                Intent intentrelease = new Intent(UserActivity.this,GoodsDesActivity.class);
                intentrelease.putExtra("user",user);
                startActivity(intentrelease);
                break;
            case R.id.myshop:

                break;
            case R.id.user_return_look:
                Intent intent = new Intent(UserActivity.this,MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                break;
            case R.id.sign_out:
                Intent signoutIntent = new Intent(this,LoginActivity.class);
                startActivity(signoutIntent);
                break;
            default:
                break;
        }
    }
}
