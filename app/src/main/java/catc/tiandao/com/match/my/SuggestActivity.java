package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.GridSpacingItemDecoration;
import catc.tiandao.com.match.common.MyGridLayoutManager;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.UnitConverterUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.SuggestTypeAdapter;
import catc.tiandao.com.matchlibrary.ben.SuggestBen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SuggestActivity extends BaseActivity implements View.OnClickListener {


    private RecyclerView notice_recycler;
    private EditText input_text;
    private TextView text_leng;
    private Button submit;

    private LinearLayoutManager mLinearLayoutManager;
    private SuggestTypeAdapter mAdapter;

    private GetSuggestTypeRun run;
    private SaveSuggestRun saveRun;

    private List<SuggestBen> mList = new ArrayList(  );

    private int typeId;
    private String content;

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
        setContentView( R.layout.activity_suggest );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTitleText( "投诉与建议" );
        viewInfo();
        GetSuggestType();
    }


    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        image.setOnClickListener( this);

        notice_recycler = ViewUtls.find( this,R.id.type_recycler );
        submit = ViewUtls.find( this,R.id.submit );
        input_text = ViewUtls.find( this,R.id.input_text );
        text_leng = ViewUtls.find( this,R.id.text_leng );

        RecyclerView.LayoutManager mLayoutManager = new MyGridLayoutManager(this, 3);
        notice_recycler.setLayoutManager(mLayoutManager);
        mAdapter = new SuggestTypeAdapter(this, mList);
        // 设置adapter
        notice_recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

                typeId = mList.get( postion ).getId();

                mAdapter.setShowType( postion );
                mAdapter.notifyDataSetChanged();

            }
        } );
        int space = UnitConverterUtils.dip2px(this, 15);
        notice_recycler.addItemDecoration(new GridSpacingItemDecoration(3,space,false));


        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_leng.setText( s.length() + "/240" );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        submit.setOnClickListener( this );

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                SuggestActivity.this.onBackPressed();
                break;
            case R.id.submit:
                getContent();
                break;


        }



    }




    private void GetSuggestType() {

        try{

            if (CheckNet.isNetworkConnected(SuggestActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //token=***& name=新用户名
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SuggestActivity.this ) );

                run = new GetSuggestTypeRun(param);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(SuggestActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     *
     * */
    class GetSuggestTypeRun implements Runnable{
        private HashMap<String, String> param;

        GetSuggestTypeRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( SuggestActivity.this,HttpUtil.GET_SUGGEST_TYPE ,param,new HttpUtil.HttpUtilInterface(){
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


            // {"code":0,"message":"成功","data":[{"Id":1,"Name":"优化建议"},{"Id":2,"Name":"系统bug"},{"Id":3,"Name":"赛事内容"},{"Id":4,"Name":"设计不好看"},{"Id":5,"Name":"其他"}]}
            if(code == 0) {

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<SuggestBen>>(){}.getType();
                    List<SuggestBen> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        mList.addAll( list );
                    }


                    typeId = mList.get( 0 ).getId();

                    mAdapter.notifyDataSetChanged();

                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    private void getContent() {


        content = input_text.getText().toString();

        if(content == null || content.isEmpty()){
            Toast.makeText( SuggestActivity.this,"请输入内容",Toast.LENGTH_SHORT ).show();
            return ;
        }

        SaveSuggest(content);

    }


    private void SaveSuggest(String content) {

        try{

            if (CheckNet.isNetworkConnected(SuggestActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //http:// 域名/LSQB/ SaveSuggest? token=***&typeId=投诉类型id&content=内容
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( SuggestActivity.this ) );
                param.put("typeId", typeId + "" );
                param.put("content", content );

                saveRun = new SaveSuggestRun(param);
                ThreadPoolManager.getsInstance().execute(saveRun);

            } else {

                Toast.makeText(SuggestActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     *
     * */
    class SaveSuggestRun implements Runnable{
        private HashMap<String, String> param;

        SaveSuggestRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {
            HttpUtil.post( SuggestActivity.this,HttpUtil.SAVE_SUGGEST ,param,new HttpUtil.HttpUtilInterface(){
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
                SuggestActivity.this.onBackPressed();
            }

            Toast.makeText( SuggestActivity.this,message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(run != null)
            ThreadPoolManager.getsInstance().cancel(run);

        if(saveRun != null)
            ThreadPoolManager.getsInstance().cancel(saveRun);


    }






    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        SuggestActivity.this.finish();
    }

}
