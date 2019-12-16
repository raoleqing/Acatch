package catc.tiandao.com.match;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.common.Constant;

public class SplashActivity extends AppCompatActivity {

    private MyHandler mHandler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView( R.layout.activity_splash );

        //这里设置为app正常态
        AppUtils.getInstance().setAppStatus( Constant.STATUS_NORMAL);

        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }

       // setProgressVisibility( View.GONE );

    }

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = (SplashActivity) reference.get();
            if (activity != null) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

                activity.finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


}
