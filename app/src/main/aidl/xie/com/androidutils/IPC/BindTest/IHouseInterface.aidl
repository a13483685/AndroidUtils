// IHouseInterface.aidl
package xie.com.androidutils.IPC.BindTest;

// Declare any non-default types here with import statements
import xie.com.androidutils.IPC.BindTest.House;
import xie.com.androidutils.IPC.BindTest.IOnNewHouseArrivedListener;
//parcelable House;
interface IHouseInterface {
    List<House> getHouseList();
    void addHouse(inout House house);
    void registerListener(IOnNewHouseArrivedListener listener);
    void unregisterListener(IOnNewHouseArrivedListener listener);
}
