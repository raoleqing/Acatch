package catc.tiandao.com.matchlibrary.ben;

public class NewsBen {


   /* "id": 13,
            "iDianZanCount": 0,
            "iZhuanFaCount": 0,
            "cCommentCount": 0,
            "cTitle": "罗马官宣姆希塔良租借加盟将承担全部18万英镑周薪",
            "titleImageUrl": "http://www.leisuvip1.com/Image/GetImage/25815",
            "titleVideoUrl": "",
            "dtPublish": "10-26 15:36:00"*/

    private int id;
    private int iDianZanCount;
    private int iZhuanFaCount;
    private int cCommentCount;
    private String cTitle;
    private String titleImageUrl;
    private String titleVideoUrl;
    private String dtPublish;
    private String newDetail;
    private int hasZan;
    private int isSelet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getiDianZanCount() {
        return iDianZanCount;
    }

    public void setiDianZanCount(int iDianZanCount) {
        this.iDianZanCount = iDianZanCount;
    }

    public int getiZhuanFaCount() {
        return iZhuanFaCount;
    }

    public void setiZhuanFaCount(int iZhuanFaCount) {
        this.iZhuanFaCount = iZhuanFaCount;
    }

    public int getcCommentCount() {
        return cCommentCount;
    }

    public void setcCommentCount(int cCommentCount) {
        this.cCommentCount = cCommentCount;
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

    public String getTitleVideoUrl() {
        return titleVideoUrl;
    }

    public void setTitleVideoUrl(String titleVideoUrl) {
        this.titleVideoUrl = titleVideoUrl;
    }

    public String getDtPublish() {
        return dtPublish;
    }

    public void setDtPublish(String dtPublish) {
        this.dtPublish = dtPublish;
    }

    public int getIsSelet() {
        return isSelet;
    }

    public void setIsSelet(int isSelet) {
        this.isSelet = isSelet;
    }


    public int getHasZan() {
        return hasZan;
    }

    public void setHasZan(int hasZan) {
        this.hasZan = hasZan;
    }

    public String getNewDetail() {
        return newDetail;
    }

    public void setNewDetail(String newDetail) {
        this.newDetail = newDetail;
    }
}
