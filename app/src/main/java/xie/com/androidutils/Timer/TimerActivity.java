package xie.com.androidutils.Timer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import xie.com.androidutils.R;
import xie.com.androidutils.Utils.ToastUtils;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = this.getClass().getName();
    Button bt1 =null;
    Button bt2 =null;
    private static int i =0;
    private static Handler handler = null;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        i = 0;
                        handler.removeMessages(1);
                        ToastUtils.showToast(getApplicationContext(),"i is : "+i);
                        break;
                    case 2:
                        i = 0;
                        ToastUtils.showToast(getApplicationContext(),"i is : "+i);
                        timer.cancel();
                        handler.removeMessages(2);
                }
            }
        };
        bt1 = findViewById(R.id.btn1);
        bt2 = findViewById(R.id.btn2);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn1:
                i = 1;
                ToastUtils.showToast(this,"i is : "+i);
                Message message = new Message();
                message.what = 1;
                handler.sendMessageDelayed(message,1000);
                break;
            case R.id.btn2:
                i = 1;
                ToastUtils.showToast(this,"i is : "+i);
                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                };
                timer.schedule(timerTask,1000);
        }
    }
}
