package catc.tiandao.com.match.my;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button login_but;
    private TextView but_type1,but_type2,but_type3,but_type4,but_type5,but_type6;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate( R.layout.fragment_my, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        login_but = ViewUtls.find( view,R.id.login_but );
        but_type1 = ViewUtls.find( view,R.id.but_type1 );
        but_type2 = ViewUtls.find( view,R.id.but_type2 );
        but_type3 = ViewUtls.find( view,R.id.but_type3 );
        but_type4 = ViewUtls.find( view,R.id.but_type4 );
        but_type5 = ViewUtls.find( view,R.id.but_type5 );
        but_type6 = ViewUtls.find( view,R.id.but_type6 );

        login_but.setOnClickListener( this );
        but_type1.setOnClickListener( this );
        but_type2.setOnClickListener( this );
        but_type3.setOnClickListener( this );
        but_type4.setOnClickListener( this );
        but_type5.setOnClickListener( this );
        but_type6.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_but:

                Intent intent01 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent01);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.but_type1:
                Intent intent02 = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent02);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;
            case R.id.but_type2:
                Intent intent03 = new Intent(getActivity(), AttentionActivity.class);
                startActivity(intent03);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                break;
            case R.id.but_type3:

                Intent intent04 = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent04);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.but_type4:

                Intent intent05 = new Intent(getActivity(), MySetActivity.class);
                startActivity(intent05);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);


                break;
            case R.id.but_type5:


                Intent intent06 = new Intent(getActivity(), SuggestActivity.class);
                startActivity(intent06);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);


                break;
            case R.id.but_type6:

                Intent intent07 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent07);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);


                break;

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
