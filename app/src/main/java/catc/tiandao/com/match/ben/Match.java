package catc.tiandao.com.match.ben;

public class Match {


    private int matchId;////赛事ID
    private String matchTime;// //赛事时间
    private String matchName;////赛事具体名称
    private int matchNum;//第几轮比赛
    private long matchBeginTime;//具体时间
    private int matchStatus;
    private MainTeam mainTeam;
    private BeMatch beMatchTeam;





    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(int matchNum) {
        this.matchNum = matchNum;
    }

    public long getMatchBeginTime() {
        return matchBeginTime;
    }

    public void setMatchBeginTime(long matchBeginTime) {
        this.matchBeginTime = matchBeginTime;
    }

    public MainTeam getMainTeam() {
        return mainTeam;
    }

    public void setMainTeam(MainTeam mainTeam) {
        this.mainTeam = mainTeam;
    }

    public BeMatch getBeMatchTeam() {
        return beMatchTeam;
    }

    public void setBeMatchTeam(BeMatch beMatchTeam) {
        this.beMatchTeam = beMatchTeam;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }
}
