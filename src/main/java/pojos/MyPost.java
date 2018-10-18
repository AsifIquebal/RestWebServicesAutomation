package pojos;

public class MyPost {

    private String title;
    private String body;
    private String userId;

    public String getTitle() {
        return title;
    }

    public MyPost setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public MyPost setBody(String body) {
        this.body = body;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public MyPost setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
