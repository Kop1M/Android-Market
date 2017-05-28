package com.kremol.market4;

import java.io.Serializable;

/**
 * Created by flexible on 2017/5/27.
 */

public class Product implements Serializable {
    private int product_id;
    private String title;
    private String publishtime;
    private String about;
    private String userforsale;
    private int productprize;
    private String type;

    public final int getProduct_id() {
        return product_id;
    }
    public final void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
    public final String getTitle(){return title;}
    public final void setTitle(String title){this.title = title;}
    public final String getPublishtime(){return publishtime;}
    public final void setPublishtime(String publishtime){this.publishtime = publishtime;}
    public final String getAbout(){return about;}
    public final void setAbout(String about){this.about = about;}
    public final String getUserforsale(){return userforsale;}
    public final void setUserforsale(String about){this.userforsale = userforsale;}
    public final int getProductprize() {
        return productprize;
    }
    public final void setProductprize(int productprize) {
        this.productprize = productprize;
    }
    public final String getType(){return type;}
    public final void setType(String type){this.type = type;}


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "[product_id:"+product_id+" title:"+title+" publishtime:"+publishtime+" about:"+about+
                "userforsale:"+userforsale+"productprize:"+productprize+"type:"+type+"]";
    }
}
