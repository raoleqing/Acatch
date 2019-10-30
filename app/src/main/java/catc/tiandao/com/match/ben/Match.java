package catc.tiandao.com.match.ben;

import java.io.Serializable;

public class Match implements Serializable {


/*    	"matchId": 3504313,
                "matchName": null,
                "matchStatusId": 10,
                "matchStatus": "中断",
                "matchDate": "2019-10-19",
                "matchWeek": "六",
                "matchTime": "23:59",
                "matchEventName": "匈甲",
                "matchRound": 0,
                "matchBeginTime": 0,
                "isCollection": 0,
                "homeTeamLogoUrl": "http:\/\/www.leisuvip1.com\/images\/LSQB\/yongshidui.jpeg",
                "homeTeamName": "",
                "awayTeamLogoUrl": "http:\/\/www.leisuvip1.com\/images\/LSQB\/yongshidui.jpeg",
                "awayTeamName": "卡普斯华利"


                 "matchId": "xxx",   //赛事ID
            "matchStatusId": "xxx",   //1-未开赛，2-9都是进行中， 10结束，大于10都是中断、取消类的
            "matchName":"NBA常规赛",      //比赛名称
            "matchStatus":"",             //未开始 进行中  已结束
“matchDate”：日期
“matchWeek”：“四”，//星期四
            "matchTime": “18:00”,//18点开球
" matchEventName ": "法甲,
            "isCollection": 1,    //1已收藏 0未收藏
			//主队
			"homeTeamLogoUrl":"xxxx",    //球队图标URL
			"homeTeamName":"广州恒大",    //球队名称
			//客队
			"awayTeamLogoUrl":"xxxx",    //球队图标URL
			"awayTeamName":"广州恒大",    //球队名称


                */




    private String matchId;////赛事ID
    private String matchName;/////比赛名称
    private int matchStatusId;//1-未开赛，2-9都是进行中， 10结束，大于10都是中断、取消类的
    private String matchStatus;//未开始 进行中  已结束
    private String matchDate;//日期
    private String matchWeek;//星期
    private String matchTime;//18点开球
    private String matchEventName;//法甲
    private int matchRound;
    private String matchBeginTime;//
    private int isCollection;////1已收藏 0未收藏
    private String homeTeamLogoUrl;////主队 球队图标URL
    private String homeTeamName;//主队 球队名称
    private String awayTeamLogoUrl;//球队图标URL
    private String awayTeamName;//球队名称


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public int getMatchStatusId() {
        return matchStatusId;
    }

    public void setMatchStatusId(int matchStatusId) {
        this.matchStatusId = matchStatusId;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchWeek() {
        return matchWeek;
    }

    public void setMatchWeek(String matchWeek) {
        this.matchWeek = matchWeek;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchEventName() {
        return matchEventName;
    }

    public void setMatchEventName(String matchEventName) {
        this.matchEventName = matchEventName;
    }

    public int getMatchRound() {
        return matchRound;
    }

    public void setMatchRound(int matchRound) {
        this.matchRound = matchRound;
    }

    public String getMatchBeginTime() {
        return matchBeginTime;
    }

    public void setMatchBeginTime(String matchBeginTime) {
        this.matchBeginTime = matchBeginTime;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public String getHomeTeamLogoUrl() {
        return homeTeamLogoUrl;
    }

    public void setHomeTeamLogoUrl(String homeTeamLogoUrl) {
        this.homeTeamLogoUrl = homeTeamLogoUrl;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamLogoUrl() {
        return awayTeamLogoUrl;
    }

    public void setAwayTeamLogoUrl(String awayTeamLogoUrl) {
        this.awayTeamLogoUrl = awayTeamLogoUrl;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }
}
