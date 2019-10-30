package catc.tiandao.com.match.adapter;

import android.content.Context;
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

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.Expert;
import catc.tiandao.com.match.ben.ZhiShu;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class ZhiShuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<ZhiShu> mList;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;

    private LayoutInflater mInflater;




    public ZhiShuAdapter(Context mContext, List<ZhiShu> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater=LayoutInflater.from(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView =mInflater.inflate( R.layout.zhi_shu_item,parent,false);
            MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MyViewHolder) {

            ZhiShu mZhiShu = mList.get( position );
            //正常数据
            MyViewHolder mMyViewHolder = (MyViewHolder)holder;

            if(position % 2 == 1){
                mMyViewHolder.item_layout.setBackgroundColor( ContextCompat.getColor( mContext,R.color.bg5 ) );
            }else {
                mMyViewHolder.item_layout.setBackgroundColor( ContextCompat.getColor( mContext,R.color.white ) );
            }

            mMyViewHolder.company.setText( mZhiShu.getCompany() );

            if(mZhiShu.getChuPan() != null && !mZhiShu.getChuPan().equals( "null" ) && !mZhiShu.getChuPan().equals( "" )){

                String array[] = mZhiShu.getChuPan().split( "," );
                if(array.length > 0){
                    mMyViewHolder.chuPan_value1.setText( array[0] );
                }

                if(array.length > 1){
                    mMyViewHolder.chuPan_value2.setText( array[1] );
                }


                if(array.length > 2){
                    mMyViewHolder.chuPan_value3.setText( array[2] );
                }
            }

            if(mZhiShu.getJiShiPan() != null && !mZhiShu.getJiShiPan().equals( "null" ) && !mZhiShu.getJiShiPan().equals( "" )){
                String array[] = mZhiShu.getJiShiPan().split( "," );
                if(array.length > 0){
                    mMyViewHolder.jishipan_value1.setText( array[0] );
                }

                if(array.length > 1){
                    mMyViewHolder.jishipan_value2.setText( array[1] );
                }


                if(array.length > 2){
                    mMyViewHolder.jishipan_value3.setText( array[2] );
                }

            }




        }

    }

    @Override
    public int getItemCount() {
        return mList == null && mList.size() == 0 ? 0 : mList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private LinearLayout item_layout;
        private TextView company;
        private TextView chuPan_value1;
        private TextView chuPan_value2;
        private TextView chuPan_value3;
        private TextView jishipan_value1;
        private TextView jishipan_value2;
        private TextView jishipan_value3;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);

            this.item_layout = ViewUtls.find( view,R.id.item_layout );
            this.company = ViewUtls.find( view,R.id.company );
            this.chuPan_value1 = ViewUtls.find( view,R.id.chuPan_value1 );
            this.chuPan_value2 = ViewUtls.find( view,R.id.chuPan_value2 );
            this.chuPan_value3 = ViewUtls.find( view,R.id.chuPan_value3 );
            this.jishipan_value1 = ViewUtls.find( view,R.id.jishipan_value1 );
            this.jishipan_value2 = ViewUtls.find( view,R.id.jishipan_value2 );
            this.jishipan_value3 = ViewUtls.find( view,R.id.jishipan_value3 );
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
    public void addItem(List<ZhiShu> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mList);
        mList.removeAll(mList);
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<ZhiShu> newDatas) {
        mList.addAll(newDatas);
        notifyDataSetChanged();
    }


}
