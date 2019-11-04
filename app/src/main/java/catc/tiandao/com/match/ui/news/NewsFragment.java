package catc.tiandao.com.match.ui.news;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
import catc.tiandao.com.match.common.CommentDialog;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.score.BallFragment;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
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
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements CommentDialog.MyDialogInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RelativeLayout rl_contianer;
    private PopupWindow popupWindow;

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
    private NewOperationRun setRun;
    private int pageSize = 10;
    private int page = 1;
    private int lastVisibleItem;//现在滑动到那个下标
    private boolean isRun;
    private boolean isData = true;

    private OnFragmentInteractionListener mListener;
    private CommentDialog mDialog;


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
                    String type = bundle2.getString("type");
                    setParseData(result2,type,msg.arg1);
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

        rl_contianer = ViewUtls.find( view,R.id.rl_contianer );
        ball_recycler = ViewUtls.find( view,R.id.ball_recycler );
        no_data = ViewUtls.find( view,R.id.no_data );


        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        ball_recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new NewsAdapter(getActivity(),mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {

                NewsBen mNewsBen = mList.get( postion );

                switch (view.getId()){
                    case R.id.news_item:

                        Intent intent02 = new Intent(getActivity(), NewsDetailsActivity.class);
                        intent02.putExtra( NewsDetailsActivity.NEW_ID, mNewsBen.getId());
                        startActivity(intent02);
                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                        break;

                    case R.id.zhuanfa:

                        if(UserUtils.isLanded( getActivity())){
                            String shreUrl = "http://www.leisuvip1.com/New/Index/token="+ UserUtils.getToken( getActivity() )+"&newId=" + mNewsBen.getId();
                            showShare(postion,mNewsBen.getId(),shreUrl,mNewsBen.getcTitle(),mNewsBen.getcTitle());
                        }else {
                            UserUtils.startLongin( getActivity());
                        }

                        break;
                    case R.id.comment:

                        if(mDialog == null){
                            mDialog = new CommentDialog( getActivity(), postion, mNewsBen.getId(),NewsFragment.this );
                        }
                        mDialog.show();


                        break;
                    case R.id.dianzan:


                        if(UserUtils.isLanded( getActivity())){
                            NewOperation("dianZan",postion,mNewsBen.getId(),"");
                        }else {
                            UserUtils.startLongin( getActivity() );
                        }


                        break;

                }





            }
        });
        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));



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
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });


    }


    @Override
    public void commentButton(Dialog mDialog, int postiont,int newId, String comment) {



        if(UserUtils.isLanded( getActivity() )){
            NewOperation("pingLun",postiont,newId,comment);
        }else {
            UserUtils.startLongin( getActivity() );
        }


        mDialog.dismiss();



    }


    private void showShare(int postiont,int newId,String shareUrl,String shareTitle,String shareDescription) {
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
                    singleShare( SHARE_MEDIA.QZONE,postiont,newId,shareUrl,shareTitle,shareDescription);
                    popupWindow.dismiss();
                }
            });
            contentView.findViewById(R.id.iv_moment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN_CIRCLE,postiont,newId,shareUrl,shareTitle,shareDescription);
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
    private void singleShare(SHARE_MEDIA shareMedia,int postiont,int newId ,String shareUrl,String shareTitle,String shareDescription) {

        int logoResId = R.mipmap.app_icon;

        UmengUtil.shareSinglePlatform(getActivity(), shareMedia, shareUrl,shareTitle, logoResId, shareDescription);

        NewOperation("zhuanFa",postiont,newId,"");
    }





    //dianZan，zhuanFa，pingLun，shouCang
    private void NewOperation(String type,int postiont,int newId,String pingLun) {

        try{
            if (CheckNet.isNetworkConnected( getActivity())) {

                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));
                // type=类型& newId=新闻id& pingLun=评论内容
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( getActivity() ) );
                param.put("type",type);
                param.put("newId", newId + "");
                param.put("pingLun", "");

                setRun = new NewOperationRun(param,type,postiont);
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
    class NewOperationRun implements Runnable{
        private HashMap<String, String> param;
        private String type;
        private int postiont;

        NewOperationRun(HashMap<String, String> param,String type,int postiont){
            this.param =param;
            this.type = type;
            this.postiont = postiont;
        }

        @Override
        public void run() {

            HttpUtil.post( getActivity(),HttpUtil.NEW_OPERATION ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    data.putString( "type", type );
                    message.setData( data );
                    message.what = 0x003;
                    message.arg1 = postiont;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void setParseData(String result,String type,int postint) {

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

                if(type.equals( "dianZan" )){

                    mList.get( postint ).setiDianZanCount(  mList.get( postint ).getiDianZanCount() + 1 );
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT ).show();

                }else if(type.equals( "pingLun" )){

                    mList.get( postint ).setcCommentCount(  mList.get( postint ).getcCommentCount() + 1 );
                    mAdapter.notifyDataSetChanged();

                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT ).show();

                }else if(type.equals( "zhuanFa" )){

                    mList.get( postint ).setiZhuanFaCount(  mList.get( postint ).getiZhuanFaCount() + 1 );
                    mAdapter.notifyDataSetChanged();

                }

            }else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT ).show();
            }




        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

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
