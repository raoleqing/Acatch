package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.BasketballAdapter;
import catc.tiandao.com.match.adapter.CollectionAdapter;
import catc.tiandao.com.match.ben.BallFragmentBen;
import catc.tiandao.com.match.ben.DateBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.score.ScoreDetailsActivity;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.ui.news.NewsDetailsActivity;
import catc.tiandao.com.match.utils.ViewUtls;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AttentionActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView myRecyclerView;
    private BasketballAdapter mAdapter;

    private List<BallFragmentBen> mList = new ArrayList(  );
    private TextView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_attention );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        setTitleText( "我的收藏" );
        viewInfo();
        no_data.setVisibility( View.VISIBLE );
        setProgressVisibility( View.GONE );

    }

    private void viewInfo() {
        ImageView image = ViewUtls.find( this,R.id.activity_return );
        no_data = ViewUtls.find( this,R.id.no_data );
        myRecyclerView = ViewUtls.find( this,R.id.myRecyclerView );


        image.setOnClickListener( this);


        // 设置布局管理器
        myRecyclerView.setLayoutManager(new LinearLayoutManager( this ));
        mAdapter = new BasketballAdapter(AttentionActivity.this,mList,0);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {


            }
        });
        // 设置adapter
        myRecyclerView.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        myRecyclerView.addItemDecoration(new DividerItemDecoration(AttentionActivity.this, DividerItemDecoration.VERTICAL));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                AttentionActivity.this.onBackPressed();
                break;
        }

    }



    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        AttentionActivity.this.finish();
    }


}
