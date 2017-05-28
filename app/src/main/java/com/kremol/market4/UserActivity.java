package com.kremol.market4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    TextView nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user = (User) getIntent().getSerializableExtra("user");

        /*获得控件*/
        headImage = (ImageView) findViewById(R.id.user_head_information);
        nickName = (TextView) findViewById(R.id.user_nick_name);

        /*
         设置用户属性
         */
        headImage.setImageResource(user.getHeadPortraits());
        nickName.setText(user.getNickname());


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
