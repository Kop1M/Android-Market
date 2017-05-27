package com.kremol.market4;

/**
 * Created by flexible on 2017/5/28.
 */

/*条目类：每个ListView中的每个条目*/
public class Entry {
    private String nickName;
    private int headImage;
    private String productName;
    private Product product; //商品的详细信息

    public Entry(int headImage, String nickName, Product product, String productName) {
        this.headImage = headImage;
        this.nickName = nickName;
        this.product = product;
        this.productName = productName;
    }

    public int getHeadImage() {
        return headImage;
    }

    public void setHeadImage(int headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
