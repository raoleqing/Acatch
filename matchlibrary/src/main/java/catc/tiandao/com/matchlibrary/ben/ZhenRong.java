package catc.tiandao.com.matchlibrary.ben;

public class ZhenRong {

    //{\"id\":41437,\"first\":1,\"name\":\"扬尼斯\",\"shirt_number\":\"1\",\"position\":\"G\"}

   /* 1."id": 33712,//球员id
            2."first ": 1,//是否首发，1-是 0-否
            3."name": "约书亚·里登顿",//球员名称
            4."logo": "48989a56b066857e32fa5fb6d77df9a9.png",//球员logo,地址前缀:http://cdn.sportnanoapi.com/football/player/48989a56b066857e32fa5fb6d77df9a9.png
            5."shirt_number": 4,//球衣号
            6."position": "D",//球员位置,F前锋 M中锋 D后卫 G守门员,其他为未知*/

   private String id;
   private int first;
   private String name;
   private int shirt_number;
   private String position;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShirt_number() {
        return shirt_number;
    }

    public void setShirt_number(int shirt_number) {
        this.shirt_number = shirt_number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
