package catc.tiandao.com.match.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.MainAutoSwitchAdapter;
import catc.tiandao.com.match.adapter.SampleAdapter;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.ben.Banner;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.ui.event.PopularActivity;
import catc.tiandao.com.match.ui.expert.ExpertActivity;
import catc.tiandao.com.match.ui.news.NewsActivity;
import catc.tiandao.com.match.utils.UnitConverterUtils;
import catc.tiandao.com.match.utils.ViewUtls;
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

    private List<Banner> bannerArray = new ArrayList<Banner>();
    private MainAutoSwitchAdapter lsTopAdapter = null;


    private List<AreaMatch> list = new ArrayList();
    private SampleAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        ls_top = ViewUtls.find(view, R.id.ls_top);
        mRecyclerView = ViewUtls.find(view, R.id.match_recycler);
        popular = ViewUtls.find(view, R.id.popular);
        expert = ViewUtls.find(view, R.id.expert);
        news = ViewUtls.find(view, R.id.news);

        popular.setOnClickListener( this );
        expert.setOnClickListener( this );
        news.setOnClickListener( this );


        // 设置广告的高度
        ViewGroup.LayoutParams lp = ls_top.getLayoutParams();
        int width = UnitConverterUtils.getDeviceWidth(getActivity());
        int height = (int)(width * 0.6);
        lp.height = height;
        ls_top.setLayoutParams(lp);

        Banner mBanner = new Banner();
        mBanner.setImageUrl( R.drawable.ad_image1);

        Banner mBanner1 = new Banner();
        mBanner1.setImageUrl( R.drawable.ad_image2 );

        Banner mBanner2 = new Banner();
        mBanner2.setImageUrl( R.drawable.ad_image1);


        bannerArray.add( mBanner);
        bannerArray.add( mBanner1);
        bannerArray.add( mBanner2);

        setAdvManage();


        // 设置布局管理器
       /* LinearLayoutManager mLinearLayoutManager =new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };*/


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SampleAdapter(getActivity(),list);
        adapter.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {
               switch (view.getId()){
                   case R.id.group_item_more:
                       Intent intent01 = new Intent(getActivity(), PopularActivity.class);
                       startActivity(intent01);
                       ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                       break;
                   case R.id.item_icon:
                   case R.id.item_name:
                   case R.id.item_title:
                       Intent intent02 = new Intent(getActivity(), MatchDetailsActivity.class);
                       startActivity(intent02);
                       ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                       break;

               }
            }
        } );

        mRecyclerView.setNestedScrollingEnabled( false );
        mRecyclerView.setAdapter(adapter);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.popular:

                Intent intent01 = new Intent(getActivity(), PopularActivity.class);
                startActivity(intent01);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.expert:
                Intent intent02 = new Intent(getActivity(), ExpertActivity.class);
                startActivity(intent02);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;
            case R.id.news:
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
