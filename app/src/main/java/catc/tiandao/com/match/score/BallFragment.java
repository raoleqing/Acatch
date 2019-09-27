package catc.tiandao.com.match.score;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.BallAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BallFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 进行中
 */
public class BallFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout tab_layout;
    private RecyclerView ball_recycler;

    private LinearLayoutManager mLinearLayoutManager;
    private BallAdapter mAdapter;
    private List<BallBen> mList = new ArrayList<>(  );



    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BallFragment newInstance(int param1, String param2) {
        BallFragment fragment = new BallFragment();
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
        View view = inflater.inflate( R.layout.fragment_ball, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        tab_layout = ViewUtls.find( view,R.id.tab_layout );
        ball_recycler = ViewUtls.find( view,R.id.ball_recycler );

        // 设置布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        ball_recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new BallAdapter(getActivity(),mList);
        mAdapter.setOnItemClickListener(new MyItemClickListener(){
            @Override
            public void onItemClick(View view, int postion, int type) {
                Intent intent01 = new Intent(getActivity(), ScoreDetailsActivity.class);
                startActivity(intent01);
                ((Activity)getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

            }
        });
        // 设置adapter
        ball_recycler.setAdapter(mAdapter);
        // 设置Item增加、移除动画
        ball_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        ball_recycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        if(mParam1 == 2 || mParam1 == 3){
            tab_layout.setVisibility( View.VISIBLE );
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
