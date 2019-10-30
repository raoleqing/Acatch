package catc.tiandao.com.match.adapter;

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
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.JiShiBasket;
import catc.tiandao.com.match.ben.JiShiShiJian;
import catc.tiandao.com.match.ben.JiShiShiJianByBasket;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;

public class ImmediateAdapter extends RecyclerView.Adapter<ImmediateAdapter.MyViewHolder>  {


    private Context mContext;
    private List<JiShiShiJianByBasket> list;
    private MyItemClickListener mItemClickListener;
    private DisplayImageOptions options;


    private LayoutInflater mInflater;

    public ImmediateAdapter(Context mContext, List<JiShiShiJianByBasket> list) {
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
    public ImmediateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.immediate_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        JiShiShiJianByBasket mJiShiShiJian = list.get( position );
        List<JiShiBasket> mList = mJiShiShiJian.getmList();
        if(mList.size() > 0){
            JiShiBasket mJiShiBasket = mList.get( 0 );
            holder.title_text1.setText( "第"+(position + 1) +"节 " +  mJiShiBasket.getTime() );
            holder.title_text2.setText( mJiShiBasket.getBiFen());
        }

        holder.item_content.removeAllViews();

        for(int i = 0; i< mList.size(); i++){
            JiShiBasket mJiShiBasket =  mList.get( i );
            View view = LayoutInflater.from(mContext).inflate(R.layout.jishi_item, null);
            TextView item_text = ViewUtls.find( view,R.id.item_text );
            item_text.setText( mJiShiBasket.getMiaoShu() );

            holder.item_content.addView( view );
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
        private ImageView line1;
        private TextView title_text1;
        private TextView title_text2;
        private LinearLayout item_content;
        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            item_view = ViewUtls.find( view,R.id.item_view );
            line1 = ViewUtls.find( view,R.id.line1 );
            title_text1 = ViewUtls.find( view,R.id.title_text1 );
            title_text2 = ViewUtls.find( view,R.id.title_text2 );
            item_content = ViewUtls.find( view,R.id.item_content );
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
