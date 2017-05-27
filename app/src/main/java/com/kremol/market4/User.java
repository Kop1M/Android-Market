package com.kremol.market4;

/**
 * Created by flexible on 2017/5/27.
 */

public class User {

    private int user_id;
    private String user_name;
    private String password;
    private String telephonenumber;
    private int headPortraits;
    private String nickname;
    private String address;


    public final int getUser_id() {
        return user_id;
    }
    public final String getUser_name() {
        return user_name;
    }
    public final String getPassword() {
        return password;
    }
    public final String getTelephonenumber() {
        return telephonenumber;
    }
    public final int getHeadPortraits() {
        return headPortraits;
    }
    public final String getAddress() {
        return address;
    }
    public final String getNickname() {
        return nickname;
    }


    public final void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public final void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public final void setPassword(String password) {
        this.password = password;
    }
    public final void setTelephonenumber(String telephonenumber) {
        this.telephonenumber = telephonenumber;
    }
    public final void setHeadPortraits(int headPortraits) {
        this.headPortraits = headPortraits;
    }
    public final void setAddress(String address) {
        this.address = address;
    }
    public final void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ",user_name="+user_name+",password=" + password + ",telephonenumber=" + telephonenumber+ ",headPortraits=" + headPortraits +",nickname="+nickname+ ",address="+address+"]";
    }

}
