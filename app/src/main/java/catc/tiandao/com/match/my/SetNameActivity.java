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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class SetNameActivity extends BaseActivity implements View.OnClickListener {



    private EditText input_name;
    private ImageView close_icon;

    private UpdateNameRun run;
    private String newName;


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
        setContentView( R.layout.activity_set_name );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "修改姓名" );
        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        TextView activity_text = ViewUtls.find( this,R.id.activity_text );

        input_name = ViewUtls.find( this,R.id.input_name );
        close_icon = ViewUtls.find( this,R.id.close_icon );


        activity_text.setText( "保存" );
        image.setOnClickListener( this);
        activity_text.setOnClickListener( this);
        close_icon.setOnClickListener( this);

        String nickName = SharedPreferencesUtil.getString( this,UserUtils.NICKNAME);

        input_name.setText( nickName );

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                SetNameActivity.this.onBackPressed();
                break;
            case R.id.close_icon:

                input_name.setText( "" );
                break;
            case R.id.activity_text:
                getContent();
                break;

        }
    }


    // phone_text,code_text,password;
    private void getContent() {

        newName = input_name.getText().toString();


        if(newName == null || newName.isEmpty()){
            Toast.makeText( SetNameActivity.this,"请输姓名",Toast.LENGTH_SHORT ).show();
            return ;
        }

        UpdateName(newName);

    }


    private void UpdateName(String name) {


        try{

            if (CheckNet.isNetworkConnected(SetNameActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& name=新用户名
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SetNameActivity.this ) );
                param.put( "name",name );

                run = new UpdateNameRun(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(SetNameActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    /**
     *
     * */
    class UpdateNameRun implements Runnable{
        private HashMap<String, String> param;

        UpdateNameRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( SetNameActivity.this,HttpUtil.UPDATE_NAME ,param,new HttpUtil.HttpUtilInterface(){
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
                SetNameActivity.this.onBackPressed();
                SharedPreferencesUtil.putString( SetNameActivity.this,UserUtils.NICKNAME,newName );
            }

            Toast.makeText( SetNameActivity.this,message ,Toast.LENGTH_SHORT).show();

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
        SetNameActivity.this.finish();
    }
}
