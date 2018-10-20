// IOnNewHouseArrivedListener.aidl
package xie.com.androidutils.IPC.BindTest;

// Declare any non-default types here with import statements
import xie.com.androidutils.IPC.BindTest.House;
interface IOnNewHouseArrivedListener {
    void onNewHouseArrived(in House newHouse);
}
