package com.kremol.market4;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


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
import java.util.ArrayList;
import java.util.List;



import static android.R.id.list;

public class LoginActivity extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private static final int LOGING=100;
    private static final int REGISTER=101;

    private TextView tv_login, tv_register;
    private ViewFlipper vf_login;
    private GestureDetector detector; // 手势检测
    private Animation leftInAnimation;
    private Animation leftOutAnimation;
    private Animation rightInAnimation;
    private Animation rightOutAnimation;
    private Context mContext;
    private AlertDialog dialog_login,dialog_register,dialog_confirm_login,dialog_loading;
    private ProgressBar progress_login;
    private User userRegist;
    private String responseText;
    private String username;
    private String password;
    private String address;
    private String telephone;
    private String usernike;
    private Gson gson;
    //URL
    private String loginURL = "http://47.93.249.197:8080/secondary/verifyUserServlet?username=";

    private String registerURL = "http://47.93.249.197:8088/scondary/addUserServlet?user=";
    //{"address":"天津市","headPortraits":0,"nickname":"王二小", "password":"12345", "telephonenumber":"2345678976543", "user_id":0,"user_name":"张三"}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initDatas();
        initListerer();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //Log.i("ming","msg: "+msg.obj.toString());
                    gson = new Gson();
                    User user =  gson.fromJson(responseText,User.class);
                    if (user.getUser_name().equals(username)){
                        dialog_loading.dismiss();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("user",user);
                        //bundle.putInt("image",user.getHeadPortraits());
                        startActivity(intent);
                        dialog_login.dismiss();
                        finish();
                    } else{
                        dialog_loading.dismiss();
                        Toast.makeText(getApplication(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    dialog_register.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle("注册成功");
                    builder.setMessage("是否立刻登录");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle=new Bundle();
                                    User user = new User();
                                    user.setUser_name(username);
                                    user.setPassword(password);
                                    user.setTelephonenumber(telephone);
                                    user.setAddress(address);
                                    user.setNickname(usernike);
                                    //bundle.putInt("image",user.getHeadPortraits());
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                    );
                    builder.setNegativeButton("取消", null);
                    dialog_confirm_login=builder.create();
                    dialog_confirm_login.show();
                    break;
                case 0 :
                    Toast.makeText(LoginActivity.this,"连接服务器错误",Toast.LENGTH_SHORT).show();
            }

        }
    };


    private void initViews() {
        mContext = this;
        progress_login=(ProgressBar)findViewById(R.id.progress_login);
        vf_login = (ViewFlipper) findViewById(R.id.vf_login);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
    }

    private void initDatas() {
        detector = new GestureDetector(this);

        vf_login.addView(getImageView(R.drawable.splash));
        vf_login.addView(getImageView(R.drawable.img_guide_item_one));
        vf_login.addView(getImageView(R.drawable.img_guide_item_two));

        // 动画效果
        leftInAnimation = AnimationUtils.loadAnimation(this, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(this, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(this, R.anim.right_in);
        rightOutAnimation = AnimationUtils
                .loadAnimation(this, R.anim.right_out);

        dialog_loading=new AlertDialog.Builder(mContext).create();
        View view= LayoutInflater.from(mContext).inflate(R.layout.dialog_loading,null);
        dialog_loading.setView(view);
    }

    private void initListerer() {
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                showLoginDialog();
                break;
            case R.id.tv_register:
                showRegisterDialog();
                break;
        }
    }

    /**
     * 登录的dialog
     **/
    private void showLoginDialog() {
        dialog_login = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_login, null);
        Button btn_login = (Button) view.findViewById(R.id.btn_login);
        final EditText et_username =(EditText)view.findViewById(R.id.et_username);
        final EditText et_userpsw  =(EditText)view.findViewById(R.id.et_userpsw);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_userpsw.getText().toString();
                setLogin(username,password);

            }
        });
        dialog_login.setView(view);
        dialog_login.show();
    }

    /**
     * 注册的dialog
     **/
    private void showRegisterDialog() {
        dialog_register = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_register, null);
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        final EditText et_username=(EditText)view.findViewById(R.id.et_username);
        final EditText et_userpsw = (EditText)view.findViewById(R.id.et_userpsw);
        final EditText et_userpsw_again=(EditText)view.findViewById(R.id.et_userpsw_again);
        final EditText et_user_tel=(EditText)view.findViewById(R.id.et_user_tel);
        final EditText et_usernike  =(EditText)view.findViewById(R.id.et_usernike);
        final EditText et_useraddre  =(EditText)view.findViewById(R.id.et_useraddre);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                System.out.println("==========================" + username);
                password = et_userpsw.getText().toString();
                System.out.println("==========================" + password);
                telephone = et_user_tel.getText().toString();
                System.out.println("==========================" + telephone);
                address = et_useraddre.getText().toString();
                System.out.println("==========================" + address);
                usernike = et_usernike.getText().toString();
                System.out.println("==========================" + usernike);

                userRegist = new User();
                userRegist.setAddress(address);
                userRegist.setNickname(usernike);
                userRegist.setUser_name(username);
                userRegist.setPassword(password);
                userRegist.setTelephonenumber(telephone);
                userRegist.setHeadPortraits(10);
                userRegist.setUser_id(10);

                setRegister(username,password, et_userpsw_again.getText().toString(),telephone,address,usernike);
//                startActivity(new Intent(mContext, MainActivity.class));
//                finish();
            }
        });
        dialog_register.setView(view);
        dialog_register.show();
    }


    /**
     * 得到imageview
     **/
    private ImageView getImageView(int id) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(id);
        return imageView;
    }

    private void setLogin(String name,String psw) {
        if (name.equals("")||psw.equals("")){
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(LoginActivity.this,username +"nnnnnnnn " + password ,Toast.LENGTH_SHORT).show();

        dialog_loading.show();

        //启动子线程访问数据库
        new Thread(new LoginRunner()).start();

    }

    private class LoginRunner implements Runnable {
        @Override
        public void run(){

            String url = loginURL + username + "&password=" + password;
            //String a = "http://47.93.249.197:8080/secondary/verifyUserServlet?username=张三&password=123";

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    responseText = EntityUtils.toString(response.getEntity());//返回的参数
                    handler.sendEmptyMessage(1);
                }else{

                    handler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private void setRegister(String name,String psw,String psw_again,String tel,String addr,String nike){
        if (name.equals("")||psw.equals("")||tel.equals("")){
            Toast.makeText(getApplication(),"用户名或密码或手机号码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (psw_again.equals("")){
            Toast.makeText(getApplication(),"确定密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!psw.equals(psw_again)){
            Toast.makeText(getApplication(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }

        //启动子线程，访问数据库，注册账号
        new Thread(new RegisterRunner()).start();

    }
    private class RegisterRunner implements Runnable {
        @Override
        public void run(){

            gson = new Gson();
            String registJsonURL = gson.toJson(userRegist);
            String url = null;
            try {
                url = registerURL + URLEncoder.encode(gson.toJson(userRegist),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);

                System.out.println( " +++++++++++++++++++++++++++++++++++++++++++++" + url);

                //设置字符集
                //request.setEntity();
                HttpResponse response = client.execute(request);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String responseText = EntityUtils.toString(response.getEntity());//返回的参数
                    Boolean a = gson.fromJson(responseText,Boolean.class);

                    if(a) {
                        handler.sendEmptyMessage(2);
                    }else {
                        handler.sendEmptyMessage(0);
                    }
                }else{
                    handler.sendEmptyMessage(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            vf_login.setInAnimation(leftInAnimation);
            vf_login.setOutAnimation(leftOutAnimation);
            vf_login.showNext();// 向右滑动
            return true;
        } else if (e1.getX() - e2.getY() < -120) {
            vf_login.setInAnimation(rightInAnimation);
            vf_login.setOutAnimation(rightOutAnimation);
            vf_login.showPrevious();// 向左滑动
            return true;
        }
        return false;
    }

}

