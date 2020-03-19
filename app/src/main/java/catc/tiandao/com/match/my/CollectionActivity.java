package catc.tiandao.com.match.my;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CommItemDecoration;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.utils.GetTokenUtils;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.CommentDialog;
import catc.tiandao.com.match.ui.news.NewsDetailsActivity;
import catc.tiandao.com.match.utils.UmengUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.CollectionAdapter;
import catc.tiandao.com.matchlibrary.ben.NewsBen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionActivity extends BaseActivity implements View.OnClickListener ,CommentDialog.MyDialogInterface{


    private RelativeLayout rl_contianer;
    private PopupWindow popupWindow;


    private TextView activity_text;
    private RecyclerView myRecyclerView;
    private RelativeLayout botton_view;
    private TextView no_data;
    private TextView del_all;
    private LinearLayout del_view;
    private ImageView del_icon;
    private TextView del_text;

    private LinearLayoutManager mLinearLayoutManager;
    private CollectionAdapter mAdapter;
    private List<NewsBen> mList = new ArrayList(  );


    private int delType;

    private int page = 1;
    private int pageSize = 10;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;
    private GetFootballMath run;
    private DeleteNewCollectRun delRun;
    private NewOperationRun setRun;

    private int delNumber = 0;

    private CommentDialog mDialog;

    private static final int BOND = 0x004;


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    parseData(result1);
                    break;
                case 0x002:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    delParseData(result2,msg.arg1);
                    break;
                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    String type = bundle3.getString("type");
                    setParseData(result3,type,msg.arg1);
                    break;
                case BOND:
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;

                default:
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_collection );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "我的收藏" );
        viewInfo();

        getData();

        no_data.setVisibility( View.VISIBLE );
        setProgressVisibility( View.GONE );

    }



    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        activity_text = ViewUtls.find( this,R.id.activity_text );
        activity_text.setOnClickListener( this );
        activity_text.setText( "编辑" );

        image.setOnClickListener( this);
        activity_text.setOnClickListener( this);

        rl_contianer = ViewUtls.find( this,R.id.rl_contianer );
        no_data = ViewUtls.find( this,R.id.no_data );
        del_all = ViewUtls.find( this,R.id.del_all );
        del_view = ViewUtls.find( this,R.id.del_view );
        del_icon = ViewUtls.find( this,R.id.del_icon );
        del_text = ViewUtls.find( this,R.id.del_text );
        botton_view = ViewUtls.find( this,R.id.botton_view );

        del_all.setOnClickListener( this );
        del_view.setOnClickListener( this );

        myRecyclerView = ViewUtls.find(this, R.id.myRecyclerView );
        mLinearLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CollectionAdapter(this,mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

                NewsBen mNewsBen = mList.get( postion );

                switch (view.getId()){
                    case R.id.news_item:
                    case R.id.item_title:

                        if(delType == 0){
                            Intent intent02 = new Intent(CollectionActivity.this, NewsDetailsActivity.class);
                            intent02.putExtra( NewsDetailsActivity.NEW_ID, mNewsBen.getId());
                            startActivity(intent02);
                            overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                        }else {
                            int isSelet = mNewsBen.getIsSelet();
                            if(isSelet == 0){
                                delNumber++;
                            }else {
                                delNumber--;
                            }

                            setDelView();
                            mList.get( postion ).setIsSelet( isSelet == 1 ? 0: 1 );
                            mAdapter.notifyDataSetChanged();

                        }
                        break;

                    case R.id.zhuanfa:

                        String shreUrl = "http://www.leisuvip1.com/New/Index/token="+ UserUtils.getToken( CollectionActivity.this )+"&newId=" + mNewsBen.getId();
                        showShare(postion,mNewsBen.getId(),shreUrl,mNewsBen.getcTitle(),mNewsBen.getcTitle());

                        break;
                    case R.id.comment:

                        if(mDialog == null){
                            mDialog = new CommentDialog( CollectionActivity.this, postion, mNewsBen.getId(), CollectionActivity.this );
                        }
                        mDialog.show();

                        myHandler.sendEmptyMessageDelayed(BOND,100);


                        break;
                    case R.id.dianzan_view:

                        NewOperation("dianZan",postion,mNewsBen.getId(),"");


                        break;

                }



            }
        });

        //上拉加载
        //addOnScrollListener
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("lastVisibleItem: " + lastVisibleItem);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 >= mList.size()) {

                    if(isData){
                        mAdapter.changeMoreStatus(1);
                        mAdapter.notifyDataSetChanged();
                        if(!isRun){
                            getData();
                        }
                    }else {
                        mAdapter.changeMoreStatus(-1);
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });





        // 设置adapter
        myRecyclerView.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线

        int color = ContextCompat.getColor( CollectionActivity.this,R.color.line_01 );
        myRecyclerView.addItemDecoration( CommItemDecoration.createVertical(CollectionActivity.this, color,1));
       // myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void setDelView() {

        if(delNumber > 0){
            del_icon.setBackgroundResource( R.mipmap.collect_icon_delete);
            del_text.setTextColor( ContextCompat.getColor( CollectionActivity.this,R.color.text1 )  );
        }else {
            del_icon.setBackgroundResource( R.mipmap.collect_icon_delete_default);
            del_text.setTextColor( ContextCompat.getColor( CollectionActivity.this,R.color.text6 )  );
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                CollectionActivity.this.onBackPressed();
                break;
            case R.id.activity_text:

                if(delType == 0){
                    delType = 1;
                    if(mAdapter != null){
                        mAdapter.setShowType( 1 );
                        mAdapter.notifyDataSetChanged();
                    }


                    activity_text.setText( "取消" );
                    botton_view.setVisibility( View.VISIBLE );
                }else {
                    delType = 0;
                    if(mAdapter != null){
                        mAdapter.setShowType( 0 );
                        mAdapter.notifyDataSetChanged();
                    }


                    activity_text.setText( "编辑" );
                    botton_view.setVisibility( View.GONE );
                }

                break;
            case R.id.del_all:
                DeleteNewCollect(0);
                break;
            case R.id.del_view:

                if(delNumber > 0){
                    DeleteNewCollect(1);
                }

                break;

        }
    }

    private void DeleteNewCollect(int type) {



        try{

            if (CheckNet.isNetworkConnected( CollectionActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                String ids = getNewIds(type);

                //token=***& pageSize=每页多少条& page=第几页
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( CollectionActivity.this ) );
                param.put("ids", ids);

                delRun = new DeleteNewCollectRun(param,type);
                ThreadPoolManager.getsInstance().execute(delRun);


            } else {

                Toast.makeText(CollectionActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );

                isRun = false;
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String getNewIds(int type) {

        StringBuffer buffer = new StringBuffer(  );
        for(int i = 0; i< mList.size(); i++){
            NewsBen mNewsBen = mList.get( i );
            if(type == 0){
                buffer.append( mNewsBen.getId() + "," );
            }else {
                if(mNewsBen.getIsSelet() == 1){
                    buffer.append( mNewsBen.getId() + "," );
                }
            }
        }

        String ids = buffer.toString();
        if(ids != null && ids.length() > 0){
            ids = ids.substring( 0 ,ids.length() - 1 );
        }

        return ids;

    }



    /**
     *
     * */
    class DeleteNewCollectRun implements Runnable{
        private HashMap<String, String> param;
        private int type;

        DeleteNewCollectRun(HashMap<String, String> param,int type){
            this.param =param;
            this.type = type;
        }

        @Override
        public void run() {



            HttpUtil.post( CollectionActivity.this,HttpUtil.DELETE_NEW_COLLECT ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.arg1 = type;
                    message.what = 0x002;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void delParseData(String result,int type) {

        isRun = false;

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if(code == 0) {

                if(type == 0){
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                }else {
                    for(int i = 0; i< mList.size() ; i++){
                        if(mList.get( i ).getIsSelet() == 1){
                            mList.remove( i );
                            i--;
                        }
                    }
                    mAdapter.notifyDataSetChanged();

                }

                if(mList.size() > 0){
                    no_data.setVisibility( View.GONE );
                }else {
                    no_data.setVisibility( View.VISIBLE );
                }





            }

            Toast.makeText( CollectionActivity.this,message,Toast.LENGTH_SHORT ).show();

            if(mList.size() > 0){
                no_data.setVisibility( View.GONE );
            }else {
                no_data.setVisibility( View.VISIBLE );
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    private void getData() {



        isRun = true;

        try{

            if (CheckNet.isNetworkConnected( CollectionActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& pageSize=每页多少条& page=第几页
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( CollectionActivity.this ) );
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                run = new GetFootballMath(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(CollectionActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );

                isRun = false;
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * */
    class GetFootballMath implements Runnable{
        private HashMap<String, String> param;

        GetFootballMath(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {



            HttpUtil.post( CollectionActivity.this,HttpUtil.GET_MY_NEW ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void parseData(String result) {

        isRun = false;

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if(code == 0) {
                if(page == 1 && mList.size() > 0){
                    mList.clear();
                }

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<NewsBen>>(){}.getType();
                    List<NewsBen> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        mList.addAll( list );
                    }


                    if(list.size() < pageSize || list.size() == 0 ){
                        isData = false;
                        mAdapter.changeMoreStatus( -1 );
                    }else {
                        page ++ ;
                    }

                }

                mAdapter.notifyDataSetChanged();

            }else if(code == Constant.CODE){
                GetTokenUtils utils = new GetTokenUtils(CollectionActivity.this);
                utils.getToken( new GetTokenUtils.GetTokenUtilInterface() {
                    @Override
                    public void onResponse() {
                        getData();
                    }
                } );
            }

            if(mList.size() > 0){
                activity_text.setVisibility( View.VISIBLE );
                no_data.setVisibility( View.GONE );
            }else {
                activity_text.setVisibility( View.GONE );
                no_data.setVisibility( View.VISIBLE );
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    private void showShare(int postiont,int newId,String shareUrl,String shareTitle,String shareDescription) {
        //分享
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(CollectionActivity.this).inflate(R.layout.pop_share, null);
            popupWindow = new PopupWindow(CollectionActivity.this);
            popupWindow.setContentView(contentView);
            popupWindow.setAnimationStyle(R.style.bottomShowAnimStyle);
            popupWindow.setWidth( LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1f);
                }
            });

            contentView.findViewById(R.id.iv_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare( SHARE_MEDIA.QZONE,postiont,newId,shareUrl,shareTitle,shareDescription);
                    popupWindow.dismiss();
                }
            });
            contentView.findViewById(R.id.iv_moment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN_CIRCLE,postiont,newId,shareUrl,shareTitle,shareDescription);
                    popupWindow.dismiss();
                }
            });

            contentView.findViewById(R.id.iv_wiexin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN,postiont,newId,shareUrl,shareTitle,shareDescription);
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rl_contianer, Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.5f);
    }


    /***设置背景透明度*/
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    /***分享*/
    private void singleShare(SHARE_MEDIA shareMedia,int postiont,int newId ,String shareUrl,String shareTitle,String shareDescription) {

        int logoResId = R.mipmap.app_icon;

        UmengUtil.shareSinglePlatform(CollectionActivity.this, shareMedia, shareUrl,shareTitle, logoResId, shareDescription);

        NewOperation("zhuanFa",postiont,newId,"");
    }



    //dianZan，zhuanFa，pingLun，shouCang
    private void NewOperation(String type,int postiont,int newId,String pingLun) {

        try{
            if (CheckNet.isNetworkConnected( CollectionActivity.this)) {

                setProgressVisibility( View.VISIBLE );
                // type=类型& newId=新闻id& pingLun=评论内容
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( CollectionActivity.this ) );
                param.put("type",type);
                param.put("newId", newId + "");
                param.put("pingLun", "");

                setRun = new NewOperationRun(param,type,postiont);
                ThreadPoolManager.getsInstance().execute(setRun);
            } else {

                Toast.makeText(CollectionActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
               setProgressVisibility( View.GONE);
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /**
     *
     * */
    class NewOperationRun implements Runnable{
        private HashMap<String, String> param;
        private String type;
        private int postiont;

        NewOperationRun(HashMap<String, String> param,String type,int postiont){
            this.param =param;
            this.type = type;
            this.postiont = postiont;
        }

        @Override
        public void run() {

            HttpUtil.post( CollectionActivity.this,HttpUtil.NEW_OPERATION ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    data.putString( "type", type );
                    message.setData( data );
                    message.what = 0x003;
                    message.arg1 = postiont;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void setParseData(String result,String type,int postint) {

        if(result == null){
           setProgressVisibility( View.GONE );
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            if(code == 0) {

                if(type.equals( "dianZan" )){

                    int hasZan =  mList.get( postint ).getHasZan();
                    if(hasZan == 0){
                        mList.get( postint ).setiDianZanCount(  mList.get( postint ).getiDianZanCount() + 1 );
                        mList.get( postint ).setHasZan( 1 );
                    }else {
                        mList.get( postint ).setiDianZanCount(  mList.get( postint ).getiDianZanCount() - 1 );
                        mList.get( postint ).setHasZan( 0 );
                    }


                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(CollectionActivity.this,message,Toast.LENGTH_SHORT ).show();

                }else if(type.equals( "pingLun" )){

                    mList.get( postint ).setcCommentCount(  mList.get( postint ).getcCommentCount() + 1 );
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(CollectionActivity.this,message,Toast.LENGTH_SHORT ).show();

                }else if(type.equals( "zhuanFa" )){

                    mList.get( postint ).setiZhuanFaCount(  mList.get( postint ).getiZhuanFaCount() + 1 );
                    mAdapter.notifyDataSetChanged();

                }

            }else {
                Toast.makeText(CollectionActivity.this,message,Toast.LENGTH_SHORT ).show();
            }




        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    @Override
    public void commentButton(Dialog mDialog, int postiont, int newId, String comment) {


        if(UserUtils.isLanded( CollectionActivity.this )){
            NewOperation("pingLun",postiont,newId,comment);
        }else {
            UserUtils.startLongin( CollectionActivity.this );
        }


        mDialog.dismiss();
    }



    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        CollectionActivity.this.finish();
    }
}
