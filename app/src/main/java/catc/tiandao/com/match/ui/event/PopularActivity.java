package catc.tiandao.com.match.ui.event;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.my.CollectionFragment;
import catc.tiandao.com.match.score.MatchSelection;
import catc.tiandao.com.match.utils.ViewUtls;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


/**
 * 赛事
 * */
public class PopularActivity extends BaseActivity implements View.OnClickListener, OnFragmentInteractionListener  {

    public static final String BALL_TYPE = "BallType";
    public static final String AREA_ID = "areaId";

    private Button football,blueBall;
    private ImageView tv_switch;
    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment mContent;


    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int onPosition = 0;

    private int areaId = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_popular );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        int BallType = getIntent().getIntExtra( BALL_TYPE,0 );
        areaId = getIntent().getIntExtra( AREA_ID,0 );

        setTitleVisibility( View.GONE );
        viewInfo();
        ContentInfo();
        if(BallType > 0){
            setContontView(BallType);
        }
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {
        manager = getSupportFragmentManager();



        ImageView image = ViewUtls.find( this,R.id.tv_return );
        tv_switch = ViewUtls.find( this,R.id.tv_switch );
        image.setOnClickListener( this);


        football = ViewUtls.find( this,R.id.football );
        blueBall = ViewUtls.find( this,R.id.blueBall );

        football.setOnClickListener( this );
        blueBall.setOnClickListener( this );
        tv_switch.setOnClickListener( this );

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_return:
                PopularActivity.this.onBackPressed();
                break;
            case R.id.football:
                if (onPosition != 0) {
                    setContontView( 0 );
                }
                break;
            case R.id.blueBall:
                if (onPosition != 1) {
                    setContontView( 1 );
                }
                break;
            case R.id.tv_switch:

                if(onPosition == 0){
                    Intent intent01 = new Intent(PopularActivity.this, MatchSelection.class);
                    startActivity(intent01);
                    overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }else {
                    Intent intent01 = new Intent( PopularActivity.this, SelectActivity.class);
                    startActivity(intent01);
                    overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }


                break;

        }
    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {
        onPosition = i;
        // TODO Auto-generated method stub
        transaction = manager.beginTransaction();

        int color01 = ContextCompat.getColor( this,R.color.text1);
        int color02 = ContextCompat.getColor(this,R.color.white);

        switch (i) {
            case 0:

                football.setBackgroundResource( R.drawable.bg_search_normal1 );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2_host );
                football.setTextColor( color02 );
                blueBall.setTextColor( color01 );


                if (fragment01 == null) {
                    fragment01 = EventFragment.newInstance(0,areaId);
                }
                switchContent(fragment01, "fragment01");
                break;
            case 1:
                football.setBackgroundResource( R.drawable.bg_search_normal1_host );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2 );
                football.setTextColor( color01 );
                blueBall.setTextColor( color02 );

                if (fragment02 == null) {
                    fragment02 =  EventFragment.newInstance(1,areaId);
                }
                switchContent(fragment02, "fragment02");
                break;

            default:
                break;
        }
    }




    private void ContentInfo() {
        if(fragment01 == null)
            fragment01 = EventFragment.newInstance(0,areaId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.score_content, fragment01,"fragment01").show(fragment01);
        mContent = fragment01;
        transaction.commit();
    }

    /** 修改显示的内容 不会重新加载 **/
    public void switchContent(Fragment to, String tag) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContent).add(R.id.score_content, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

        if(uri.toString().equals( OnFragmentInteractionListener.PROGRESS_SHOW)){
            setProgressVisibility(View.VISIBLE);
        }else if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_HIDE)){
            setProgressVisibility(View.GONE);
        }

    }

    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        PopularActivity.this.finish();
    }

}
