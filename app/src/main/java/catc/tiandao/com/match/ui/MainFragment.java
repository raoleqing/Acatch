package catc.tiandao.com.match.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.MainAutoSwitchAdapter;
import catc.tiandao.com.match.adapter.SampleAdapter;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.BallFragmentBen;
import catc.tiandao.com.match.ben.Banner;
import catc.tiandao.com.match.ben.MainNewsBen;
import catc.tiandao.com.match.ben.MatchNew;
import catc.tiandao.com.match.ben.NewsBen;
import catc.tiandao.com.match.ben.ZhenRong;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.my.LoginActivity;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.ui.event.PopularActivity;
import catc.tiandao.com.match.ui.expert.ExpertActivity;
import catc.tiandao.com.match.ui.news.NewsActivity;
import catc.tiandao.com.match.utils.DES;
import catc.tiandao.com.match.utils.DeviceUtils;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.match.widgets.loopswitch.AutoSwitchView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AutoSwitchView ls_top;
    private RecyclerView mRecyclerView;
    private ConstraintLayout mConstraintLayout;

    private LinearLayout popular,expert,news;
    private TextView group_more;
    private LinearLayout item_content;

    private List<Banner> bannerArray = new ArrayList<Banner>();
    private MainAutoSwitchAdapter lsTopAdapter = null;


    private List<AreaMatch> mlist = new ArrayList();
    private SampleAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private GetHomeHeadNewRun run;
    private GetChangedNewRun changedRun;
    private GetHomeFootballDataRun footballRun;
    private GetBasketballDataRun basketBallRun;
    private int pageSize = 10;
    private int page = 1;


    private DisplayImageOptions options;

    private boolean isGetData1 = false;
    private boolean isGetData2 = false;
    private boolean isGetData3 = false;



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
                    changedParseData(result2);
                    break;
                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    footballParseData(result3);
                    break;
                case 0x004:
                    Bundle bundle4 = msg.getData();
                    String result4 = bundle4.getString("result");
                    basketParseData(result4);

                    break;
                default:
                    break;
            }
        }


    };

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }

        EventBus.getDefault().register(this);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading( R.mipmap.mall_cbg )          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg )  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg )       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer( UnitConverterUtils.dip2px( getActivity(),6 )))  // 设置成圆角图片
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );
        viewInfo(view);
        getData(0);
        return view;
    }

    private void getData(int type) {
        if(!isGetData1){
            GetHomeHeadNew();
        }

        if(!isGetData2){
            GetHomeFootballData();
        }

        if(!isGetData3){
            GetChangedNew();
        }

    }


    private void viewInfo(View view) {

        ls_top = ViewUtls.find(view, R.id.ls_top);
        mRecyclerView = ViewUtls.find(view, R.id.match_recycler);
        popular = ViewUtls.find(view, R.id.popular);
        expert = ViewUtls.find(view, R.id.expert);
        news = ViewUtls.find(view, R.id.news);
        group_more = ViewUtls.find(view, R.id.group_more);
        item_content = ViewUtls.find(view, R.id.item_content);

        popular.setOnClickListener( this );
        expert.setOnClickListener( this );
        news.setOnClickListener( this );
        group_more.setOnClickListener( this );


        // 设置广告的高度
        ViewGroup.LayoutParams lp = ls_top.getLayoutParams();
        int width = UnitConverterUtils.getDeviceWidth(getActivity());
        int height = (int)(width * 0.6);
        lp.height = height;
        ls_top.setLayoutParams(lp);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SampleAdapter(getActivity(),mlist);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

                AreaMatch mAreaMatch = mlist.get( postion );

               switch (view.getId()){
                   case R.id.group_item_more:

                       Intent intent01 = new Intent(getActivity(), PopularActivity.class);
                       intent01.putExtra( PopularActivity.AREA_ID, mAreaMatch.getAreaId());
                       if(mAreaMatch.getAreaName().equals( "篮球赛事" )){
                           intent01.putExtra( PopularActivity.BALL_TYPE, 1);
                       }else {
                           intent01.putExtra( PopularActivity.BALL_TYPE, 0);
                       }
                       startActivity(intent01);
                       ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                       break;

               }
            }
        } );

        mRecyclerView.setNestedScrollingEnabled( false );
        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.popular:

                Intent intent01 = new Intent(getActivity(), PopularActivity.class);
                startActivity(intent01);
                ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.expert:
                Intent intent02 = new Intent(getActivity(), ExpertActivity.class);
                startActivity(intent02);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;
            case R.id.news:
            case R.id.group_more:
                Intent intent03 = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent03);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;


        }

    }




    private void setAdvManage() {

        lsTopAdapter = new MainAutoSwitchAdapter(getActivity(),bannerArray);
        ls_top.setAdapter(lsTopAdapter);

    }


    /**
     * 头部新闻
     * **/
    private void GetHomeHeadNew() {

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                run = new GetHomeHeadNewRun(param);
                ThreadPoolManager.getsInstance().execute(run);

            } else {

                isGetData1 = false;
                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            isGetData1 = false;
            e.printStackTrace();
        }
    }


    /**
     *
     * */
    class GetHomeHeadNewRun implements Runnable{
        private HashMap<String, String> param;

        GetHomeHeadNewRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( getActivity(),HttpUtil.GET_HOME_HEADNEW,param,new HttpUtil.HttpUtilInterface(){
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
            return;
        }

        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );



            if(code == 0) {

                isGetData1 = true;

                bannerArray.clear();

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Banner>>(){}.getType();
                    List<Banner> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        bannerArray.addAll( list );
                    }
                }

                setAdvManage();

                lsTopAdapter.notifyDataSetChanged();



            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //获取首页足球数据
    private void GetHomeFootballData() {

        if (CheckNet.isNetworkConnected( getActivity())) {

            //http:// 域名/LSQB/ GetHomeHeadNew? token=***& pageSize=每页条数&page=第几页
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

            footballRun = new GetHomeFootballDataRun();
            ThreadPoolManager.getsInstance().execute(footballRun);

        } else {
            Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *
     * */
    class GetHomeFootballDataRun implements Runnable{


        @Override
        public void run() {

            HttpUtil.get( getActivity(),HttpUtil.GET_HOME_FOOTBALL_DATA,new HttpUtil.HttpUtilInterface(){
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



    private void footballParseData(String result) {

        GetBasketballData();

        if(result == null){
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try{


            System.out.println("足球:" +  result );

            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            if(code == 0) {
                mlist.clear();
                isGetData2 = true;
                JSONArray data = obj.optJSONArray( "data" );
                Gson gson = new Gson();
                Type type = new TypeToken<List<AreaMatch>>(){}.getType();
                List<AreaMatch> list = gson.fromJson(data.toString(),type);
                if(list.size() > 0){
                    mlist.addAll( list );
                }

                mAdapter.notifyDataSetChanged();


            }




        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }



    private void GetBasketballData() {

        if (CheckNet.isNetworkConnected( getActivity())) {

            basketBallRun = new GetBasketballDataRun();
            ThreadPoolManager.getsInstance().execute(basketBallRun);

        } else {
            Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *
     * */
    class GetBasketballDataRun implements Runnable{


        @Override
        public void run() {

            HttpUtil.get( getActivity(),HttpUtil.GET_BASKETBALL_DATA,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x004;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void basketParseData(String result) {


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
                Gson gson = new Gson();
                Type type = new TypeToken<List<MatchNew>>(){}.getType();
                List<MatchNew> list = gson.fromJson(data.toString(),type);
                if(list.size() > 0){
                    AreaMatch mAreaMatch = new AreaMatch();
                    mAreaMatch.setAreaName( "篮球赛事" );
                    mAreaMatch.setMatchL( list );
                    mlist.add( mAreaMatch );

                }

                mAdapter.notifyDataSetChanged();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }




    //获取转会与交易新闻
    private void GetChangedNew() {

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {


                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                changedRun = new GetChangedNewRun(param);
                ThreadPoolManager.getsInstance().execute(changedRun);

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
    class GetChangedNewRun implements Runnable{
        private HashMap<String, String> param;

        GetChangedNewRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( getActivity(),HttpUtil.GET_CHANGED_NEW,param,new HttpUtil.HttpUtilInterface(){
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



    private void changedParseData(String result) {

        if(result == null){
            return;
        }

        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            //Self referencing loop detected with type 'Castle.Proxies.UploadFileProxy'. Path 'data[0].TitleImageFile.BaseFold.UploadFile'


            if(code == 0) {

                isGetData3 = true;

                JSONArray data = obj.optJSONArray( "data" );
                Gson gson = new Gson();
                Type type = new TypeToken<List<MainNewsBen>>(){}.getType();
                List<MainNewsBen> list = gson.fromJson(data.toString(),type);

                item_content.removeAllViews();

                for(int i = 0; i< list.size(); i++) {

                    MainNewsBen mMainNewsBen = list.get( i );


                    View view =LayoutInflater.from( getActivity() ).inflate( R.layout.match_new_item, null );
                    TextView item_title = ViewUtls.find( view, R.id.item_title );
                    TextView item_time = ViewUtls.find( view, R.id.item_time );
                    ImageView item_image = ViewUtls.find( view, R.id.item_image );
                    TextView Topping = ViewUtls.find( view,R.id.Topping );

                    if(i == 0){
                        Topping.setVisibility( View.VISIBLE );
                    }else {
                        Topping.setVisibility( View.GONE);
                    }

                    if(Constant.isData( mMainNewsBen.getTitleImageUrl() )){
                        item_image.setVisibility( View.VISIBLE );
                        ImageLoader.getInstance().displayImage( mMainNewsBen.getTitleImageUrl(), item_image, options );
                    }else {
                        item_image.setVisibility( View.GONE );
                    }



                    view.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                        }
                    } );

                    item_content.addView( view );
                }

            }

        }catch (Exception e){
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

    @Subscribe
    public void onEvent(Object event) {
        if (Constant.APP_NET_SUCCESS.equals(event)) {
            getData(1);
        }else if (Constant.UP_MAIN.equals(event)){
            isGetData1 = false;
            isGetData2 = false;
            isGetData3 = false;

            getData(2);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        EventBus.getDefault().unregister(this);

        ThreadPoolManager.getsInstance().cancel(run);
        ThreadPoolManager.getsInstance().cancel(changedRun);
        ThreadPoolManager.getsInstance().cancel(footballRun);
        ThreadPoolManager.getsInstance().cancel(basketBallRun);
    }


}
