package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.NoticeAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.ViewUtls;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends BaseActivity implements View.OnClickListener{


    private RecyclerView notice_recycler;

    private LinearLayoutManager mLinearLayoutManager;
    private NoticeAdapter mAdapter;
    private List<BallBen> mList = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notice );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "通知" );
         viewInfo();
        setProgressVisibility( View.GONE );
    }


    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);


        notice_recycler = ViewUtls.find( this,R.id.notice_recycler );

        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        notice_recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NoticeAdapter(this,mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        });
        // 设置adapter
        notice_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        notice_recycler.setItemAnimator(new DefaultItemAnimator());

        //添加Android自带的分割线
        notice_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                NoticeActivity.this.onBackPressed();
                break;


        }
    }





    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        NoticeActivity.this.finish();
    }
}
