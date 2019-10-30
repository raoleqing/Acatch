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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

public class SetPassword extends BaseActivity implements View.OnClickListener {

    public static final String PHONE = "phone";
    public static final String CODE = "code";

    private EditText password1,password2;
    private Button login;

    private String phone,code;
    private String paw1;

    private RegisterRun run;

    private boolean isPassword1,isPassword2;


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
        setContentView( R.layout.activity_set_password );

        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        phone = getIntent().getStringExtra( PHONE );
        code = getIntent().getStringExtra( CODE );

        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView activity_return = ViewUtls.find( this,R.id.activity_return );
        password1 = ViewUtls.find( this,R.id.password_text1 );
        password2 = ViewUtls.find( this,R.id.password_text2 );
        login = ViewUtls.find( this,R.id.login );

        activity_return.setOnClickListener(this);
        login.setOnClickListener(this);


        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password1.getText().toString().trim().length() >= 6){
                    isPassword1 = true;
                }else{
                    isPassword1 = false;
                }

                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password2.getText().toString().trim().length() >= 6){
                    isPassword2 = true;
                }else{
                    isPassword2 = false;
                }
                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void setLoginBut() {


        if(isPassword1 && isPassword2){
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
                SetPassword.this.onBackPressed();
                break;
            case R.id.login:

                getContent();

                break;
        }

    }

    private void getContent() {

        paw1 = password1.getText().toString();
        String paw2 = password2.getText().toString();

        if(paw1 == null || paw1.length() == 0){
            Toast.makeText( SetPassword.this,"请输入密码",Toast.LENGTH_SHORT ).show();
            return ;
        }

        if(!paw1.equals( paw2 )){
            Toast.makeText( SetPassword.this,"两次密码不一至",Toast.LENGTH_SHORT ).show();
            return ;
        }

        Register(phone,code,paw1);


    }


    //
    private void Register(String phone,String code,String paw1) {


        try{

            if (CheckNet.isNetworkConnected(SetPassword.this)) {

                setProgressVisibility( View.VISIBLE );
                String phoneKey = SharedPreferencesUtil.getString( SetPassword.this,SharedPreferencesUtil.USER_KEY );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phoneKey", phoneKey);
                param.put("phone", phone);
                param.put( "psw", DES.encode(DES.KEY,paw1) );
                param.put( "code",code );

                run = new RegisterRun(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(SetPassword.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    /**
     *
     * */
    class RegisterRun implements Runnable{
        private HashMap<String, String> param;

        RegisterRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


          HttpUtil.post( SetPassword.this,HttpUtil.REGISTER ,param,new HttpUtil.HttpUtilInterface(){

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

                SharedPreferencesUtil.putString( SetPassword.this,UserUtils.PHONE,phone );
                SharedPreferencesUtil.putString( SetPassword.this,UserUtils.PASSWORD,paw1);

                Intent intent = new Intent(SetPassword.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
                SetPassword.this.finish();

            }else {
                Toast.makeText( SetPassword.this,message ,Toast.LENGTH_SHORT).show();
            }


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
        SetPassword.this.finish();
    }


}
