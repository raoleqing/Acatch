package catc.tiandao.com.match.ben;

public class ShareInfoBan {

     /*"shareTitle"   : "分享的标题",
             "shareDesc"    : "分享的描述文本",
             "shareImageUrl": "图片的url可以用本地icon也行",
             "shareLink"    : "分享点击跳转的链接"*/

     private String shareTitle;
     private String shareDesc;
     private String shareImageUrl;
     private String shareLink;

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }
}
