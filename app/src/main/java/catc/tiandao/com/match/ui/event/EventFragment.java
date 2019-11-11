package catc.tiandao.com.match.ui.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.EventAdapter;
import catc.tiandao.com.match.ben.Match;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.common.RecyclerItemClickListener;
import catc.tiandao.com.match.score.ScoreDetailsActivity;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 赛事列表
 */
public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView ball_recycler;
    private LinearLayout no_data;

    private LinearLayoutManager layoutManager;
    private EventAdapter mAdapter;
    private List<Match> mList = new ArrayList<>(  );
    private GetFootballMath run;
    private StickyRecyclerHeadersDecoration headersDecor;
    private FootballMatchCollectAndCancelRun setRun;


    // TODO: Rename and change types of parameters
    private int mParam1;
    private int areaId;

    private int page = 1;
    private int pageSize = 20;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;


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
                case 0x003:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    setParseData(result2,msg.arg1);
                    break;
                default:
                    break;
            }
        }


    };

    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param areaId Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(int param1, int areaId) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, param1 );
        args.putInt( ARG_PARAM2, areaId );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getInt( ARG_PARAM1 );
            areaId = getArguments().getInt( ARG_PARAM2 );
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_collection, container, false );
        viewInfo(view);
        getData();

        return view;
    }



    private void viewInfo(View view) {


        ball_recycler = ViewUtls.find( view,R.id.ball_recycler );
        no_data = ViewUtls.find( view,R.id.no_data );


        // 设置布局管理器
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ball_recycler.setLayoutManager(layoutManager);
        mAdapter = new EventAdapter(getContext(),mList,mParam1);
        ball_recycler.setAdapter(mAdapter);

        // 为RecyclerView添加LayoutManager

        // 为RecyclerView添加Decorator装饰为RecyclerView中的Item添加Header头部器
        // （自动获取头部ID，将相邻的ID相同的聚合到一起形成一个Header）
        headersDecor = new StickyRecyclerHeadersDecoration(mAdapter); //设置recyclerView需要的Decoration
        ball_recycler.addItemDecoration(headersDecor);
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mSwipeRefreshLayout = ViewUtls.find( view,R.id.swipeRefreshLayout );

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
                    getData();
                }
            }
        });



        //上拉加载
        //addOnScrollListener
        ball_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });




        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

                Match mMatch = mList.get( postion );

                switch (view.getId()){


                    case R.id.item_view:
                        if(postion + 1 < mList.size()){

                            String matchName;
                            if(mParam1 == 0){
                                matchName =  mMatch.getMatchEventName() + " 第"+ mMatch.getMatchRound() + "轮  " + mMatch.getMatchTime();
                            }else {
                                matchName = mMatch.getMatchEventName() + " " + mMatch.getMatchTime();
                            }

                            if(mMatch.getMatchStatusId() > 1){
                                Intent intent01 = new Intent(getActivity(), ScoreDetailsActivity.class);
                                intent01.putExtra( ScoreDetailsActivity.BALL_TYPE,  mParam1);
                                intent01.putExtra( ScoreDetailsActivity.BALL_ID,  mMatch.getMatchId());
                                intent01.putExtra( ScoreDetailsActivity.MATCH_NAME,  matchName);
                                startActivity(intent01);
                                ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }else {
                                //
                                Intent intent01 = new Intent(getActivity(), MatchDetailsActivity.class);
                                intent01.putExtra( MatchDetailsActivity.BALL_TYPE,  mParam1);
                                intent01.putExtra( MatchDetailsActivity.BALL_ID,  mMatch.getMatchId());
                                startActivity(intent01);
                                ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                            }


                        }


                        break;
                    case R.id.is_collection:

                        if(UserUtils.isLanded( getActivity() )){
                            FootballMatchCollectAndCancel(mMatch.getMatchId(),postion);

                        }else {
                            UserUtils.startLongin( getActivity() );
                        }

                        break;

                }
            }
        } );

       // initEvent();

    }

    private void initEvent() {
        // 为RecyclerView添加普通Item的点击事件（点击Header无效）

        ball_recycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
        }));
        // 为RecyclerView添加Header的点击事件
        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(ball_recycler, headersDecor);
        touchListener.setOnHeaderClickListener(new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(View header, int position, final long headerId) {

            }
        });

        ball_recycler.addOnItemTouchListener(touchListener);
    }








    private void getData() {

        //http:// 域名/LSQB/ GetFootballMath? token=***& pageSize=每页多少条& page=第几页

        isRun = true;

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

                //token=***& pageSize=每页多少条& page=第几页
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");
                param.put("areaId", areaId + "");

                run = new GetFootballMath(param);
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
     *
     * */
    class GetFootballMath implements Runnable{
        private HashMap<String, String> param;

        GetFootballMath(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            String methodName;
            if(mParam1 == 0){
                methodName = HttpUtil.GET_FOOTBALL_MATH;
            }else {
                methodName = HttpUtil.GET_BASKETBALL_MATH;
            }

            HttpUtil.post( getActivity(),methodName ,param,new HttpUtil.HttpUtilInterface(){
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
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
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
                    Type type = new TypeToken<List<Match>>(){}.getType();
                    List<Match> list = gson.fromJson(data.toString(),type);
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

            mSwipeRefreshLayout.setRefreshing(false);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
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

                Match mBallBen = mList.get( poistion );
                int iCollection = mBallBen.getIsCollection();

                if(iCollection == 0){
                    mList.get( poistion ).setIsCollection( 1 );
                }else {
                    mList.get( poistion ).setIsCollection( 0 );

                }

                mAdapter.notifyDataSetChanged();
            }


            Toast.makeText( getActivity(),message,Toast.LENGTH_SHORT ).show();


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

    @Subscribe
    public void onEvent(Object event) {
        if (Constant.UP_BALL.equals( event ) && mParam1 == 0) {
            page = 1;
            getData();

        }else if(Constant.UP_BASEKET_BALL.equals( event ) && mParam1 == 1){
            page = 1;
            getData();
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        mListener = null;
    }


}
