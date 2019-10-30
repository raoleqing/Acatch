package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import catc.tiandao.com.match.adapter.ExpertAdapter;
import catc.tiandao.com.match.adapter.ZhiShuAdapter;
import catc.tiandao.com.match.ben.Expert;
import catc.tiandao.com.match.ben.ExponentBen;
import catc.tiandao.com.match.ben.ZhiShu;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.expert.ExpertActivity;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExponentFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 指数
 */
public class ExponentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tab_bar1, tab_bar2, tab_bar3,no_data;
    private int contentIndex = 0;
    private RecyclerView myRecyclerView1,myRecyclerView2,myRecyclerView3;

    // TODO: Rename and change types of parameters
    private int BallType;
    private String matchId;

    private OnFragmentInteractionListener mListener;

    GetFootballMatchDetail run;

    private ZhiShuAdapter mZhiShuAdapter1;
    private ZhiShuAdapter mZhiShuAdapter2;
    private ZhiShuAdapter mZhiShuAdapter3;
    private List<ZhiShu> mList1 = new ArrayList(  );
    private List<ZhiShu> mList2 = new ArrayList(  );
    private List<ZhiShu> mList3 = new ArrayList(  );


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

    public ExponentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExponentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExponentFragment newInstance(int BallType, String matchId) {
        ExponentFragment fragment = new ExponentFragment();
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
        View view = inflater.inflate( R.layout.fragment_exponent, container, false );
        viewInfo(view);
        getData( matchId );
        return view;
    }

    private void viewInfo(View view) {

        tab_bar1 = ViewUtls.find( view,R.id.tab_bar1 );
        tab_bar2 = ViewUtls.find( view,R.id.tab_bar2 );
        tab_bar3 = ViewUtls.find( view,R.id.tab_bar3 );
        no_data = ViewUtls.find( view,R.id.no_data );
        myRecyclerView1 = ViewUtls.find( view,R.id.myRecyclerView1 );
        myRecyclerView2 = ViewUtls.find( view,R.id.myRecyclerView2 );
        myRecyclerView3 = ViewUtls.find( view,R.id.myRecyclerView3 );

        tab_bar1.setOnClickListener( this );
        tab_bar2.setOnClickListener( this );
        tab_bar3.setOnClickListener( this );



        setRecyclerView1();
        setRecyclerView2();
        setRecyclerView3();


    }

    private void setRecyclerView1() {

        myRecyclerView1.setLayoutManager(new LinearLayoutManager( getActivity() ) );
        myRecyclerView1.setHasFixedSize(true);
        myRecyclerView1.setNestedScrollingEnabled(false);

        mZhiShuAdapter1 = new ZhiShuAdapter(getContext(),mList1);
        // 设置adapter
        myRecyclerView1.setAdapter(mZhiShuAdapter1);
        // 设置Item增加、移除动画
        myRecyclerView1.setItemAnimator(new DefaultItemAnimator());


    }


    private void setRecyclerView2() {

        myRecyclerView2.setLayoutManager(new LinearLayoutManager( getActivity() ) );
        myRecyclerView2.setHasFixedSize(true);
        myRecyclerView2.setNestedScrollingEnabled(false);

        mZhiShuAdapter2 = new ZhiShuAdapter(getContext(),mList2);
        // 设置adapter
        myRecyclerView2.setAdapter(mZhiShuAdapter2);
        // 设置Item增加、移除动画
        myRecyclerView2.setItemAnimator(new DefaultItemAnimator());


    }


    private void setRecyclerView3() {

        myRecyclerView3.setLayoutManager(new LinearLayoutManager( getActivity() ) );
        myRecyclerView3.setHasFixedSize(true);
        myRecyclerView3.setNestedScrollingEnabled(false);

        mZhiShuAdapter3= new ZhiShuAdapter(getContext(),mList3);
        // 设置adapter
        myRecyclerView3.setAdapter(mZhiShuAdapter1);
        // 设置Item增加、移除动画
        myRecyclerView3.setItemAnimator(new DefaultItemAnimator());


    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tab_bar1:

                if (contentIndex != 0){
                    setViewContent(0);
                }

                break;
            case R.id.tab_bar2:

                if (contentIndex != 1){
                    setViewContent(1);
                }

                break;
            case R.id.tab_bar3:

                if (contentIndex != 2){
                    setViewContent(2);
                }

                break;


        }

    }

    private void setViewContent(int i) {

        contentIndex = i;

        int bg01 = R.mipmap.home_nam_bg;
        int bg02 = R.mipmap.away_name_bg;
        int text1 = ContextCompat.getColor(getActivity(),R.color.white);
        int text2 = ContextCompat.getColor(getActivity(),R.color.text6);

        switch (i) {
            case 0:
                tab_bar1.setBackgroundResource( bg01 );
                tab_bar2.setBackgroundResource( bg02 );
                tab_bar3.setBackgroundResource( bg02 );
                tab_bar1.setTextColor( text1 );
                tab_bar2.setTextColor( text2 );
                tab_bar3.setTextColor( text2 );

                myRecyclerView1.setVisibility( View.VISIBLE );
                myRecyclerView2.setVisibility( View.GONE );
                myRecyclerView3.setVisibility( View.GONE );

                if(mList1.size()> 0){
                    no_data.setVisibility( View.GONE );
                }else {
                    no_data.setVisibility( View.VISIBLE );
                }

                break;
            case 1:

                tab_bar1.setBackgroundResource( bg02 );
                tab_bar2.setBackgroundResource( bg01 );
                tab_bar3.setBackgroundResource( bg02 );
                tab_bar1.setTextColor( text2 );
                tab_bar2.setTextColor( text1 );
                tab_bar3.setTextColor( text2 );

                myRecyclerView1.setVisibility( View.GONE );
                myRecyclerView2.setVisibility( View.VISIBLE );
                myRecyclerView3.setVisibility( View.GONE );

                if(mList2.size()> 0){
                    no_data.setVisibility( View.GONE );
                }else {
                    no_data.setVisibility( View.VISIBLE );
                }


                break;
            case 2:
                tab_bar1.setBackgroundResource( bg02 );
                tab_bar2.setBackgroundResource( bg02 );
                tab_bar3.setBackgroundResource( bg01 );

                tab_bar1.setTextColor( text2 );
                tab_bar2.setTextColor( text2 );
                tab_bar3.setTextColor( text1 );

                myRecyclerView1.setVisibility( View.GONE );
                myRecyclerView2.setVisibility( View.GONE );
                myRecyclerView3.setVisibility( View.VISIBLE );

                if(mList3.size()> 0){
                    no_data.setVisibility( View.GONE );
                }else {
                    no_data.setVisibility( View.VISIBLE );
                }



                break;

            default:
                break;
        }


    }




    private void getData(String matchId) {

        //http:// 域名/LSQB/ GetFootballMatchDetail_zhiShu? token=***& matchId=比赛id


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
                methodName = HttpUtil.GET_FOOTBALL_MATCHDETAIL_ZHISHU;
            }else {
                methodName = HttpUtil.GET_BASKETBALL_MATCHDETAIL_ZHISHU;
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


            if(code == 0) {

                //{"code":0,"message":"成功","data":{"asia":[],"eu":[],"bs":[]}}

                JSONObject data = obj.optJSONObject( "data" );

                JSONArray asia = data.optJSONArray( "asia" );
                if(asia.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ZhiShu>>(){}.getType();
                    List<ZhiShu> list = gson.fromJson(asia.toString(),type);
                    if(list.size() > 0){
                        mList1.addAll( list );
                    }

                    mZhiShuAdapter1.notifyDataSetChanged();
                    no_data.setVisibility( View.GONE );
                }else {
                    no_data.setVisibility( View.VISIBLE );
                }





                JSONArray eu = data.optJSONArray( "eu" );

                if(eu.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ZhiShu>>(){}.getType();
                    List<ZhiShu> list = gson.fromJson(eu.toString(),type);
                    if(list.size() > 0){
                        mList2.addAll( list );
                    }

                    mZhiShuAdapter2.notifyDataSetChanged();

                }


                JSONArray bs = data.optJSONArray( "bs" );

                if(bs.length() > 0 ){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<ZhiShu>>(){}.getType();
                    List<ZhiShu> list = gson.fromJson(bs.toString(),type);
                    if(list.size() > 0){
                        mList3.addAll( list );
                    }

                    mZhiShuAdapter3.notifyDataSetChanged();

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
