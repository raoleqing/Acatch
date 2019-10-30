package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class MySetActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout push_but_layout01;
    private RelativeLayout push_but_layout02;
    private RelativeLayout push_but_layout03;
    private RelativeLayout push_but_layout04;
    private ImageView push_but_icon01;
    private ImageView push_but_icon02;
    private ImageView push_but_icon03;
    private ImageView push_but_icon04;
    private TextView sign_out;

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
        push_but_icon01 = (ImageView) findViewById(R.id.push_but_icon01);
        push_but_icon02 = (ImageView) findViewById(R.id.push_but_icon02);
        push_but_icon03 = (ImageView) findViewById(R.id.push_but_icon03);
        push_but_icon04 = (ImageView) findViewById(R.id.push_but_icon04);
        sign_out = (TextView) findViewById(R.id.sign_out);


        push_but_layout01.setOnClickListener(this);
        push_but_layout02.setOnClickListener(this);
        push_but_layout03.setOnClickListener(this);
        push_but_layout04.setOnClickListener(this);
        sign_out.setOnClickListener(this);


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
                    push_but_icon01.setBackgroundResource(R.mipmap.push_but_iocn02);
                } else {
                    isOpen1 = 1;
                    push_but_icon01.setBackgroundResource(R.mipmap.push_but_iocn01);
                }


                break;
            case R.id.push_but_layout02:

                if (isOpen2 == 1) {
                    isOpen2 = 0;
                    push_but_icon02.setBackgroundResource(R.mipmap.push_but_iocn02);
                } else {
                    isOpen2 = 1;
                    push_but_icon02.setBackgroundResource(R.mipmap.push_but_iocn01);
                }

                break;

            case R.id.push_but_layout03:

                if (isOpen3 == 1) {
                    isOpen3 = 0;
                    push_but_icon03.setBackgroundResource(R.mipmap.push_but_iocn02);
                } else {
                    isOpen3 = 1;
                    push_but_icon03.setBackgroundResource(R.mipmap.push_but_iocn01);
                }
                break;

            case R.id.push_but_layout04:

                if (isOpen4 == 1) {
                    isOpen4 = 0;
                    push_but_icon04.setBackgroundResource(R.mipmap.push_but_iocn02);
                } else {
                    isOpen4 = 1;
                    push_but_icon04.setBackgroundResource(R.mipmap.push_but_iocn01);
                }
                break;
            case R.id.sign_out:

                LoginOff();
                break;

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
