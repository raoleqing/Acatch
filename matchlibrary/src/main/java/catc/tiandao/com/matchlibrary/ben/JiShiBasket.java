package catc.tiandao.com.matchlibrary.ben;

public class JiShiBasket {

    // {\"time\":\"12:00\",\"type\":2,\"BiFen\":\"0-0\",\"MiaoShu\":\"德维恩·戴德蒙 vs. 鲁迪·戈贝尔 (内马尼亚·别利察 得到球)\"},

    private String time;
    private int type;
    private String BiFen;
    private String MiaoShu;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBiFen() {
        return BiFen;
    }

    public void setBiFen(String biFen) {
        BiFen = biFen;
    }

    public String getMiaoShu() {
        return MiaoShu;
    }

    public void setMiaoShu(String miaoShu) {
        MiaoShu = miaoShu;
    }
}
