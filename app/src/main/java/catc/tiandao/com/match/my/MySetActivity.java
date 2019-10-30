package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.ImageUtils;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.DataCleanManager;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;


/**
 * 设置
 * **/
public class MySetActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout push_but_layout01;
    private RelativeLayout push_but_layout02;
    private RelativeLayout push_but_layout03;
    private RelativeLayout push_but_layout04;
    private ImageView push_but_icon01;
    private ImageView push_but_icon02;
    private ImageView push_but_icon03;
    private ImageView push_but_icon04;
    private RelativeLayout clear_cache;
    private TextView sign_out;
    private TextView cache_text;

    private String fileSize;

    private int isOpen1 = 1;
    private int isOpen2 = 0;
    private int isOpen3 = 0;
    private int isOpen4 = 0;


    private LoginOffRun run;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    parseData(result1);
                    break;
                default:
                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_set );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        setTitleText( "设置" );
        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);

        push_but_layout01 = (RelativeLayout) findViewById(R.id.push_but_layout01);
        push_but_layout02 = (RelativeLayout) findViewById(R.id.push_but_layout02);
        push_but_layout03 = (RelativeLayout) findViewById(R.id.push_but_layout03);
        push_but_layout04 = (RelativeLayout) findViewById(R.id.push_but_layout04);
        clear_cache = (RelativeLayout) findViewById(R.id.clear_cache);
        push_but_icon01 = (ImageView) findViewById(R.id.push_but_icon01);
        push_but_icon02 = (ImageView) findViewById(R.id.push_but_icon02);
        push_but_icon03 = (ImageView) findViewById(R.id.push_but_icon03);
        push_but_icon04 = (ImageView) findViewById(R.id.push_but_icon04);
        sign_out = (TextView) findViewById(R.id.sign_out);
        cache_text = (TextView) findViewById(R.id.cache_text);

        if(UserUtils.isLanded( this )){
            sign_out.setVisibility( View.VISIBLE );
        }else {
            sign_out.setVisibility( View.GONE );
        }


        push_but_layout01.setOnClickListener(this);
        push_but_layout02.setOnClickListener(this);
        push_but_layout03.setOnClickListener(this);
        push_but_layout04.setOnClickListener(this);
        sign_out.setOnClickListener(this);

        setCacheText();

    }


    /*
     * 设置缓存
     * **/
    private void setCacheText() {
        // TODO Auto-generated method stub
        try {
            String path = ImageUtils.getAlbumFilesDir(this).getPath();
            File file = new File(path);
            fileSize = DataCleanManager.getCacheSize(file);
            cache_text.setText(fileSize + "  ");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                MySetActivity.this.onBackPressed();
                break;
            case R.id.push_but_layout01:


                if (isOpen1 == 1) {
                    isOpen1 = 0;
                    push_but_icon01.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    isOpen1 = 1;
                    push_but_icon01.setBackgroundResource(R.mipmap.slide_on);
                }


                break;
            case R.id.push_but_layout02:

                if (isOpen2 == 1) {
                    isOpen2 = 0;
                    push_but_icon02.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    isOpen2 = 1;
                    push_but_icon02.setBackgroundResource(R.mipmap.slide_on);
                }

                break;

            case R.id.push_but_layout03:

                if (isOpen3 == 1) {
                    isOpen3 = 0;
                    push_but_icon03.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    isOpen3 = 1;
                    push_but_icon03.setBackgroundResource(R.mipmap.slide_on);
                }
                break;

            case R.id.push_but_layout04:

                if (isOpen4 == 1) {
                    isOpen4 = 0;
                    push_but_icon04.setBackgroundResource(R.mipmap.slide_off);
                    acquireWakeLock();
                    releaseWakeLock();
                } else {
                    isOpen4 = 1;
                    push_but_icon04.setBackgroundResource(R.mipmap.slide_on);

                    acquireWakeLock();
                }
                break;
            case R.id.sign_out:

                LoginOff();
                break;

            case R.id.clear_cache:
                //清除缓存
                if(fileSize != null && !fileSize.startsWith("0.0")){
                    showCloseDialog();
                }
                break;

        }



    }



    private PowerManager.WakeLock mWakeLock;

    private void acquireWakeLock() {
        if(mWakeLock == null) {
            PowerManager pm = (PowerManager)getSystemService( Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    this.getClass().getCanonicalName());
            mWakeLock.acquire();

        }

    }

    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

    }

    /**
     *清除缓存提示
     **/
    private void showCloseDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("现在的缓存为" + fileSize + ", 是否要清理").setTitle("提示")
                .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = ImageUtils.getAlbumFilesDir(MySetActivity.this).getPath();
                        DataCleanManager.cleanInternalCache(MySetActivity.this,path);
                        setCacheText();
                    }
                }).setNegativeButton("取消", null).show();
    }


    private void LoginOff() {

        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );

                run = new LoginOffRun(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(MySetActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     *
     * */
    class LoginOffRun implements Runnable{
        private HashMap<String, String> param;

        LoginOffRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.LOGIN_OFF ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void parseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            //{"code":1,"message":"验证码错误","data":null}
            if(code == 0) {

                UserUtils.SignOut(MySetActivity.this);
                MySetActivity.this.onBackPressed();

            }

            Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(run != null)
            ThreadPoolManager.getsInstance().cancel(run);


    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        MySetActivity.this.finish();
    }


}
