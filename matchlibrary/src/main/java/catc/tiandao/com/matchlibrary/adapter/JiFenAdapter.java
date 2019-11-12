package catc.tiandao.com.matchlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.R;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.matchlibrary.ben.JiFen;

public class JiFenAdapter extends RecyclerView.Adapter<JiFenAdapter.MyViewHolder>  {


    private Context mContext;
    private List<JiFen> list;
    private MyItemClickListener mItemClickListener;

    private LayoutInflater mInflater;

    public JiFenAdapter(Context mContext, List<JiFen> list) {
        this.mContext = mContext;
        this.list = list;
        this.mInflater=LayoutInflater.from(mContext);



    }

    @Override
    public JiFenAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.jifen_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        JiFen mJiFen = list.get( position );
        holder.JiFen_text1.setText( mJiFen.getName() );
        holder.JiFen_text2.setText( mJiFen.getPlayed() + "" );
        holder.JiFen_text3.setText( mJiFen.getWon() + "" );
        holder.JiFen_text4.setText( mJiFen.getDrawn() +"" );
        holder.JiFen_text5.setText( mJiFen.getLost() + "" );
        holder.JiFen_text6.setText( mJiFen.getJin_shi() );
        holder.JiFen_text7.setText( mJiFen.getDiff() + "" );
        holder.JiFen_text8.setText( mJiFen.getPosition() + "" );
        holder.JiFen_text9.setText( mJiFen.getIsHome() + "" );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView JiFen_text1,JiFen_text2,JiFen_text3,JiFen_text4,JiFen_text5,JiFen_text6,JiFen_text7,JiFen_text8,JiFen_text9;

        private MyItemClickListener mListener;

        public MyViewHolder(View view, MyItemClickListener listener) {
            super(view);
            JiFen_text1 = ViewUtls.find( view,R.id.JiFen_text1 );
            JiFen_text2 = ViewUtls.find( view,R.id.JiFen_text2);
            JiFen_text3 = ViewUtls.find( view,R.id.JiFen_text3 );
            JiFen_text4 = ViewUtls.find( view,R.id.JiFen_text4 );
            JiFen_text5 = ViewUtls.find( view,R.id.JiFen_text5 );
            JiFen_text6 = ViewUtls.find( view,R.id.JiFen_text6 );
            JiFen_text7 = ViewUtls.find( view,R.id.JiFen_text7 );
            JiFen_text8 = ViewUtls.find( view,R.id.JiFen_text8 );
            JiFen_text9 = ViewUtls.find( view,R.id.JiFen_text9 );

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
