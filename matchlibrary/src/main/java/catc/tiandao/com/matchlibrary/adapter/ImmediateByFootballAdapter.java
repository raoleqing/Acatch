package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.JiShiShiJian;

public class ImmediateByFootballAdapter extends RecyclerView.Adapter<ImmediateByFootballAdapter.MyViewHolder>  {


    private Context mContext;
    private List<JiShiShiJian> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;


    private LayoutInflater mInflater;

    public ImmediateByFootballAdapter(Context mContext, List<JiShiShiJian> list) {
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
    public ImmediateByFootballAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.football_immediate_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        JiShiShiJian mJiShiShiJian = list.get( position );
        int pos = mJiShiShiJian.getPosition();
        if(pos == 1){
            //主
            holder.home_layout.setVisibility( View.VISIBLE );
            holder.away_layout.setVisibility( View.GONE );

            holder.home_time.setText( mJiShiShiJian.getTime() );
            int resource = getImageResource(mJiShiShiJian.getType());
            if(resource > 0){
                holder.home_icon.setImageResource( resource );
                holder.home_icon.setVisibility( View.VISIBLE );
            }else {
                holder.home_icon.setVisibility( View.GONE );
            }

            holder.home_text.setText( mJiShiShiJian.getData() );



        }else {
            holder.home_layout.setVisibility( View.GONE );
            holder.away_layout.setVisibility( View.VISIBLE );

            holder.away_time.setText( mJiShiShiJian.getTime() );
            int resource = getImageResource(mJiShiShiJian.getType());
            if(resource > 0){
                holder.away_icon.setImageResource( resource );
                holder.away_icon.setVisibility( View.VISIBLE );
            }else {
                holder.away_icon.setVisibility( View.GONE );
            }

            holder.away_text.setText( mJiShiShiJian.getData() );


        }

        if(position == list.size() - 1){
            holder.line1.setVisibility( View.GONE );
        }else {
            holder.line1.setVisibility( View.VISIBLE );
        }

        holder.item_view.post(new Runnable() {
            @Override
            public void run() {
                int height = holder.item_view.getHeight();

                ViewGroup.LayoutParams params = holder.line1.getLayoutParams(); //取控件textView当前的布局参数
                params.height = height;
                holder.line1.setLayoutParams(params);


            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private RelativeLayout item_view;
        private View line1;
        private RelativeLayout home_layout;
        private TextView home_time;
        private TextView home_text;
        private ImageView home_icon;
        private LinearLayout away_layout;
        private TextView away_time;
        private ImageView away_icon;
        private TextView away_text;
        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            item_view = ViewUtls.find( view,R.id.item_view );
            line1 = ViewUtls.find( view,R.id.line1 );
            home_layout = ViewUtls.find( view,R.id.home_layout );
            home_time = ViewUtls.find( view,R.id.home_time );
            home_text = ViewUtls.find( view,R.id.home_text );
            home_icon = ViewUtls.find( view,R.id.home_icon );
            away_layout = ViewUtls.find( view,R.id.away_layout );
            away_time = ViewUtls.find( view,R.id.away_time );
            away_icon = ViewUtls.find( view,R.id.away_icon );
            away_text = ViewUtls.find( view,R.id.away_text );

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




    //黄牌、红牌、进球、换人、射门（任意球、球门球、点球）
    private int getImageResource(int type) {

        int res = 0;

        switch (type){
            case 3:
                //黄牌
                res =  R.mipmap.event_icon_yellowcard;
               break;
            case 4:

                res =  R.mipmap.event_icon_redcard;

                //红牌
                break;
            case 1:

                res =  R.mipmap.event_icon_football;
                //进球
                break;
            case 9:
                //换人
                res =  R.mipmap.event_icon_change;
                break;
            case 6:
            case 7:
            case 8:
                //射门
                res =  R.mipmap.event_icon_corners;
                break;
        }

        return res;


    }





}
