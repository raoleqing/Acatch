package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.History;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyViewHolder>  {


    private Context mContext;
    private List<History> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;
    private int BallType;

    private LayoutInflater mInflater;

    public RecordAdapter(Context mContext, List<History> list, int BallType) {
        this.mContext = mContext;
        this.list = list;
        this.BallType = BallType;
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
    public RecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.record_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        History mHistory = list.get( position );
        //2019-04-16T01:00:00+08:00
        String dt = mHistory.getDt();
        if(dt != null && dt.length() >0){
            String date = dt.substring( 0, dt.indexOf( "T" ) );
            String[] dateArray = date.split( "\\-" );
            holder.item_text1.setText( dateArray[0] +  "\n" + dateArray[1] + "-" + dateArray[2] );
        }


        //{\"dt\":\"2019-04-16T01:00:00+08:00\",\"eventName\":\"瑞典超\",\
        // "team1\":\"佐加顿斯\",\"team1Score\":2,\"score\":\"2-1\",\"team2\":\"哥德堡\",\"team2Score\":1,\"panlu\":\"1.0赢\"}


        holder.item_text2.setText( mHistory.getEventName() );
        holder.item_text3.setText( mHistory.getTeam1() );
        holder.item_text4.setText( mHistory.getScore() );
        holder.item_text5.setText( mHistory.getTeam2() );
        if(BallType == 0){
            holder.item_text6.setText( mHistory.getPanlu() );
            holder.item_text6.setVisibility( View.VISIBLE );
        }else {
            holder.item_text6.setVisibility( View.GONE );
        }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView item_text1,item_text2,item_text3,item_text4,item_text5,item_text6;
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
