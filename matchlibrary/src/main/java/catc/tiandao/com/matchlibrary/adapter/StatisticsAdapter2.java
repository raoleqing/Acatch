package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.ben.BasketZhenRong;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;

public class StatisticsAdapter2 extends RecyclerView.Adapter<StatisticsAdapter2.MyViewHolder>  {


    private Context mContext;
    private List<BasketZhenRong> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;


    private LayoutInflater mInflater;

    public StatisticsAdapter2(Context mContext, List<BasketZhenRong> list) {
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
    public StatisticsAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.statistics_item2,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BasketZhenRong mBasketZhenRong = list.get( position );
        holder.item_text1.setText( mBasketZhenRong.getName_zh() );
        holder.item_text2.setText( mBasketZhenRong.getIsShouFa() == 0 ? "是":"否" );
        //在场持续时间^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^+/-值^得分^是否出场(1-出场，0-没出场)^是否在场上（0-在场上，1-没在场上）^是否是替补（1-替补，0-首发）
        //13^2-8^0-0^0-0^2^2^4^0^0^0^1^0^2^4^1^0^0\
        String Note = mBasketZhenRong.getNote();
        String[] noteArray = Note.split( "\\^" );
        holder.item_text3.setText( noteArray[0] );
        if(noteArray.length > 14)
        holder.item_text4.setText( noteArray[14] );
        holder.item_text5.setText( noteArray[1] );
        holder.item_text6.setText( noteArray[2] );


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView item_text1;
        private TextView item_text2;
        private TextView item_text3;
        private TextView item_text4;
        private TextView item_text5;
        private TextView item_text6;
        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            item_text1 = ViewUtls.find( view,R.id.item_text1 );
            item_text2 = ViewUtls.find( view,R.id.item_text2 );
            item_text3 = ViewUtls.find( view,R.id.item_text3 );
            item_text4 = ViewUtls.find( view,R.id.item_text4 );
            item_text5 = ViewUtls.find( view,R.id.item_text5 );
            item_text6 = ViewUtls.find( view,R.id.item_text6 );
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
