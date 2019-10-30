package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.BasketZhenRong;
import catc.tiandao.com.match.ben.ZhenRong;
import catc.tiandao.com.match.ben.ZhiShu;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class ZhenRongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context mContext;
    private List<Object> mList;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;

    private LayoutInflater mInflater;




    public ZhenRongAdapter(Context mContext, List<Object> mList,int showType) {
        this.mContext = mContext;
        this.mList = mList;
        this.showType = showType;
        this.mInflater=LayoutInflater.from(mContext);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView =mInflater.inflate( R.layout.zhenrong_item,parent,false);
            MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MyViewHolder) {
            MyViewHolder mMyViewHolder = (MyViewHolder)holder;


            Object obj = mList.get( position );
            if(obj instanceof ZhenRong ){

                ZhenRong mZhenRong = (ZhenRong)obj;
                if(showType == 0){

                    mMyViewHolder.home_layout.setVisibility( View.VISIBLE );
                    mMyViewHolder.away_layout.setVisibility( View.GONE );
                    //shirt_number
                    mMyViewHolder.home_icon.setText(mZhenRong.getShirt_number() + ""  );
                    mMyViewHolder.home_name.setText(  mZhenRong.getName() );
                }else {
                    mMyViewHolder.home_layout.setVisibility( View.GONE );
                    mMyViewHolder.away_layout.setVisibility( View.VISIBLE );
                    //shirt_number
                    mMyViewHolder.away_icon.setText(mZhenRong.getShirt_number() + ""  );
                    mMyViewHolder.away_name.setText(  mZhenRong.getName() );
                }


            }else if(obj instanceof BasketZhenRong){
                BasketZhenRong mBasketZhenRong = (BasketZhenRong)obj;



                if(showType == 0){

                    mMyViewHolder.home_layout.setVisibility( View.VISIBLE );
                    mMyViewHolder.away_layout.setVisibility( View.GONE );
                    //shirt_number
                    mMyViewHolder.home_icon.setText(mBasketZhenRong.getQiuYi() + ""  );
                    mMyViewHolder.home_name.setText(  mBasketZhenRong.getName_zh());
                }else {
                    mMyViewHolder.home_layout.setVisibility( View.GONE );
                    mMyViewHolder.away_layout.setVisibility( View.VISIBLE );
                    //shirt_number
                    mMyViewHolder.away_icon.setText(mBasketZhenRong.getQiuYi() + ""  );
                    mMyViewHolder.away_name.setText(  mBasketZhenRong.getName_zh() );
                }



            }




        }

    }

    @Override
    public int getItemCount() {
        return mList == null && mList.size() == 0 ? 0 : mList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {



        private LinearLayout home_layout;
        private RelativeLayout away_layout;
        private TextView home_icon,away_icon;
        private TextView home_name,away_name;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);

            this.home_layout = ViewUtls.find( view,R.id.home_layout );
            this.away_layout = ViewUtls.find( view,R.id.away_layout );
            this.home_icon = ViewUtls.find( view,R.id.home_icon );
            this.away_icon = ViewUtls.find( view,R.id.away_icon );
            this.home_name = ViewUtls.find( view,R.id.home_name );
            this.away_name = ViewUtls.find( view,R.id.away_name );
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



}
