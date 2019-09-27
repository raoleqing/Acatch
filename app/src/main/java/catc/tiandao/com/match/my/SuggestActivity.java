package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.NoticeAdapter;
import catc.tiandao.com.match.adapter.SuggestTypeAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.common.GridSpacingItemDecoration;
import catc.tiandao.com.match.common.MyGridLayoutManager;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class SuggestActivity extends BaseActivity implements View.OnClickListener {


    private RecyclerView notice_recycler;

    private LinearLayoutManager mLinearLayoutManager;
    private SuggestTypeAdapter mAdapter;
    private String[] array = {"优化建议","系统bug","赛事内容","设计不好看","其他"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_suggest );

        setTitleText( "投诉与建议" );
        viewInfo();
        setProgressVisibility( View.GONE );
    }


    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);

        notice_recycler = ViewUtls.find( this,R.id.type_recycler );

        RecyclerView.LayoutManager mLayoutManager = new MyGridLayoutManager(this, 3);
        notice_recycler.setLayoutManager(mLayoutManager);
        mAdapter = new SuggestTypeAdapter(this, array);
        // 设置adapter
        notice_recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

                mAdapter.setShowType( postion );
                mAdapter.notifyDataSetChanged();



            }
        } );
        int space = UnitConverterUtils.dip2px(this, 15);
        notice_recycler.addItemDecoration(new GridSpacingItemDecoration(3,space,false));

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                SuggestActivity.this.onBackPressed();
                break;


        }



    }





    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        SuggestActivity.this.finish();
    }

}
