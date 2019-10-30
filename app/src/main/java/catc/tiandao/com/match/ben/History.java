package catc.tiandao.com.match.ben;

public class History {

     //{\"dt\":\"2019-04-16T01:00:00+08:00\",\"eventName\":\"瑞典超\",\
    // "team1\":\"佐加顿斯\",\"team1Score\":2,\"score\":\"2-1\",\"team2\":\"哥德堡\",\"team2Score\":1,\"panlu\":\"1.0赢\"}

    private String dt;
    private String eventName;
    private String team1;
    private int team1Score;
    private String score;
    private String team2;
    private int team2Score;
    private String panlu;


    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getPanlu() {
        return panlu;
    }

    public void setPanlu(String panlu) {
        this.panlu = panlu;
    }
}
