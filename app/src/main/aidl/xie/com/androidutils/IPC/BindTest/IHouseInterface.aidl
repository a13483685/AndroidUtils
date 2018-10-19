// IHouseInterface.aidl
package xie.com.androidutils.IPC.BindTest;

// Declare any non-default types here with import statements
import xie.com.androidutils.IPC.BindTest.House;
//parcelable House;
interface IHouseInterface {
    List<House> getHouseList();
    void addHouse(inout House house);
}
