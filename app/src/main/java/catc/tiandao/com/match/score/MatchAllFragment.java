package catc.tiandao.com.match.score;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.MatchAllAdapter;
import catc.tiandao.com.match.adapter.SelectAdapter;
import catc.tiandao.com.match.ben.FootballEvent;
import catc.tiandao.com.match.ben.FootballEventAll;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.GridSpacingItemDecoration;
import catc.tiandao.com.match.common.MyGridLayoutManager;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchAllFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView select_recycler;
    private TextView select_all;
    private TextView Inverse_selection;
    private TextView selection_number;
    private TextView submit;


    private MatchAllAdapter mAdapter;
    private List<FootballEvent> mList = new ArrayList();
    private GetAllFootballEventRun run;
    private SetUser2EventRun setRun;

    // TODO: Rename and change types of parameters
    private int areaId;
    private int iUserChoose;
    private int number;

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
                    mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                    break;
                case 0x003:
                    mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
                    break;
                case 0x004:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    setParseData(result2);
                    break;
                default:
                    break;
            }
        }


    };

    public MatchAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param areaId Parameter 1.
     * @param iUserChoose Parameter 2.
     * @return A new instance of fragment MatchAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchAllFragment newInstance(int areaId, int iUserChoose) {
        MatchAllFragment fragment = new MatchAllFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, areaId );
        args.putInt( ARG_PARAM2, iUserChoose );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            areaId = getArguments().getInt( ARG_PARAM1 );
            iUserChoose = getArguments().getInt( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_match_all, container, false );
        ViewInfo(view);
        getData();
        return view;
    }

    private void ViewInfo(View view) {

        select_recycler = ViewUtls.find( view ,R.id.select_recycler );

        select_recycler = ViewUtls.find( view ,R.id.select_recycler );

        select_all = ViewUtls.find( view ,R.id.select_all );
        Inverse_selection = ViewUtls.find( view ,R.id.Inverse_selection );
        selection_number = ViewUtls.find( view ,R.id.selection_number );
        submit = ViewUtls.find( view ,R.id.submit );

        select_all.setOnClickListener( this );
        Inverse_selection.setOnClickListener( this );
        submit.setOnClickListener( this );


        RecyclerView.LayoutManager mLayoutManager = new MyGridLayoutManager(getActivity(), 3);
        select_recycler.setLayoutManager(mLayoutManager);
        mAdapter = new MatchAllAdapter( getActivity(),mList );
        // 设置adapter
        select_recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {
                FootballEvent mFootballEvent = mList.get( postion );
                if(mFootballEvent.getiUserChoose() == 0){
                    mList.get( postion ).setiUserChoose( 1 );
                    number --;
                }else {
                    mList.get( postion ).setiUserChoose( 0 );
                    number ++;
                }
                selection_number.setText( "隐藏了"+ number+"场" );

                mAdapter.notifyDataSetChanged();

            }
        } );

        int space = UnitConverterUtils.dip2px(getActivity(), 15);
        int space1 = UnitConverterUtils.dip2px(getActivity(), 20);
        select_recycler.addItemDecoration(new GridSpacingItemDecoration(3,space,space1,false));


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.select_all:
                selectAll();
                break;
            case R.id.Inverse_selection:
                InverseSselection();
                break;
            case R.id.submit:

                //url：http:// 域名/LSQB/ SetUser2Event? token=***& areaId=区域id&eventIds=赛事id
                SetUser2Event();

                break;
        }

    }

    private void SetUser2Event() {

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

                String eventIds = getEventIds();

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("areaId",  "0");
                param.put("eventIds", eventIds);

                setRun = new SetUser2EventRun(param);
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
    class SetUser2EventRun implements Runnable{
        private HashMap<String, String> param;

        SetUser2EventRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( getActivity(),HttpUtil.SET_USER2_EVENT,param,new HttpUtil.HttpUtilInterface(){
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

                EventBus.getDefault().post( Constant.UP_BALL);
                getActivity().onBackPressed();


            }

            Toast.makeText( getActivity(),message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }



    private String getEventIds() {

        String eventIds = "";

        for(int i = 0; i< mList.size(); i++){
            FootballEvent mFootballEvent = mList.get( i );
            if(mFootballEvent.getiUserChoose() == 1){
                eventIds += mFootballEvent.getId() + ",";
            }

        }

        if(eventIds.length() > 0){
            eventIds = eventIds.substring( 0,eventIds.length() - 1 );
        }

        return eventIds;

    }

    private void selectAll() {

        number = 0;

        myHandler.sendEmptyMessage( 0x002 );

        for(int i = 0; i< mList.size(); i++){

            mList.get( i ).setiUserChoose( 1 );

        }

        selection_number.setText( "隐藏了"+ number+"场" );


        mAdapter.notifyDataSetChanged();
        myHandler.sendEmptyMessage( 0x003 );

    }



    private void InverseSselection() {

        number = 0;

        myHandler.sendEmptyMessage( 0x002 );

        for(int i = 0; i< mList.size(); i++){
            FootballEvent mFootballEvent = mList.get( i );
            if(mFootballEvent.getiUserChoose() == 0){
                mList.get( i ).setiUserChoose( 1 );
            }else {
                mList.get( i ).setiUserChoose( 0 );
                number ++;
            }

        }

        selection_number.setText( "隐藏了"+ number+"场" );

        mAdapter.notifyDataSetChanged();
        myHandler.sendEmptyMessage( 0x003 );


    }


    private void getNumber() {

        number = 0;


        for(int i = 0; i< mList.size(); i++){
            FootballEvent mFootballEvent = mList.get( i );
            if(mFootballEvent.getiUserChoose() == 0){
                number ++;
            }

        }

        selection_number.setText( "隐藏了"+ number+"场" );


    }


    private void getData() {

       /* iUserChoose ：1-选，0-未选
        url：http:// 域名/LSQB/ GetAllFootballEvent? token=***&areaId=区域*/

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("areaId", areaId + "");

                run = new GetAllFootballEventRun(param);
                ThreadPoolManager.getsInstance().execute(run);

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
    class GetAllFootballEventRun implements Runnable{
        private HashMap<String, String> param;

        GetAllFootballEventRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


                    HttpUtil.post( getActivity(),HttpUtil.GET_ALL_FOOTBALL_EVENT,param,new HttpUtil.HttpUtilInterface(){
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

                mList.clear();

                JSONArray data = obj.optJSONArray( "data" );
                if(data.length() > 0){
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<FootballEvent>>(){}.getType();
                    List<FootballEvent> list = gson.fromJson(data.toString(),type);
                    if(list.size() > 0){
                        mList.addAll( list );
                    }

                }


                mAdapter.notifyDataSetChanged();

                getNumber();

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
