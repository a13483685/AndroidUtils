package xie.com.androidutils.IPC.BinderPool;

import android.os.RemoteException;

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a+b;
    }
}
