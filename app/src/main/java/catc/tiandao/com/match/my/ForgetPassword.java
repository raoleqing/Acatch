package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.DES;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPassword extends BaseActivity implements View.OnClickListener {


    private EditText phone_text,code_text,password;
    private TextView get_code;
    private Button login;

    private boolean isName,isPassword,iscode;


    private boolean isCountDownTimer = false;// 正在倒数
    CountDownTimer timer = null;
    private final long COUNT = 60000; // 倒数时间（毫秒）
    private final long INTERVAL = 1000; // 时间间隔（毫秒）

    private GetRegisterSmsRun run;
    private SaveChangePswRun saveRun;

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
                case 0x002:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    saveParseData(result2);
                    break;
                default:
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forget_password );

        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);


        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView activity_return = ViewUtls.find( this,R.id.activity_return );
        phone_text = ViewUtls.find( this,R.id.phone_text );
        code_text = ViewUtls.find( this,R.id.code_text );
        password = ViewUtls.find( this,R.id.password );
        get_code = ViewUtls.find( this,R.id.get_code );
        login = ViewUtls.find( this,R.id.login );

        activity_return.setOnClickListener(this);
        get_code.setOnClickListener(this);
        login.setOnClickListener(this);



        phone_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone_text.getText().toString().trim().length() >= 11) {
                    isName = true;
                    if(!isCountDownTimer){
                        get_code.setTextColor( ContextCompat.getColor( ForgetPassword.this,R.color.text1 ) );
                    }

                }else {
                    if(!isCountDownTimer){
                        get_code.setTextColor( ContextCompat.getColor( ForgetPassword.this,R.color.text6 ) );
                    }

                    isName = false;
                }
                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        code_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(code_text.getText().toString().trim().length() >= 4){
                    iscode = true;
                }else{
                    iscode = false;
                }

                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().toString().trim().length() >= 6){
                    isPassword = true;
                }else{
                    isPassword = false;
                }

                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                ForgetPassword.this.onBackPressed();
                break;
            case R.id.get_code:
                if (isName && !isCountDownTimer) {
                    GetRegisterSms();
                }
            case R.id.login:

                getContent();
                break;
        }

    }



    private void GetRegisterSms() {


        try{

            if (CheckNet.isNetworkConnected(ForgetPassword.this)) {

                get_code.setClickable( false );
                String phone = phone_text.getText().toString();

                setProgressVisibility( View.VISIBLE );

                run = new GetRegisterSmsRun(phone);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(ForgetPassword.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    /*
        获取验证码
     * */
    class GetRegisterSmsRun implements Runnable{

        private String phone;
        GetRegisterSmsRun(String phone){
            this.phone = phone;
        }

        @Override
        public void run(){
            HttpUtil.getDatasync(ForgetPassword.this,HttpUtil.GET_PASSWORD_SMS +  DES.encode(DES.KEY,phone),new HttpUtil.HttpUtilInterface(){

                @Override
                public void onResponse(String result) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
                    myHandler.sendMessage( message );
                }
            } );

        }
    }


    private void parseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            //{"code":0,"message":"获取成功","data":null}
            if(code == 0) {
                Toast.makeText( ForgetPassword.this,"验证码已发送，请注意查收" ,Toast.LENGTH_SHORT).show();
                sendCodes();
            }else {
                Toast.makeText( ForgetPassword.this,message ,Toast.LENGTH_SHORT).show();
                get_code.setClickable( true );
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }


    private void sendCodes() {
        // TODO Auto-generated method stub
        isCountDownTimer = true;
        get_code.setTextColor( ContextCompat.getColor( ForgetPassword.this,R.color.text6 ) );
//        tv_code.setBackgroundResource(R.drawable.background_selector18);
        if(timer == null) {
            timer = new CountDownTimer(COUNT, INTERVAL) { // 倒数
                @Override
                public void onTick(long millisUntilFinished) {
                    get_code.setText("重新发送" + String.valueOf(millisUntilFinished / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    get_code.setText("获取验证码");
                    isCountDownTimer = false;

                    if(isName){
                        get_code.setTextColor( ContextCompat.getColor( ForgetPassword.this,R.color.text1 ) );
                    }else {
                        get_code.setTextColor( ContextCompat.getColor( ForgetPassword.this,R.color.text6 ) );
                    }

                }
            };
        }
        timer.start();
    }


    // phone_text,code_text,password;
    private void getContent() {

        String phone = phone_text.getText().toString();
        String code = code_text.getText().toString();
        String paw = password.getText().toString();

        if(phone == null || phone.isEmpty()){
            Toast.makeText( ForgetPassword.this,"请输入手机号码",Toast.LENGTH_SHORT ).show();
            return ;
        }

        if(code == null || code.isEmpty()){
            Toast.makeText( ForgetPassword.this,"请输入验证码",Toast.LENGTH_SHORT ).show();
            return ;
        }

        if(paw == null || paw.isEmpty()){
            Toast.makeText( ForgetPassword.this,"请输入密码",Toast.LENGTH_SHORT ).show();
            return ;
        }

        SaveChangePsw(phone,code,paw);


    }


    //
    private void SaveChangePsw(String phone,String code,String paw1) {


        try{

            if (CheckNet.isNetworkConnected(ForgetPassword.this)) {

                setProgressVisibility( View.VISIBLE );

                //url：http:// 域名/LSQB/ SaveChangePsw? phone=手机号& psw=密码& code=验证码
                HashMap<String, String> param = new HashMap<>(  );
                param.put("phone",phone );
                param.put( "psw",DES.encode(DES.KEY,paw1) );
                param.put( "code",code );

                saveRun = new SaveChangePswRun(param);
                ThreadPoolManager.getsInstance().execute(saveRun);


            } else {

                Toast.makeText(ForgetPassword.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    /**
     *
     * */
    class SaveChangePswRun implements Runnable{
        private HashMap<String, String> param;

        SaveChangePswRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( ForgetPassword.this,HttpUtil.SAVE_CHANGE_PSW ,param,new HttpUtil.HttpUtilInterface(){
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



    private void saveParseData(String result) {

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
                ForgetPassword.this.finish();
            }else {
                Toast.makeText( ForgetPassword.this,message ,Toast.LENGTH_SHORT).show();
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }


    private void setLoginBut() {

        if(isName && iscode && isPassword){
            login.setBackgroundResource( R.drawable.bg_search_normal11_host );
            login.setClickable( true );
        }else {
            login.setBackgroundResource( R.drawable.bg_search_normal11 );
            login.setClickable( false );
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(run != null)
            ThreadPoolManager.getsInstance().cancel(run);


        if(saveRun != null)
            ThreadPoolManager.getsInstance().cancel(saveRun);

    }



    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        ForgetPassword.this.finish();
    }

}
