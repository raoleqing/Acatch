package catc.tiandao.com.matchlibrary.ben;

public class BasketJiShuTongJi {

    //{\"techId\":1,\"techName\":null,\"ZhuDui\":\"18\",\"KeDui\":\"7\",\"iOrder\":0}

    private int techId;
    private String techName;
    private float ZhuDui;
    private float KeDui;
    private int iOrder;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public float getZhuDui() {
        return ZhuDui;
    }

    public void setZhuDui(float zhuDui) {
        ZhuDui = zhuDui;
    }

    public float getKeDui() {
        return KeDui;
    }

    public void setKeDui(float keDui) {
        KeDui = keDui;
    }

    public int getiOrder() {
        return iOrder;
    }

    public void setiOrder(int iOrder) {
        this.iOrder = iOrder;
    }
}
