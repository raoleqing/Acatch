package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.utils.DES;
import catc.tiandao.com.matchlibrary.ViewUtls;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText uername,code_text;
    private CheckBox myCheckbox;
    private TextView protocol,get_code;
    private Button login;

    private boolean isName,iscode,isCheck;
    private boolean isCountDownTimer = false;// 正在倒数
    CountDownTimer timer = null;
    private final long COUNT = 60000; // 倒数时间（毫秒）
    private final long INTERVAL = 1000; // 时间间隔（毫秒）

    private GetRegisterSmsRun run;


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
        setContentView( R.layout.activity_registered );

        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {


        ImageView activity_return = ViewUtls.find( this,R.id.activity_return );
        uername = ViewUtls.find( this,R.id.phone_text );
        code_text = ViewUtls.find( this,R.id.code_text );
        get_code = ViewUtls.find( this,R.id.get_code );
        myCheckbox = ViewUtls.find( this,R.id.myCheckbox );
        protocol = ViewUtls.find( this,R.id.protocol );
        login = ViewUtls.find( this,R.id.login );

        activity_return.setOnClickListener(this);
        protocol.setOnClickListener(this);
        login.setOnClickListener(this);
        get_code.setOnClickListener(this);

        get_code.setClickable( false );


        uername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(uername.getText().toString().trim().length() >= 11) {
                    isName = true;
                    if(!isCountDownTimer){
                        get_code.setTextColor( ContextCompat.getColor( RegisteredActivity.this,R.color.text1 ) );
                        get_code.setClickable( true );
                    }

                }else {
                    if(!isCountDownTimer){
                        get_code.setTextColor( ContextCompat.getColor( RegisteredActivity.this,R.color.text6 ) );
                        get_code.setClickable( false );
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


        myCheckbox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
                setLoginBut();
            }
        } );
    }


    private void setLoginBut() {


        if(isName && iscode &&isCheck){
            login.setBackgroundResource( R.drawable.bg_search_normal11_host );
            login.setClickable( true );
        }else {
            login.setBackgroundResource( R.drawable.bg_search_normal11 );
            login.setClickable( false );
        }


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                RegisteredActivity.this.onBackPressed();
                break;

            case R.id.get_code:

                if (isName && !isCountDownTimer) {

                    GetRegisterSms();

                }
                break;

            case R.id.login:

                getContent();


                break;

        }

    }


    private void GetRegisterSms() {


        try{

            if (CheckNet.isNetworkConnected(RegisteredActivity.this)) {

                get_code.setClickable( false );
                String phone = uername.getText().toString();

                setProgressVisibility( View.VISIBLE );

                run = new GetRegisterSmsRun(phone);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(RegisteredActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private void getContent() {

        if(isName && iscode &&isCheck){

            String phone = uername.getText().toString();
            String code = code_text.getText().toString();

            Intent intent01 = new Intent(RegisteredActivity.this, SetPassword.class);
            intent01.putExtra(  SetPassword.PHONE,phone);
            intent01.putExtra( SetPassword.CODE, code);
            startActivity(intent01);
            overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
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

            HttpUtil.getDatasync(RegisteredActivity.this,HttpUtil.GET_REGISTER_SMS + DES.encode(DES.KEY,phone),new HttpUtil.HttpUtilInterface(){

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
                Toast.makeText( RegisteredActivity.this,"验证码已发送，请注意查收" ,Toast.LENGTH_SHORT).show();
                sendCodes();
            }else {
                Toast.makeText( RegisteredActivity.this,message ,Toast.LENGTH_SHORT).show();
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
        get_code.setTextColor( ContextCompat.getColor( RegisteredActivity.this,R.color.text6 ) );
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
                        get_code.setTextColor( ContextCompat.getColor( RegisteredActivity.this,R.color.text1 ) );
                        get_code.setClickable( true );
                    }else {
                        get_code.setTextColor( ContextCompat.getColor( RegisteredActivity.this,R.color.text6 ) );
                        get_code.setClickable( false );
                    }

                }
            };
        }
        timer.start();
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
        RegisteredActivity.this.finish();
    }



}
