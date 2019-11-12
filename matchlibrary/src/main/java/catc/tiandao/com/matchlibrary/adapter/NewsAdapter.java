package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.MyItemLongClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.NewsBen;
import cn.jzvd.JzvdStd;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<NewsBen> mList;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;

    private static final int TYPE_ITEM1 =0;  //普通Item View
    private static final int TYPE_ITEM2 =1;  //普通Item View
    private static final int TYPE_FOOTER = 2;  //顶部FootView
    private int load_more_status=0;  //上拉加载更多状态-默认为0
    private LayoutInflater mInflater;

    private DisplayImageOptions options;


    //没有数据了
    public  static final int NO_DATA = -1;
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;


    public NewsAdapter(Context mContext, List<NewsBen> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater=LayoutInflater.from(mContext);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading( R.mipmap.mall_cbg )          // 设置图片下载期间显示的图片
                .showImageForEmptyUri( R.mipmap.mall_cbg )  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg )       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer( UnitConverterUtils.dip2px( mContext,6 )))  // 设置成圆角图片
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM1){
            View itemView =mInflater.inflate( R.layout.news_item1,parent,false);
            MyViewHolderByVideo viewHolder = new MyViewHolderByVideo(itemView, mItemClickListener, mItemLongClickListener);
            return viewHolder;
        }else if(viewType==TYPE_ITEM2){
            View itemView =mInflater.inflate( R.layout.news_item,parent,false);
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
            NewsBen mNewsBen = mList.get( position );

            mMyViewHolder.item_title.setText( mNewsBen.getcTitle() );

            if(mNewsBen.getHasZan() == 0){
                mMyViewHolder.dianzan_icon.setBackgroundResource( R.mipmap.video_icon_like_default );
            }else {
                mMyViewHolder.dianzan_icon.setBackgroundResource( R.mipmap.video_icon_like);
            }

            mMyViewHolder.zhuanfa.setText( " " + mNewsBen.getiZhuanFaCount());
            mMyViewHolder.comment.setText( " " + mNewsBen.getcCommentCount());
            mMyViewHolder.dianzan.setText( " " + mNewsBen.getiDianZanCount());

            ImageLoader.getInstance().displayImage(mNewsBen.getTitleImageUrl(), mMyViewHolder.item_image,options);

        }else if(holder instanceof MyViewHolderByVideo){

            NewsBen mNewsBen = mList.get( position );

            MyViewHolderByVideo mMyViewHolder = (MyViewHolderByVideo)holder;
            mMyViewHolder.jz_video.setVisibility( View.VISIBLE );
            mMyViewHolder.jz_video.setUp(mNewsBen.getTitleVideoUrl(),mNewsBen.getcTitle());
            mMyViewHolder.jz_video.thumbImageView.setImageURI( Uri.parse( mNewsBen.getTitleImageUrl() ) );

            mMyViewHolder.item_title.setText( mNewsBen.getcTitle() );

            if(mNewsBen.getHasZan() == 0){
                mMyViewHolder.dianzan_icon.setBackgroundResource( R.mipmap.video_icon_like_default );
            }else {
                mMyViewHolder.dianzan_icon.setBackgroundResource( R.mipmap.video_icon_like);
            }

            mMyViewHolder.zhuanfa.setText( " " + mNewsBen.getiZhuanFaCount());
            mMyViewHolder.comment.setText( " " + mNewsBen.getcCommentCount() );
            mMyViewHolder.dianzan.setText( " " + mNewsBen.getiDianZanCount() );


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
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
       if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
           if(mList.get( position ).getTitleVideoUrl() != null && mList.get( position ).getTitleVideoUrl().length() > 0){
               return TYPE_ITEM1;
           }else {
               return TYPE_ITEM2;
           }

        }


    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private LinearLayout news_item;
        private TextView item_title;
        private LinearLayout dianzan_view;
        private TextView zhuanfa,comment,dianzan;
        private ImageView dianzan_icon;
        private ImageView item_image;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);
            this.news_item = ViewUtls.find( view,R.id.news_item);
            this.item_title = ViewUtls.find( view,R.id.item_title);
            this.zhuanfa = ViewUtls.find( view,R.id.zhuanfa);
            this.comment = ViewUtls.find( view,R.id.comment);
            this.dianzan_view = ViewUtls.find( view,R.id.dianzan_view);
            this.dianzan = ViewUtls.find( view,R.id.dianzan);
            this.dianzan_icon = ViewUtls.find( view,R.id.dianzan_icon);
            this.item_image = ViewUtls.find( view,R.id.item_image);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            news_item.setOnClickListener( this );
            zhuanfa.setOnClickListener( this );
            comment.setOnClickListener( this );
            dianzan_view.setOnClickListener( this );

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


    class MyViewHolderByVideo extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private TextView item_title;
        private LinearLayout dianzan_view;
        private TextView zhuanfa,comment,dianzan;
        private ImageView dianzan_icon;
        private JzvdStd jz_video;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolderByVideo(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);
            this.item_title = ViewUtls.find( view,R.id.item_title);
            this.dianzan_view = ViewUtls.find( view,R.id.dianzan_view);
            this.zhuanfa = ViewUtls.find( view,R.id.zhuanfa);
            this.dianzan_icon = ViewUtls.find( view,R.id.dianzan_icon);
            this.comment = ViewUtls.find( view,R.id.comment);
            this.dianzan = ViewUtls.find( view,R.id.dianzan);
            this.jz_video = ViewUtls.find( view,R.id.jz_video);
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            item_title.setOnClickListener( this );
            zhuanfa.setOnClickListener( this );
            comment.setOnClickListener( this );
            dianzan_view.setOnClickListener( this );

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
    public void addItem(List<NewsBen> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mList);
        mList.removeAll(mList);
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<NewsBen> newDatas) {
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
