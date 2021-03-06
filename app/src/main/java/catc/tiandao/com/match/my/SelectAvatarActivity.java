package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.GridSpacingItemDecoration;
import catc.tiandao.com.match.common.MyGridLayoutManager;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.SelectAvatarAdapter;
import catc.tiandao.com.matchlibrary.ben.Avatart;

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


public class SelectAvatarActivity extends BaseActivity implements View.OnClickListener {

    private List<Avatart> mList = new ArrayList<>(  );
    private SelectAvatarAdapter mAdapter;
    private int onPostion = 0;

    private GetHeads run;
    private UpdateHeadIconRun upRun;

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
                    saveParseData(result2);
                    break;
                default:
                    break;
            }
        }


    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select_avatar );

        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        setTitleText( "选择头像" );

        viewInfo();
        getData();

        setProgressVisibility( View.GONE );
    }


    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        TextView activity_text = ViewUtls.find( this,R.id.activity_text );

        activity_text.setText( "保存" );
        image.setOnClickListener( this);
        activity_text.setOnClickListener( this);

        RecyclerView myRecyclerView = ViewUtls.find( this,R.id.myRecyclerView );
        RecyclerView.LayoutManager mLayoutManager = new MyGridLayoutManager(this, 3);
        myRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SelectAvatarAdapter( this,mList );

        // 设置adapter
        myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

                if(postion != onPostion){
                    onPostion = postion;
                    mAdapter.setShowType(  postion );
                    mAdapter.notifyDataSetChanged();
                }


            }
        } );

        int space = UnitConverterUtils.dip2px(this, 15);
        myRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3,space,space,false));



    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:
                SelectAvatarActivity.this.onBackPressed();
                break;
            case R.id.activity_text:

                int headId = mList.get( onPostion ).getId();
                UpdateHeadIcon(headId);
                break;
        }
    }

    private void UpdateHeadIcon(int headId) {

        try{

            if (CheckNet.isNetworkConnected( SelectAvatarActivity.this)) {

                setProgressVisibility( View.VISIBLE );


                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SelectAvatarActivity.this ) );
                param.put("headId", headId + "");

                upRun = new UpdateHeadIconRun(param);
                ThreadPoolManager.getsInstance().execute(upRun);


            } else {

                Toast.makeText(SelectAvatarActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );

            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * */
    class UpdateHeadIconRun implements Runnable{
        private HashMap<String, String> param;

        UpdateHeadIconRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {



            HttpUtil.post( SelectAvatarActivity.this,HttpUtil.UPDATE_HEADICON,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x002;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void saveParseData(String result) {

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

                String iconUrl = mList.get( onPostion ).getHeadFileUrl();
                SharedPreferencesUtil.putString( SelectAvatarActivity.this,UserUtils.ICONURL, iconUrl);
                SelectAvatarActivity.this.onBackPressed();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }





    private void getData() {

        try{

            if (CheckNet.isNetworkConnected( SelectAvatarActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& pageSize=每页多少条& page=第几页
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SelectAvatarActivity.this ) );

                run = new GetHeads(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(SelectAvatarActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );

            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * */
    class GetHeads implements Runnable{
        private HashMap<String, String> param;

        GetHeads(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {



            HttpUtil.post( SelectAvatarActivity.this,HttpUtil.GET_HEADS,param,new HttpUtil.HttpUtilInterface(){
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

                Gson gson = new Gson();
                Type type = new TypeToken<List<Avatart>>(){}.getType();
                List<Avatart> list = gson.fromJson(data.toString(),type);
                if(list.size() > 0){
                    mList.addAll( list );
                }


                mAdapter.notifyDataSetChanged();


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
        SelectAvatarActivity.this.finish();
    }


}
