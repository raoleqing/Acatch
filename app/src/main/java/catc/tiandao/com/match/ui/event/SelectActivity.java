package catc.tiandao.com.match.ui.event;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.matchlibrary.ViewUtls;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends BaseActivity implements View.OnClickListener,OnFragmentInteractionListener {

    public static final String AREA_ID = "areaId";

    private Button game_type1,game_type2;
    private View game_type1_view,game_type2_view;
    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment mContent;


    private ViewPager mViewPager;

    private List<Fragment> list = null;
    private FragmentManager manager;
    private MyAdapter adapter;

    private int onPosition;


    private int areaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "赛事选择" );
        areaId = this.getIntent().getIntExtra( AREA_ID,0 );

        Constant.isSelect = false;
        Constant.mList.clear();

        viewInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {
        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);


        game_type1 = ViewUtls.find( this,R.id.game_type1 );
        game_type2 = ViewUtls.find( this,R.id.game_type2 );
        game_type1_view = ViewUtls.find( this,R.id.game_type1_view );
        game_type2_view = ViewUtls.find( this,R.id.game_type2_view );

        game_type1.setOnClickListener( this );
        game_type2.setOnClickListener( this );



        list = new ArrayList<Fragment>();
        list.add( SelectFragment1.newInstance(areaId,1,0));
        list.add( SelectFragment.newInstance(areaId,1,1));


        manager = getSupportFragmentManager();
        mViewPager = ViewUtls.find( this,R.id.ball_viewpager );
        adapter = new MyAdapter(manager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

        ViewPageListerner vl = new ViewPageListerner();
        mViewPager.addOnPageChangeListener(vl);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                SelectActivity.this.onBackPressed();
                break;
            case R.id.game_type1:
                if (onPosition != 0) {
                    setContontView( 0 );
                }
                break;
            case R.id.game_type2:
                if (onPosition != 1) {
                    setContontView( 1 );
                }
                break;

        }
    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {

        onPosition = i;
        mViewPager.setCurrentItem( i );

        int color01 = ContextCompat.getColor( this,R.color.text1);
        int color02 = ContextCompat.getColor(this,R.color.text4);


        switch (i) {
            case 0:

                game_type1.setTextColor( color01 );
                game_type2.setTextColor( color02 );
                game_type1_view.setVisibility( View.VISIBLE );
                game_type2_view.setVisibility( View.GONE );

                break;
            case 1:
                game_type1.setTextColor( color02 );
                game_type2.setTextColor( color01 );
                game_type1_view.setVisibility( View.GONE );
                game_type2_view.setVisibility( View.VISIBLE );

                break;

            default:
                break;
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
            if(arg0 == 0){
                EventBus.getDefault().post( Constant.SELECT_UP);
            }
            setContontView(arg0);

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
        SelectActivity.this.finish();
    }
}
