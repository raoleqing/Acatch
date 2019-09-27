package catc.tiandao.com.match.ben;

import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.util.List;

import androidx.annotation.NonNull;


/**
 * 父控件数据
 * **/
public class MatchBen implements BaseExpandableRecyclerViewAdapter.BaseGroupBean<Match> {

    private String matchTime;
    private List<Match> matchList;

    public MatchBen(@NonNull List<Match> matchList, @NonNull String matchTime) {
        this.matchTime = matchTime;
        this.matchList = matchList;
    }

    @Override
    public int getChildCount() {
        return matchList.size();
    }

    @Override
    public boolean isExpandable() {
        return getChildCount() > 0;
    }


    @Override
    public Match getChildAt(int index) {
        return matchList.size() <= index ? null : matchList.get(index);
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}
