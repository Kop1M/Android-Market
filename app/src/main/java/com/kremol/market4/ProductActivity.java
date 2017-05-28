package com.kremol.market4;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener{

    Product product;
    TextView productDescripotin,productPrice;
    ImageView productImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (Product) getIntent().getSerializableExtra("product");

        /*设置product属性*/
        productDescripotin.setText(product.getAbout());
        productPrice.setText(product.getProductprize());
        productImage.setImageResource(R.drawable.changongzi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*显示卖家信息*/
            case R.id.seller_information:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setTitle("卖家信息");
                builder.setMessage(product.getUserforsale());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       ProductActivity.this.finish();
                   }
                });
                builder.create().show();
                break;
            /*生成一个订单发送给服务器*/
            case R.id.add_in_shopcart:
                break;
            /*让服务器处理所有订单*/
            case R.id.buy_now:
                break;
            default:
                break;
        }
    }
}
