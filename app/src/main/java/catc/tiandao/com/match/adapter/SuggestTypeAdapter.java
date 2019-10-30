package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.ben.SuggestBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class SuggestTypeAdapter extends RecyclerView.Adapter<SuggestTypeAdapter.MyViewHolder>  {


    private Context mContext;
    private List<SuggestBen> mList ;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private int showType = 0;


    private LayoutInflater mInflater;




    public SuggestTypeAdapter(Context mContext, List<SuggestBen> mList ) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater=LayoutInflater.from(mContext);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.suggest_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.item_text.setText( mList.get( position ).getName());
        if(showType == position){
            holder.item_text.setBackgroundResource( R.drawable.bg_search_normal12 );
            holder.item_text.setTextColor( ContextCompat.getColor( mContext,R.color.white ) );
        }else {
            holder.item_text.setBackgroundResource( R.drawable.bg_search_normal13 );
            holder.item_text.setTextColor( ContextCompat.getColor( mContext,R.color.text4 ) );
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private TextView item_text;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
                super(view);

            item_text = ViewUtls.find( view,R.id.item_text );
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            item_text.setOnClickListener( this );

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





    public void setShowType(int type){
        this.showType = type;
    }
}
