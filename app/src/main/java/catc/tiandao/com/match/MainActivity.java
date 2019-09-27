package catc.tiandao.com.match;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.my.MyFragment;
import catc.tiandao.com.match.score.ScoreFragment;
import catc.tiandao.com.match.ui.MainFragment;
import catc.tiandao.com.match.utils.ViewUtls;


public class MainActivity extends BaseActivity implements View.OnClickListener , OnFragmentInteractionListener {


    private LinearLayout main_host_layout01,main_host_layout02,main_host_layout03;
    private ImageView main_host_image01,main_host_image02,main_host_image03;
    private TextView main_host_text01,main_host_text02,main_host_text03;

    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment fragment03;
    private Fragment mContent;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int onPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitleVisibility( View.GONE );
        setTranslucentStatus();
        viewInfo();
        ContentInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {

        manager = getSupportFragmentManager();

        main_host_layout01 = ViewUtls.find( this,R.id.main_host_layout01 );
        main_host_layout02 = ViewUtls.find( this,R.id.main_host_layout02 );
        main_host_layout03 = ViewUtls.find( this,R.id.main_host_layout03 );
        main_host_image01 = ViewUtls.find( this,R.id.main_host_image01 );
        main_host_image02 = ViewUtls.find( this,R.id.main_host_image02 );
        main_host_image03 = ViewUtls.find( this,R.id.main_host_image03 );
        main_host_text01 = ViewUtls.find( this,R.id.main_host_text01 );
        main_host_text02 = ViewUtls.find( this,R.id.main_host_text02 );
        main_host_text03 = ViewUtls.find( this,R.id.main_host_text03 );

        main_host_layout01.setOnClickListener( this );
        main_host_layout02.setOnClickListener( this );
        main_host_layout03.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.main_host_layout01:
                if(onPosition != 0){
                    setContontView(0);
                }
                break;
            case R.id.main_host_layout02:
                if(onPosition != 1){
                    setContontView(1);
                }
                break;
            case R.id.main_host_layout03:
                if(onPosition != 2){
                    setContontView(2);
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

        int color01 = ContextCompat.getColor(MainActivity.this,R.color.text1);
        int color02 = ContextCompat.getColor(MainActivity.this,R.color.text2);

        switch (i) {
            case 0:

                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home_default );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me_default );
                main_host_text01.setTextColor( color01 );
                main_host_text02.setTextColor( color02 );
                main_host_text03.setTextColor( color02 );

                if (fragment01 == null) {
                    fragment01 = ScoreFragment.newInstance("","");
                }
                switchContent(fragment01, "fragment01");
                break;
            case 1:

                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match_default );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me_default );
                main_host_text01.setTextColor( color02 );
                main_host_text02.setTextColor( color01 );
                main_host_text03.setTextColor( color02 );

                if (fragment02 == null) {
                    fragment02 =  MainFragment.newInstance("","");
                }
                switchContent(fragment02, "fragment02");
                break;
            case 2:
                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match_default );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home_default );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me );
                main_host_text01.setTextColor( color02 );
                main_host_text02.setTextColor( color02 );
                main_host_text03.setTextColor( color01 );


                if (fragment03 == null) {
                    fragment03 =  MyFragment.newInstance("","");
                }
                switchContent(fragment03, "fragment03");
                break;
            default:
                break;
        }
    }




    private void ContentInfo() {
        if(fragment02 == null)
            fragment02 = MainFragment.newInstance("","");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, fragment02,"fragment02").show(fragment02);
        mContent = fragment02;
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

        if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_SHOW)){
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
        MainActivity.this.finish();

    }


}
