package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.ViewUtls;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AttentionActivity extends BaseActivity implements View.OnClickListener, OnFragmentInteractionListener {

    private Button football,blueBall;
    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment mContent;


    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int onPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_collection );

        setTitleVisibility( View.GONE );
        viewInfo();
        ContentInfo();
        setProgressVisibility( View.GONE );
    }

    private void viewInfo() {
        manager = getSupportFragmentManager();

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);


        football = ViewUtls.find( this,R.id.football );
        blueBall = ViewUtls.find( this,R.id.blueBall );

        football.setOnClickListener( this );
        blueBall.setOnClickListener( this );

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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
                    fragment01 = CollectionFragment.newInstance(0,"");
                }
                switchContent(fragment01, "fragment01");
                break;
            case 1:
                football.setBackgroundResource( R.drawable.bg_search_normal1_host );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2 );
                football.setTextColor( color01 );
                blueBall.setTextColor( color02 );

                if (fragment02 == null) {
                    fragment02 =  CollectionFragment.newInstance(1,"");
                }
                switchContent(fragment02, "fragment02");
                break;

            default:
                break;
        }
    }




    private void ContentInfo() {
        if(fragment01 == null)
            fragment01 = CollectionFragment.newInstance(0,"");

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
        AttentionActivity.this.finish();
    }
}
