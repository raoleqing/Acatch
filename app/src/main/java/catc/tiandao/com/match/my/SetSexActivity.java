package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class SetSexActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout male_layout,female_layout;
    private ImageView iv_male,iv_female;


    private String sex;
    private UpdateSexRun run;

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
        setContentView( R.layout.activity_set_sex );

        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "性别" );
        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        TextView activity_text = ViewUtls.find( this,R.id.activity_text );

        male_layout = ViewUtls.find( this,R.id.male_layout );
        female_layout = ViewUtls.find( this,R.id.female_layout );
        iv_male = ViewUtls.find( this,R.id.iv_male );
        iv_female = ViewUtls.find( this,R.id.iv_female );


        activity_text.setText( "保存" );
        image.setOnClickListener( this);
        activity_text.setOnClickListener( this);
        male_layout.setOnClickListener( this);
        female_layout.setOnClickListener( this);

        sex = SharedPreferencesUtil.getString( this,UserUtils.SEX );
        if(sex == null || sex.equals( "" )){
            sex = "1";
        }

        if(sex.equals( "1" )){
            iv_male.setVisibility( View.VISIBLE );
            iv_female.setVisibility( View.GONE );
        }else {
            iv_male.setVisibility( View.GONE );
            iv_female.setVisibility( View.VISIBLE );
        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                SetSexActivity.this.onBackPressed();
                break;
            case R.id.activity_text:
                UpdateSex(sex);
                break;
            case R.id.male_layout:
                if(!sex.equals( "1" )){
                    sex = "1";
                    iv_male.setVisibility( View.VISIBLE );
                    iv_female.setVisibility( View.GONE );
                }

                break;
            case R.id.female_layout:
                if(!sex.equals( "0" )){
                    sex = "0";
                    iv_male.setVisibility( View.GONE );
                    iv_female.setVisibility( View.VISIBLE );
                }
                break;

        }
    }



    private void UpdateSex(String sex) {


        try{

            if (CheckNet.isNetworkConnected(SetSexActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& name=新用户名
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SetSexActivity.this ) );
                param.put( "sex",sex );

                run = new UpdateSexRun(param);
                ThreadPoolManager.getsInstance().execute(run);

            } else {

                Toast.makeText(SetSexActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }



    }



    /**
     *
     * */
    class UpdateSexRun implements Runnable{
        private HashMap<String, String> param;

        UpdateSexRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( SetSexActivity.this,HttpUtil.UPDATE_SEX ,param,new HttpUtil.HttpUtilInterface(){
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
                SetSexActivity.this.onBackPressed();
                SharedPreferencesUtil.putString( SetSexActivity.this,UserUtils.SEX,sex );
            }

            Toast.makeText( SetSexActivity.this,message ,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        SetSexActivity.this.finish();
    }

}
