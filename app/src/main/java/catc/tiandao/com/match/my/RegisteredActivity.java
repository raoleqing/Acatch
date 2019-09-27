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

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText uername,password;
    private CheckBox myCheckbox;
    private TextView protocol;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registered );
        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {


        ImageView activity_return = ViewUtls.find( this,R.id.activity_return );
        uername = ViewUtls.find( this,R.id.phone_text );
        password = ViewUtls.find( this,R.id.code_text );
        myCheckbox = ViewUtls.find( this,R.id.myCheckbox );
        protocol = ViewUtls.find( this,R.id.protocol );
        login = ViewUtls.find( this,R.id.login );

        activity_return.setOnClickListener(this);
        protocol.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                RegisteredActivity.this.onBackPressed();
                break;
            case R.id.protocol:

                break;
            case R.id.login:
                Intent intent01 = new Intent(RegisteredActivity.this, SetPassword.class);
                startActivity(intent01);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;

        }

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
