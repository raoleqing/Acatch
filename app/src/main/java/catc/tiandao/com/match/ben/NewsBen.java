package catc.tiandao.com.match.ben;

public class NewsBen {


    /*news":[{
        "newId": "xxxx",
                "title": "新闻的标题",
                "videoUrl": "视频的播放URL",
                "imageUrl": "视频的默认背景图片",       //如果是视频的，就是视频的背景图，如果是普通的，就是新闻配图
                "shareCount": "838",                 //新闻分享人数
                "comment": "1984",                   //新闻评论人数
                "like": "2535",                      //新闻点赞人数
                "shareInfo":{
            "shareTitle"   : "分享的标题",
                    "shareDesc"    : "分享的描述文本",
                    "shareImageUrl": "图片的url可以用本地icon也行",
                    "shareLink"    : "分享点击跳转的链接"
        },
        "isLike": 0         //是否点过赞 0否1是
    }]
    */

    private int newId;
    private String title;
    private String videoUrl;
    private String imageUrl;
    private int shareCount;
    private int comment;
    private int like;
    private int isLike;
    private ShareInfoBan shareInfo;

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public ShareInfoBan getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfoBan shareInfo) {
        this.shareInfo = shareInfo;
    }
}
