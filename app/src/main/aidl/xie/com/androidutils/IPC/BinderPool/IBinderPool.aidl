// IBinderPool.aidl
package xie.com.androidutils.IPC.BinderPool;

// 用来查询，调用那个binder

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
