package catc.tiandao.com.match.ui.news;

import android.content.Context;
import android.content.Intent;
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
import catc.tiandao.com.match.adapter.NewsAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.ben.NewsBen;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.score.BallFragment;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView ball_recycler;
    private TextView no_data;

    private LinearLayoutManager mLinearLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBen> mList = new ArrayList(  );

    private String[] types = {"推荐","赛场花絮","体育新闻","体育边料"};


    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private GetNewByType run;
    private int pageSize = 10;
    private int page = 1;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;

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
                default:
                    break;
            }
        }


    };

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(int param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_PARAM1, param1 );
        args.putString( ARG_PARAM2, param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {
            mParam1 = getArguments().getInt( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_news, container, false );
        viewInfo(view);
        getData();
        return view;
    }


    private void viewInfo(View view) {

        ball_recycler = ViewUtls.find( view,R.id.ball_recycler );
        no_data = ViewUtls.find( view,R.id.no_data );


        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        ball_recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NewsAdapter(getActivity(),mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

                Intent intent02 = new Intent(getActivity(), NewsDetailsActivity.class);
                startActivity(intent02);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

            }
        });
        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }


    private void getData() {

        isRun = true;

        try{

            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));

                String type = types[mParam1];

                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity()) );
                param.put("type",type);
                param.put("pageSize", pageSize + "");
                param.put("page", page + "");

                //http:// 域名/LSQB/ GetNewByType? token=***& type=类型& pageSize=每页条数&page=第几页

                run = new GetNewByType(param);
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
    class GetNewByType implements Runnable {
        private HashMap<String, String> param;

        GetNewByType(HashMap<String, String> param) {
            this.param = param;
        }

        @Override
        public void run() {


            HttpUtil.post( getActivity(), HttpUtil.GET_NEWBY_TYPE, param, new HttpUtil.HttpUtilInterface() {
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
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

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            if(code == 0) {

                if(page == 1 && mList.size() > 0){
                    mList.clear();
                }


                JSONArray data = obj.optJSONArray( "data" );
                if(Constant.isData( data.toString() )){
                    if(data.length() > 0 ){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<NewsBen>>(){}.getType();
                        List<NewsBen> list = gson.fromJson(data.toString(),type);
                        if(list.size() > 0){
                            mList.addAll( list );
                        }

                        if(list.size() < pageSize || list.size() == 0 ){
                            isData = false;
                            mAdapter.changeMoreStatus( -1 );
                        }else {
                            page ++ ;
                        }


                        mAdapter.notifyDataSetChanged();
                    }

                    if(mList.size() == 0){
                        no_data.setVisibility( View.VISIBLE );
                    }else {
                        no_data.setVisibility( View.GONE );
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
