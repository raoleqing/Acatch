package catc.tiandao.com.match.ui.event;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;

import java.util.ArrayList;
import java.util.List;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.ExponentBen;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExponentFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 指数
 */
public class ExponentFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tab_bar1, tab_bar2, tab_bar3;
    private int contentIndex = 0;

    private SmartTable table;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExponentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExponentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExponentFragment newInstance(String param1, String param2) {
        ExponentFragment fragment = new ExponentFragment();
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
        View view = inflater.inflate( R.layout.fragment_exponent, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        tab_bar1 = ViewUtls.find( view,R.id.tab_bar1 );
        tab_bar2 = ViewUtls.find( view,R.id.tab_bar2 );
        tab_bar3 = ViewUtls.find( view,R.id.tab_bar3 );
        table = ViewUtls.find(view,R.id.table);

        tab_bar1.setOnClickListener( this );
        tab_bar2.setOnClickListener( this );
        tab_bar3.setOnClickListener( this );



        List<ExponentBen> list = new ArrayList<>();

        list.add(new ExponentBen("BET","0.8", "2.75", "1.0", "0.8", "2.75", "1.0","0.8", "2.75", "1.0"));
        list.add(new ExponentBen("易胜","0.8", "2.75", "1.0", "0.8", "2.75", "1.0","0.8", "2.75", "1.0"));
        list.add(new ExponentBen("10BET","0.8", "2.75", "1.0", "0.8", "2.75", "1.0","0.8", "2.75", "1.0"));
        list.add(new ExponentBen("10B用","0.8", "2.75", "1.0", "0.8", "2.75", "1.0","0.8", "2.75", "1.0"));

        table.setData( list );
        //table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));




    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tab_bar1:

                if (contentIndex != 0){
                    setViewContent(0);
                }

                break;
            case R.id.tab_bar2:

                if (contentIndex != 1){
                    setViewContent(1);
                }

                break;
            case R.id.tab_bar3:

                if (contentIndex != 2){
                    setViewContent(2);
                }

                break;


        }

    }

    private void setViewContent(int i) {

        contentIndex = i;

        int bg01 = R.drawable.bg_search_normal17;
        int bg02 = R.drawable.bg_search_normal15;
        int text1 = ContextCompat.getColor(getActivity(),R.color.white);
        int text2 = ContextCompat.getColor(getActivity(),R.color.text1);

        switch (i) {
            case 0:
                tab_bar1.setBackgroundResource( bg01 );
                tab_bar2.setBackgroundResource( bg02 );
                tab_bar3.setBackgroundResource( bg02 );
                tab_bar1.setTextColor( text1 );
                tab_bar2.setTextColor( text2 );
                tab_bar3.setTextColor( text2 );

                break;
            case 1:

                tab_bar1.setBackgroundResource( bg02 );
                tab_bar2.setBackgroundResource( bg01 );
                tab_bar3.setBackgroundResource( bg02 );
                tab_bar1.setTextColor( text2 );
                tab_bar2.setTextColor( text1 );
                tab_bar3.setTextColor( text2 );

                break;
            case 2:
                tab_bar1.setBackgroundResource( bg02 );
                tab_bar2.setBackgroundResource( bg02 );
                tab_bar3.setBackgroundResource( bg01 );

                tab_bar1.setTextColor( text2 );
                tab_bar2.setTextColor( text2 );
                tab_bar3.setTextColor( text1 );


                break;

            default:
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
