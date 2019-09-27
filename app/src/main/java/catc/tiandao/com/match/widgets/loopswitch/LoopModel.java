package catc.tiandao.com.match.widgets.loopswitch;

public class LoopModel {

  private String title;
  private String url;
  private String targetUrl;
  private String type;
  private String des;

  public LoopModel(String title, String url,String targetUrl,String type,String des) {
    this.title = title;
    this.url = url;
    this.targetUrl = targetUrl;
    this.type = type;
    this.des = des;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTargetUrl() {
    return targetUrl;
  }

  public void setTargetUrl(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDes() {
    return des;
  }

  public void setDes(String des) {
    this.des = des;
  }
}
