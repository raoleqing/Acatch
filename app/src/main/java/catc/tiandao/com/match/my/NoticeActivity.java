package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;

import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.NoticeAdapter;
import catc.tiandao.com.matchlibrary.ben.NewsBen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends BaseActivity implements View.OnClickListener{


    private RecyclerView notice_recycler;
    private TextView no_data;

    private LinearLayoutManager mLinearLayoutManager;
    private NoticeAdapter mAdapter;
    private List<NewsBen> mList = new ArrayList(  );

    private int pageSize = 10;
    private int page = 1;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;

    private GetNewByType run;


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage( msg );
            switch (msg.what) {
                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString( "result" );
                    parseData( result1 );
                    break;

                default:
                    break;
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notice );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "通知" );
         viewInfo();
        getData();

    }


    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);


        notice_recycler = ViewUtls.find( this,R.id.notice_recycler );
        no_data = ViewUtls.find( this,R.id.no_data );

        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(this);
        notice_recycler.setLayoutManager(mLinearLayoutManager);

        // 设置布局管理器
        mAdapter = new NoticeAdapter(NoticeActivity.this,mList);

        // 设置adapter
        notice_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        notice_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        notice_recycler.addItemDecoration(new DividerItemDecoration(NoticeActivity.this, DividerItemDecoration.VERTICAL));



        //上拉加载
        //addOnScrollListener
        notice_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                NoticeActivity.this.onBackPressed();
                break;


        }
    }


    private void getData() {

        isRun = true;

        try{

            if (CheckNet.isNetworkConnected(NoticeActivity.this)) {

                setProgressVisibility( View.VISIBLE );


                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( NoticeActivity.this) );
                param.put("type","通知");
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                run = new GetNewByType(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(NoticeActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
              setProgressVisibility( View.GONE );

                isRun = false;
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /**
     *获取足球比分列表
     * */

    /**
     *
     * */
    class GetNewByType implements Runnable {
        private HashMap<String, String> param;

        GetNewByType(HashMap<String, String> param) {
            this.param = param;
        }

        @Override
        public void run() {


            HttpUtil.post( NoticeActivity.this, HttpUtil.GET_NEWBY_TYPE, param, new HttpUtil.HttpUtilInterface() {
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
                    myHandler.sendMessage( message );
                }
            } );

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
                if(Constant.isData( data.toString() )){
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


                        mAdapter.notifyDataSetChanged();
                    }

                    if(mList.size() == 0){
                        no_data.setVisibility( View.VISIBLE );
                    }else {
                        no_data.setVisibility( View.GONE );
                    }


                }else {
                    no_data.setVisibility( View.VISIBLE );
                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
           setProgressVisibility( View.GONE );
        }

    }





    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        NoticeActivity.this.finish();
    }
}
