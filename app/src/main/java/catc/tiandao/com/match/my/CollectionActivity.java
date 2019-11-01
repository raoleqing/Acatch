package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.CollectionAdapter;
import catc.tiandao.com.match.adapter.NewsAdapter;
import catc.tiandao.com.match.ben.Match;
import catc.tiandao.com.match.ben.NewsBen;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.event.EventFragment;
import catc.tiandao.com.match.ui.news.NewsDetailsActivity;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionActivity extends BaseActivity implements View.OnClickListener {


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
    private int pageSize = 20;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;
    private GetFootballMath run;


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


        no_data = ViewUtls.find( this,R.id.no_data );
        del_all = ViewUtls.find( this,R.id.del_all );
        del_view = ViewUtls.find( this,R.id.del_view );
        del_icon = ViewUtls.find( this,R.id.del_icon );
        del_text = ViewUtls.find( this,R.id.del_text );
        botton_view = ViewUtls.find( this,R.id.botton_view );

        myRecyclerView = ViewUtls.find(this, R.id.myRecyclerView );
        mLinearLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CollectionAdapter(this,mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

                NewsBen mNewsBen = mList.get( postion );

                Intent intent02 = new Intent(CollectionActivity.this, NewsDetailsActivity.class);
                intent02.putExtra( NewsDetailsActivity.NEW_ID, mNewsBen.getId());
                startActivity(intent02);
               overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

            }
        });
        // 设置adapter
        myRecyclerView.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
                    activity_text.setText( "取消" );
                    botton_view.setVisibility( View.VISIBLE );
                }else {
                    delType = 0;
                    activity_text.setText( "编辑" );
                    botton_view.setVisibility( View.GONE );
                }

                break;

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

            }

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





    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        CollectionActivity.this.finish();
    }
}
