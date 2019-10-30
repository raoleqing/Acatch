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
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CircleImageView;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.utils.UserUtils;
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

    private CircleImageView user_icon;
    private Button login_but;
    private TextView user_name;
    private TextView but_type1,but_type2,but_type3,but_type4,but_type5,but_type6;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DisplayImageOptions options;

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

        EventBus.getDefault().register(this);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_def_avatar)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_def_avatar)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_def_avatar)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_my, container, false );
        viewInfo(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserContent();
    }

    private void viewInfo(View view) {

        user_icon = ViewUtls.find( view,R.id.user_icon );
        user_name = ViewUtls.find( view,R.id.user_name );
        login_but = ViewUtls.find( view,R.id.login_but );
        but_type1 = ViewUtls.find( view,R.id.but_type1 );
        but_type2 = ViewUtls.find( view,R.id.but_type2 );
        but_type3 = ViewUtls.find( view,R.id.but_type3 );
        but_type4 = ViewUtls.find( view,R.id.but_type4 );
        but_type5 = ViewUtls.find( view,R.id.but_type5 );
        but_type6 = ViewUtls.find( view,R.id.but_type6 );

        user_icon.setOnClickListener( this );
        user_name.setOnClickListener( this );
        login_but.setOnClickListener( this );
        but_type1.setOnClickListener( this );
        but_type2.setOnClickListener( this );
        but_type3.setOnClickListener( this );
        but_type4.setOnClickListener( this );
        but_type5.setOnClickListener( this );
        but_type6.setOnClickListener( this );
    }


    private void setUserContent() {

        if(UserUtils.isLanded( getActivity() )){
            String Appavatar = UserUtils.getUserAvatar(getActivity());
            if (Appavatar != null && !"".equals(Appavatar)) {
                ImageLoader.getInstance().displayImage(Appavatar, user_icon,options);
            }
            login_but.setVisibility( View.GONE );
            user_name.setVisibility( View.VISIBLE );
            user_name.setText( UserUtils.getNickName( getActivity() ) );
        }else {
            ImageLoader.getInstance().displayImage( "drawable://" + R.mipmap.icon_def_avatar, user_icon );
            login_but.setVisibility( View.VISIBLE );
            user_name.setVisibility( View.GONE );
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.user_name:
            case R.id.user_icon:

                if(UserUtils.isLanded( getActivity() )){
                    Intent intent = new Intent(getActivity(), MyDataActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }else {
                    Intent intent01 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent01);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }


                break;
            case R.id.login_but:

                Intent intent01 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent01);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.but_type1:


                if(UserUtils.isLanded( getActivity() )){
                    Intent intent02 = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent02);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }else {
                  UserUtils.startLongin( getActivity() );
                }



                break;
            case R.id.but_type2:

                if(UserUtils.isLanded( getActivity() )){
                    Intent intent03 = new Intent(getActivity(), AttentionActivity.class);
                    startActivity(intent03);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);
                }else {
                    UserUtils.startLongin( getActivity() );
                }

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

                if(UserUtils.isLanded( getActivity() )){
                    Intent intent06 = new Intent(getActivity(), SuggestActivity.class);
                    startActivity(intent06);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                }else {
                    UserUtils.startLongin( getActivity() );
                }


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

    @Subscribe
    public void onEvent(String event){
      if(Constant.LOGIN_SUCCESS.equals(event)){
          setUserContent();
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        EventBus.getDefault().unregister(this);
    }




}
