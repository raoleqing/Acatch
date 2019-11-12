package catc.tiandao.com.match.score;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;

public class ScoreDetailsActivity extends BaseActivity implements View.OnClickListener ,OnFragmentInteractionListener{

    public static final String BALL_TYPE = "BallType";
    public static final String BALL_ID = "BallId";
    public static final String MATCH_NAME = "match_name";

    private ImageView iv_return;
    private ImageView iv_up;
    private TextView match_name;

    private Button game_type1,game_type2;
    private View game_type1_view,game_type2_view;

    private int onPosition = 0;
    StatisticsFragment fragment01;
    ImmediateEventByFootBall fragment02;
    private Fragment mContent;

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private int BallType;
    private String BallId;
    private String MatchName;

    private String HomeTeamLogoUrl;
    private String AwayTeamLogoUrl;

    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_score_details );

        setTitleVisibility( View.GONE );
        setTranslucentStatus();

        BallType = this.getIntent().getIntExtra( BALL_TYPE,0 );
        BallId = this.getIntent().getStringExtra( BALL_ID );
        MatchName = this.getIntent().getStringExtra( MATCH_NAME );

        viewInfo();
        ContentInfo();

        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        manager = getSupportFragmentManager();

        int radius = UnitConverterUtils.dip2px(this,22 );

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(radius))  // 设置成圆角图片
                .build();

        iv_return = ViewUtls.find( this,R.id.iv_return );
        iv_up = ViewUtls.find( this,R.id.iv_up );
        match_name = ViewUtls.find( this,R.id.match_name );


        game_type1 = ViewUtls.find( this,R.id.game_type1 );
        game_type2 = ViewUtls.find( this,R.id.game_type2 );
        game_type1_view = ViewUtls.find( this,R.id.game_type1_view );
        game_type2_view = ViewUtls.find( this,R.id.game_type2_view );

        iv_up.setOnClickListener( this );
        iv_return.setOnClickListener( this );
        game_type1.setOnClickListener( this );
        game_type2.setOnClickListener( this );

        match_name.setText( MatchName );

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_return:
                ScoreDetailsActivity.this.onBackPressed();
                break;
            case R.id.iv_up:
                upData();
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

    private void upData() {

        if(fragment01 != null){
            fragment01.upData();
        }

        if(fragment02 != null){
            fragment02.upData();
        }


    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {
        onPosition = i;

        transaction = manager.beginTransaction();

        int color01 = ContextCompat.getColor( this,R.color.text1);
        int color02 = ContextCompat.getColor(this,R.color.text4);


        switch (i) {
            case 0:

                game_type1.setTextColor( color01 );
                game_type2.setTextColor( color02 );
                game_type1_view.setVisibility( View.VISIBLE );
                game_type2_view.setVisibility( View.GONE );

                if (fragment01 == null) {
                    fragment01 = StatisticsFragment.newInstance(BallType,BallId);
                }
                switchContent(fragment01, "fragment01");
                break;
            case 1:
                game_type1.setTextColor( color02 );
                game_type2.setTextColor( color01 );
                game_type1_view.setVisibility( View.GONE );
                game_type2_view.setVisibility( View.VISIBLE );

                if (fragment02 == null) {
                    fragment02 =  ImmediateEventByFootBall.newInstance(BallType,BallId);

                }
                switchContent(fragment02, "fragment02");
                break;

            default:
                break;
        }

    }



        private void ContentInfo() {
            if(fragment01 == null)
                fragment01 = StatisticsFragment.newInstance(BallType,BallId);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_content, fragment01,"fragment01").show(fragment01);
            mContent = fragment01;
            transaction.commit();
        }

        /** 修改显示的内容 不会重新加载 **/
        public void switchContent(Fragment to, String tag) {
            if (mContent != to) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!to.isAdded()) {
                    transaction.hide(mContent).add(R.id.main_content, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
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
        overridePendingTransition( R.anim.day_push_right_in01, R.anim.day_push_right_out);
        ScoreDetailsActivity.this.finish();
    }
}
