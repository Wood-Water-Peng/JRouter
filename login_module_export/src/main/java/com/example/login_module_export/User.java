package com.example.login_module_export;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author jacky.peng
 * @Date 2021/5/26 3:45 PM
 * @Version 1.0
 * <p>
 * 暴露给其他模块的用户类
 */
public class User implements Parcelable {
    private String name;
    //0-普通用户   1-VIP用户   2-管理员
    int level = 0;
    boolean isLogin;

    public User(String name) {
        this.name = name;
    }
    public User(String name,int level) {
        this.name = name;
        this.level=level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getName() {
        return name;
    }


    public int getLevel() {
        return level;
    }

    public boolean isLogin() {
        return isLogin;
    }

    protected User(Parcel in) {
        name = in.readString();
        level = in.readInt();
        isLogin = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(level);
        dest.writeByte((byte) (isLogin ? 1 : 0));
    }
}
