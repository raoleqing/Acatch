package catc.tiandao.com.match.ui.expert;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.ExpertAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.ViewUtls;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpertActivity extends BaseActivity implements  View.OnClickListener{

    private RecyclerView ball_recycler;

    private LinearLayoutManager mLinearLayoutManager;
    private ExpertAdapter mAdapter;
    private List<BallBen> mList = new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert );
        setTitleVisibility( View.GONE );
        setTranslucentStatus();
        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        ImageView returnImage = ViewUtls.find( this,R.id.tv_return );
        returnImage.setOnClickListener( this );

        ball_recycler = ViewUtls.find( this,R.id.ball_recycler );

        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        };

        ball_recycler.setLayoutManager(mLinearLayoutManager);
        ball_recycler.setHasFixedSize(true);
        ball_recycler.setNestedScrollingEnabled(false);

        mAdapter = new ExpertAdapter(this,mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

                showExpertCodeDialog();

            }
        });
        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(ExpertActivity.this, DividerItemDecoration.VERTICAL));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                ExpertActivity.this.onBackPressed();
                break;
        }

    }


    private void showExpertCodeDialog() {


        View view = getLayoutInflater().inflate(R.layout.expert_code, null);
        ImageView close_icon = ViewUtls.find( view,R.id.close_image );


        final Dialog dialog = new Dialog(ExpertActivity.this, R.style.car_order_dialog);
        dialog.setContentView(view);
        dialog.show();

        close_icon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } );

    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        ExpertActivity.this.finish();
    }



}
