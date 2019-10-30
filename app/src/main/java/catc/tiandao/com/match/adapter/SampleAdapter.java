package catc.tiandao.com.match.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.MatchNew;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;
import catc.tiandao.com.match.score.ScoreDetailsActivity;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;

public class SampleAdapter  extends RecyclerView.Adapter<SampleAdapter.MyViewHolder>  {


    private Context mContext;
    private List<AreaMatch> list;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private DisplayImageOptions options;
    private View matchView1 = null;
    private View matchView2 = null;


    private LayoutInflater mInflater;

    public SampleAdapter(Context mContext, List<AreaMatch> list) {
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
    public SampleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        View itemView =mInflater.inflate( R.layout.listitem_group,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(itemView, mItemClickListener, mItemLongClickListener);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.item_content.removeAllViews();


        AreaMatch mAreaMatch = list.get( position);
        holder.group_item_name.setText( mAreaMatch.getAreaName() );
        List<MatchNew> matchL =  mAreaMatch.getMatchL();
        for(int i = 0; i< matchL.size(); i++){

         /*   "id": 2770362,
//			"title": null,
//			"dt": "21:00",
//			"name1": "吉大港坝州",
//			"image1": "http://www.leisuvip1.com/Image/GetImage/10400",
//			"name2": "喀拉拉邦FC",
//			"image2": "http://www.leisuvip1.com/Image/GetImage/680",
//			"iturn": 0,
//			"eventName": "球会友谊",
//			"iTop": 0,
//			"titleImageUrl": ""*/

            MatchNew mMatchNew = matchL.get( i );

            if(mMatchNew.getTitle() != null && mMatchNew.getTitle().length() > 0){
                View matchView1 = mInflater.inflate(R.layout.match_new_item, null,false);
                TextView item_title = ViewUtls.find( matchView1,R.id.item_title );
                TextView item_time = ViewUtls.find( matchView1,R.id.item_time );
                ImageView item_image = ViewUtls.find( matchView1,R.id.item_image );
                TextView Topping = ViewUtls.find( matchView1,R.id.Topping );

                if(position == 0){
                    Topping.setVisibility( View.VISIBLE );
                }else {
                    Topping.setVisibility( View.GONE);
                }

                item_title.setText( mMatchNew.getTitle() );
                item_time.setText( mMatchNew.getDt() + " " + mMatchNew.getName1() + " vs " + mMatchNew.getName2() );
                ImageLoader.getInstance().displayImage(mMatchNew.getTitleImageUrl(), item_image,options);


                matchView1.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(mAreaMatch.getAreaName().equals( "篮球赛事" )){
                            if(mMatchNew.getMatchStatusId() > 1){
                                Intent intent01 = new Intent(mContext, ScoreDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 1);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }else {
                                Intent intent01 = new Intent(mContext, MatchDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 1);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }

                        }else {

                            if(mMatchNew.getMatchStatusId() > 1){
                                Intent intent01 = new Intent(mContext, ScoreDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 0);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }else {
                                Intent intent01 = new Intent(mContext, MatchDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 0);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }

                        }
                    }
                } );

                holder.item_content.addView( matchView1 );
            }else {


                View  matchView2 = mInflater.inflate(R.layout.match_item, null,false);
                TextView home_name = ViewUtls.find( matchView2,R.id.home_name );
                ImageView home_icon = ViewUtls.find( matchView2,R.id.home_icon );
                TextView item_time = ViewUtls.find( matchView2,R.id.item_time );
                TextView item_name = ViewUtls.find( matchView2,R.id.item_name );
                ImageView away_icon = ViewUtls.find( matchView2,R.id.away_icon );
                TextView away_name = ViewUtls.find( matchView2,R.id.away_name );
                home_name.setText( mMatchNew.getName1() );
                away_name.setText( mMatchNew.getName2() );
                ImageLoader.getInstance().displayImage(mMatchNew.getImage1(), home_icon,options);
                ImageLoader.getInstance().displayImage(mMatchNew.getImage2(), away_icon,options);
                item_time.setText( mMatchNew.getDt());
                item_name.setText( mMatchNew.getEventName() + " 第" + mMatchNew.getIturn() + "轮");

                matchView2.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(mAreaMatch.getAreaName().equals( "篮球赛事" )){
                            if(mMatchNew.getMatchStatusId() > 1){
                                Intent intent01 = new Intent(mContext, ScoreDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 1);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }else {
                                Intent intent01 = new Intent(mContext, MatchDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 1);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }

                        }else {

                            if(mMatchNew.getMatchStatusId() > 1){
                                Intent intent01 = new Intent(mContext, ScoreDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 0);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }else {
                                Intent intent01 = new Intent(mContext, MatchDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID, mMatchNew.getId());
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE, 0);
                                mContext.startActivity(intent01);
                                ((Activity)mContext).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }

                        }



                    }
                } );



                holder.item_content.addView( matchView2 );

            }


        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private TextView group_item_name;
        private TextView group_item_more;
        private LinearLayout item_content;
        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public MyViewHolder(View view, MyItemClickListener listener, MyItemLongClickListener longClickListener) {
            super(view);

            group_item_name = ViewUtls.find( view,R.id.group_item_name );
            group_item_more = ViewUtls.find( view,R.id.group_item_more );
            item_content = ViewUtls.find( view,R.id.item_content );
            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            group_item_more.setOnClickListener( this );

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
