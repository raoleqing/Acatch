package catc.tiandao.com.match.score;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CommItemDecoration;
import catc.tiandao.com.match.my.NoticeActivity;
import catc.tiandao.com.match.utils.GetTokenUtils;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.matchlibrary.MyItemClickListener;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.match.common.PromptDialog;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.match.widgets.datepicker.CustomDatePicker;
import catc.tiandao.com.match.widgets.datepicker.DateFormatUtils;
import catc.tiandao.com.matchlibrary.adapter.BallAdapter;
import catc.tiandao.com.matchlibrary.ben.BallBen;
import catc.tiandao.com.matchlibrary.ben.DateBen;

/**
 *
 * 进行中
 */
public class BallFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private LinearLayout tab_layout,tab_bar6,no_data;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView ball_recycler;
    private int[] tarLayout = {R.id.tab_bar1,R.id.tab_bar2,R.id.tab_bar3,R.id.tab_bar4,R.id.tab_bar5};
    private int[] textIdArray1 = {R.id.tab_bar1_text1,R.id.tab_bar2_text1,R.id.tab_bar3_text1,R.id.tab_bar4_text1,R.id.tab_bar5_text1};
    private int[] textIdArray2 = {R.id.tab_bar1_text2,R.id.tab_bar2_text2,R.id.tab_bar3_text2,R.id.tab_bar4_text2,R.id.tab_bar5_text2};
    private TextView[] textArray1 = new TextView[5];
    private TextView[] textArray2 = new TextView[5];

    private List<DateBen> dateList = new ArrayList(  );

    private LinearLayoutManager mLinearLayoutManager;
    private BallAdapter mAdapter;
    private List<BallBen> mList = new ArrayList<>(  );

    private List<BallBen> mList1 = new ArrayList<>(  );
    private List<BallBen> mList2 = new ArrayList<>(  );


    //type——going-进行中；todayUnstart-未开始；unstart-赛程；end-赛果；focus-我的关注
    private String types[] = {"going","todayUnstart","unstart","end","focus"};


    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;

    private GetFootballScoreRun run1;
    private GetFootballScoreRun run;
    private int pageSize = 30;
    private int page = 1;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;

    private String onDate = "";

    private CustomDatePicker mDatePicker;
    private FootballMatchCollectAndCancelRun setRun;

    PromptDialog promptDialog;

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
                    onParseData(result2);
                    break;
                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    setParseData(result3,msg.arg1);
                    break;
                case 0x004:
                    getPromtData();
                    break;
                case 0x005:

                    if(promptDialog != null){
                        promptDialog.dismiss();
                        promptDialog = null;
                    }
                    break;
                default:
                    break;
            }
        }


    };


    public BallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BallFragment newInstance(int param1, int param2) {
        BallFragment fragment = new BallFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, param1 );
        args.putInt( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getInt( ARG_PARAM1 );
            mParam2 = getArguments().getInt( ARG_PARAM2 );
        }
        EventBus.getDefault().register(this);
        setData();

        if(mParam2 == 0){
            myHandler.sendEmptyMessageDelayed( 0x004,15000 );
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_ball, container, false );
        viewInfo(view);
        initDatePicker();
        getData("");
        //开启一个定时器
        if(mParam2 == 0){

        }
        return view;
    }

    private void viewInfo(View view) {

        tab_layout = ViewUtls.find( view,R.id.tab_layout );
        no_data = ViewUtls.find( view,R.id.no_data );
        tab_bar6 = ViewUtls.find( view,R.id.tab_bar6 );
        mSwipeRefreshLayout = ViewUtls.find( view,R.id.swipeRefreshLayout );
        ball_recycler = ViewUtls.find( view,R.id.ball_recycler );
        tab_bar6.setOnClickListener( this );

        for(int i = 0; i< tarLayout.length ;i++){

            DateBen mDateBen = dateList.get( i );

            LinearLayout layout = ViewUtls.find( view,tarLayout[i] );
            textArray1[i] = ViewUtls.find( view,textIdArray1[i] );
            textArray2[i] = ViewUtls.find( view,textIdArray2[i] );
            textArray1[i].setText( mDateBen.getShowDate() );
            textArray2[i].setText( mDateBen.getWeek() );
            layout.setOnClickListener( this );
        }


        if(mParam2 == 3){
            textArray2[0].setVisibility( View.VISIBLE );
            textArray1[4].setText( "今天" );
            textArray2[4].setVisibility( View.GONE );

            textArray1[4].setTextColor( ContextCompat.getColor( getActivity(),R.color.text1 ) );
            textArray1[0].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );
            textArray2[0].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );

        }else {
            textArray2[4].setVisibility( View.VISIBLE );
            textArray1[0].setText( "今天" );
            textArray2[0].setVisibility( View.GONE );
            textArray1[0].setTextColor( ContextCompat.getColor( getActivity(),R.color.text1 ) );
            textArray1[4].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );
            textArray2[4].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );
        }


        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        ball_recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new BallAdapter(getActivity(),mList,mParam2);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {
                BallBen mBallBen =  mList.get( postion );

                if(view.getId() == R.id.is_collection){
                    if(UserUtils.isLanded( getActivity() )){
                        FootballMatchCollectAndCancel(mBallBen.getMatchId(),postion);
                    }else {
                        UserUtils.startLongin( getActivity() );
                    }

                }else{
                    if(mBallBen.getMatchStatusId() > 1){

                        String matchName =  mBallBen.getMatchEventName() + " 第"+ mBallBen.getMatchRound() + "轮  " + mBallBen.getMatchTime();

                        Intent intent01 = new Intent(getActivity(), ScoreDetailsActivity.class);
                        intent01.putExtra( ScoreDetailsActivity.BALL_TYPE,  0);
                        intent01.putExtra( ScoreDetailsActivity.BALL_ID,  mBallBen.getMatchId() );
                        intent01.putExtra( ScoreDetailsActivity.MATCH_NAME,  matchName);
                        startActivity(intent01);
                        ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                    }else {
                        //
                        Intent intent01 = new Intent(getActivity(), MatchDetailsActivity.class);
                        intent01.putExtra( MatchDetailsActivity.BALL_TYPE,  0);
                        intent01.putExtra( MatchDetailsActivity.BALL_ID,  mBallBen.getMatchId());
                        startActivity(intent01);
                        ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                    }
                }





            }
        });


        /*setOnRefreshListener(OnRefreshListener):添加下拉刷新监听器
        setRefreshing(boolean):显示或者隐藏刷新进度条
        isRefreshing():检查是否处于刷新状态
        setColorSchemeResources():设置进度条的颜色主题，最多设置四种，以前的setColorScheme()方法已经弃用了
        */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!isRun){
                    page = 1;
                    getData(onDate);
                }
            }
        });

        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //ball_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        int color = ContextCompat.getColor( getActivity(),R.color.line_01 );
        ball_recycler.addItemDecoration( CommItemDecoration.createVertical(getActivity(), color,1));


        if(mParam2 == 2 || mParam2 == 3){
            tab_layout.setVisibility( View.VISIBLE );
        }else{
            tab_layout.setVisibility( View.GONE );
        }



        //上拉加载
        //addOnScrollListener
        ball_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() ) {
                    mAdapter.changeMoreStatus(1);
                    mAdapter.notifyDataSetChanged();

                    if(isData && !isRun){
                        getData(onDate);
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


    private void initDatePicker() {
        long beginTimestamp;
        long endTimestamp;

        if(mParam2 == 3){

            Calendar calendar = Calendar.getInstance();
            String startYearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int startMonth = calendar.get(Calendar.MONTH) + 1;//获取月份
            String startMonthStr = startMonth < 10 ? "0" + startMonth : startMonth + "";
            int statrDay = calendar.get(Calendar.DATE);//获取日
            String startDayStr = statrDay < 10 ? "0" + statrDay : statrDay + "";
            String startDate = startYearStr + "-" + startMonthStr + "-" + startDayStr;

            endTimestamp = DateFormatUtils.str2Long(startDate, false);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
            String yearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int month = calendar.get(Calendar.MONTH) + 1;//获取月份
            String monthStr = month < 10 ? "0" + month : month + "";
            int day = calendar.get(Calendar.DATE);//获取日
            String dayStr = day < 10 ? "0" + day : day + "";
            String toData = yearStr + "-" + monthStr + "-" + dayStr;

            beginTimestamp = DateFormatUtils.str2Long(toData , false);



        }else {

            Calendar calendar = Calendar.getInstance();

            String startYearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int startMonth = calendar.get(Calendar.MONTH) + 1;//获取月份
            String startMonthStr = startMonth < 10 ? "0" + startMonth : startMonth + "";
            int statrDay = calendar.get(Calendar.DATE);//获取日
            String startDayStr = statrDay < 10 ? "0" + statrDay : statrDay + "";
            String startDate = startYearStr + "-" + startMonthStr + "-" + startDayStr;
            beginTimestamp = DateFormatUtils.str2Long(startDate , false);

            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);


            String yearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int month = calendar.get(Calendar.MONTH) + 1;//获取月份
            String monthStr = month < 10 ? "0" + month : month + "";
            int day = calendar.get(Calendar.DATE);//获取日
            String dayStr = day < 10 ? "0" + day : day + "";
            String toData = yearStr + "-" + monthStr + "-" + dayStr;

            endTimestamp = DateFormatUtils.str2Long(toData, false);

        }


        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                String dataStr = DateFormatUtils.long2Str(timestamp, false);
                page = 1;
                getData( dataStr );
            }
        }, beginTimestamp, endTimestamp);


        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tab_bar6:
                mDatePicker.show(dateList.get( 0 ).getDate());
                break;
        }

        for(int i = 0; i< tarLayout.length; i++){
            if(v.getId() == tarLayout[i]){
                textArray1[i].setTextColor( ContextCompat.getColor( getActivity(),R.color.text1 ) );
                textArray2[i].setTextColor( ContextCompat.getColor( getActivity(),R.color.text1 ) );
                onDate = dateList.get( i ).getDate();
                page = 1;
                getData(onDate);
            }else {
                textArray1[i].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );
                textArray2[i].setTextColor( ContextCompat.getColor( getActivity(),R.color.text2 ) );
            }
        }

    }


    private void FootballMatchCollectAndCancel(String matchId,int postion) {

        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("matchId", matchId);

                setRun = new FootballMatchCollectAndCancelRun(param,postion);
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
        private int poistion;

        FootballMatchCollectAndCancelRun(HashMap<String, String> param,int poistion){
            this.param =param;
            this.poistion = poistion;
        }

        @Override
        public void run() {

            HttpUtil.post( getActivity(),HttpUtil.FOOTBALL_MATCH_COLLECT_ANDCANCEL ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x003;
                    message.arg1 = poistion;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void setParseData(String result,int poistion) {

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

                BallBen mBallBen = mList.get( poistion );
                int iCollection = mBallBen.getIsCollection();

                if(iCollection == 0){
                    mList.get( poistion ).setIsCollection( 1 );
                }else {
                    mList.get( poistion ).setIsCollection( 0 );
                    if(mParam2 == 4){
                        mList.remove( poistion );
                    }
                }

                //刷新
                Constant.SenType = mParam2;
                EventBus.getDefault().post( Constant.UP_BALL_1);


                mAdapter.notifyDataSetChanged();
            }

            if(mList.size() > 0){

                no_data.setVisibility( View.GONE );
            }else {
                no_data.setVisibility( View.VISIBLE );
            }

            Toast.makeText( getActivity(),message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }



    private void  getPromtData(){

       if (CheckNet.isNetworkConnected( getActivity())) {


           HashMap<String, String> param = new HashMap<>(  );
           param.put("token", UserUtils.getToken( getActivity()) );
           param.put("type","going");
           param.put("date","");
           param.put("pageSize", pageSize + "");
           param.put("page","1");

           run1 = new GetFootballScoreRun(param,1);
           ThreadPoolManager.getsInstance().execute(run1);



       }

   }



    private void getData(String date) {

        isRun = true;

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                String type = types[mParam2];

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity()) );
                param.put("type",type);
                param.put("date",date);
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                run = new GetFootballScoreRun(param,0);
                ThreadPoolManager.getsInstance().execute(run);


            } else {

                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));

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
    class GetFootballScoreRun implements Runnable {
        private HashMap<String, String> param;
        private int type;

        GetFootballScoreRun(HashMap<String, String> param,int type ) {
            this.param = param;
            this.type = type;
        }

        @Override
        public void run() {


            HttpUtil.post( getActivity(), HttpUtil.GET_FOOTBALL_SCORE, param, new HttpUtil.HttpUtilInterface() {
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    if(type == 1){
                        message.what = 0x002;
                    }else {
                        message.what = 0x001;
                    }

                    myHandler.sendMessage( message );
                }
            } );

        }

    }

    private void parseData(String result) {

        isRun = false;

        if(result == null){
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }


        try{

            System.out.println(types[mParam2] + ":" + result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if(code == 0) {

                if(page == 1 && mList.size() > 0){
                    mList.clear();
                    mList1.clear();
                    mList2.clear();
                }
                mSwipeRefreshLayout.setRefreshing(false);

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<BallBen>>(){}.getType();
                    List<BallBen> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        if(mParam2 == 4){

                            for(int i = 0; i< list.size(); i++){
                                BallBen mBallBen = list.get( i );
                                if(mBallBen.getMatchStatusId() >= 8){
                                    mList2.add( mBallBen );
                                }else {
                                    mList1.add( mBallBen );
                                }
                            }

                            mList.clear();

                            mList.addAll( mList1 );
                            mList.addAll( mList2 );

                           // mAdapter.setShowEndType( mList1.size() - 1 );

                        }else {
                            mList.addAll( list );
                        }

                    }



                    if(list.size() < pageSize || list.size() == 0 ){
                        isData = false;
                        mAdapter.changeMoreStatus( -1 );
                    }else {
                        isData = true;
                        page ++ ;
                    }


                    mAdapter.notifyDataSetChanged();

                }

                if(mList.size() == 0){
                    no_data.setVisibility( View.VISIBLE );
                }else {
                    no_data.setVisibility( View.GONE );
                }


            }else if(code == Constant.CODE){
                GetTokenUtils utils = new GetTokenUtils( getActivity());
                utils.getToken( new GetTokenUtils.GetTokenUtilInterface() {
                    @Override
                    public void onResponse() {
                        getData(onDate);
                    }
                } );
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(mListener != null){
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            }

        }

    }



    private void onParseData(String result) {

        myHandler.sendEmptyMessageDelayed( 0x004,15000 );


        if(result == null){
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
                    Type type = new TypeToken<List<BallBen>>(){}.getType();
                    List<BallBen> list = gson.fromJson(data.toString(),type);

                    List<BallBen> newList = new ArrayList<>(  );

                    for(int i = 0; i< list.size(); i++){
                        BallBen onBallBen =  list.get( i );
                       if(onBallBen.getHomeTeamScore() > 0 || onBallBen.getAwayTeamScore() > 0){
                           if(i < mList.size()){
                               for(int j = 0; j< mList.size(); j++){
                                   BallBen mBallBen =  mList.get( j );
                                   if(onBallBen.getMatchId() == mBallBen.getMatchId()){
                                       if( mBallBen.getHomeTeamScore() != onBallBen.getHomeTeamScore() || mBallBen.getAwayTeamScore() != onBallBen.getAwayTeamScore()){
                                           mList.set( i, onBallBen);
                                           newList.add( onBallBen );
                                       }
                                   }
                               }


                           }
                       }

                    }


                    if(newList.size() > 0){
                        mAdapter.notifyDataSetChanged();
                        showPromptDialog(newList);
                    }




                }

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showPromptDialog(List<BallBen> newList) {

        if(promptDialog == null){
            promptDialog = new PromptDialog(getActivity(),newList);
            promptDialog.show();
        }

        myHandler.sendEmptyMessageDelayed( 0x005,5000 );
    }


    private void setData() {

        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dft1 = new SimpleDateFormat("MM-dd");
        Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);

        if(mParam2 == 3){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 5);
        }

        for(int i = 0; i< 5; i++){
            if(i > 0){
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            }

            String yearStr = calendar.get(Calendar.YEAR)+"";//获取年份
            int month = calendar.get(Calendar.MONTH) + 1;//获取月份
            String monthStr = month < 10 ? "0" + month : month + "";
            int day = calendar.get(Calendar.DATE);//获取日
            String dayStr = day < 10 ? "0" + day : day + "";
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            String weekStr = "";
            switch (week) {
                case 1:
                    weekStr = "周日";
                    break;
                case 2:
                    weekStr = "周一";
                    break;
                case 3:
                    weekStr = "周二";
                    break;
                case 4:
                    weekStr = "周三";
                    break;
                case 5:
                    weekStr = "周四";
                    break;
                case 6:
                    weekStr = "周五";
                    break;
                case 7:
                    weekStr = "周六";
                    break;
                default:
                    break;
            }

            DateBen mDate = new DateBen();
            mDate.setShowDate( monthStr + "-" + dayStr );
            mDate.setWeek( weekStr );
            mDate.setDate( yearStr + "-" + monthStr + "-" + dayStr );

            dateList.add( mDate );
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

    @Subscribe
    public void onEvent(Object event) {
        if (Constant.UP_SCORE.equals(event) || Constant.UP_BALL.equals( event )) {
            page = 1;
            getData(onDate);

        }else if(Constant.UP_BALL_1.equals( event ) && Constant.SenType != mParam2){
            page = 1;
            getData(onDate);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mDatePicker.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
