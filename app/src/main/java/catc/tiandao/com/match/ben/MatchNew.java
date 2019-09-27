package catc.tiandao.com.match.ben;

public class MatchNew {

//    {
//        "matchId":"xxx",                                //赛事ID
//            "title":"巴西国际vs桑托斯首发：XXX 先发领衔三叉戟",   //标题
//            "time" : "19:30",                               //时间
//            "matchInfo": "巴西国际vs桑托斯",                  //对战球队信息
//            "imageUrl": "xxxx",                             //赛事新闻URL
//            "isFirst" : 1                                  //是否置顶 0否 1是
//    }


    private int matchId;
    private String title;
    private String time;
    private String matchInfo;
    private String imageUrl;
    private int isFirst;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(String matchInfo) {
        this.matchInfo = matchInfo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }
}
