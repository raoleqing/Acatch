package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.utils.ViewUtls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ForgetPassword extends BaseActivity implements View.OnClickListener {


    private EditText password1,password2;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forget_password );
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

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                ForgetPassword.this.onBackPressed();
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
        ForgetPassword.this.finish();
    }

}
