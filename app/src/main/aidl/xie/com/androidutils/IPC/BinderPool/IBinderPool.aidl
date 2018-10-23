// IBinderPool.aidl
package xie.com.androidutils.IPC.BinderPool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
