package catc.tiandao.com.match.ben;

import java.util.List;

//首页数据
public class AreaMatch {

    /* "matchName":"欧洲赛事",
             "matchNewList":[],
             "matchList":[]
        */


    private String matchName;
    private List<MatchNew> matchNewList;
    private List<Match> matchList;


    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public List<MatchNew> getMatchNewList() {
        return matchNewList;
    }

    public void setMatchNewList(List<MatchNew> matchNewList) {
        this.matchNewList = matchNewList;
    }

    public List<Match> getMatchList() {
        return matchList;
    }

    public void setMatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}
