package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.utils.ViewUtls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView close_image;
    private EditText uername,password;
    private CheckBox myCheckbox;
    private TextView protocol,registered,forget_password;
    private Button login;
    private ImageView wexin_but,qq_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        setTitleVisibility( View.GONE );
        setTranslucentStatus();

        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {


        close_image = ViewUtls.find( this,R.id.close_image );
        uername = ViewUtls.find( this,R.id.uername );
        password = ViewUtls.find( this,R.id.password );
        myCheckbox = ViewUtls.find( this,R.id.myCheckbox );
        protocol = ViewUtls.find( this,R.id.protocol );
        registered = ViewUtls.find( this,R.id.registered );
        forget_password = ViewUtls.find( this,R.id.forget_password );
        login = ViewUtls.find( this,R.id.login );
        wexin_but = ViewUtls.find( this,R.id.wexin_but );
        qq_but = ViewUtls.find( this,R.id.qq_but );
        close_image.setOnClickListener(this);
        protocol.setOnClickListener(this);
        registered.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        wexin_but.setOnClickListener(this);
        qq_but.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.close_image:
                LoginActivity.this.onBackPressed();
                break;
            case R.id.protocol:

                break;
            case R.id.registered:

                Intent intent01 = new Intent(LoginActivity.this, RegisteredActivity.class);
                startActivity(intent01);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.forget_password:

                Intent intent02 = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent02);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.wexin_but:

                break;
            case R.id.qq_but:

                break;
        }

    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        LoginActivity.this.finish();
    }



}
