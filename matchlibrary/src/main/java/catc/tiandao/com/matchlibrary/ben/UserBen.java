package catc.tiandao.com.matchlibrary.ben;

public class UserBen {

    //"token":"2dac2028-526c-40d9-9546-3547d298ba5c","nickName":"13006884459","iconUrl":null
    //{"userId":104,"sex":1,"token":"1b4fb49e-b01a-4133-a1a1-065ed4047fc2","nickName":"13873146635","iconUrl":null}}

    private String userId;
    private String phone;
    private String token;
    private String nickName;
    private String iconUrl;
    private String sex;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
