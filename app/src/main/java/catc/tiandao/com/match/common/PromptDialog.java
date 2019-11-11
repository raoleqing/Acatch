package catc.tiandao.com.match.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.adapter.BallAdapter;
import catc.tiandao.com.match.adapter.BallPromptAdapter;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.score.ScoreDetailsActivity;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;

public class PromptDialog extends Dialog {

	private Context mContext;
	private LinearLayout myRecyclerView;

	private List<BallBen> list;



	public PromptDialog(Context mContext, List<BallBen> list ) {
		super(mContext, R.style.customDialog);
		this.list = list;
		this.mContext = mContext;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.promt_dialog);
		myRecyclerView = (LinearLayout) this.findViewById(R.id.myRecyclerView);
		for(int i = 0; i< list.size(); i++){
			View view = getLayoutInflater().inflate(R.layout.ball_promt_item, null);
			TextView time = ViewUtls.find( view,R.id.time );
			TextView home_name = ViewUtls.find( view,R.id.home_name );
			TextView away_name = ViewUtls.find( view,R.id.away_name );
			TextView home_score = ViewUtls.find( view,R.id.home_score );
			TextView away_score = ViewUtls.find( view,R.id.away_score );

			BallBen mBallBen = list.get( i );

			time.setText( mBallBen.getMatchBeginTime() );
			home_name.setText( mBallBen.getHomeTeamName() );
			away_name.setText( mBallBen.getAwayTeamName() );
			home_score.setText( mBallBen.getHomeTeamScore() + "");
			away_score.setText( mBallBen.getAwayTeamScore() + "" );

			myRecyclerView.addView( view );

		}


		this.setCanceledOnTouchOutside(true);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().getAttributes().width = ViewGroup.LayoutParams.FILL_PARENT;
		getWindow().setWindowAnimations(R.style.PopupAnimation);

	}



}
