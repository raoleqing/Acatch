package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.ImageUtils;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.DataCleanManager;
import catc.tiandao.com.match.utils.DeviceUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
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

    private int notice;
    private int sleep;
    private int shake;//抖动
    private int sound;//声音

    private GetMySettingRun getRun;
    private LoginOffRun run;
    private SetNoticeRun setRun;
    private SetShakeRun setShakeRun;
    private SetSoundRun setSoundRun;
    private SetSellpRun setSleepRun;
    private AppLoginOnRun getTokenRun;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    //获取
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    getParseData(result1);
                    break;
                case 0x002:
                    //退出登录
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    parseData(result2);
                    break;
                case 0x003:
                    //关注
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    setNoticeParseData(result3);
                    break;
                case 0x004:
                    //
                    Bundle bundle4 = msg.getData();
                    String result4 = bundle4.getString("result");
                    shakeParseData(result4);
                    break;
                case 0x005:
                    //
                    Bundle bundle5 = msg.getData();
                    String result5 = bundle5.getString("result");
                    soundParseData(result5);
                    break;
                case 0x006:
                    //
                    Bundle bundle6 = msg.getData();
                    String result6 = bundle6.getString("result");
                    sleepParseData(result6);
                    break;
                case 0x007:
                    Bundle bundle7 = msg.getData();
                    String result7 = bundle7.getString("result");
                    getTokenParseData(result7);

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
        GetMySetting();
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
                int res = notice == 0 ? 1 : 0;
                SetNotice(res);

                break;
            case R.id.push_but_layout02:


                int res1 = shake == 0 ? 1 : 0;
                SetShake(res1);

                break;

            case R.id.push_but_layout03:


                int res2 = sound == 0 ? 1 : 0;
                SetSound(res2);

                break;
            case R.id.push_but_layout04:

                int res3 = sleep == 0 ? 1 : 0;

                SetSleep(res3);
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



    private void GetMySetting() {
        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );

                getRun = new GetMySettingRun(param);
                ThreadPoolManager.getsInstance().execute(getRun);


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
    class GetMySettingRun implements Runnable{
        private HashMap<String, String> param;

        GetMySettingRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.GET_MY_SETTING ,param,new HttpUtil.HttpUtilInterface(){
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



    private void getParseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            if(code == 0) {
                //{"notice":0,"sleep":0,"shake":0,"sound":0}}

                JSONObject data = obj.optJSONObject( "data" );
                notice = data.optInt( "notice" );
                sleep = data.optInt( "sleep" );
                shake = data.optInt( "shake" );
                sound = data.optInt( "sound" );


                SharedPreferencesUtil.putInt(MySetActivity.this, SharedPreferencesUtil.SHAKE,shake );
                SharedPreferencesUtil.putInt( MySetActivity.this,SharedPreferencesUtil.SOUND,sound );

                setContent();


            }

           // Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }

    private void setContent() {

        if (notice == 0) {
            push_but_icon01.setBackgroundResource(R.mipmap.slide_off);
        } else {
            push_but_icon01.setBackgroundResource(R.mipmap.slide_on);
        }

        if (shake == 0) {
            push_but_icon02.setBackgroundResource(R.mipmap.slide_off);
        } else {
            push_but_icon02.setBackgroundResource(R.mipmap.slide_on);
        }

        if (sound == 0) {
            push_but_icon03.setBackgroundResource(R.mipmap.slide_off);
        } else {
            push_but_icon03.setBackgroundResource(R.mipmap.slide_on);
        }

        if (sleep == 0) {
            push_but_icon04.setBackgroundResource(R.mipmap.slide_off);
        } else {
            push_but_icon04.setBackgroundResource(R.mipmap.slide_on);
        }
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
                    message.what = 0x002;
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
                String userKey = SharedPreferencesUtil.getString( this,SharedPreferencesUtil.USER_KEY );
                AppLoginOn(userKey);


            }

            Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            MySetActivity.this.onBackPressed();
            setProgressVisibility( View.GONE );
        }

    }


    //phonekey=手机key& ip=手机ip
    private void AppLoginOn(String phonekey) {

        try{

            if (CheckNet.isNetworkConnected( MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phonekey",phonekey );
                param.put( "ip", DeviceUtils.getIPAddress(MySetActivity.this) );

                getTokenRun = new AppLoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(getTokenRun);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class AppLoginOnRun implements Runnable{
        private HashMap<String, String> param;

        AppLoginOnRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( MySetActivity.this,HttpUtil.APP_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x007;
                    myHandler.sendMessage( message );

                }
            });

        }
    }


    private void getTokenParseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{


            System.out.println("获取 token: " +  result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            // {"code":0,"message":"登录成功","data":{"sex":0,"token":"f9d8498a-ee56-4d18-8f69-215dcb0e7168","nickName":null,"iconUrl":null}}
            if(code == 0) {
                JSONObject data = obj.optJSONObject( "data" );
                String token = data.optString( "token" );
                SharedPreferencesUtil.putString( MySetActivity.this,UserUtils.TOKEN, token);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }



    }







    private void SetNotice(int res) {

        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );
                param.put("res", res + "");

                setRun = new SetNoticeRun(param);
                ThreadPoolManager.getsInstance().execute(setRun);


            } else {

                Toast.makeText(MySetActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }





    /**
     *设置-仅通知关注
     * */
    class SetNoticeRun implements Runnable{
        private HashMap<String, String> param;

        SetNoticeRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.SET_NOTICE ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x003;
                    myHandler.sendMessage( message );
                }
            });

        }
    }



    //设置-仅通知关注
    private void setNoticeParseData(String result) {

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
                if (notice == 1) {
                    notice = 0;
                    push_but_icon01.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    notice = 1;
                    push_but_icon01.setBackgroundResource(R.mipmap.slide_on);
                }

            }

            Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    private void SetShake(int res) {

        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );
                param.put("res", res + "");

                setShakeRun = new SetShakeRun(param);
                ThreadPoolManager.getsInstance().execute(setShakeRun);


            } else {

                Toast.makeText(MySetActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }





    /**
     *设置-仅通知关注
     * */
    class SetShakeRun implements Runnable{
        private HashMap<String, String> param;

        SetShakeRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.SET_SHAKE ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x004;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    //
    private void shakeParseData(String result) {

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
                if (shake == 1) {
                    shake = 0;
                    push_but_icon02.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    shake = 1;
                    push_but_icon02.setBackgroundResource(R.mipmap.slide_on);
                }


                SharedPreferencesUtil.putInt(MySetActivity.this, SharedPreferencesUtil.SHAKE,shake );

            }

            Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    private void SetSound(int res) {

        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );
                param.put("res", res + "");

                setSoundRun = new SetSoundRun(param);
                ThreadPoolManager.getsInstance().execute(setSoundRun);


            } else {

                Toast.makeText(MySetActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }





    /**
     *设置-仅通知关注
     * */
    class SetSoundRun implements Runnable{
        private HashMap<String, String> param;

        SetSoundRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.SET_SOUND ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x005;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    //设置-仅通知关注
    private void soundParseData(String result) {

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
                if (sound == 1) {
                    sound = 0;
                    push_but_icon03.setBackgroundResource(R.mipmap.slide_off);
                } else {
                    sound = 1;
                    push_but_icon03.setBackgroundResource(R.mipmap.slide_on);
                }

            }

            SharedPreferencesUtil.putInt( MySetActivity.this,SharedPreferencesUtil.SOUND,sound );

            Toast.makeText( MySetActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }


    private void SetSleep(int res) {

        try{

            if (CheckNet.isNetworkConnected(MySetActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( MySetActivity.this ) );
                param.put("res", res + "");

                setSleepRun = new SetSellpRun(param);
                ThreadPoolManager.getsInstance().execute(setSleepRun);


            } else {

                Toast.makeText(MySetActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }





    /**
     *设置-仅通知关注
     * */
    class SetSellpRun implements Runnable{
        private HashMap<String, String> param;

        SetSellpRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( MySetActivity.this,HttpUtil.SET_SLEEP ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x006;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    //设置-仅通知关注
    private void sleepParseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if (sleep == 1) {
                sleep = 0;
                push_but_icon04.setBackgroundResource(R.mipmap.slide_off);
                acquireWakeLock();
                releaseWakeLock();
            } else {
                sleep = 1;
                push_but_icon04.setBackgroundResource(R.mipmap.slide_on);

                acquireWakeLock();
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
