package catc.tiandao.com.match.my;

import androidx.appcompat.app.AppCompatActivity;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.UserBen;
import catc.tiandao.com.match.common.CircleImageView;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyDataActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout iv_name, iv_sex;
    private CircleImageView user_icon;
    private TextView user_name,user_phone,user_sex;

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_my_data );

        setTitleText( "个人资料" );

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_def_avatar)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_def_avatar)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_def_avatar)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();



        viewInfo();
        setUserContent();
        setProgressVisibility( View.GONE );
    }

    @Override
    protected void onResume() {
        super.onResume();

        setData();
    }

    private void setData() {

        UserBen muUserBen = UserUtils.getUserInfo( this );

        user_name.setText( muUserBen.getNickName() );
        user_phone.setText( muUserBen.getPhone() );
        user_sex.setText( muUserBen.getSex() );
    }

    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);

        iv_name = ViewUtls.find( this,R.id.iv_name );
        iv_sex = ViewUtls.find( this,R.id.iv_sex );
        user_name = ViewUtls.find( this,R.id.user_name );
        user_phone = ViewUtls.find( this,R.id.user_phone );
        user_sex = ViewUtls.find( this,R.id.user_sex );

        iv_name.setOnClickListener( this );
        iv_sex.setOnClickListener( this );

    }


    private void setUserContent() {

        String Appavatar = UserUtils.getUserAvatar(MyDataActivity.this);
        if (Appavatar != null && !"".equals(Appavatar)) {
            ImageLoader.getInstance().displayImage(Appavatar, user_icon,options);
        }
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                MyDataActivity.this.onBackPressed();
                break;
            case R.id.iv_name:

                Intent intent = new Intent(MyDataActivity.this, SetNameActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;

            case R.id.iv_sex:

                Intent intent1 = new Intent(MyDataActivity.this, SetSexActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);


                break;
        }
    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        MyDataActivity.this.finish();
    }

}
