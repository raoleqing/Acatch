package catc.tiandao.com.match.ui.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.RecordAdapter;
import catc.tiandao.com.match.adapter.SampleAdapter;
import catc.tiandao.com.match.ben.AreaMatch;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.expert.ExpertActivity;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link dataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView history_recycler,recent_recyclerview1,recent_recyclerview2;

    private RecordAdapter mAdapter1;
    private RecordAdapter mAdapter2;
    private RecordAdapter mAdapter3;
    private List<AreaMatch> list = new ArrayList();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public dataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dataFragment newInstance(String param1, String param2) {
        dataFragment fragment = new dataFragment();
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
        View view = inflater.inflate( R.layout.fragment_data, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        history_recycler = ViewUtls.find( view,R.id.history_recycler );
        recent_recyclerview1 = ViewUtls.find( view,R.id.recent_recyclerview1 );
        recent_recyclerview2 = ViewUtls.find( view,R.id.recent_recyclerview2 );

       setHistory();
        setRecent1();
        setRecent2();


    }

    private void setHistory() {

        history_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter1 = new RecordAdapter(getActivity(),list);
        mAdapter1.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        history_recycler.setNestedScrollingEnabled( false );
        history_recycler.setAdapter(mAdapter1);
        // 设置Item增加、移除动画
        history_recycler.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        history_recycler.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));

    }


    private void setRecent1() {

        recent_recyclerview1.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter2 = new RecordAdapter(getActivity(),list);
        mAdapter2.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        recent_recyclerview1.setNestedScrollingEnabled( false );
        recent_recyclerview1.setAdapter(mAdapter2);
        // 设置Item增加、移除动画
        recent_recyclerview1.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        recent_recyclerview1.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));




    }


    private void setRecent2() {

        recent_recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter3 = new RecordAdapter(getActivity(),list);
        mAdapter3.setOnItemClickListener( new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, int type) {

            }
        } );

        recent_recyclerview2.setNestedScrollingEnabled( false );
        recent_recyclerview2.setAdapter(mAdapter3);
        // 设置Item增加、移除动画
        recent_recyclerview2.setItemAnimator(new DefaultItemAnimator());
        //添加Android自带的分割线
        recent_recyclerview2.addItemDecoration(new DividerItemDecoration( getActivity(), DividerItemDecoration.VERTICAL));




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
