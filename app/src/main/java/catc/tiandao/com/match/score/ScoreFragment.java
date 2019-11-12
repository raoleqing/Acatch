package catc.tiandao.com.match.score;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.match.ui.event.SelectActivity;
import catc.tiandao.com.matchlibrary.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button football,blueBall;


    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment mContent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int onPosition = 0;

    private OnFragmentInteractionListener mListener;

    public ScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreFragment newInstance(String param1, String param2) {
        ScoreFragment fragment = new ScoreFragment();
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
        View view = inflater.inflate( R.layout.fragment_score, container, false );
        viewInfo(view);
        ContentInfo();
        return view;
    }

    private void viewInfo(View view) {

        manager = getChildFragmentManager();

        ImageView tv_switch = ViewUtls.find( view,R.id.tv_switch );
        football = ViewUtls.find( view,R.id.football );
        blueBall = ViewUtls.find( view,R.id.blueBall );

        tv_switch.setOnClickListener( this );
        football.setOnClickListener( this );
        blueBall.setOnClickListener( this );


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_switch:

                if(onPosition == 0){
                    Intent intent01 = new Intent( getActivity(), MatchSelection.class);
                    startActivity(intent01);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }else {
                    Intent intent01 = new Intent( getActivity(), SelectActivity.class);
                    startActivity(intent01);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }


                break;
            case R.id.football:
                if (onPosition != 0) {
                    setContontView( 0 );
                }
                break;
            case R.id.blueBall:
                if (onPosition != 1) {
                    setContontView( 1 );
                }
                break;

        }
    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {
        onPosition = i;
        // TODO Auto-generated method stub
        transaction = manager.beginTransaction();

        int color01 = ContextCompat.getColor( getActivity(),R.color.text1);
        int color02 = ContextCompat.getColor(getActivity(),R.color.white);

        switch (i) {
            case 0:

                football.setBackgroundResource( R.drawable.bg_search_normal1 );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2_host );
                football.setTextColor( color02 );
                blueBall.setTextColor( color01 );


                if (fragment01 == null) {
                    fragment01 = BallTypeFragment.newInstance(0,"");
                }
                switchContent(fragment01, "fragment01");
                break;
            case 1:
                football.setBackgroundResource( R.drawable.bg_search_normal1_host );
                blueBall.setBackgroundResource( R.drawable.bg_search_normal2 );
                football.setTextColor( color01 );
                blueBall.setTextColor( color02 );

                if (fragment02 == null) {
                    fragment02 =  BallTypeFragment.newInstance(1,"");
                }
                switchContent(fragment02, "fragment02");
                break;

            default:
                break;
        }
    }




    private void ContentInfo() {
        if(fragment01 == null)
            fragment01 = BallTypeFragment.newInstance(0,"");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.score_content, fragment01,"fragment01").show(fragment01);
        mContent = fragment01;
        transaction.commit();
    }

    /** 修改显示的内容 不会重新加载 **/
    public void switchContent(Fragment to, String tag) {
        if (mContent != to) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContent).add(R.id.score_content, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
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
