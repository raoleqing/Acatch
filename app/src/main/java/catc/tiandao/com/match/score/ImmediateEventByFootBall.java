package catc.tiandao.com.match.score;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.ImmediateAdapter;
import catc.tiandao.com.match.adapter.ImmediateByFootballAdapter;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.BasketJiShuTongJi;
import catc.tiandao.com.match.ben.JiShiBasket;
import catc.tiandao.com.match.ben.JiShiShiJian;
import catc.tiandao.com.match.ben.JiShiShiJianByBasket;
import catc.tiandao.com.match.ben.JiShuTongJi;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 *即时事件
 */
public class ImmediateEventByFootBall extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView ie_recycler1;
    private TextView no_data;


    private ImmediateByFootballAdapter mAdapter1;
    private ImmediateAdapter mAdapter2;
    private List<JiShiShiJian> mList = new ArrayList();
    private List<JiShiShiJianByBasket> mList1 = new ArrayList();


    // TODO: Rename and change types of parameters
    private int BallType;
    private String BallId;

    private GetFootballScoreDetail run;
    private GetBasketballScoreDetail basketRun;

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

                case 0x002:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    basketBallParseData(result2);
                    break;

                case 0x004:
                    getImmediateData();
                    break;

                default:
                    break;
            }
        }


    };

    public ImmediateEventByFootBall() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param BallType Parameter 1.
     * @param BallId Parameter 2.
     * @return A new instance of fragment ImmediateEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static ImmediateEventByFootBall newInstance(int BallType, String BallId) {
        ImmediateEventByFootBall fragment = new ImmediateEventByFootBall();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.football_immediate_event, container, false );
        viewInfo(view);
        getImmediateData();
        return view;
    }




    private void viewInfo(View view) {

        ie_recycler1 = ViewUtls.find( view,R.id.ie_recycler1 );
        no_data = ViewUtls.find( view,R.id.no_data );

        if(BallType == 0){
            setEventToend1();
        }else {
            setEventToend2();
        }

    }


    public void upData() {

        getImmediateData();
    }

    private void getImmediateData() {

        if(BallType == 0){
            getData();
        }else {
            GetBasketball();
        }
    }




    private void setEventToend1() {

        ie_recycler1.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter1 = new ImmediateByFootballAdapter(getActivity(),mList);
        mAdapter1.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        ie_recycler1.setNestedScrollingEnabled( false );
        ie_recycler1.setAdapter(mAdapter1);
        // 设置Item增加、移除动画
        ie_recycler1.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //statistics_recycler1.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));
    }


    private void setEventToend2() {

        ie_recycler1.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter2 = new ImmediateAdapter(getActivity(),mList1);
        mAdapter2.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        ie_recycler1.setNestedScrollingEnabled( false );
        ie_recycler1.setAdapter(mAdapter2);
        // 设置Item增加、移除动画
        ie_recycler1.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        //statistics_recycler1.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));
    }


    private void getData() {


        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
               // mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
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

            HttpUtil.post( getActivity(),HttpUtil.GET_FOOTBALL_SHIJIAN ,param,new HttpUtil.HttpUtilInterface(){
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

        myHandler.sendEmptyMessageDelayed( 0x004,15000 );

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

                JSONObject data = obj.optJSONObject( "data");
                String livel = data.optString( "liveL" );
                if(livel != null && !livel.equals( "null" )){
                    livel = livel.replace("\\\"","");
                    JSONArray liveLObj = new JSONArray( livel );
                    mList.clear();

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<JiShiShiJian>>(){}.getType();
                    List<JiShiShiJian> list = gson.fromJson(liveLObj.toString(),type);
                    if(list.size() > 0){
                        mList.addAll( list );
                    }else {
                        no_data.setVisibility( View.VISIBLE );
                    }


                }else {

                    no_data.setVisibility( View.VISIBLE );
                }


                mAdapter1.notifyDataSetChanged();

            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }



    private void GetBasketball() {


        try{
            if (CheckNet.isNetworkConnected( getActivity())) {
                //mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
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

            HttpUtil.post( getActivity(),HttpUtil.GET_BASKETBALL_SHIJIAN ,param,new HttpUtil.HttpUtilInterface(){
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

        myHandler.sendEmptyMessageDelayed( 0x004,15000 );

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
               JSONObject data = obj.optJSONObject( "data");
               String livel = data.optString( "liveL" );

                mList1.clear();


               if(livel != null && !livel.equals( "null" ) && livel.length() > 0){
                   livel = livel.replace("\\\"","");
                   JSONArray liveLObj = new JSONArray( livel );



                   if(liveLObj != null && liveLObj.length() > 0){
                       for(int i = 0; i< liveLObj.length(); i++){
                           JiShiShiJianByBasket mJiShiShiJianByBasket = new JiShiShiJianByBasket();
                           JSONArray array = liveLObj.optJSONArray( i );

                           Gson gson = new Gson();
                           Type type = new TypeToken<List<JiShiBasket>>(){}.getType();
                           List<JiShiBasket> list = gson.fromJson(array.toString(),type);
                           if(list.size() > 0){
                               mJiShiShiJianByBasket.setmList( list );
                               mList1.add( mJiShiShiJianByBasket );
                           }
                       }


                       mAdapter2.notifyDataSetChanged();

                   }else {
                       no_data.setVisibility( View.VISIBLE );
                   }
               }else {
                   no_data.setVisibility( View.VISIBLE );
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
