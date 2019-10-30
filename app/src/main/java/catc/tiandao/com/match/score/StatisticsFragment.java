package catc.tiandao.com.match.score;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.RecordAdapter;
import catc.tiandao.com.match.adapter.StatisticsAdapter1;
import catc.tiandao.com.match.adapter.StatisticsAdapter2;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.ben.BasketJiShuTongJi;
import catc.tiandao.com.match.ben.BasketZhenRong;
import catc.tiandao.com.match.ben.JiShuTongJi;
import catc.tiandao.com.match.ben.Match;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.event.EventFragment;
import catc.tiandao.com.match.ui.event.PopularActivity;
import catc.tiandao.com.match.ui.event.SelectActivity;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * 技术统计
 */
public class StatisticsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView team1_logoUrl1,team2_logoUrl1;
    private TextView team1_name1,team1_score,team2_score,match_status,team2_name1;
    private TextView not_data,not_data1,not_data2;


    private LinearLayout content_layout1,content_layout2,content_layout3;
    private RecyclerView statistics_recycler,statistics_recycler1,statistics_recycler2;
    private RelativeLayout content_layout4,content4_layout1,content4_layout2;

    private TextView home_name,team1Score1,team1Score2,team1Score3 ,team1Score4,team1Score;
    private TextView away_name,team2Score1,team2Score2,team2Score3 ,team2Score4,team2Score;

    private ImageView team1_logoUrl,team2_logoUrl;
    private TextView team1_name,team2_name;
    private Button football,blueBall;

    private StatisticsAdapter1 mAdapter;
    private List<Object> list = new ArrayList();

    private List<BasketZhenRong> mlist1 = new ArrayList();
    private StatisticsAdapter2 mAdapter1;

    private List<BasketZhenRong> mlist2 = new ArrayList();
    private StatisticsAdapter2 mAdapter2;

    private int onPosition = 0;



    // TODO: Rename and change types of parameters
    private int BallType;
    private String BallId;
    private int iCollection;
    private ImageView iv_collection;

    private OnFragmentInteractionListener mListener;

    private GetFootballScoreDetail run;
    private GetBasketballScoreDetail basketRun;
    private FootballMatchCollectAndCancelRun setRun;
    DisplayImageOptions options;


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
                    basketBallParseData(result2);
                    break;

                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    setParseData(result3);
                    break;
                default:
                    break;
            }
        }


    };

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param BallType Parameter 1.
     * @param BallId Parameter 2.
     * @return A new instance of fragment StatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticsFragment newInstance(int BallType, String BallId) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, BallType );
        args.putString( ARG_PARAM2, BallId );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            BallType = getArguments().getInt( ARG_PARAM1 );
            BallId = getArguments().getString( ARG_PARAM2 );
        }

        int radius = UnitConverterUtils.dip2px(getContext(),11 );

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(radius))  // 设置成圆角图片
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_statistics, container, false );
        infoView(view);
        return view;
    }



    private void infoView(View view) {

        iv_collection = ViewUtls.find( getActivity(),R.id.iv_collection );
        team1_logoUrl1 = ViewUtls.find( getActivity(),R.id.team1_logoUrl );
        team2_logoUrl1 = ViewUtls.find( getActivity(),R.id.team2_logoUrl );


        team1_name1 = ViewUtls.find( getActivity(),R.id.team1_name );
        team1_score = ViewUtls.find( getActivity(),R.id.team1_score );
        team2_score = ViewUtls.find( getActivity(),R.id.team2_score );
        team2_name1 = ViewUtls.find( getActivity(),R.id.team2_name );
        match_status = ViewUtls.find( getActivity(),R.id.match_status );
        football = ViewUtls.find( view,R.id.football );
        blueBall = ViewUtls.find( view,R.id.blueBall );

        iv_collection.setOnClickListener( this );
        football.setOnClickListener( this );
        blueBall.setOnClickListener( this );



        not_data = ViewUtls.find( view,R.id.not_data );
        not_data1 = ViewUtls.find( view,R.id.not_data1 );
        not_data2 = ViewUtls.find( view,R.id.not_data2 );
        content_layout1 = ViewUtls.find( view,R.id.content_layout1 );
        content_layout2 = ViewUtls.find( view,R.id.content_layout2 );
        content_layout3 = ViewUtls.find( view,R.id.content_layout3 );
        content_layout4 = ViewUtls.find( view,R.id.content_layout4 );
        content4_layout1 = ViewUtls.find( view,R.id.content4_layout1 );
        content4_layout2 = ViewUtls.find( view,R.id.content4_layout2 );

        home_name = ViewUtls.find( view,R.id.home_name );
        team1Score1 = ViewUtls.find( view,R.id.team1Score1 );
        team1Score2 = ViewUtls.find( view,R.id.team1Score2 );
        team1Score3 = ViewUtls.find( view,R.id.team1Score3 );
        team1Score4 = ViewUtls.find( view,R.id.team1Score4 );
        team1Score = ViewUtls.find( view,R.id.team1Score );

        away_name = ViewUtls.find( view,R.id.away_name );
        team2Score1 = ViewUtls.find( view,R.id.team2Score1 );
        team2Score2 = ViewUtls.find( view,R.id.team2Score2 );
        team2Score3 = ViewUtls.find( view,R.id.team2Score3 );
        team2Score4 = ViewUtls.find( view,R.id.team2Score4 );
        team2Score = ViewUtls.find( view,R.id.team2Score );


        statistics_recycler = ViewUtls.find( view,R.id.statistics_recycler );
        statistics_recycler1 = ViewUtls.find( view,R.id.statistics_recycler1 );
        statistics_recycler2 = ViewUtls.find( view,R.id.statistics_recycler2 );
        team1_logoUrl = ViewUtls.find( view,R.id.team1_logoUrl );
        team2_logoUrl = ViewUtls.find( view,R.id.team2_logoUrl );
        team1_name = ViewUtls.find( view,R.id.team1_name );
        team2_name = ViewUtls.find( view,R.id.team2_name );

        setRecent1();
        setRecent2();
        setRecent3();

        if(BallType == 0){
            content_layout1.setVisibility( View.GONE );
            content_layout3.setVisibility( View.GONE );
            content_layout4.setVisibility( View.GONE );
            getData();
        }else {
            content_layout1.setVisibility( View.VISIBLE );
            content_layout3.setVisibility( View.VISIBLE );
            content_layout4.setVisibility( View.VISIBLE );

            GetBasketball();

        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.football:
                if (onPosition != 0) {
                    setContontView( 0 );
                }
                break;
            case R.id.blueBall:
                if (onPosition != 1) {
                    setContontView( 1 );
                }
                break;
            case R.id.iv_collection:

                //http:// 域名/LSQB/ FootballMatchCollectAndCancel? token=***& matchId=比赛id
                FootballMatchCollectAndCancel();

                break;

        }
    }


    private void FootballMatchCollectAndCancel() {

        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("matchId", BallId);

                setRun = new FootballMatchCollectAndCancelRun(param);
                ThreadPoolManager.getsInstance().execute(setRun);
            } else {

                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *
     * */
    class FootballMatchCollectAndCancelRun implements Runnable{
        private HashMap<String, String> param;

        FootballMatchCollectAndCancelRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            String method;
            if(BallType == 0){
                method = HttpUtil.FOOTBALL_MATCH_COLLECT_ANDCANCEL;
            }else {
                method = HttpUtil.BASKETBAL_MATCH_COLLECT_ANDCANCEL;
            }

            HttpUtil.post( getActivity(),method ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x003;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void setParseData(String result) {

        if(result == null){
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );



            if(code == 0) {

                if(iCollection == 0){
                    iCollection = 1;
                    iv_collection.setImageResource( R.mipmap.icon_collect );
                }else {
                    iCollection = 0;
                    iv_collection.setImageResource( R.mipmap.navbar_icon_collect_white_default );
                }


                if(BallType == 0){
                    EventBus.getDefault().post( Constant.UP_BALL);
                }else {
                    EventBus.getDefault().post( Constant.UP_BASEKET_BALL);
                }

            }

            Toast.makeText( getActivity(),message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }


    private void setRecent1() {

        statistics_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StatisticsAdapter1(getActivity(),list);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        statistics_recycler.setNestedScrollingEnabled( false );
        statistics_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        statistics_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //statistics_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));


    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {
        onPosition = i;
        // TODO Auto-generated method stub


        int color01 = ContextCompat.getColor( getContext(),R.color.text1);
        int color02 = ContextCompat.getColor(getContext(),R.color.white);

        switch (i) {
            case 0:

                football.setBackgroundResource( R.drawable.bg_search_normal1 );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2_host );
                football.setTextColor( color02 );
                blueBall.setTextColor( color01 );

                content4_layout1.setVisibility( View.VISIBLE );
                content4_layout2.setVisibility( View.GONE );


                break;
            case 1:
                football.setBackgroundResource( R.drawable.bg_search_normal1_host );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2 );
                football.setTextColor( color01 );
                blueBall.setTextColor( color02 );

                content4_layout1.setVisibility( View.GONE );
                content4_layout2.setVisibility( View.VISIBLE );
                break;

            default:
                break;
        }
    }




    private void setRecent2() {

        statistics_recycler1.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter1 = new StatisticsAdapter2(getActivity(),mlist1);
        mAdapter1.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        statistics_recycler1.setNestedScrollingEnabled( false );
        statistics_recycler1.setAdapter(mAdapter1);
        // 设置Item增加、移除动画
        statistics_recycler1.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //statistics_recycler2.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }

    private void setRecent3() {

        statistics_recycler2.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter2 = new StatisticsAdapter2(getActivity(),mlist2);
        mAdapter2.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        statistics_recycler2.setNestedScrollingEnabled( false );
        statistics_recycler2.setAdapter(mAdapter2);
        // 设置Item增加、移除动画
        statistics_recycler2.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //statistics_recycler2.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }


    private void getData() {

       /* 足球比分详情页-技术统计
        说明： 这里的json都是经过处理的，命名蛮规范的，有不明白的群里直接问吧。
        url：http:// 域名/LSQB/ GetFootballScoreDetail_JiShuTongJi?token=***& matchId=比赛id*/


        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("matchId", BallId);

                run = new GetFootballScoreDetail(param);
                ThreadPoolManager.getsInstance().execute(run);
            } else {

                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class GetFootballScoreDetail implements Runnable{
        private HashMap<String, String> param;

        GetFootballScoreDetail(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( getActivity(),HttpUtil.GET_FOOTBALL_SCORE_DETAIL ,param,new HttpUtil.HttpUtilInterface(){
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
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );



            if(code == 0) {



                JSONObject data = obj.optJSONObject( "data" );
                String team1Name = data.optString( "team1Name" );
                String team2Name = data.optString( "team2Name" );
                String team1Logo = data.optString( "team1Logo" );
                String team2Logo = data.optString( "team2Logo" );
                String team1Score = data.optString( "team1Score" );
                String team2Score = data.optString( "team2Score" );
                String statusName = data.optString( "statusName" );
                iCollection = data.optInt( "iCollection" );

                if(iCollection == 0){
                    iv_collection.setImageResource( R.mipmap.navbar_icon_collect_white_default );
                }else {
                    iv_collection.setImageResource( R.mipmap.icon_collect );
                }


                ImageLoader.getInstance().displayImage(team1Logo, team1_logoUrl1,options);
                ImageLoader.getInstance().displayImage(team1Logo, team1_logoUrl,options);

                ImageLoader.getInstance().displayImage(team2Logo, team2_logoUrl1,options);
                ImageLoader.getInstance().displayImage(team2Logo, team2_logoUrl,options);
                team1_name.setText( team1Name );
                team1_name1.setText( team1Name );
                team2_name.setText( team2Name );
                team2_name1.setText( team2Name );

                team1_score.setText( team1Score );
                team2_score.setText( team2Score );
                match_status.setText( statusName );



                String jiShuTongJiJson = data.optString( "jiShuTongJiJson" );
                if(jiShuTongJiJson != null && !jiShuTongJiJson.equals( "null" )){
                    list.clear();

                    jiShuTongJiJson = jiShuTongJiJson.replace("\\\"","");
                    JSONArray array = new JSONArray( jiShuTongJiJson );
                    if(array.length() > 0){

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<JiShuTongJi>>(){}.getType();
                        List<JiShuTongJi> mlist = gson.fromJson(array.toString(),type);
                        if(mlist.size() > 0){
                            list.addAll( mlist );
                        }

                    }else {

                        not_data.setVisibility( View.VISIBLE );

                    }

                    mAdapter.notifyDataSetChanged();
                }else {
                    not_data.setVisibility( View.VISIBLE );
                }






            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }



    private void GetBasketball() {

       /* 足球比分详情页-技术统计
        说明： 这里的json都是经过处理的，命名蛮规范的，有不明白的群里直接问吧。
        url：http:// 域名/LSQB/ GetBasketballScoreDetail_JiShuTongJi?token=***& matchId=比赛id*/


        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                //BallId = "3505489";
                param.put("matchId", BallId);

                basketRun = new GetBasketballScoreDetail(param);
                ThreadPoolManager.getsInstance().execute(basketRun);
            } else {

                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class GetBasketballScoreDetail implements Runnable{
        private HashMap<String, String> param;

        GetBasketballScoreDetail(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( getActivity(),HttpUtil.GET_BASKETBALL_SCORE_DETAIL ,param,new HttpUtil.HttpUtilInterface(){
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



    private void basketBallParseData(String result) {

        if(result == null){
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if(code == 0) {

                JSONObject data = obj.optJSONObject( "data" );
                String team1Name = data.optString( "team1Name" );
                String team2Name = data.optString( "team2Name" );
                String team1Logo = data.optString( "team1Logo" );
                String team2Logo = data.optString( "team2Logo" );
                String teamScore1 = data.optString( "team1Score" );
                String teamScore2 = data.optString( "team2Score" );
                String statusName = data.optString( "statusName" );


                ImageLoader.getInstance().displayImage( team1Logo, team1_logoUrl1, options );
                ImageLoader.getInstance().displayImage( team1Logo, team1_logoUrl, options );

                ImageLoader.getInstance().displayImage( team2Logo, team2_logoUrl1, options );
                ImageLoader.getInstance().displayImage( team2Logo, team2_logoUrl, options );
                team1_name.setText( team1Name );
                team1_name1.setText( team1Name );
                football.setText( team1Name );
                team2_name.setText( team2Name );
                team2_name1.setText( team2Name );
                blueBall.setText( team2Name );

                team1Score1.setText( teamScore1 );
                team2Score1.setText( teamScore2 );
                match_status.setText( statusName );


                home_name.setText( team1Name );
                team1Score1.setText( data.optString( "team1Score1" ) );
                team1Score2.setText( data.optString( "team1Score2" ) );
                team1Score3.setText( data.optString( "team1Score3" ) );
                team1Score4.setText( data.optString( "team1Score4" ) );
                team1Score.setText( data.optString( "team1Score" ) );

                away_name.setText( team2Name );
                team2Score1.setText( data.optString( "team2Score1" ) );
                team2Score2.setText( data.optString( "team2Score2" ) );
                team2Score3.setText( data.optString( "team2Score3" ) );
                team2Score4.setText( data.optString( "team2Score4" ) );
                team2Score.setText( data.optString( "team2Score" ) );

                String jiShuTongJiJson = data.optString( "jiShuTongJiJson" );

                if (jiShuTongJiJson != null && !jiShuTongJiJson.equals( "null" )) {
                    jiShuTongJiJson = jiShuTongJiJson.replace( "\\\"", "" );
                    JSONArray array = new JSONArray( jiShuTongJiJson );
                    if (array.length() > 0) {

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<BasketJiShuTongJi>>() {
                        }.getType();
                        List<BasketJiShuTongJi> mList = gson.fromJson( array.toString(), type );
                        if (mList.size() > 0) {
                            list.addAll( mList );
                        }

                    } else {
                        not_data.setVisibility( View.VISIBLE );
                    }

                    mAdapter.notifyDataSetChanged();
                } else {
                    not_data.setVisibility( View.VISIBLE );
                }


                // {\"id\":11994,\"name_zh\":\"达里奥·萨里奇\",\"name_zht\":\"\",\"name_en\":\"dario saric\",\"imgUrl\":\"ceb1ffc565c505992f7be68a5182516c.png\",\"QiuYi\":\"20\",\"Note\":\"34^5-8^3-6^2-4^1^9^10^3^2^0^1^2^12^15^1^1^0\",\"type\":1,\"IsShouFa\":1},{\"id\":12123,\"name_zh\":\"凯利·乌布雷\",\"name_zht\":\"\",\"name_en\":\"kelly oubre jr\",\"imgUrl\":\"07f2cf164cdf074c4c8352c341977021.png\",\"QiuYi\":\"3\",\"Note\":\"21^5-10^1-2^9-9^2^3^5^1^3^1^1^5^12^20^1^0^0\",\"type\":1,\"IsShouFa\":1},{\"id\":11873,\"name_zh\":\"阿隆·贝恩斯\",\"name_zht\":\"\",\"name_en\":\"aron baynes\",\"imgUrl\":\"d8e99b8b0fe31d7602598c7693064d4c.png\",\"QiuYi\":\"46\",\"Note\":\"23^4-9^1-4^5-6^2^6^8^3^0^2^1^4^0^14^1^0^0\",\"type\":1,\"IsShouFa\":1},{\"id\":12128,\"name_zh\":\"杰文·卡特\",\"name_zht\":\"\",\"name_en\":\"jevon carter\",\"imgUrl\":\"ad72c32f9f16e88aeb3f822037afca68.png\",\"QiuYi\":\"4\",\"Note\":\"33^3-7^2-5^0-0^0^3^3^6^3^0^4^3^10^8^1^1^0\",\"type\":1,\"IsShouFa\":1},{\"id\":12144,\"name_zh\":\"德文·布克\",\"name_zht\":\"\",\"name_en\":\"devin booker\",\"imgUrl\":\"ba95ed4cc7e691c863f6b4a212dd0964.png\",\"QiuYi\":\"1\",\"Note\":\"38^10-20^3-6^7-8^1^5^6^8^0^0^4^5^11^30^1^0^0\",\"type\":1,\"IsShouFa\":1},{\"id\":11778,\"name_zh\":\"弗兰克·卡明斯基\",\"name_zht\":\"\",\"name_en\":\"frank kaminsky\",\"imgUrl\":\"40966c287937299a5729b8911efccda8.png\",\"QiuYi\":\"8\",\"Note\":\"29^7-13^3-7^1-2^0^8^8^6^0^0^3^4^15^18^1^0^1\",\"type\":1,\"IsShouFa\":0},{\"id\":13730,\"name_zh\":\"卡梅伦·约翰逊\",\"name_zht\":\"\",\"name_en\":\"cameron johnson\",\"imgUrl\":\"626ad3cafb851987b70d860dee59d0a8.png\",\"QiuYi\":\"23\",\"Note\":\"9^1-3^1-3^1-2^1^0^1^0^1^0^1^2^-9^4^1^1^1\",\"type\":1,\"IsShouFa\":0},{\"id\":12186,\"name_zh\":\"米卡尔·布里奇斯\",\"name_zht\":\"\",\"name_en\":\"mikal bridges\",\"imgUrl\":\"7476de92660aef296520bfcc04932d23.png\",\"QiuYi\":\"25\",\"Note\":\"26^3-5^1-3^0-1^1^3^4^0^0^0^1^4^-2^7^1^1^1\",\"type\":1,\"IsShouFa\":0},{\"id\":12392,\"name_zh\":\"埃利·奥科博\",\"name_zht\":\"\",\"name_en\":\"elie okobo\",\"imgUrl\":\"86a683e3ba2efaf89ff8cc3eb3ccfe07.png\",\"QiuYi\":\"2\",\"Note\":\"5^2-3^0-1^0-0^0^2^2^1^0^0^0^1^-6^4^1^1^1\",\"type\":1,\"IsShouFa\":0},{\"id\":11717,\"name_zh\":\"泰勒·约翰逊\",\"name_zht\":\"\",\"name_en\":\"tyler johnson\",\"imgUrl\":\"33ee431c5a549acb203fb86e92a39d5f.png\",\"QiuYi\":\"16\",\"Note\":\"21^3-8^2-6^2-2^1^1^2^4^0^0^0^4^-6^10^1^0^1\",\"type\":1,\"IsShouFa\":0},{\"id\":12265,\"name_zh\":\"谢赫·迪亚洛\",\"name_zht\":\"\",\"name_en\":\"cheick diallo\",\"imgUrl\":\"7eec33ff8fb0451db5f13cbf6a7a6d69.png\",\"QiuYi\":\"14\",\"Note\":\"0^0-0^0-0^0-0^0^0^0^0^0^0^0^0^0^0^0^1^1\",\"type\":1,\"IsShouFa\":0},{\"id\":12489,\"name_zh\":\"德安德烈·艾顿\",\"name_zht\":\"\",\"name_en\":\"deandre ayton\",\"imgUrl\":\"ffc5a1f7deedf65c688b1503c222ef55.png\",\"QiuYi\":\"22\",\"Note\":\"0^0-0^0-0^0-0^0^0^0^0^0^0^0^0^0^0^0^1^1\",\"type\":1,\"IsShouFa\":0},{\"id\":14634,\"name_zh\":\"贾里�


                String PlayersData = data.optString( "PlayersData" );

                    if (PlayersData != null && !PlayersData.equals( "null" )) {
                        PlayersData = PlayersData.replace( "\\\"", "" );
                        JSONArray array1 = new JSONArray( PlayersData );
                        if (array1.length() > 0) {

                            Gson gson = new Gson();
                            Type type = new TypeToken<List<BasketZhenRong>>() {
                            }.getType();
                            List<BasketZhenRong> list = gson.fromJson( array1.toString(), type );

                            for (int i = 0; i < list.size(); i++) {
                                BasketZhenRong mBasketZhenRong = list.get( i );
                                if (mBasketZhenRong.getType() == 1) {
                                    mlist1.add( mBasketZhenRong );
                                } else if (mBasketZhenRong.getType() == 2) {
                                    mlist2.add( mBasketZhenRong );
                                }
                            }

                            mAdapter1.notifyDataSetChanged();
                            mAdapter2.notifyDataSetChanged();

                        } else {
                            not_data1.setVisibility( View.VISIBLE );
                            not_data2.setVisibility( View.VISIBLE );
                        }

                    } else {
                        not_data1.setVisibility( View.VISIBLE );
                        not_data2.setVisibility( View.VISIBLE );
                    }
                }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
