package xie.com.androidutils.IPC.Parcel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 序列化对象
 */
public class User implements Parcelable {
    private String userName;
    private String userId;
    private String userSex;
    private int userAge;
    private House mHouse;

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userAge=" + userAge +
                ", mHouse=" + mHouse.toString() +
                '}';
    }

    public House getmHouse() {
        return mHouse;
    }

    public void setmHouse(House mHouse) {
        this.mHouse = mHouse;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //序列化功能由一些列的write完成
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userId);
        dest.writeString(this.userSex);
        dest.writeInt(this.userAge);
        dest.writeParcelable(this.mHouse, flags);//如果传递的是对象，那么必须喜欢滴flag,是当前线程的上下文加载器
    }

    protected User(Parcel in) {
        this.userName = in.readString();
        this.userId = in.readString();
        this.userSex = in.readString();
        this.userAge = in.readInt();
        this.mHouse = in.readParcelable(House.class.getClassLoader());
    }
    //反序列化由CREATOR来完成
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);//序列化对象
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];//序列化数组
        }
    };
}
