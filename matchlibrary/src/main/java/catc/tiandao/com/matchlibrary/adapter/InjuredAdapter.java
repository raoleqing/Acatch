package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.Injured;

public class InjuredAdapter extends RecyclerView.Adapter<InjuredAdapter.MyViewHolder>  {


    private Context mContext;
    private List<Injured> list;
    private MyItemClickListener mItemClickListener;

    private LayoutInflater mInflater;
    private DisplayImageOptions options;

    public InjuredAdapter(Context mContext, List<Injured> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater=LayoutInflater.from(mContext);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri( R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();



    }

    @Override
    public InjuredAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.injured_list_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Injured mInjured = list.get( position );
        String url = "http://cdn.sportnanoapi.com/football/player/ " + mInjured.getLogo();
        ImageLoader.getInstance().displayImage(url, holder.icon,options);

        holder.name.setText( mInjured.getName() );
        holder.item_text1.setText( getPosition(mInjured.getPosition()));
        holder.item_text2.setText( mInjured.getReason() );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView name,item_text1,item_text2;

        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            icon = ViewUtls.find( view,R.id.icon );
            name = ViewUtls.find( view,R.id.name);
            item_text1 = ViewUtls.find( view,R.id.item_text1 );
            item_text2 = ViewUtls.find( view,R.id.item_text2 );
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



    //6.F前锋 M中锋 D后卫 G守门员,其他为未知
    public static String getPosition(String position) {

        if(position.equals( "F" )){
            return "前锋";
        }else if(position.equals( "M" )){
            return "中锋";
        }else if(position.equals( "D" )){
            return "后卫";
        }else if(position.equals( " G" )){
            return "守门员";
        }

        return "";


    }



}
