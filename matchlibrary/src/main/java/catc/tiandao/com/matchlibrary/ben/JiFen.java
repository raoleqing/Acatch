package catc.tiandao.com.matchlibrary.ben;

public class JiFen {

    //{\"name\":\"佐加顿斯\",\
    // "played\":27,\
    // "won\":18,\
    // "drawn\":5,\
    // "lost\":4,\
    // "jin_shi\":\"47/17\",
    // \"diff\":30,\
    // "pts\":59,\
    // "position\":3,\
    // "isHome\":0},

    private String name;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private String jin_shi;
    private int diff;
    private int pts;
    private int position;
    private int isHome;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getDrawn() {
        return drawn;
    }

    public void setDrawn(int drawn) {
        this.drawn = drawn;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public String getJin_shi() {
        return jin_shi;
    }

    public void setJin_shi(String jin_shi) {
        this.jin_shi = jin_shi;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsHome() {
        return isHome;
    }

    public void setIsHome(int isHome) {
        this.isHome = isHome;
    }
}
