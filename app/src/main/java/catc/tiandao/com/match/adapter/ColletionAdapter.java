package catc.tiandao.com.match.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.BallBen;
import catc.tiandao.com.match.ben.Match;
import catc.tiandao.com.match.ben.MatchBen;
import catc.tiandao.com.match.common.MyItemClickListener;
import catc.tiandao.com.match.common.MyItemLongClickListener;

/**
 * Created by Administrator on 2017/12/7 0007.
 */
public class ColletionAdapter extends BaseExpandableRecyclerViewAdapter<MatchBen, Match, ColletionAdapter.GroupVH, ColletionAdapter.ChildVH> {


    private Context mContext;
    private List<MatchBen> mList;

    public ColletionAdapter(Context mContext,List<MatchBen> list) {
        this.mList = list;
        this.mContext = mContext;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public MatchBen getGroupItem(int position) {
        return mList.get(position);
    }

    @Override
    public GroupVH onCreateGroupViewHolder(ViewGroup parent, int groupViewType) {
        return new GroupVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.sample_item, parent, false));
    }

    @Override
    public ChildVH onCreateChildViewHolder(ViewGroup parent, int childViewType) {
        return new ChildVH(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.collection_item, parent, false));
    }

    @Override
    public void onBindGroupViewHolder(GroupVH holder, MatchBen mMatchBen, boolean isExpanding) {
        holder.nameTv.setText(mMatchBen.getMatchTime());
    }

    @Override
    public void onBindChildViewHolder(ChildVH holder, MatchBen mMatchBen, Match mMatch) {
       // holder.nameTv.setText(Match.getName());
    }

    static class GroupVH extends BaseExpandableRecyclerViewAdapter.BaseGroupViewHolder {
        TextView nameTv;

        GroupVH(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.group_item_name);
        }

        @Override
        protected void onExpandStatusChanged(RecyclerView.Adapter relatedAdapter, boolean isExpanding) {
           // foldIv.setImageResource(isExpanding ? R.drawable.ic_arrow_expanding : R.drawable.ic_arrow_folding);
        }
    }

    static class ChildVH extends RecyclerView.ViewHolder {
       // TextView nameTv;

        ChildVH(View itemView) {
            super(itemView);
           // nameTv = (TextView) itemView.findViewById(R.id.child_item_name);
        }
    }

}
