package catc.tiandao.com.match.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.Banner;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.widgets.loopswitch.AutoLoopSwitchBaseAdapter;

public class MainAutoSwitchAdapter extends AutoLoopSwitchBaseAdapter {

  private Activity mContext;

  private List<Banner> mDatas;

  DisplayImageOptions options = new DisplayImageOptions.Builder()
          .showImageOnLoading( R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
          .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
          .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
          .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
          .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
          .build();

  public MainAutoSwitchAdapter() {
    super();
  }

  public MainAutoSwitchAdapter(Activity mContext, List<Banner> mDatas) {
    this.mContext = mContext;
      this.mDatas = mDatas;
  }

  @Override
  public int getDataCount() {
    return mDatas == null ? 0 : mDatas.size();
  }

  @Override
  public View getView(final int position) {
    final Banner mBanner = mDatas.get(position);

    View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, null);
    ImageView banner_img = ViewUtls.find( view,R.id.banner_img );
    TextView banner_title = ViewUtls.find( view,R.id.banner_title );

    banner_img.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
    ImageLoader.getInstance().displayImage(mBanner.getTitleImageUrl(), banner_img, options);
    //ImageLoader.getInstance().displayImage("drawable://" + mBanner.getTitleImageUrl(), imageView);

    banner_title.setText( mBanner.getcTitle() );

    banner_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        if (!CheckNet.isNetworkConnected(mContext)) {
          Toast.makeText( mContext,"没有可用的网络连接，请检查网络设置",Toast.LENGTH_LONG ).show();
          return;
        }


      }
    });
    return view;
  }

  @Override
  public Object getItem(int position) {
    if (position >= 0 && position < getDataCount()) {

      return mDatas.get(position);
    }
    return null;
  }


  @Override
  public View getEmptyView() {
    return null;
  }

  @Override
  public void updateView(View view, int position) {

  }


  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    super.destroyItem(container, position, object);
  }
}
