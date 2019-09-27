package catc.tiandao.com.match.ben;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name = "指数")
public class ExponentBen {


    @SmartColumn(id =1,name = "公司")
    private String name;

    @SmartColumn(id=2,name="初盘")
    private String firt1;
    @SmartColumn(id=3,name="初盘")
    private String firt2;
    @SmartColumn(id=4,name="初盘")
    private String firt3;

    @SmartColumn(id=5,name="即时盘")
    private String firt4;
    @SmartColumn(id=6,name="即时盘")
    private String firt5;
    @SmartColumn(id=7,name="即时盘")
    private String firt6;

    @SmartColumn(id=8,name="滚球盘")
    private String firt7;
    @SmartColumn(id=9,name="滚球盘")
    private String firt8;
    @SmartColumn(id=10,name="滚球盘")
    private String firt9;


    public ExponentBen(String name, String firt1, String firt2, String firt3,String firt4, String firt5, String firt6,String firt7, String firt8, String firt9) {
        this.name = name;
        this.firt1 = firt1;
        this.firt2 = firt2;
        this.firt3 = firt3;
        this.firt4 = firt4;
        this.firt5 = firt5;
        this.firt6 = firt6;
        this.firt7 = firt7;
        this.firt8 = firt8;
        this.firt9 = firt9;
    }




}
