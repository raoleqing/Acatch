package catc.tiandao.com.match.score;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import catc.tiandao.com.match.R;
import catc.tiandao.com.matchlibrary.OnFragmentInteractionListener;
import catc.tiandao.com.matchlibrary.ViewUtls;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BallTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BallTypeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int[] typeViews = {R.id.game_type1,R.id.game_type2,R.id.game_type3,R.id.game_type4,R.id.game_type5};
    private int[] typeIndicators = {R.id.game_type1_view,R.id.game_type2_view,R.id.game_type3_view,R.id.game_type4_view,R.id.game_type5_view};
    private TextView[] types = new TextView[5];
    private View[] Indicators = new View[5];


    private ViewPager mViewPager;

    private List<Fragment> list = null;
    private FragmentManager manager;
    private MyAdapter adapter;

    private int onPosition;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BallTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BallTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BallTypeFragment newInstance(int param1, String param2) {
        BallTypeFragment fragment = new BallTypeFragment();
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
        View view = inflater.inflate( R.layout.fragment_ball_type, container, false );
        viewInfo(view);
        return view;
    }

    private void viewInfo(View view) {

        for(int i = 0; i< typeViews.length; i++){
            types[i] = ViewUtls.find( view, typeViews[i]);
            Indicators[i] = ViewUtls.find( view, typeIndicators[i]);
            types[i].setOnClickListener( this );

        }


        list = new ArrayList<Fragment>();
        if(mParam1 == 0){
            list.add(BallFragment.newInstance(mParam1,0));
            list.add(BallFragment.newInstance(mParam1,1));
            list.add(BallFragment.newInstance(mParam1,2));
            list.add(BallFragment.newInstance(mParam1,3));
            list.add(BallFragment.newInstance(mParam1,4));
        }else {
            list.add(BallFragmentFragment.newInstance(mParam1,0));
            list.add(BallFragmentFragment.newInstance(mParam1,1));
            list.add(BallFragmentFragment.newInstance(mParam1,2));
            list.add(BallFragmentFragment.newInstance(mParam1,3));
            list.add(BallFragmentFragment.newInstance(mParam1,4));
        }



        manager = getChildFragmentManager();
        mViewPager = ViewUtls.find( view,R.id.ball_viewpager );
        adapter = new MyAdapter(manager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        ViewPageListerner vl = new ViewPageListerner();
        mViewPager.addOnPageChangeListener(vl);

    }




    @Override
    public void onClick(View v) {

        for(int i = 0; i< typeViews.length; i++){

            if(v.getId() == typeViews[i]){
                mViewPager.setCurrentItem( i );
            }
        }
    }


    /*
     * 修改中间的内容 *
     */
    private void setContontView(int position) {
        onPosition = position;
        // TODO Auto-generated method stub

        int color01 = ContextCompat.getColor( getActivity(),R.color.text1);
        int color02 = ContextCompat.getColor(getActivity(),R.color.text2);

        for(int i = 0; i< typeViews.length; i++){
            if(position == i){
                types[i].setTextColor( color01 );
                Indicators[i].setVisibility( View.VISIBLE );
            }else {

                types[i].setTextColor( color02 );
                Indicators[i].setVisibility( View.GONE );
            }
        }
    }






    /**
     * viewPage滑动事件
     */
    class ViewPageListerner implements ViewPager.OnPageChangeListener {

        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        // 当当前页面被滑动时调用
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub;
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            // 修改标题栏
            setContontView(arg0);
        }

    }





    /**
     * 没动加载 FragmentPagerAdapter
     *
     */
    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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
