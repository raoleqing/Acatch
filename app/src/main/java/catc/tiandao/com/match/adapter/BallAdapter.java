package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class BallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<BallBen> mList;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;

    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status=0;  //上拉加载更多状态-默认为0
    private LayoutInflater mInflater;

    DisplayImageOptions options;

    //没有数据了
    public  static final int NO_DATA = -1;
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中showType
    public static final int  LOADING_MORE=1;





    public BallAdapter(Context mContext, List<BallBen> mList,int showType) {
        this.mContext = mContext;
        this.mList = mList;
        this.showType = showType;
        this.mInflater=LayoutInflater.from(mContext);

        int radius = UnitConverterUtils.dip2px(mContext,11 );

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(radius))  // 设置成圆角图片
                .build();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            View itemView =mInflater.inflate( R.layout.ball_item,parent,false);
            MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
            return viewHolder;
        }else if(viewType==TYPE_FOOTER){
            View foot_view = mInflater.inflate(R.layout.xlistview_footer,parent,false);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view,mItemClickListener, mItemLongClickListener);
            return footViewHolder;
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MyViewHolder) {
            //正常数据
            MyViewHolder mMyViewHolder = (MyViewHolder)holder;

            BallBen mBallBen = mList.get( position );
            mMyViewHolder.match_event_name.setText( mBallBen.getMatchEventName() + " 第"+ mBallBen.getMatchRound()+"轮 " +  mBallBen.getMatchTime());
            //"matchStatusId": 4, //1-未开赛，2-7都是进行中， 8结束，大于8都是中断、取消类的
            if(mBallBen.getMatchStatusId() >= 2 && mBallBen.getMatchStatusId() <= 7){
                mMyViewHolder.match_status.setText(  mBallBen.getMatchBeginTime() );
            }else {
                mMyViewHolder.match_status.setText(  mBallBen.getMatchStatus() );
            }

            mMyViewHolder.match_value.setText(mBallBen.getNote());

            if(mBallBen.getIsCollection() == 0){
                mMyViewHolder.is_collection.setBackgroundResource( R.mipmap.icon_collect_default );
            }else {
                mMyViewHolder.is_collection.setBackgroundResource( R.mipmap.icon_collect );
            }

            if(mBallBen.getHomeTeamRed() > 0){
                mMyViewHolder.home_red.setVisibility( View.VISIBLE );
                mMyViewHolder.home_red.setText( mBallBen.getHomeTeamRed() + "" );
            }else {
                mMyViewHolder.home_red.setVisibility( View.GONE );

            }

            if(mBallBen.getHomeTeamYellow() > 0){
                mMyViewHolder.home_yellow.setVisibility( View.VISIBLE );
                mMyViewHolder.home_yellow.setText( mBallBen.getHomeTeamYellow() + "" );
            }else {
                mMyViewHolder.home_yellow.setVisibility( View.GONE );
            }

            mMyViewHolder.home_team_name.setText( mBallBen.getHomeTeamName() );
            ImageLoader.getInstance().displayImage(mBallBen.getHomeTeamLogoUrl(), mMyViewHolder.home_team_logoUrl,options);

            mMyViewHolder.score.setText( mBallBen.getHomeTeamScore() + " - " +  mBallBen.getAwayTeamScore());

            ImageLoader.getInstance().displayImage(mBallBen.getAwayTeamLogoUrl(), mMyViewHolder.away_team_logoUrl,options);
            mMyViewHolder.away_teamName.setText( mBallBen.getAwayTeamName() );

            if(mBallBen.getAwayTeamRed() > 0){
                mMyViewHolder.away_red.setVisibility( View.VISIBLE );
                mMyViewHolder.away_red.setText( mBallBen.getHomeTeamRed() + "" );
            }else {
                mMyViewHolder.away_red.setVisibility( View.GONE );

            }

            if(mBallBen.getAwayTeamYellow() > 0){
                mMyViewHolder.away_yellow.setVisibility( View.VISIBLE );
                mMyViewHolder.away_yellow.setText( mBallBen.getHomeTeamYellow() + "" );
            }else {
                mMyViewHolder.away_yellow.setVisibility( View.GONE );
            }


        }else if(holder instanceof FootViewHolder){
            //上拉加载
            FootViewHolder footViewHolder=(FootViewHolder)holder;
            switch (load_more_status){
                case NO_DATA:
                    footViewHolder.mContentView.setVisibility(View.GONE);
                    break;
                case PULLUP_LOAD_MORE:
                    footViewHolder.mHintView.setText("上拉加载更多...");
                    footViewHolder.mProgressBar.setVisibility(View.INVISIBLE);
                    footViewHolder.mContentView.setVisibility(View.VISIBLE);
                    break;
                case LOADING_MORE:
                    footViewHolder.mHintView.setText("正在加载更多数据...");
                    footViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    footViewHolder.mContentView.setVisibility(View.VISIBLE);
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return mList == null || mList.size() == 0 ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
       if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private TextView match_event_name;
        private TextView match_status;
        private ImageView is_collection;
        private TextView home_red;
        private TextView home_yellow;
        private TextView home_team_name;
        private ImageView home_team_logoUrl;
        private TextView score;
        private ImageView away_team_logoUrl;
        private TextView away_teamName;
        private TextView away_red;
        private TextView away_yellow;
        private TextView match_value;

        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);

            match_event_name = ViewUtls.find( view,R.id.match_event_name );
            match_status = ViewUtls.find( view,R.id.match_status );
            is_collection = ViewUtls.find( view,R.id.is_collection );
            home_red = ViewUtls.find( view,R.id.home_red );
            home_yellow = ViewUtls.find( view,R.id.home_yellow );
            home_team_name = ViewUtls.find( view,R.id.home_team_name );
            home_team_logoUrl = ViewUtls.find( view,R.id.home_team_logoUrl );
            score = ViewUtls.find( view,R.id.score );
            away_team_logoUrl = ViewUtls.find( view,R.id.away_team_logoUrl );
            away_teamName = ViewUtls.find( view,R.id.away_teamName );
            away_red = ViewUtls.find( view,R.id.away_red );
            away_yellow = ViewUtls.find( view,R.id.away_yellow );
            match_value = ViewUtls.find( view,R.id.match_value );

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            view.setOnClickListener( this );

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

        /**
         * 长按监听
         */
        @Override
        public boolean onLongClick(View arg0) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(arg0, getPosition());
            }
            return true;
        }
    }


    class FootViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private RelativeLayout mContentView;
        private ProgressBar mProgressBar;
        private TextView mHintView;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public FootViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);
            mContentView = (RelativeLayout) view.findViewById(R.id.xlistview_footer_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.xlistview_footer_progressbar);
            mHintView = (TextView) view.findViewById(R.id.xlistview_footer_hint_textview);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
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

        /**
         * 长按监听
         */
        @Override
        public boolean onLongClick(View arg0) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(arg0, getPosition());
            }
            return true;
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


    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }


    //添加数据
    public void addItem(List<BallBen> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mList);
        mList.removeAll(mList);
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<BallBen> newDatas) {
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     * @param status
     */
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }




    public void setShowType(int type){
        this.showType = type;
    }
}
