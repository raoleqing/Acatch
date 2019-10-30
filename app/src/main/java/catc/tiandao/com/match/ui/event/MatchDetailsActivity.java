package catc.tiandao.com.match.ui.event;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.socialize.bean.SHARE_MEDIA;

import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UmengUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;


/**
 * 球赛详情页
 **/
public class MatchDetailsActivity extends BaseActivity implements View.OnClickListener, OnFragmentInteractionListener {

    public static final String BALL_TYPE = "BallType";
    public static final String BALL_ID = "BallId";

    private int[] typeViews = {R.id.game_type1, R.id.game_type2, R.id.game_type3, R.id.game_type4, R.id.game_type5};
    private int[] typeIndicators = {R.id.game_type1_view, R.id.game_type2_view, R.id.game_type3_view, R.id.game_type4_view, R.id.game_type5_view};
    private TextView[] types = new TextView[5];
    private View[] Indicators = new View[5];


    private int contentIndex = 0;
    Fragment fragment01;
    Fragment fragment02;
    Fragment fragment03;
    Fragment fragment04;
    Fragment fragment05;
    private Fragment mContent;

    private int BallType;
    private String BallId;
    private String MatchName;
    private String HomeTeamLogoUrl = "", AwayTeamLogoUrl = "", HomeTeamName = "", AwayTeamName = "";

    private DisplayImageOptions options;
    private RelativeLayout rl_contianer;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        setTitleVisibility(View.GONE);
        setTranslucentStatus();

        BallType = this.getIntent().getIntExtra(BALL_TYPE, 0);
        BallId = this.getIntent().getStringExtra(BALL_ID);

        viewInfo();
        initContent();
        setProgressVisibility(View.GONE);
    }

    private void viewInfo() {

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();

        ImageView iv_return = ViewUtls.find(this, R.id.iv_return);
        ImageView iv_share = ViewUtls.find(this, R.id.iv_share);
        rl_contianer = ViewUtls.find(this, R.id.rl_container);


        iv_return.setOnClickListener(this);
        iv_share.setOnClickListener(this);

        for (int i = 0; i < typeViews.length; i++) {
            types[i] = ViewUtls.find(this, typeViews[i]);
            Indicators[i] = ViewUtls.find(this, typeIndicators[i]);
            types[i].setOnClickListener(this);

        }


    }

    private void initContent() {
        if (fragment01 == null)
            fragment01 = IntelligenceFragment.newInstance(BallType, BallId);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, fragment01, "fragment01").show(fragment01);
        mContent = fragment01;
        transaction.commit();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_return:

                MatchDetailsActivity.this.onBackPressed();

                break;

            case R.id.iv_share:
                showShare();
                break;

        }


        for (int i = 0; i < typeViews.length; i++) {

            if (v.getId() == typeViews[i]) {
                if (i == 4) {

                    if (UserUtils.isLanded(MatchDetailsActivity.this)) {
                        setContontViewColor(i);
                        setViewContent(i);
                    } else {
                        UserUtils.startLongin(MatchDetailsActivity.this);
                    }

                } else {

                    setContontViewColor(i);
                    setViewContent(i);

                }

            }

        }
    }

    private void showShare() {
        //分享
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_share, null);
            popupWindow = new PopupWindow(this);
            popupWindow.setContentView(contentView);
            popupWindow.setAnimationStyle(R.style.bottomShowAnimStyle);
            popupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1f);
                }
            });

            contentView.findViewById(R.id.iv_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.QZONE);
                    popupWindow.dismiss();
                }
            });
            contentView.findViewById(R.id.iv_moment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rl_contianer, Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.5f);
    }

    /***设置背景透明度*/
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    /***分享*/
    private void singleShare(SHARE_MEDIA shareMedia) {
//        足球：http://www.leisuvip1.com/New/Football?matchid=比赛id
//        篮球：http://www.leisuvip1.com/New/Basketball?matchid=比赛id
        int logoResId = R.mipmap.app_icon;

        UmengUtil.shareSinglePlatform(this, shareMedia, "http://www.leisuvip1.com/New/Football?matchid=" + BallId, "分享标题", logoResId, "分享描述内容");
    }


    /*
     * 修改中间的内容 *
     */
    private void setContontViewColor(int position) {
        // TODO Auto-generated method stub

        int color01 = ContextCompat.getColor(this, R.color.text1);
        int color02 = ContextCompat.getColor(this, R.color.text4);

        for (int i = 0; i < typeViews.length; i++) {
            if (position == i) {
                types[i].setTextColor(color01);
                Indicators[i].setVisibility(View.VISIBLE);
            } else {

                types[i].setTextColor(color02);
                Indicators[i].setVisibility(View.GONE);
            }
        }
    }


    private void setViewContent(int i) {

        contentIndex = i;


        switch (i) {
            case 0:

                if (fragment01 == null) {
                    fragment01 = IntelligenceFragment.newInstance(BallType, BallId);
                }
                switchContent(fragment01);

                break;
            case 1:


                if (fragment02 == null) {
                    fragment02 = ExponentFragment.newInstance(BallType, BallId);
                }
                switchContent(fragment02);
                break;
            case 2:

                if (fragment03 == null) {
                    fragment03 = BattleFragment.newInstance(BallType, BallId, HomeTeamLogoUrl, AwayTeamLogoUrl, HomeTeamName, AwayTeamName);
                }
                switchContent(fragment03);

                break;
            case 3:

                if (fragment04 == null) {
                    fragment04 = dataFragment.newInstance(BallType, BallId);
                }
                switchContent(fragment04);
                break;

            case 4:

                if (fragment05 == null) {
                    fragment05 = MemberInformation.newInstance(BallType, BallId);
                }
                switchContent(fragment05);
                break;

            default:
                break;
        }


    }


    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContent).add(R.id.main_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }


    public void setTitleContent(String HomeTeamLogoUrl, String AwayTeamLogoUrl, String HomeTeamName, String AwayTeamName) {

        this.HomeTeamLogoUrl = HomeTeamLogoUrl;
        this.AwayTeamLogoUrl = AwayTeamLogoUrl;
        this.HomeTeamName = HomeTeamName;
        this.HomeTeamName = HomeTeamName;
        this.AwayTeamName = AwayTeamName;

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

        if (uri.toString().equals(OnFragmentInteractionListener.PROGRESS_SHOW)) {
            setProgressVisibility(View.VISIBLE);
        } else if (uri.toString().equals(OnFragmentInteractionListener.PROGRESS_HIDE)) {
            setProgressVisibility(View.GONE);
        }

    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        MatchDetailsActivity.this.finish();
    }


}
