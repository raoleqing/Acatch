package catc.tiandao.com.match.ben;

public class NewsBen {


    /*news":[{
        { iDianZanCount:点赞数,
iZhuanFaCount:转发数,
 cCommentCount:评论数,
cTitle:标题,
titleImageUrl:带标题图片就显示标题图片,
titleVideoUrl:带视频就显示视频（图片和视频只会有一个）
dtPublish：发布时间

        "iDianZanCount": 0,
		"iZhuanFaCount": 0,
		"cCommentCount": 0,
		"cTitle": "测试777777777",
		"titleImageUrl": "http://www.leisuvip1.com/Image/GetImage/25810",
		"titleVideoUrl": "http://www.leisuvip1.com/Image/GetImage/",
		"dtPublish": "10-26 15:36:00"


    }]
    */

    private int id;
    private int iDianZanCount;
    private int iZhuanFaCount;
    private int cCommentCount;
    private String cTitle;
    private String titleImageUrl;
    private String titleVideoUrl;
    private String dtPublish;
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
}
