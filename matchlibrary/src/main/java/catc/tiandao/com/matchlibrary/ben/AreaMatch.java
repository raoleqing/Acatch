package catc.tiandao.com.matchlibrary.ben;

import java.util.List;

//首页数据
public class AreaMatch {

    /* "areaId": 1,
		"areaName": "国际",
		"matchL":
        */


    private int areaId;
    private String areaName;
    private List<MatchNew> matchL;


    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<MatchNew> getMatchL() {
        return matchL;
    }

    public void setMatchL(List<MatchNew> matchL) {
        this.matchL = matchL;
    }
}
