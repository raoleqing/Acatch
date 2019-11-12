package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.matchlibrary.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberInformation#newInstance} factory method to
 * create an instance of this fragment.
 * 情报
 */
public class MemberInformation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int BallType;
    private String matchId;

    private OnFragmentInteractionListener mListener;
    private GetFootballMatchDetail run;

    private WebView mWebView;
    private TextView no_data;

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

    public MemberInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemberInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberInformation newInstance(int BallType, String matchId) {
        MemberInformation fragment = new MemberInformation();
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
        View view = inflater.inflate( R.layout.fragment_member_information, container, false );
        vewInfo(view);
        getData(matchId);
        return view;
    }

    private void vewInfo(View view) {

        mWebView = ViewUtls.find( view,R.id.mWebView );

        setWebView();

    }


    private void setWebView() {

       /* // 能够的调用JavaScript代码
        //        // product_picture_html = new WebView(ProductDetails.this);
        //        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //        // 加载HTML字符串进行显示
        //        mWebView.getSettings().setJavaScriptEnabled(true);
        //        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //        mWebView.getSettings().setUseWideViewPort(true);
        //        mWebView.getSettings().setLoadWithOverviewMode(true);
        //        mWebView.setSaveEnabled(true);
        //        mWebView.getSettings().setRenderPriority( WebSettings.RenderPriority.HIGH);
        //        mWebView.getSettings().setSupportZoom(false);// 支持缩放
        //        //mWebView.loadData(urlStr, "text/html; charset=UTF-8", null);// 这种写法可以正确解码*/


        WebSettings webSettings = mWebView.getSettings();
        //5.0后http与https图片加载兼容
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 支持获取手势焦点，输入用户名、密码或其他
        mWebView.requestFocusFromTouch();
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setPluginState( WebSettings.PluginState.ON); // 支持插件
        webSettings.setRenderPriority( WebSettings.RenderPriority.HIGH); // 提高渲染的优先级
        // 设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。
        // 若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
        webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件
        //webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存，只从网络获取数据.
        webSettings.setAppCacheEnabled(true);// 应用可以有缓存
        webSettings.setDomStorageEnabled(true);// 设置可以使用localStorage
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);// 缓存最多可以有10M
        webSettings.setPluginState( WebSettings.PluginState.ON);
        webSettings.setTextZoom(100);//设置webview内部字体的缩放比例
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        // 触摸焦点起作用
        mWebView.requestFocusFromTouch();
        mWebView.requestFocus();

        // 用于视 频 播放
        mWebView.getSettings().setPluginState( WebSettings.PluginState.ON);



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
                methodName = HttpUtil.GET_FOOTBALL_MATCHDETAIL_HUIYUAN;
            }else {
                methodName = HttpUtil.GET_BASKETBALL_MATCHDETAIL_HUIYUAN;
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

                String data = obj.optString( "data" );
                if(Constant.isData( data )){
                    mWebView.loadUrl(data);

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
