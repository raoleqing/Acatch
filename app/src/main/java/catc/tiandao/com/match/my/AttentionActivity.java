package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.match.score.BallFragment;
import catc.tiandao.com.match.score.BallFragmentFragment;
import catc.tiandao.com.matchlibrary.ViewUtls;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注
 * **/
public class AttentionActivity extends BaseActivity implements View.OnClickListener, OnFragmentInteractionListener {

    private int[] typeViews = {R.id.game_type1,R.id.game_type2};
    private int[] typeIndicators = {R.id.game_type1_view,R.id.game_type2_view};
    private TextView[] types = new TextView[2];
    private View[] Indicators = new View[2];
    private ViewPager mViewPager;
    private List<Fragment> list = null;
    private FragmentManager manager;
    private MyAdapter adapter;
    private int onPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_attention );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        setTitleText( "我的关注" );
        viewInfo();
        setProgressVisibility( View.GONE );

    }

    private void viewInfo() {

        ImageView activity_return = ViewUtls.find( this,R.id.activity_return );
        activity_return.setOnClickListener( this );

        for(int i = 0; i< typeViews.length; i++){
            types[i] = ViewUtls.find( this, typeViews[i]);
            Indicators[i] = ViewUtls.find( this, typeIndicators[i]);
            types[i].setOnClickListener( this );
        }

        list = new ArrayList<Fragment>();
            list.add(BallFragment.newInstance(0,4));
            list.add(BallFragmentFragment.newInstance(1,4));

        manager = getSupportFragmentManager();
        mViewPager = ViewUtls.find( this,R.id.ball_viewpager );
        adapter = new MyAdapter(manager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        ViewPageListerner vl = new ViewPageListerner();
        mViewPager.addOnPageChangeListener(vl);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_return:
                AttentionActivity.this.onBackPressed();
                break;
        }

        for(int i = 0; i< typeViews.length; i++){

            if(v.getId() == typeViews[i]){
                mViewPager.setCurrentItem( i );
            }
        }
    }

    /*
     * 修改中间的内容 *
     */
    private void setContontView(int position) {
        onPosition = position;
        // TODO Auto-generated method stub

        int color01 = ContextCompat.getColor( this,R.color.text1);
        int color02 = ContextCompat.getColor(this,R.color.text2);

        for(int i = 0; i< typeViews.length; i++){
            if(position == i){
                types[i].setTextColor( color01 );
                Indicators[i].setVisibility( View.VISIBLE );
            }else {

                types[i].setTextColor( color02 );
                Indicators[i].setVisibility( View.GONE );
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_SHOW)){
            setProgressVisibility(View.VISIBLE);
        }else if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_HIDE)){
            setProgressVisibility(View.GONE);
        }

    }

    /**
     * viewPage滑动事件
     */
    class ViewPageListerner implements ViewPager.OnPageChangeListener {

        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        // 当当前页面被滑动时调用
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub;
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            // 修改标题栏
            setContontView(arg0);
        }

    }

    /**
     * 没动加载 FragmentPagerAdapter
     *
     */
    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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
