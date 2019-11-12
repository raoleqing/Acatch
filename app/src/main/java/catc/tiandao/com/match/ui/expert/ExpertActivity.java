package catc.tiandao.com.match.ui.expert;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.ExpertAdapter;
import catc.tiandao.com.matchlibrary.ben.Expert;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
专家
**/
public class ExpertActivity extends BaseActivity implements  View.OnClickListener{

    private RecyclerView ball_recycler;

    private LinearLayoutManager mLinearLayoutManager;
    private ExpertAdapter mAdapter;
    private List<Expert> mList = new ArrayList<>(  );

    private GetExpertRun run;
    private int pageSize = 10;
    private int page = 1;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;
    private DisplayImageOptions options;

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
                default:
                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_expert );
        setTitleVisibility( View.GONE );
        setTranslucentStatus();
        viewInfo();
        GetExpert();
       //setProgressVisibility( View.GONE );
    }



    private void viewInfo() {

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading( R.mipmap.mall_cbg )          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg )  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg )       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();


        ImageView returnImage = ViewUtls.find( this,R.id.tv_return );
        returnImage.setOnClickListener( this );

        ball_recycler = ViewUtls.find( this,R.id.ball_recycler );

        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        };

        ball_recycler.setLayoutManager(mLinearLayoutManager);
        ball_recycler.setHasFixedSize(true);
        ball_recycler.setNestedScrollingEnabled(false);

        mAdapter = new ExpertAdapter(this,mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {
                Expert mExpert = mList.get( postion );

                showExpertCodeDialog(mExpert);

            }
        });
        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(ExpertActivity.this, DividerItemDecoration.VERTICAL));



        //上拉加载
        //addOnScrollListener
        ball_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() && isData) {
                    mAdapter.changeMoreStatus(1);
                    mAdapter.notifyDataSetChanged();
                    if(!isRun){
                        GetExpert();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                ExpertActivity.this.onBackPressed();
                break;
        }

    }


    private void GetExpert() {

        isRun = true;

        try{

            if (CheckNet.isNetworkConnected( ExpertActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& pageSize=每页多少条& page=第几页
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( ExpertActivity.this ) );
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                run = new GetExpertRun(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(ExpertActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
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
    class GetExpertRun implements Runnable{
        private HashMap<String, String> param;

        GetExpertRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( ExpertActivity.this,HttpUtil.GET_EXPERT ,param,new HttpUtil.HttpUtilInterface(){
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

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Expert>>(){}.getType();
                    List<Expert> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        mList.addAll( list );
                    }

                    if(list.size() < 10 || list.size() == 0 ){
                        isData = false;
                        mAdapter.changeMoreStatus( -1 );
                    }else {
                        page ++ ;
                    }

                    mAdapter.notifyDataSetChanged();

                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }


    private void showExpertCodeDialog(Expert mExpert) {

        View view = getLayoutInflater().inflate(R.layout.expert_code, null);
        ImageView code_image = ViewUtls.find( view,R.id.code_image );
        TextView expert_name = ViewUtls.find( view,R.id.expert_name );
        TextView expert_qq = ViewUtls.find( view,R.id.expert_qq );
        TextView expert_wx = ViewUtls.find( view,R.id.expert_wx );

        expert_name.setText( "扫一扫，添加" + mExpert.getSpecialName() );
        expert_qq.setText( "QQ号: " + mExpert.getQq() );
        expert_wx.setText( "QQ号: " + mExpert.getWechat() );
        ImageLoader.getInstance().displayImage(mExpert.getIcon(), code_image,options);



        final Dialog dialog = new Dialog(ExpertActivity.this, R.style.car_order_dialog);
        dialog.setContentView(view);
        dialog.show();




    }


    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        ExpertActivity.this.finish();
    }



}
