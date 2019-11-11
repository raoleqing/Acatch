package catc.tiandao.com.match.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.TeamGoa;

public class CommentDialog extends Dialog {

	private Activity mContext;
	private EditText input_comment;
	private TextView comment_but;
	private MyDialogInterface myDialogInterface;
	private int newId;
	private int postion;


	public CommentDialog(Activity mContext, int postion, int newId , MyDialogInterface myDialogInterface) {
		super(mContext, R.style.customDialog);
		this.myDialogInterface = myDialogInterface;
		this.mContext = mContext;
		this.newId = newId;
		this.postion = postion;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.comment_dialog);
		comment_but = (TextView) this.findViewById(R.id.comment_but);
		input_comment = (EditText) this.findViewById(R.id.input_comment);

		comment_but.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String comment = input_comment.getText().toString();
				if(comment != null && comment.length() > 0){
					myDialogInterface.commentButton( CommentDialog.this,postion,newId, comment);
				}else {
					Toast.makeText( mContext,"请输入评论",Toast.LENGTH_SHORT ).show();
				}

			}
		} );


		this.setCanceledOnTouchOutside(true);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().getAttributes().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().getAttributes().width = ViewGroup.LayoutParams.FILL_PARENT;
		getWindow().setWindowAnimations(R.style.PopupAnimation);
	}


	public interface MyDialogInterface {

		void commentButton(Dialog mDialog,int postion,int newId,String comment);
	}


/*	*//**
	 * 显示键盘
	 *
	 * @param et 输入焦点
	 *//*
	public void showInput(final EditText et) {
		et.requestFocus();
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	*//**
	 * 隐藏键盘
	 *//*
	protected void hideInput() {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService( mContext.INPUT_METHOD_SERVICE );
		View v =  mContext.getWindow().peekDecorView();
		if (null != v) {
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
		}

	}*/

}
