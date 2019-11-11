package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.JiShuTongJi;
import catc.tiandao.com.match.ben.Match;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.score.StatisticsFragment;
import catc.tiandao.com.match.utils.UmengUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntelligenceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntelligenceFragment extends Fragment implements View.OnClickListener,OnFragmentInteractionListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout rl_contianer;
    private PopupWindow popupWindow;

    private Button home_name, away_name;
    private TextView good_content,bad_content;

    private ImageView iv_collection;
    private ImageView home_Team_LogoUrl,away_Team_LogoUrl;
    private TextView match_Name,home_TeamName,home_score,away_score,match_status,away_Team_Name;


    // TODO: Rename and change types of parameters
    private int BallType;
    private String matchId;
    private int iCollection;
    private String shareTitle;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();


    private OnFragmentInteractionListener mListener;
    private GetFootballMatchDetail run;
    private FootballMatchCollectAndCancelRun setRun;

    private int showType = 0;
    private String team1Good,team2Good ,team1Bad ,team2Bad;

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
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    setParseData(result3);
                    break;

                default:
                    break;
            }
        }


    };


    public IntelligenceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param matchId Parameter 1.
     * @param matchId Parameter 2.
     * @return A new instance of fragment IntelligenceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntelligenceFragment newInstance(int BallType, String matchId) {
        IntelligenceFragment fragment = new IntelligenceFragment();
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
        View view = inflater.inflate( R.layout.fragment_intelligence, container, false );
        viewInfo(view);
        getData(matchId);
        return view;
    }



    private void viewInfo(View view) {
        ImageView iv_share = ViewUtls.find(getActivity(), R.id.iv_share);
        rl_contianer = ViewUtls.find(getActivity(), R.id.rl_container);

        iv_collection = (ImageView) getActivity().findViewById( R.id.iv_collection );
        match_Name = (TextView) getActivity().findViewById( R.id.match_Name );
        home_score = (TextView) getActivity().findViewById( R.id.home_score );
        away_score = (TextView) getActivity().findViewById( R.id.away_score );
        home_Team_LogoUrl = ViewUtls.find( getActivity(),R.id.home_Team_LogoUrl );
        away_Team_LogoUrl = ViewUtls.find( getActivity(),R.id.away_Team_LogoUrl );
        home_TeamName = ViewUtls.find( getActivity(),R.id.home_TeamName );
        home_score = ViewUtls.find( getActivity(),R.id.home_score );
        away_score = ViewUtls.find( getActivity(),R.id.away_score );
        away_Team_Name = ViewUtls.find( getActivity(),R.id.away_Team_Name );
        match_status = ViewUtls.find( getActivity(),R.id.match_status );


        home_name = ViewUtls.find( view,R.id.home_name );
        away_name = ViewUtls.find( view,R.id.away_name );
        good_content = ViewUtls.find( view,R.id.good_content );
        bad_content = ViewUtls.find( view,R.id.bad_content );

        iv_share.setOnClickListener( this );
        home_name.setOnClickListener( this );
        away_name.setOnClickListener( this );
        iv_collection.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_share:
                if(UserUtils.isLanded( getActivity())){
                    showShare();
                }else {
                    UserUtils.startLongin( getActivity());
                }

                break;

            case R.id.home_name:

                if(showType != 0){
                    showType = 0;
                    setContent(showType);
                }

                break;

            case R.id.away_name:

                if(showType != 1){
                    showType = 2;
                    setContent(showType);
                }

                break;
            case R.id.iv_collection:

                if(UserUtils.isLanded( getActivity() )){
                    FootballMatchCollectAndCancel();
                }else {
                    UserUtils.startLongin( getActivity() );
                }


                break;

        }

    }

    private void showShare() {
        //分享
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share, null);
            popupWindow = new PopupWindow(getActivity());
            popupWindow.setContentView(contentView);
            popupWindow.setAnimationStyle(R.style.bottomShowAnimStyle);
            popupWindow.setWidth( LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1f);
                }
            });

            contentView.findViewById(R.id.iv_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare( SHARE_MEDIA.QZONE);
                    popupWindow.dismiss();
                }
            });
            contentView.findViewById(R.id.iv_moment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    popupWindow.dismiss();
                }
            });



            contentView.findViewById(R.id.iv_wiexin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN);
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rl_contianer, Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.5f);
    }

    /***设置背景透明度*/
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    /***分享*/
    private void singleShare(SHARE_MEDIA shareMedia) {
       /* 新闻网址：http://www.leisuvip1.com/New/Index? token=**&newId=新闻id
        分享-足球网址：http://www.leisuvip1.com/New/Football? matchId=比赛id
        分享-篮球网址：http://www.leisuvip1.com/New/ Basketball? matchId=比赛id*/

        String url;
        if(BallType == 0){
            url = "http://www.leisuvip1.com/New/Football?matchId=" + matchId;
        }else {
            url = "http://www.leisuvip1.com/New/Basketball?matchId=" + matchId;
        }


        int logoResId = R.mipmap.app_icon;

        UmengUtil.shareSinglePlatform(getActivity(), shareMedia, url, shareTitle, logoResId, shareTitle);
    }



    private void FootballMatchCollectAndCancel() {

        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("matchId", matchId);

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

    @Override
    public void onFragmentInteraction(Uri uri) {

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



    private void getData(String matchId) {

        //url：http:// 域名/LSQB/ GetFootballMatchDetail_qingBaoFenXi? token=***& matchId=比赛id

        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

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
                methodName = HttpUtil.GET_FOOTBALL_MATCH_DETAIL;
            }else {
                methodName = HttpUtil.GET_BASKETBALL_MATCHDETAIL;
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

//

            if(code == 0) {

                JSONObject data = obj.optJSONObject( "data" );

                shareTitle = data.optString( "shareTitle" );
                String eventName = data.optString( "eventName" );

                String dt = data.optString( "dt" );
                String team1Name = data.optString( "team1Name" );
                String team1Logo = data.optString( "team1Logo" );
                int team1Score = data.optInt( "team1Score" );
                iCollection = data.optInt( "iCollection" );
                team1Good = data.optString( "team1Good" );
                team1Bad = data.optString( "team1Bad" );


                if(iCollection == 0){
                    iv_collection.setImageResource( R.mipmap.navbar_icon_collect_white_default );
                }else {
                    iv_collection.setImageResource( R.mipmap.icon_collect );
                }



                if(team1Good == null || team1Good.equals( "" ) || team1Good.equals( "null" )){
                    team1Good = "暂无数据";
                }

                if(team1Bad == null || team1Bad.equals( "" ) || team1Bad.equals( "null" )){
                    team1Bad = "暂无数据";
                }

                String team2Name = data.optString( "team2Name" );
                String team2Logo = data.optString( "team2Logo" );
                int team2Score = data.optInt( "team2Score" );
                team2Good = data.optString( "team2Good" );
                team2Bad = data.optString( "team2Bad" );

                if(team2Good == null || team2Good.equals( "" ) || team2Good.equals( "null" )){
                    team2Good = "暂无数据";
                }

                if(team2Bad == null || team2Bad.equals( "" ) || team2Bad.equals( "null" )){
                    team2Bad = "暂无数据";
                }


                if(BallType == 0){
                    int iTurn = data.optInt( "iTurn" ,0);
                    if(iTurn > 0){
                        match_Name.setText( dt + " | " +  eventName + "第" +iTurn + "轮");
                    }else {
                        match_Name.setText( dt + " | " +  eventName);
                    }
                }else {
                    match_Name.setText( dt + " | " +  eventName);
                }

                home_score.setText( team1Score + "" );
                away_score.setText( team2Score + "" );

                home_TeamName.setText( team1Name );
                away_Team_Name.setText( team2Name );
               // match_status.setText( mMatch.getMatchStatus() );

                ImageLoader.getInstance().displayImage(team1Logo, home_Team_LogoUrl,options);
                ImageLoader.getInstance().displayImage(team2Logo, away_Team_LogoUrl,options);

                home_name.setText( team1Name );
                away_name.setText( team2Name );

                ((MatchDetailsActivity)getActivity()).setTitleContent( team1Logo,team2Logo,team1Name,team2Name );

                setContent(showType);


            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }





    private void setContent(int showType) {


        if(showType == 0){

            home_name.setTextColor( ContextCompat.getColor( getContext(),R.color.white ) );
            away_name.setTextColor( ContextCompat.getColor( getContext(),R.color.text6 ) );
            home_name.setBackgroundResource( R.mipmap.home_nam_bg );
            away_name.setBackgroundResource( R.mipmap.away_name_bg );
            good_content.setText( team1Good );
            bad_content.setText( team1Bad );

        }else {


            home_name.setTextColor( ContextCompat.getColor( getContext(),R.color.text6 ) );
            away_name.setTextColor( ContextCompat.getColor( getContext(),R.color.white ) );
            home_name.setBackgroundResource( R.mipmap.away_name_bg );
            away_name.setBackgroundResource( R.mipmap.home_nam_bg );

            good_content.setText( team2Good );
            bad_content.setText( team2Bad );
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
