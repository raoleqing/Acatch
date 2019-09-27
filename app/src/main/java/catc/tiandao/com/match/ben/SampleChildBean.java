package catc.tiandao.com.match.ben;

public class SampleChildBean {
    private String mName;
    private int position;

    public SampleChildBean(String name,int position) {
        this.mName = name;
        this.position = position;
    }

    public String getName() {
        return mName;
    }

    public int getPosition() {
        return position;
    }
}
