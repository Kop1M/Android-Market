package com.kremol.market4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserMsgActivity extends AppCompatActivity {

    protected Toolbar mToolbar;
    private TextView txtusername;
    private TextView  txttelephone;
    private TextView  txtaddress;
    private TextView  txtnikeName;
    private ImageView ivImageView;
    private String username;
    private String password;
    private String address;
    private String telephone;
    private String nikename;
    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

       User user = (User) getIntent().getSerializableExtra("user");
        username = user.getUser_name();
        password = user.getPassword();
        address =user.getAddress();
        telephone = user.getTelephonenumber();
        nikename = user.getNickname();

        //image = bundle.getInt("image");
        //实体化
        ivImageView = (ImageView) findViewById(R.id.userPhoto);
        txtusername = (TextView)findViewById(R.id.txtUsername);
        txttelephone = (TextView)findViewById(R.id.txtUserTel);
        txtaddress = (TextView)findViewById(R.id.txtUserAdd);
        txtnikeName = (TextView)findViewById(R.id.txtUserNike);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mToolbar.setNavigationIcon(R.drawable.icback);
        mToolbar.setTitle("个人信息");
        //mToolbar.setLogo(R.drawable.market);//设计图片
        setSupportActionBar(mToolbar);
        Bitmap bmBitmap = toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.meimei));
        ivImageView.setImageBitmap(bmBitmap);
        txtusername.setText(username);
        txttelephone.setText(telephone);
        txtaddress.setText(address);
        txtnikeName.setText(nikename);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(UserMsgActivity.this,MainActivity.class);
                Bundle bundle=new Bundle();
                User user = new User();
                user.setUser_name(username );
                user.setPassword(password );
                user.setTelephonenumber(telephone );
                user.setAddress(address );
                user.setNickname(nikename );
                //bundle.putInt("image",image);
                a.putExtra("user",user);
                startActivity(a);
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        Intent a = new Intent(UserMsgActivity.this,UserEditActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("username",username);
                        bundle.putString("password",password);
                        bundle.putString("telephone",telephone);
                        bundle.putString("address",address);
                        bundle.putString("nikename",nikename);
                        //bundle.putInt("image",image);
                        a.putExtras(bundle);
                        startActivity(a);
                        break;
                }
                return false;
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.acount_menu, menu);
        return true;
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {//把图片转换为圆形函数
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }


}

