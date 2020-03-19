package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.ben.Match;

/**
 * Created by Administrator on 2017/12/7 0007.
 * ExpertAdapter
 * AnimalAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
//继承并实现StickyRecyclerHeadersAdapter

    private Context mContext;
        private List<Match> dataList;
        private int BallType;

    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status=0;  //上拉加载更多状态-默认为0
    private LayoutInflater mInflater;

    private MyItemClickListener mItemClickListener;


    //没有数据了
    public  static final int NO_DATA = -1;
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;



    DisplayImageOptions options;


    public EventAdapter(Context mContext, List<Match> dataList, int BallType) {
            this.dataList = dataList;
            this.mContext = mContext;
            this.BallType = BallType;
            setHasStableIds(true);
            this.mInflater=LayoutInflater.from(mContext);

            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                   // .displayer(new RoundedBitmapDisplayer(radius))  // 设置成圆角图片
                    .build();
        }

        @Override
        public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            //进行判断显示类型，来创建返回不同的View
            if (viewType == TYPE_ITEM) {
                View itemView = mInflater.inflate( R.layout.collection_item, parent, false );
                MyViewHolder viewHolder = new MyViewHolder( itemView,mItemClickListener );
                return viewHolder;
            } else if (viewType == TYPE_FOOTER) {
                View foot_view = mInflater.inflate( R.layout.xlistview_footer, parent, false );
                FootViewHolder footViewHolder = new FootViewHolder( foot_view );
                return footViewHolder;
            }

            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


            if(holder instanceof MyViewHolder) {

                final MyViewHolder myViewHolder = (MyViewHolder) holder;

                if (position >= dataList.size()) {
                    return;
                }
                Match mMatch = dataList.get( position );

                //  "matchEventName": "匈甲",
                //                "matchRound": 0,
                if(BallType == 0){
                    myViewHolder.match_name.setText( mMatch.getMatchEventName() + " 第"+ mMatch.getMatchRound() + "轮  " + mMatch.getMatchTime() );
                }else {
                    myViewHolder.match_name.setText( mMatch.getMatchEventName() + " " + mMatch.getMatchTime() );
                }

                myViewHolder.match_status.setText( mMatch.getMatchStatus() );

                if(mMatch.getIsCollection() == 0){
                    myViewHolder.is_collection.setImageResource( R.mipmap.icon_collect_default );
                }else {
                    myViewHolder.is_collection.setImageResource( R.mipmap.icon_collect );
                }

                myViewHolder.home_team_name.setText( mMatch.getHomeTeamName() );
                ImageLoader.getInstance().displayImage(mMatch.getHomeTeamLogoUrl(), myViewHolder.home_team_icon,options);


                myViewHolder.team_name.setText( mMatch.getAwayTeamName() );
                ImageLoader.getInstance().displayImage(mMatch.getAwayTeamLogoUrl(), myViewHolder.team_icon,options);


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
            return dataList == null || dataList.size() == 0 ? 0 : dataList.size() + 1;
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



        //必须重写  不然item会错乱
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);
            return new MyHeaderViewHolder(itemView);
        } //headView

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (position + 1 < getItemCount()) {
                MyHeaderViewHolder myHeaderViewHolder = (MyHeaderViewHolder) viewHolder;
                myHeaderViewHolder.group_item_name.setText(dataList.get(position).getMatchDate() + "  星期" + dataList.get(position).getMatchWeek());  //设置head数据
            }

        }


        @Override
        public long getHeaderId(int position) {

            if (position + 1 >= getItemCount()) {
                return -1;
            } else {
                //得到headId 唯一性
                long headName = Math.abs( dataList.get(position).getMatchDate().hashCode() );
                return headName;
            }

        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public RelativeLayout item_view;
            public TextView match_name;
            public TextView match_status;
            public ImageView is_collection;
            public TextView home_team_name;
            public ImageView home_team_icon;
            public TextView team_name;
            public ImageView team_icon;

            private MyItemClickListener mListener;

            public MyViewHolder(View view,MyItemClickListener listener) {
                super(view);
                item_view = (RelativeLayout) view.findViewById(R.id.item_view);
                match_name = (TextView) view.findViewById(R.id.match_name);
                match_status = (TextView) view.findViewById(R.id.match_status);
                is_collection = (ImageView) view.findViewById(R.id.is_collection);
                home_team_name = (TextView) view.findViewById(R.id.home_team_name);
                home_team_icon = (ImageView) view.findViewById(R.id.home_team_icon);
                team_name = (TextView) view.findViewById(R.id.team_name);
                team_icon = (ImageView) view.findViewById(R.id.team_icon);

                this.mListener = listener;
                item_view.setOnClickListener( this );
                is_collection.setOnClickListener( this );


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


        }// item



    class FootViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mContentView;
        private ProgressBar mProgressBar;
        private TextView mHintView;

        public FootViewHolder(View view) {
            super(view);
            mContentView = (RelativeLayout) view.findViewById(R.id.xlistview_footer_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.xlistview_footer_progressbar);
            mHintView = (TextView) view.findViewById(R.id.xlistview_footer_hint_textview);
        }


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


    public static class MyHeaderViewHolder extends RecyclerView.ViewHolder {

            TextView group_item_name;

            public MyHeaderViewHolder(View itemView) {
                super(itemView);
                group_item_name = (TextView) itemView.findViewById(R.id.group_item_name);
            } //head
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
