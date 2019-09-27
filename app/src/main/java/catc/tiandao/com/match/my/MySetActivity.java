package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.utils.ViewUtls;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MySetActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout push_but_layout01;
    private RelativeLayout push_but_layout02;
    private RelativeLayout push_but_layout03;
    private RelativeLayout push_but_layout04;
    private ImageView push_but_icon01;
    private ImageView push_but_icon02;
    private ImageView push_but_icon03;
    private ImageView push_but_icon04;

    private int isOpen1 = 1;
    private int isOpen2 = 0;
    private int isOpen3 = 0;
    private int isOpen4 = 0;


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


        push_but_layout01.setOnClickListener(this);
        push_but_layout02.setOnClickListener(this);
        push_but_layout03.setOnClickListener(this);
        push_but_layout04.setOnClickListener(this);


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

        }



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
