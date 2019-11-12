package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
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
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.adapter.InjuredAdapter;
import catc.tiandao.com.matchlibrary.adapter.JiFenAdapter;
import catc.tiandao.com.matchlibrary.adapter.RecordAdapter;
import catc.tiandao.com.matchlibrary.ben.History;
import catc.tiandao.com.matchlibrary.ben.Injured;
import catc.tiandao.com.matchlibrary.ben.JiFen;
import catc.tiandao.com.matchlibrary.ben.TeamGoa;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link dataFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 数据
 */
public class dataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout content_layout1,content_layout2,Injured_content,content_layout4,content_layout5;
    private TextView no_data1,no_data2,no_data3,no_data4,no_data5,no_data6;

    private LinearLayout ball_content;
    private RecyclerView jiFen_recycler,history_recycler,recent_recyclerview1,recent_recyclerview2;
    private TextView title_item1,title_item2,title_item3;

    private TextView goal1_name, goal2_name,goal1_text,goal2_text;
    private int[] goal1Id = {R.id.goal1_text1,R.id.goal1_text2,R.id.goal1_text3,R.id.goal1_text4,R.id.goal1_text5,R.id.goal1_text6};
    private int[] goal2Id = {R.id.goal2_text1,R.id.goal2_text2,R.id.goal2_text3,R.id.goal2_text4,R.id.goal2_text5,R.id.goal2_text6};
    private TextView[] goal1Texts = new TextView[goal1Id.length];
    private TextView[] goal2Texts = new TextView[goal2Id.length];

    private TextView history_text;

    private TextView team1last_text,team2last_text;
    private ImageView team1Last_icon,team2Last_icon;
    private TextView team1Last_name,team2Last_name;

    private JiFenAdapter jifenAdapter;
    private List<JiFen> jifenList = new ArrayList();

    private RecordAdapter mAdapter1;
    private RecordAdapter mAdapter2;
    private RecordAdapter mAdapter3;
    private List<History> mList1 = new ArrayList();
    private List<History> mList2 = new ArrayList();
    private List<History> mList3 = new ArrayList();

    // TODO: Rename and change types of parameters
    private int BallType;
    private String matchId;

    private OnFragmentInteractionListener mListener;
    private GetFootballMatchDetail run;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();



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

    public dataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment dataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dataFragment newInstance(int BallType, String matchId) {
        dataFragment fragment = new dataFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, BallType );
        args.putString( ARG_PARAM2, matchId );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            BallType = getArguments().getInt( ARG_PARAM1 );
            matchId = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_data, container, false );
        viewInfo(view);
        getData( matchId );
        return view;
    }

    private void viewInfo(View view) {


        content_layout1 = ViewUtls.find( view,R.id.content_layout1 );
        content_layout2 = ViewUtls.find( view,R.id.content_layout2 );
        Injured_content = ViewUtls.find( view,R.id.Injured_content );
        content_layout4 = ViewUtls.find( view,R.id.content_layout4 );
        content_layout5 = ViewUtls.find( view,R.id.content_layout5 );
        no_data1 = ViewUtls.find( view,R.id.no_data1 );
        no_data2 = ViewUtls.find( view,R.id.no_data2 );
        no_data3 = ViewUtls.find( view,R.id.no_data3 );
        no_data4 = ViewUtls.find( view,R.id.no_data4 );
        no_data5 = ViewUtls.find( view,R.id.no_data5 );

        ball_content = ViewUtls.find( view,R.id.ball_content );
        jiFen_recycler = ViewUtls.find( view,R.id.jiFen_recycler );
        history_recycler = ViewUtls.find( view,R.id.history_recycler );
        recent_recyclerview1 = ViewUtls.find( view,R.id.recent_recyclerview1 );
        recent_recyclerview2 = ViewUtls.find( view,R.id.recent_recyclerview2 );
        history_text = ViewUtls.find( view,R.id.history_text );

        team1last_text = ViewUtls.find( view,R.id.team1last_text );
        team2last_text = ViewUtls.find( view,R.id.team2last_text );
        team1Last_icon = ViewUtls.find( view,R.id.team1Last_icon );
        team2Last_icon = ViewUtls.find( view,R.id.team2Last_icon );
        team1Last_name = ViewUtls.find( view,R.id.team1Last_name );
        team2Last_name = ViewUtls.find( view,R.id.team2Last_name );

        goal1_name = ViewUtls.find( view,R.id.goal1_name );
        goal2_name = ViewUtls.find( view,R.id.goal1_name );
        goal1_text = ViewUtls.find( view,R.id.goal1_text );
        goal2_text = ViewUtls.find( view,R.id.goal2_text );
        title_item1 = ViewUtls.find( view,R.id.title_item1 );
        title_item2 = ViewUtls.find( view,R.id.title_item2 );
        title_item3 = ViewUtls.find( view,R.id.title_item3 );
        for(int i = 0; i< goal1Id.length; i++){
            goal1Texts[i] = ViewUtls.find( view,goal1Id[i] );
            goal2Texts[i] = ViewUtls.find( view,goal2Id[i] );
        }

        if(BallType == 0){
            ball_content.setVisibility( View.VISIBLE );
            title_item1.setVisibility( View.VISIBLE );
            title_item2.setVisibility( View.VISIBLE );
            title_item3.setVisibility( View.VISIBLE );
        }else {
            ball_content.setVisibility( View.GONE );
            title_item1.setVisibility( View.GONE );
            title_item2.setVisibility( View.GONE );
            title_item3.setVisibility( View.GONE );
        }

        setJiFen();
        setHistory();
        setTeam1Last();
        setTeam2Last();


    }



    private void setJiFen() {

        jiFen_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        jifenAdapter = new JiFenAdapter(getActivity(),jifenList);


        jiFen_recycler.setNestedScrollingEnabled( false );
        jiFen_recycler.setAdapter(jifenAdapter);
        // 设置Item增加、移除动画
        jiFen_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //jiFen_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }

    private void setHistory() {

        history_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter1 = new RecordAdapter(getActivity(),mList1,BallType);

        history_recycler.setNestedScrollingEnabled( false );
        history_recycler.setAdapter(mAdapter1);
        // 设置Item增加、移除动画
        history_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //history_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }

    private void setTeam1Last() {

        recent_recyclerview1.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter2 = new RecordAdapter(getActivity(),mList2,BallType);

        recent_recyclerview1.setNestedScrollingEnabled( false );
        recent_recyclerview1.setAdapter(mAdapter2);
        // 设置Item增加、移除动画
        recent_recyclerview1.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //history_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }



    private void setTeam2Last() {

        recent_recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter3 = new RecordAdapter(getActivity(),mList3,BallType);

        recent_recyclerview2.setNestedScrollingEnabled( false );
        recent_recyclerview2.setAdapter(mAdapter3);
        // 设置Item增加、移除动画
        recent_recyclerview2.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //history_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }




    private void getData(String matchId) {

        //http:// 域名/LSQB/ GetFootballMatchDetail_zhiShu? token=***& matchId=比赛id


        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

              /*  if(BallType == 0){
                    matchId = "2607835";
                }else {
                    matchId = "3505467";
                }*/


                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("matchId", matchId);

                run = new GetFootballMatchDetail(param);
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
    class GetFootballMatchDetail implements Runnable{
        private HashMap<String, String> param;

        GetFootballMatchDetail(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            String methodName;
            if(BallType == 0){
                methodName = HttpUtil.GET_FOOTBALL_MATCHDETAIL_SHUJU;
            }else {
                methodName = HttpUtil.GET_BASKETBALL_MATCHDETAIL_SHUJU;
            }


            HttpUtil.post( getActivity(),methodName,param,new HttpUtil.HttpUtilInterface(){
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




            // {"code":0,"message":"成功","data":{"team1GoalJson":null,"team2GoalJson":null,"teamLianSaiJiFen":null,"teamInjuredJson":null,"teamHistoryVsJson":null,
            // "teamHistoryVsDes":null,"team1LastJson":null,"team1LastDes":null,"team2LastJson":null,"team2LastDes":null}}

            // private LinearLayout content_layout1,content_layout2,Injured_content,content_layout4,content_layout5;
            //    private TextView no_data1,no_data2,no_data3,no_data4,no_data5,no_data6;

            if(code == 0) {

                JSONObject data = obj.optJSONObject( "data" );
                if(BallType == 0){
                    //进球分布
                    String team1GoalJson = data.optString( "team1GoalJson" );
                    if(Constant.isData(team1GoalJson)){

                        String team2GoalJson = data.optString( "team2GoalJson" );
                        setTeamGoal(team1GoalJson,team2GoalJson);

                        content_layout1.setVisibility( View.VISIBLE );
                        no_data1.setVisibility( View.GONE );
                    }else {
                        content_layout1.setVisibility( View.GONE );
                        no_data1.setVisibility( View.VISIBLE );
                    }

                    //联赛积分
                    String teamLianSaiJiFen = data.optString( "teamLianSaiJiFen" );
                    if(Constant.isData( teamLianSaiJiFen )){
                        setJiFenContent(teamLianSaiJiFen);

                        content_layout2.setVisibility( View.VISIBLE );
                        no_data2.setVisibility( View.GONE );
                    }else {
                        content_layout2.setVisibility( View.GONE );
                        no_data2.setVisibility( View.VISIBLE );
                    }



                    //伤停情况
                    String teamInjuredJson = data.optString( "teamInjuredJson" );


                    if(Constant.isData( teamInjuredJson )){
                        setInjured(teamInjuredJson);

                        Injured_content.setVisibility( View.VISIBLE );
                        no_data3.setVisibility( View.GONE );
                    }else {
                        Injured_content.setVisibility( View.GONE );
                        no_data3.setVisibility( View.VISIBLE );
                    }



                }


                ///历史交锋
                String teamHistoryVsJson = data.optString( "teamHistoryVsJson" );
                if(Constant.isData( teamHistoryVsJson )){

                    //历史交锋描述
                    String teamHistoryVsDes = data.optString( "teamHistoryVsDes" );
                    setHistoryContent(teamHistoryVsJson,teamHistoryVsDes);

                    content_layout4.setVisibility( View.VISIBLE );
                    no_data4.setVisibility( View.GONE );
                }else {
                    content_layout4.setVisibility( View.GONE );
                    no_data4.setVisibility( View.VISIBLE );
                }




                //近期战绩
                String team1LastJson = data.optString( "team1LastJson" );

                if(Constant.isData( team1LastJson )){
                    //近期战绩描述
                    String team1LastDes = data.optString( "team1LastDes" );
                    setTeam1last(team1LastJson,team1LastDes);
                    //近期战绩
                    String team2LastJson = data.optString( "team2LastJson" );
                    //近期战绩描述
                    String team2LastDes = data.optString( "team2LastDes" );
                    setTeam2last(team2LastJson,team2LastDes);

                    content_layout5.setVisibility( View.VISIBLE );
                    no_data5.setVisibility( View.GONE );
                }else {
                    content_layout5.setVisibility( View.GONE );
                    no_data5.setVisibility( View.VISIBLE );
                }







            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }

    private void setTeam2last(String team2LastJson, String team2LastDes) {

        team2LastJson = team2LastJson.replace("\\\"","");

        try {
            JSONArray jsonArray = new JSONArray( team2LastJson );
            if(jsonArray.length() > 0) {


                Gson gson = new Gson();
                Type type = new TypeToken<List<History>>() {
                }.getType();
                List<History> list = gson.fromJson( jsonArray.toString(), type );
                if(list.size() > 0){

                    team1Last_name.setText( list.get( 0 ).getEventName() );

                    mList2.addAll( list );
                }


            }

            mAdapter2.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        team2last_text.setText( team2LastDes );
    }

    private void setTeam1last(String team1LastJson, String team1LastDes) {

        team1LastJson = team1LastJson.replace("\\\"","");

        try {
            JSONArray jsonArray = new JSONArray( team1LastJson );
            if(jsonArray.length() > 0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<History>>() {
                }.getType();
                List<History> list = gson.fromJson( jsonArray.toString(), type );
                if(list.size() > 0){

                    team2Last_name.setText( list.get( 0 ).getEventName() );
                    mList3.addAll( list );

                }


            }

            mAdapter3.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        team1last_text.setText( team1LastDes );

    }

    private void setHistoryContent(String teamHistoryVsJson, String teamHistoryVsDes) {


        teamHistoryVsJson = teamHistoryVsJson.replace("\\\"","");

        try {
            JSONArray jsonArray = new JSONArray( teamHistoryVsJson );
            if(jsonArray.length() > 0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<History>>() {
                }.getType();
                List<History> list = gson.fromJson( jsonArray.toString(), type );
                if(list.size() > 0){
                    mList1.addAll( list );
                }


            }

            mAdapter1.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        history_text.setText( teamHistoryVsDes );

    }

    private void setInjured(String teamInjuredJson) {

        teamInjuredJson = teamInjuredJson.replace("\\\"","");
        Injured_content.removeAllViews();

        try {
            JSONArray jsonArray = new JSONArray( teamInjuredJson );
            if(jsonArray.length() > 0){
                List<Injured> mlist1 = new ArrayList(  );
                List<Injured> mlist2 = new ArrayList(  );

                Gson gson = new Gson();
                Type type = new TypeToken<List<Injured>>(){}.getType();
                List<Injured> list = gson.fromJson(jsonArray.toString(),type);

                for(int i = 0; i< list.size(); i++){
                    Injured mInjured = list.get( i );
                    if(mInjured.getIsHome() == 1){
                        mlist1.add( mInjured );
                    }else {
                        mlist2.add( mInjured );
                    }

                }

                if(mlist1.size() > 0){

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.injured_item, null);
                    ImageView icon = ViewUtls.find( view,R.id.icon );
                    TextView name = ViewUtls.find( view,R.id.name );

                    name.setText( mlist1.get( 0 ).getTeamName() );
                    String url = "http://cdn.sportnanoapi.com/football/player/ " + mlist1.get( 0 ).getLogo();
                    ImageLoader.getInstance().displayImage(url, icon,options);

                    RecyclerView injured_recycler = ViewUtls.find( view,R.id.injured_recycler );
                    InjuredAdapter mInjuredAdapter = new InjuredAdapter(getActivity(),mlist1);

                    injured_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    injured_recycler.setAdapter(mInjuredAdapter);
                    injured_recycler.setNestedScrollingEnabled( false );
                    // 设置Item增加、移除动画
                    injured_recycler.setItemAnimator(new DefaultItemAnimator());
                    //添加Android自带的分割线
                    injured_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));


                    Injured_content.addView( view );
                }


                if(mlist2.size() > 0){

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.injured_item, null);
                    ImageView icon = ViewUtls.find( view,R.id.icon );
                    TextView name = ViewUtls.find( view,R.id.name );

                    name.setText( mlist2.get( 0 ).getTeamName() );
                    String url = "http://cdn.sportnanoapi.com/football/player/ " + mlist2.get( 0 ).getLogo();
                    ImageLoader.getInstance().displayImage(url, icon,options);

                    RecyclerView injured_recycler = ViewUtls.find( view,R.id.injured_recycler );
                    InjuredAdapter mInjuredAdapter = new InjuredAdapter(getActivity(),mlist2);

                    injured_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    injured_recycler.setAdapter(mInjuredAdapter);
                    injured_recycler.setNestedScrollingEnabled( false );
                    // 设置Item增加、移除动画
                    injured_recycler.setItemAnimator(new DefaultItemAnimator());
                    //添加Android自带的分割线
                   // injured_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));


                    Injured_content.addView( view );
                }



            }

            jifenAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setJiFenContent(String teamLianSaiJiFen) {
        teamLianSaiJiFen = teamLianSaiJiFen.replace("\\\"","");

        try {
            JSONArray jsonArray = new JSONArray( teamLianSaiJiFen );
            if(jsonArray.length() > 0){
                Gson gson = new Gson();
                Type type = new TypeToken<List<JiFen>>(){}.getType();
                List<JiFen> list = gson.fromJson(jsonArray.toString(),type);
                if(list.size() > 0){
                    jifenList.addAll( list );
                }

            }

            jifenAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setTeamGoal(String team1GoalJson, String team2GoalJson) {

        team1GoalJson = team1GoalJson.replace("\\\"","");
        team2GoalJson = team2GoalJson.replace("\\\"","");

        try {

            JSONArray jsonArray1 = new JSONArray( team1GoalJson );
            if(jsonArray1.length() > 0){
                Gson gson = new Gson();
                Type type = new TypeToken<List<TeamGoa>>(){}.getType();
                List<TeamGoa> list = gson.fromJson(jsonArray1.toString(),type);
                String name = "";
                int count = 0;
                for(int i = 0; i< list.size(); i++){
                    if(i < list.size()){
                        TeamGoa mTeamGoa  = list.get( i );
                        count += mTeamGoa.getCount();
                        name = mTeamGoa.getTeamName();
                        goal1Texts[i].setText(mTeamGoa.getCount() + "" );
                    }
                }

                goal1_name.setText( name );
                goal1_text.setText( count + "" );



            }




            JSONArray jsonArray2 = new JSONArray( team2GoalJson );
            if(jsonArray2.length() > 0){
                Gson gson = new Gson();
                Type type = new TypeToken<List<TeamGoa>>(){}.getType();
                List<TeamGoa> list = gson.fromJson(jsonArray2.toString(),type);
                String name = "";
                int count = 0;
                for(int i = 0; i< list.size(); i++){
                    if(i < list.size()){
                        TeamGoa mTeamGoa  = list.get( i );
                        count += mTeamGoa.getCount();
                        name = mTeamGoa.getTeamName();
                        goal2Texts[i].setText(mTeamGoa.getCount() + "" );
                    }
                }

                goal2_name.setText( name );
                goal2_text.setText( count + "" );

            }




        } catch (JSONException e) {
            e.printStackTrace();
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
