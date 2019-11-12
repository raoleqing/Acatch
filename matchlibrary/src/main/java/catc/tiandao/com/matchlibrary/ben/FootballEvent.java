package catc.tiandao.com.matchlibrary.ben;

public class FootballEvent {

    /*{
       "id":90,"shortName":"英北超","iUserChoose":1,"areaName":"欧洲"
    },*/


    private int id;
    private String shortName;
    private int iUserChoose;
    private String areaName;
    private int type; // 0 为全部， 1 正赛

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getiUserChoose() {
        return iUserChoose;
    }

    public void setiUserChoose(int iUserChoose) {
        this.iUserChoose = iUserChoose;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
