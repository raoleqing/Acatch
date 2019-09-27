package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.common.GridSpacingItemDecoration;
import catc.tiandao.com.match.common.MyGridLayoutManager;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;

public class MatchAllAdapter extends RecyclerView.Adapter<MatchAllAdapter.MyViewHolder>  {


    private Context mContext;
    private List<AreaMatch> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;

    private String[] array = {"WNBA","篮冠联","篮冠联","篮冠联","篮冠联","WNBA","篮冠联","篮冠联","篮冠联","篮冠联"};
    private LayoutInflater mInflater;

    public MatchAllAdapter(Context mContext, List<AreaMatch> list) {
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
    public MatchAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.match_all_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(holder.mrecyclerview.getAdapter() == null){
            holder.mrecyclerview.setAdapter( new SelectAdapter(mContext,array) );
        }


    }

    @Override
    public int getItemCount() {
        return 3;
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private RecyclerView mrecyclerview;
        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);

            mrecyclerview = view.findViewById( R.id.recyclerview );
            RecyclerView.LayoutManager mLayoutManager = new MyGridLayoutManager(mContext, 3);
            mLayoutManager.isAutoMeasureEnabled();
            mrecyclerview.setLayoutManager(mLayoutManager);

            int space = UnitConverterUtils.dip2px(mContext, 15);
            int space1 = UnitConverterUtils.dip2px(mContext, 20);
            mrecyclerview.addItemDecoration(new GridSpacingItemDecoration(3,space,space1,false));
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
