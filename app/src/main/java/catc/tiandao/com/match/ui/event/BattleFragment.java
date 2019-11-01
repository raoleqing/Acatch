package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.ZhenRongAdapter;
import catc.tiandao.com.match.ben.BasketZhenRong;
import catc.tiandao.com.match.ben.ZhenRong;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BattleFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 阵容
 */
public class BattleFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";

    // TODO: Rename and change types of parameters
    private int BallType;
    private String BallId;

    private ImageView team1Logo, team2Logo;
    private TextView team1Name, team2Name;
    private TextView title_text;
    private TextView no_data1, no_data2;
    private RecyclerView ball_recycler1, ball_recycler2;

    private Button home_name, away_name;
    private RelativeLayout football_layout, blueball_layout;
    //足球
    private RelativeLayout football_zhenrong1, football_zhenrong2;
    private int[] playerID1 = {R.id.player_layout1, R.id.player_layout2, R.id.player_layout3};
    private int[] playerID2 = {R.id.player2_layout1, R.id.player2_layout2, R.id.player2_layout3};
    private LinearLayout[] player_layout1 = new LinearLayout[3];
    private LinearLayout[] player_layout2 = new LinearLayout[3];
    private TextView player_icon, player2_icon;
    private TextView player_name, player2_name;

    //
    private RelativeLayout blueball_zhenrong1, blueball_zhenrong2;
    private int[] blueball1Ids = {R.id.blue_ball_member1, R.id.blue_ball_member2, R.id.blue_ball_member3, R.id.blue_ball_member4, R.id.blue_ball_member5};
    private int[] blueballName1Ids = {R.id.blue_ball_name1, R.id.blue_ball_name2, R.id.blue_ball_name3, R.id.blue_ball_name4, R.id.blue_ball_name5};

    private int[] blueball2Ids = {R.id.blue_ball2_member1, R.id.blue_ball2_member2, R.id.blue_ball2_member3, R.id.blue_ball2_member4, R.id.blue_ball2_member5};
    private int[] blueballName2Ids = {R.id.blue_ball2_name1, R.id.blue_ball2_name2, R.id.blue_ball2_name3, R.id.blue_ball2_name4, R.id.blue_ball2_name5};
    private TextView[] blueball1 = new TextView[5];
    private TextView[] blueballName1 = new TextView[5];
    private TextView[] blueball2 = new TextView[5];
    private TextView[] blueballName2 = new TextView[5];
    //


    private List<Object> mList1 = new ArrayList();
    private List<Object> showList1 = new ArrayList();
    private List<Object> mList2 = new ArrayList();
    private List<Object> showList2 = new ArrayList();
    private ZhenRongAdapter mAdapter1;
    private ZhenRongAdapter mAdapter2;

    private OnFragmentInteractionListener mListener;
    GetFootballMatchDetail run;
    private DisplayImageOptions options;

    private int showType = 0;
    private String HomeTeamLogoUrl;
    private String AwayTeamLogoUrl;
    private String HomeTeamName;
    private String AwayTeamName;

    private String team1Formation = "", team2Formation = "";


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    if (BallType == 0) {
                        parseData(result1);
                    } else {
                        basketParseData(result1);
                    }

                    break;

                default:
                    break;
            }
        }


    };


    public BattleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BattleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BattleFragment newInstance(int BallType, String BallId, String HomeTeamLogoUrl, String AwayTeamLogoUrl, String HomeTeamName, String AwayTeamName) {
        BattleFragment fragment = new BattleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, BallType);
        args.putString(ARG_PARAM2, BallId);
        args.putString(ARG_PARAM3, HomeTeamLogoUrl);
        args.putString(ARG_PARAM4, AwayTeamLogoUrl);
        args.putString(ARG_PARAM5, HomeTeamName);
        args.putString(ARG_PARAM6, AwayTeamName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            BallType = getArguments().getInt(ARG_PARAM1);
            BallId = getArguments().getString(ARG_PARAM2);
            HomeTeamLogoUrl = getArguments().getString(ARG_PARAM3);
            AwayTeamLogoUrl = getArguments().getString(ARG_PARAM4);
            HomeTeamName = getArguments().getString(ARG_PARAM5);
            AwayTeamName = getArguments().getString(ARG_PARAM6);
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.mall_cbg)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.mall_cbg)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.mall_cbg)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_battle, container, false);
        viewInfo(view);
        getData(BallId);
        return view;
    }

    private void viewInfo(View view) {


        title_text = ViewUtls.find(view, R.id.title_text);
        no_data1 = ViewUtls.find(view, R.id.no_data1);
        no_data2 = ViewUtls.find(view, R.id.no_data2);
        team1Logo = ViewUtls.find(view, R.id.team1Logo);
        team2Logo = ViewUtls.find(view, R.id.team2Logo);
        team1Name = ViewUtls.find(view, R.id.team1Name);
        team2Name = ViewUtls.find(view, R.id.team2Name);
        home_name = ViewUtls.find(view, R.id.home_name);
        away_name = ViewUtls.find(view, R.id.away_name);
        football_layout = ViewUtls.find(view, R.id.football_layout);
        blueball_layout = ViewUtls.find(view, R.id.blueball_layout);
        football_zhenrong1 = ViewUtls.find(view, R.id.football_zhenrong1);
        football_zhenrong2 = ViewUtls.find(view, R.id.football_zhenrong2);
        blueball_zhenrong1 = ViewUtls.find(view, R.id.blueball_zhenrong1);
        blueball_zhenrong2 = ViewUtls.find(view, R.id.blueball_zhenrong2);

        for (int i = 0; i < player_layout1.length; i++) {
            player_layout1[i] = ViewUtls.find(view, playerID1[i]);
            player_layout2[i] = ViewUtls.find(view, playerID2[i]);
        }


        for (int i = 0; i < blueball1Ids.length; i++) {
            blueball1[i] = ViewUtls.find(view, blueball1Ids[i]);
            blueballName1[i] = ViewUtls.find(view, blueballName1Ids[i]);
            blueball2[i] = ViewUtls.find(view, blueball2Ids[i]);
            blueballName2[i] = ViewUtls.find(view, blueballName2Ids[i]);

        }

        player_icon = ViewUtls.find(view, R.id.player_icon);
        player2_icon = ViewUtls.find(view, R.id.player2_icon);
        player_name = ViewUtls.find(view, R.id.player_name);
        player2_name = ViewUtls.find(view, R.id.player2_name);


        if (BallType == 0) {
            football_layout.setVisibility(View.VISIBLE);
            blueball_layout.setVisibility(View.GONE);
        } else {
            football_layout.setVisibility(View.GONE);
            blueball_layout.setVisibility(View.VISIBLE);
        }

        home_name.setOnClickListener(this);
        away_name.setOnClickListener(this);


        ImageLoader.getInstance().displayImage(HomeTeamLogoUrl, team1Logo, options);
        ImageLoader.getInstance().displayImage(AwayTeamLogoUrl, team2Logo, options);
        team1Name.setText(HomeTeamName);
        team2Name.setText(AwayTeamName);
        home_name.setText(HomeTeamName);
        away_name.setText(AwayTeamName);

        ball_recycler1 = ViewUtls.find(view, R.id.ball_recycler1);
        ball_recycler2 = ViewUtls.find(view, R.id.ball_recycler2);
        setRecycler1();
        setRecycler2();


    }


    private void setRecycler1() {


        ball_recycler1.setLayoutManager(new LinearLayoutManager(getActivity()));
        ball_recycler1.setHasFixedSize(true);
        ball_recycler1.setNestedScrollingEnabled(false);

        mAdapter1 = new ZhenRongAdapter(getContext(), mList1, 0);
        // 设置adapter
        ball_recycler1.setAdapter(mAdapter1);
        // 设置Item增加、移除动画
        ball_recycler1.setItemAnimator(new DefaultItemAnimator());

    }

    private void setRecycler2() {


        ball_recycler2.setLayoutManager(new LinearLayoutManager(getActivity()));
        ball_recycler2.setHasFixedSize(true);
        ball_recycler2.setNestedScrollingEnabled(false);

        mAdapter2 = new ZhenRongAdapter(getContext(), mList2, 1);
        // 设置adapter
        ball_recycler2.setAdapter(mAdapter2);
        // 设置Item增加、移除动画
        ball_recycler2.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home_name:

                if (showType != 0) {
                    showType = 0;

                    title_text.setText("当前阵容: " + team1Formation);

                    home_name.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    away_name.setTextColor(ContextCompat.getColor(getContext(), R.color.text6));
                    home_name.setBackgroundResource(R.mipmap.home_nam_bg);
                    away_name.setBackgroundResource(R.mipmap.away_name_bg);

                    if (BallType == 0) {

                        football_zhenrong1.setVisibility(View.VISIBLE);
                        football_zhenrong2.setVisibility(View.GONE);

                    } else {

                        blueball_zhenrong1.setVisibility(View.VISIBLE);
                        blueball_zhenrong2.setVisibility(View.GONE);


                    }
                }


                break;
            case R.id.away_name:
                if (showType != 1) {
                    showType = 1;

                    title_text.setText("当前阵容: " + team2Formation);

                    home_name.setTextColor(ContextCompat.getColor(getContext(), R.color.text6));
                    away_name.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    home_name.setBackgroundResource(R.mipmap.away_name_bg);
                    away_name.setBackgroundResource(R.mipmap.home_nam_bg);


                    if (BallType == 0) {
                        football_zhenrong1.setVisibility(View.GONE);
                        football_zhenrong2.setVisibility(View.VISIBLE);
                    } else {
                        blueball_zhenrong1.setVisibility(View.GONE);
                        blueball_zhenrong2.setVisibility(View.VISIBLE);

                    }
                }


                break;

        }

    }


    private void getData(String matchId) {

        //http:// 域名/LSQB/ GetFootballMatchDetail_zhiShu? token=***& matchId=比赛id


        try {
            if (CheckNet.isNetworkConnected(getActivity())) {
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_SHOW));


                HashMap<String, String> param = new HashMap<>();
                param.put("token", UserUtils.getToken(getActivity()));
                param.put("matchId", matchId);


                run = new GetFootballMatchDetail(param);
                ThreadPoolManager.getsInstance().execute(run);
            } else {

                Toast.makeText(getActivity(), "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *
     */
    class GetFootballMatchDetail implements Runnable {
        private HashMap<String, String> param;

        GetFootballMatchDetail(HashMap<String, String> param) {
            this.param = param;
        }

        @Override
        public void run() {

            String methodName;
            if (BallType == 0) {
                methodName = HttpUtil.GET_FOOTBALL_MATCHDETAIL_ZHENRONG;
            } else {
                methodName = HttpUtil.GET_BASKETBALL_MATCHDETAIL_ZHENRONG;
            }


            HttpUtil.post(getActivity(), methodName, param, new HttpUtil.HttpUtilInterface() {
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("result", result);
                    message.setData(data);
                    message.what = 0x001;
                    myHandler.sendMessage(message);
                }
            });


        }
    }


    private void parseData(String result) {

        if (result == null) {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try {
            System.out.println(result);
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code", 0);
            String message = obj.optString("message");


            if (code == 0) {

                JSONObject data = obj.optJSONObject("data");
                if (data != null && data.length() > 0 && !data.toString().equals("null") && !data.toString().equals("暂无数据")) {

                    team1Formation = data.optString("team1Formation");
                    team2Formation = data.optString("team2Formation");
                    if (team1Formation == null || team1Formation.equals("") || team1Formation.equals("null")) {
                        team1Formation = "4-3-3";
                    }

                    if (team2Formation == null || team2Formation.equals("") || team2Formation.equals("null")) {
                        team2Formation = "4-3-3";
                    }

                    String[] team1 = team1Formation.split("\\-");
                    String[] team2 = team2Formation.split("\\-");
                    for (int i = 0; i < team1.length && i < 3; i++) {
                        int teamItem = Integer.parseInt(team1[i]);
                        if (teamItem > 5) {
                            team1Formation = "4-3-3";
                        }

                        int teamItem2 = Integer.parseInt(team2[i]);
                        if (teamItem2 > 5) {
                            team2Formation = "4-3-3";
                        }
                    }


                    String team1ZhenRong = data.optString("team1ZhenRong");
                    if (team1Formation != null && !team1Formation.equals("null") && !team1Formation.equals("")) {
                        team1ZhenRong = team1ZhenRong.replace("\\\"", "");

                        String team2ZhenRong = data.optString("team2ZhenRong");
                        team2ZhenRong = team2ZhenRong.replace("\\\"", "");

                        JSONArray array1 = new JSONArray(team1ZhenRong);
                        JSONArray array2 = new JSONArray(team2ZhenRong);

                        mList1.clear();
                        mList2.clear();

                        if (array1.length() > 0) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ZhenRong>>() {
                            }.getType();
                            List<ZhenRong> list = gson.fromJson(array1.toString(), type);
                            if (list.size() > 0) {
                                mList1.addAll(list);
                            }
                        }

                        if (array2.length() > 0) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ZhenRong>>() {
                            }.getType();
                            List<ZhenRong> list = gson.fromJson(array2.toString(), type);
                            if (list.size() > 0) {
                                mList2.addAll(list);
                            }
                        }


                        for (int i = 0; i < mList1.size(); i++) {

                            ZhenRong mZhenRong = (ZhenRong) mList1.get(i);
                            if (mZhenRong.getFirst() == 1) {
                                if (mZhenRong.getPosition().equals("G")) {
                                    player_icon.setText(mZhenRong.getShirt_number() + "");
                                    player_name.setText(mZhenRong.getName());
                                } else {
                                    showList1.add(mZhenRong);
                                }

                            }

                        }


                        for (int i = 0; i < mList2.size(); i++) {

                            ZhenRong mZhenRong = (ZhenRong) mList2.get(i);
                            if (mZhenRong.getFirst() == 1) {
                                if (mZhenRong.getPosition().equals("G")) {
                                    player2_icon.setText(mZhenRong.getShirt_number() + "");
                                    player2_name.setText(mZhenRong.getName());
                                } else {
                                    showList2.add(mZhenRong);
                                }

                            }

                        }


                        setContent();
                        mAdapter1.notifyDataSetChanged();
                        mAdapter2.notifyDataSetChanged();

                    } else {

                        no_data1.setVisibility(View.VISIBLE);
                        no_data2.setVisibility(View.VISIBLE);
                        football_layout.setVisibility(View.GONE);
                        blueball_layout.setVisibility(View.GONE);

                    }

                } else {

                    no_data1.setVisibility(View.VISIBLE);
                    no_data2.setVisibility(View.VISIBLE);
                    football_layout.setVisibility(View.GONE);
                    blueball_layout.setVisibility(View.GONE);

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }


    private void basketParseData(String result) {

        if (result == null) {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
            return;
        }

        try {
            System.out.println(result);
            JSONObject obj = new JSONObject(result);
            int code = obj.optInt("code", 0);
            String message = obj.optString("message");


            if (code == 0) {
                String data = obj.optString("data");
                data = data.replace("\\\"", "");

                if (data != null && data.length() > 0 && !data.equals("null") && !data.equals("暂无数据")) {
                    JSONArray array = new JSONArray(data);
                    //  {\"id\":11994,\"name_zh\":\"达里奥·萨里奇\",\"name_zht\":\"\",\"name_en\":\"dario saric\",\"imgUrl\":\"ceb1ffc565c505992f7be68a5182516c.png\",\"QiuYi\":\"20\",\"Note\":\"21^2-7^1-4^2-2^0^4^4^0^1^1^0^1^12^7^1^1^0\",\"type\":1,\"IsShouFa\":1}

                    if (array.length() > 0) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<BasketZhenRong>>() {
                        }.getType();
                        List<BasketZhenRong> list = gson.fromJson(array.toString(), type);
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                BasketZhenRong mBasketZhenRong = list.get(i);

                                if (mBasketZhenRong.getType() == 1) {
                                    mList1.add(mBasketZhenRong);
                                } else if (mBasketZhenRong.getType() == 2) {
                                    mList2.add(mBasketZhenRong);
                                }

                            }
                        } else {

                            no_data1.setVisibility(View.VISIBLE);
                            no_data2.setVisibility(View.VISIBLE);
                            football_layout.setVisibility(View.GONE);
                            blueball_layout.setVisibility(View.GONE);
                        }

                    }

                    mAdapter1.notifyDataSetChanged();
                    mAdapter2.notifyDataSetChanged();

                    setBasketContent();
                } else {

                    no_data1.setVisibility(View.VISIBLE);
                    no_data2.setVisibility(View.VISIBLE);
                    football_layout.setVisibility(View.GONE);
                    blueball_layout.setVisibility(View.GONE);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mListener.onFragmentInteraction(Uri.parse(OnFragmentInteractionListener.PROGRESS_HIDE));
        }

    }


    private void setContent() {

        if (BallType == 0) {


            title_text.setText("当前阵容: " + team1Formation);

            String[] team1 = team1Formation.split("\\-");
            String[] team2 = team2Formation.split("\\-");
            int teamItem1 = Integer.parseInt(team1[0]);
            int teamItem2 = Integer.parseInt(team1[1]);
            int teamItem3 = Integer.parseInt(team1[2]);

            int playerIndex = 0;
            for (int i = 0; i < team1.length && i < 3; i++) {
                int item = Integer.parseInt(team1[i]);
                for (int j = 0; j < item; j++) {

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.player_item, null);
                    TextView player_icon = ViewUtls.find(view, R.id.player_icon);
                    TextView player_name = ViewUtls.find(view, R.id.player_name);
                    if (playerIndex < showList1.size()) {
                        ZhenRong mZhenRong = (ZhenRong) showList1.get(playerIndex);
                        player_icon.setText(mZhenRong.getShirt_number() + "");
                        player_name.setText(mZhenRong.getName());
                        player_layout1[i].addView(view);

                        playerIndex++;
                    }
                }
            }

            if (playerIndex < 10) {

                for (int i = 0; i < team1.length && playerIndex < 10 && i < 3; i++) {
                    int item = Integer.parseInt(team1[i]);
                    if (item < 4) {
                        item = 4;
                        for (int j = 0; j < item && playerIndex < 10; j++) {
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.player_item, null);
                            TextView player_icon = ViewUtls.find(view, R.id.player_icon);
                            TextView player_name = ViewUtls.find(view, R.id.player_name);
                            player_icon.setText("");
                            player_name.setText("未知");
                            player_layout1[i].addView(view);
                            playerIndex++;
                        }
                    }


                }

            }

            int playerIndex2 = 0;
            for (int i = 0; i < team2.length && i < 3; i++) {
                int item = Integer.parseInt(team2[i]);
                for (int j = 0; j < item; j++) {

                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.player_item, null);
                    TextView player_icon = ViewUtls.find(view, R.id.player_icon);
                    player_icon.setBackgroundResource(R.mipmap.football_jersey_goalkeeper_away);
                    TextView player_name = ViewUtls.find(view, R.id.player_name);

                    if (playerIndex2 < showList2.size()) {
                        ZhenRong mZhenRong = (ZhenRong) showList2.get(playerIndex2);
                        player_icon.setText(mZhenRong.getShirt_number() + "");
                        player_name.setText(mZhenRong.getName());
                        player_layout2[i].addView(view);

                        playerIndex2++;
                    }
                }
            }

            if (playerIndex2 < 10) {

                for (int i = 0; i < team2.length && playerIndex2 < 10 && i < 3; i++) {
                    int item = Integer.parseInt(team2[i]);
                    if (item < 4) {
                        item = 4;

                        for (int j = 0; j < item && playerIndex2 < 10; j++) {
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.player_item, null);
                            TextView player_icon = ViewUtls.find(view, R.id.player_icon);
                            TextView player_name = ViewUtls.find(view, R.id.player_name);
                            player_icon.setText("");
                            player_name.setText("未知");
                            player_layout2[i].addView(view);
                            playerIndex2++;
                        }
                    }


                }

            }


        }


    }


    private void setBasketContent() {

        if (BallType == 1) {


            int index = 0;
            for (int i = 0; i < mList1.size(); i++) {

                BasketZhenRong mBasketZhenRong = (BasketZhenRong) mList1.get(i);
                if (mBasketZhenRong.getIsShouFa() == 1) {
                    if (index < 5) {

                        blueball1[index].setText(mBasketZhenRong.getQiuYi() + "");
                        blueballName1[index].setText(mBasketZhenRong.getName_zh());
                        index++;

                    }

                }

            }


            int index2 = 0;
            for (int i = 0; i < mList2.size(); i++) {

                BasketZhenRong mBasketZhenRong = (BasketZhenRong) mList2.get(i);
                if (mBasketZhenRong.getIsShouFa() == 1) {
                    if (index2 < 5) {

                        blueball2[index2].setText(mBasketZhenRong.getQiuYi() + "");
                        blueballName2[index2].setText(mBasketZhenRong.getName_zh());
                        index2++;


                    }

                }

            }


        }


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
