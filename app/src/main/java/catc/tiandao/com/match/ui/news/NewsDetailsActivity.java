package catc.tiandao.com.match.ui.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.utils.ViewUtls;
import cn.jzvd.Jzvd;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class NewsDetailsActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news_details );
        setTitleText( "资讯速递" );
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

                NewsDetailsActivity.this.onBackPressed();
                break;
        }

    }

    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        NewsDetailsActivity.this.finish();
    }



}
