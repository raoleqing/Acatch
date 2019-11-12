package catc.tiandao.com.matchlibrary.adapter;

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

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.MyItemLongClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.Expert;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class ExpertAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<Expert> mList;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;

    private DisplayImageOptions options;

    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private int load_more_status=0;  //上拉加载更多状态-默认为0
    private LayoutInflater mInflater;


    //没有数据了
    public  static final int NO_DATA = -1;
    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;




    public ExpertAdapter(Context mContext, List<Expert> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater=LayoutInflater.from(mContext);


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_def_avatar)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri( R.mipmap.icon_def_avatar)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_def_avatar)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            View itemView =mInflater.inflate( R.layout.expert_item,parent,false);
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

            Expert mExpert = mList.get( position );
            //正常数据
            MyViewHolder mMyViewHolder = (MyViewHolder)holder;

            ImageLoader.getInstance().displayImage(mExpert.getIcon(), mMyViewHolder.user_icon,options);
            mMyViewHolder.user_name.setText( mExpert.getSpecialName() );
            mMyViewHolder.user_desc.setText( mExpert.getDesc() );
            mMyViewHolder.user_wechat.setText( "微信："+ mExpert.getWechat() +" · QQ：" + mExpert.getQq() );



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


        private ImageView user_icon;
        private TextView user_name;
        private TextView user_desc;
        private TextView user_wechat;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);

            this.user_icon = ViewUtls.find( view,R.id.user_icon );
            this.user_name = ViewUtls.find( view,R.id.user_name );
            this.user_desc = ViewUtls.find( view,R.id.user_desc );
            this.user_wechat = ViewUtls.find( view,R.id.user_wechat );
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
    public void addItem(List<Expert> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mList);
        mList.removeAll(mList);
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<Expert> newDatas) {
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
