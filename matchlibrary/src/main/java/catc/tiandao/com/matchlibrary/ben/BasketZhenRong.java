package catc.tiandao.com.matchlibrary.ben;

public class BasketZhenRong {

    //{\"id\":11994,\"name_zh\":\"达里奥·萨里奇\",\"name_zht\":\"\",\"name_en\":\"dario saric\",\"imgUrl\":\"ceb1ffc565c505992f7be68a5182516c.png\",
    // \"QiuYi\":\"20\",\"Note\":\"21^2-7^1-4^2-2^0^4^4^0^1^1^0^1^12^7^1^1^0\",\"type\":1,\"IsShouFa\":1}

  /*  \"id\":12051,
            \"name_zh\":\"阿隆·戈登\",
            \"name_zht\":\"\",
            \"name_en\":\"aarongordon\",
            \"imgUrl\":\"e252545cb6eb9cbd0277dc148d12e4c7.png\",
            \"QiuYi\":\"0\",
            \"Note\":\"13^2-8^0-0^0-0^2^2^4^0^0^0^1^0^2^4^1^0^0\", //在场持续时间^命中次数-投篮次数^三分球投篮命中次数-三分投篮次数^罚球命中次数-罚球投篮次数^进攻篮板^防守篮板^总的篮板^助攻数^抢断数^盖帽数^失误次数^个人犯规次数^+/-值^得分^是否出场(1-出场，0-没出场)^是否在场上（0-在场上，1-没在场上）^是否是替补（1-替补，0-首发）
            \"type\":0//1-主队，2-客队
    */


    private String id;
    private String name_zh;
    private String name_zht;
    private String name_en;
    private String imgUrl;
    private int QiuYi;
    private String Note;
    private int type;
    private int IsShouFa;
    private String Online = "-1";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_zh() {
        return name_zh;
    }

    public void setName_zh(String name_zh) {
        this.name_zh = name_zh;
    }

    public String getName_zht() {
        return name_zht;
    }

    public void setName_zht(String name_zht) {
        this.name_zht = name_zht;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getQiuYi() {
        return QiuYi;
    }

    public void setQiuYi(int qiuYi) {
        QiuYi = qiuYi;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsShouFa() {
        return IsShouFa;
    }

    public void setIsShouFa(int isShouFa) {
        IsShouFa = isShouFa;
    }

    public String getOnline() {
        return Online;
    }

    public void setOnline(String online) {
        Online = online;
    }
}
