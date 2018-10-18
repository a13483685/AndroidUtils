package xie.com.androidutils.IPC.Parcel;

import android.os.Parcel;
import android.os.Parcelable;

public class House implements Parcelable {
    private String Location;
    private String price;
    private String userName;

    @Override
    public String toString() {
        return "House{" +
                "Location='" + Location + '\'' +
                ", price='" + price + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Location);
        dest.writeString(this.price);
        dest.writeString(this.userName);
    }

    public House() {
    }

    protected House(Parcel in) {
        this.Location = in.readString();
        this.price = in.readString();
        this.userName = in.readString();
    }

    public static final Parcelable.Creator<House> CREATOR = new Parcelable.Creator<House>() {
        @Override
        public House createFromParcel(Parcel source) {
            return new House(source);
        }

        @Override
        public House[] newArray(int size) {
            return new House[size];
        }
    };
}
