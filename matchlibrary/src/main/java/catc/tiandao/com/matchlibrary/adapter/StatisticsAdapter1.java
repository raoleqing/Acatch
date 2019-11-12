package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.BasketJiShuTongJi;
import catc.tiandao.com.matchlibrary.ben.JiShuTongJi;

public class StatisticsAdapter1 extends RecyclerView.Adapter<StatisticsAdapter1.MyViewHolder>  {


    private Context mContext;
    private List<Object> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;


    private LayoutInflater mInflater;

    public StatisticsAdapter1(Context mContext, List<Object> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater=LayoutInflater.from(mContext);


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading( R.mipmap.mall_cbg )          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg )  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg )       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer( UnitConverterUtils.dip2px( mContext,6 )))  // 设置成圆角图片
                .build();

    }

    @Override
    public StatisticsAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.statistics_item1,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Object obj = list.get( position );

        // if(holder instanceof MyViewHolder)
        if(obj instanceof JiShuTongJi){
            JiShuTongJi mJiShuTongJi = (JiShuTongJi)obj;
            holder.name.setText( mJiShuTongJi.getName() );
            holder.home.setText( mJiShuTongJi.getHome() + "" );
            holder.away.setText( mJiShuTongJi.getAway() + "" );

            int sum = mJiShuTongJi.getHome() + mJiShuTongJi.getAway();

            final float homeRatio = (float)mJiShuTongJi.getHome() / (float)sum;
            final float awayRatio = (float)mJiShuTongJi.getAway() / (float)sum;

            holder.away_view1.post(new Runnable() {
                @Override
                public void run() {
                    int width = holder.away_view1.getWidth();

                    ViewGroup.LayoutParams params = holder.home_view2.getLayoutParams(); //取控件textView当前的布局参数
                    params.width = (int)(homeRatio * (float) width);
                    holder.home_view2.setLayoutParams(params);


                    ViewGroup.LayoutParams params1 = holder.away_view2.getLayoutParams(); //取控件textView当前的布局参数
                    params1.width = (int)(awayRatio * (float)width);
                    holder.away_view2.setLayoutParams(params1);

                }
            });

        }else if(obj instanceof BasketJiShuTongJi){

            BasketJiShuTongJi mJiShuTongJi = (BasketJiShuTongJi)obj;
            holder.name.setText( mJiShuTongJi.getTechName());
            holder.home.setText( mJiShuTongJi.getZhuDui() + "" );
            holder.away.setText( mJiShuTongJi.getKeDui() + "" );

            int sum = (int)(mJiShuTongJi.getZhuDui() + mJiShuTongJi.getKeDui());

            final float homeRatio = (float)mJiShuTongJi.getZhuDui() / (float)sum;
            final float awayRatio = (float)mJiShuTongJi.getKeDui() / (float)sum;

            holder.away_view1.post(new Runnable() {
                @Override
                public void run() {
                    int width = holder.away_view1.getWidth();

                    ViewGroup.LayoutParams params = holder.home_view2.getLayoutParams(); //取控件textView当前的布局参数
                    params.width = (int)(homeRatio * (float) width);
                    holder.home_view2.setLayoutParams(params);


                    ViewGroup.LayoutParams params1 = holder.away_view2.getLayoutParams(); //取控件textView当前的布局参数
                    params1.width = (int)(awayRatio * (float)width);
                    holder.away_view2.setLayoutParams(params1);

                }
            });

        }






    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView name;
        private TextView home;
        private TextView away;
        private ImageView home_view1;
        private ImageView home_view2;
        private ImageView away_view1;
        private ImageView away_view2;
        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            name = ViewUtls.find( view,R.id.name );
            home = ViewUtls.find( view,R.id.home );
            away = ViewUtls.find( view,R.id.away );
            home_view1 = ViewUtls.find( view,R.id.home_view1 );
            home_view2 = ViewUtls.find( view,R.id.home_view2 );
            away_view1 = ViewUtls.find( view,R.id.away_view1 );
            away_view2 = ViewUtls.find( view,R.id.away_view2 );

            this.mListener = listener;
        }

        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getLayoutPosition(), 0);
            }
        }


    }



    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }



}
