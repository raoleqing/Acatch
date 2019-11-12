package catc.tiandao.com.matchlibrary.ben;

public class MainNewsBen {

   /* "data": [{
        "id": 13,
                "cTitle": "罗马官宣姆希塔良租借加盟将承担全部18万英镑周薪",
                "titleImageUrl": "http://www.leisuvip1.com/Image/GetImage/25810",
                "dtPublish": "10-26 15:36:00"
    }*/

   private int id;
   private String cTitle;
   private String titleImageUrl;
   private String dtPublish;
   private int iTop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getTitleImageUrl() {
        return titleImageUrl;
    }

    public void setTitleImageUrl(String titleImageUrl) {
        this.titleImageUrl = titleImageUrl;
    }

    public String getDtPublish() {
        return dtPublish;
    }

    public void setDtPublish(String dtPublish) {
        this.dtPublish = dtPublish;
    }

    public int getiTop() {
        return iTop;
    }

    public void setiTop(int iTop) {
        this.iTop = iTop;
    }
}
