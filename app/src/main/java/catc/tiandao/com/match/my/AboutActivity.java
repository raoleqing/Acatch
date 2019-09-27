package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.NoticeAdapter;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.ViewUtls;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about );
        setTitleText( "关于我们" );
        viewInfo();
        setProgressVisibility( View.GONE );
    }



    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                AboutActivity.this.onBackPressed();
                break;


        }
    }





    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        AboutActivity.this.finish();
    }

}
