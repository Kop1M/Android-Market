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
import android.renderscript.Script;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kremol.market4.R;
import com.kremol.market4.UserMsgActivity;

public class UserEditActivity extends AppCompatActivity {

    private ImageButton imgBtuEditPhoto;
    protected Toolbar mToolbar;
    private String username;
    private String password;
    private String address;
    private String telephone;
    private String nikename;
    private int image;
    private EditText et_username;
    private EditText et_userpsw;
    private EditText et_user_tel;
    private EditText et_user_addre;
    private EditText et_user_nikeN;
    private Button butEdit;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        et_username = (EditText)findViewById(R.id.et_username);
        et_userpsw = (EditText)findViewById(R.id.et_userpsw);
        et_user_tel = (EditText)findViewById(R.id.et_user_tel);
        et_user_addre = (EditText)findViewById(R.id.et_user_addre);
        et_user_nikeN = (EditText)findViewById(R.id.et_user_nikeN) ;
        butEdit = (Button)findViewById(R.id.butEdit) ;

        bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        password = bundle.getString("password");
        address = bundle.getString("address");
        telephone = bundle.getString("telephone");
        nikename = bundle.getString("nikename");
        //image = bundle.getInt("image");

        et_username.setText(username);
        et_userpsw.setText(password);
        et_user_tel.setText(telephone);
        et_user_addre.setText(address);
        et_user_nikeN.setText(nikename);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        mToolbar.setNavigationIcon(R.drawable.icback);
        mToolbar.setTitle("编辑个人信息");
        //mToolbar.setLogo(R.drawable.market);//设计图片
        setSupportActionBar(mToolbar);

        imgBtuEditPhoto = (ImageButton)findViewById(R.id.imgBtuEditPhoto);
        Bitmap bmBitmap = toRoundBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.meimei));
        imgBtuEditPhoto.setImageBitmap(bmBitmap);



        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(UserEditActivity.this,UserMsgActivity.class);
                // bundle.putInt("image",image);
                a.putExtras(bundle);
                startActivity(a);
            }
        });

        butEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(UserEditActivity.this,UserMsgActivity.class);
                User user = new User();
                user.setUser_name(et_username.getText().toString() );
                user.setPassword(et_userpsw.getText().toString() );
                user.setTelephonenumber(et_user_tel.getText().toString() );
                user.setAddress(et_user_addre.getText().toString() );
                user.setNickname(et_user_nikeN.getText().toString() );
                a.putExtra("user",user);
                startActivity(a);
                finish();
            }
        });

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
