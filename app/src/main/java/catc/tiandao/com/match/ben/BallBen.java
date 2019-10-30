package catc.tiandao.com.match.ben;

import java.io.Serializable;

public class BallBen implements Serializable {
   /*
    "matchId": 2767082,//赛事ID
            "matchStatusId": 4, //1-未开赛，2-7都是进行中， 8结束，大于8都是中断、取消类的
            "matchStatus": "下半场",// //未开始 进行中  已结束
            "matchTime": "09:00", //"比赛开始时间（一般展示这个）,
            "matchEventName": "哥伦乙附",//比赛名称
            "matchRound": 2, //表示第几轮比赛
            "matchBeginTime": "53'", //当matchStatus为上半场或下半场的时候列出,代表第58分钟
            "isCollection": 0, //1已收藏 0未收藏
            "HasOvertime": 0,
            "HasPenalty": 0,
            "homeTeamLogoUrl": "http://www.leisuvip1.com/images/LSQB/hxxf.jpg",//球队图标URL
            "homeTeamName": "奎迪奥",//球队名称
            "homeHalfScore": 0,//半场得分
            "homeTeamScore": 0,//全场得分
            "homeTeamCorner": 3,//角球
            "homeTeamOvertime": 0,//加时得分
            "homeTeamPenalty": 0,//点球
            "homeTeamRed": 0,//红牌
            "homeTeamYellow": 2,//黄牌
            "awayTeamLogoUrl": "http://www.leisuvip1.com/images/LSQB/shsg.png",
            "awayTeamName": "佩雷拉",
            "awayHalfScore": 0,
            "awayTeamScore": 0,
            "awayTeamCorner": 5,
            "awayTeamOvertime": 0,
            "awayTeamPenalty": 0,
            "awayTeamRed": 0,
            "awayTeamYellow": 1*/


    private String matchId;
    private int matchStatusId;
    private String matchStatus;
    private String matchTime;
    private String matchEventName;
    private int matchRound;
    private String matchBeginTime;
    private int isCollection;
    private int HasOvertime;
    private int HasPenalty;
    private String homeTeamLogoUrl;
    private String homeTeamName;
    private int homeHalfScore;
    private int homeTeamScore;
    private int homeTeamCorner;
    private int homeTeamOvertime;
    private int homeTeamPenalty;
    private int homeTeamRed;
    private int homeTeamYellow;
    private String awayTeamLogoUrl;
    private String awayTeamName;
    private int awayHalfScore;
    private int awayTeamScore;
    private int awayTeamCorner;
    private int awayTeamOvertime;
    private int awayTeamPenalty;
    private int awayTeamRed;
    private int awayTeamYellow;
    private String Note;


    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
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

    public int getHasOvertime() {
        return HasOvertime;
    }

    public void setHasOvertime(int hasOvertime) {
        HasOvertime = hasOvertime;
    }

    public int getHasPenalty() {
        return HasPenalty;
    }

    public void setHasPenalty(int hasPenalty) {
        HasPenalty = hasPenalty;
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

    public int getHomeHalfScore() {
        return homeHalfScore;
    }

    public void setHomeHalfScore(int homeHalfScore) {
        this.homeHalfScore = homeHalfScore;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public int getHomeTeamCorner() {
        return homeTeamCorner;
    }

    public void setHomeTeamCorner(int homeTeamCorner) {
        this.homeTeamCorner = homeTeamCorner;
    }

    public int getHomeTeamOvertime() {
        return homeTeamOvertime;
    }

    public void setHomeTeamOvertime(int homeTeamOvertime) {
        this.homeTeamOvertime = homeTeamOvertime;
    }

    public int getHomeTeamPenalty() {
        return homeTeamPenalty;
    }

    public void setHomeTeamPenalty(int homeTeamPenalty) {
        this.homeTeamPenalty = homeTeamPenalty;
    }

    public int getHomeTeamRed() {
        return homeTeamRed;
    }

    public void setHomeTeamRed(int homeTeamRed) {
        this.homeTeamRed = homeTeamRed;
    }

    public int getHomeTeamYellow() {
        return homeTeamYellow;
    }

    public void setHomeTeamYellow(int homeTeamYellow) {
        this.homeTeamYellow = homeTeamYellow;
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

    public int getAwayHalfScore() {
        return awayHalfScore;
    }

    public void setAwayHalfScore(int awayHalfScore) {
        this.awayHalfScore = awayHalfScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public int getAwayTeamCorner() {
        return awayTeamCorner;
    }

    public void setAwayTeamCorner(int awayTeamCorner) {
        this.awayTeamCorner = awayTeamCorner;
    }

    public int getAwayTeamOvertime() {
        return awayTeamOvertime;
    }

    public void setAwayTeamOvertime(int awayTeamOvertime) {
        this.awayTeamOvertime = awayTeamOvertime;
    }

    public int getAwayTeamPenalty() {
        return awayTeamPenalty;
    }

    public void setAwayTeamPenalty(int awayTeamPenalty) {
        this.awayTeamPenalty = awayTeamPenalty;
    }

    public int getAwayTeamRed() {
        return awayTeamRed;
    }

    public void setAwayTeamRed(int awayTeamRed) {
        this.awayTeamRed = awayTeamRed;
    }

    public int getAwayTeamYellow() {
        return awayTeamYellow;
    }

    public void setAwayTeamYellow(int awayTeamYellow) {
        this.awayTeamYellow = awayTeamYellow;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
