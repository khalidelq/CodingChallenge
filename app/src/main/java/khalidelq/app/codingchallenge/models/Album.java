package khalidelq.app.codingchallenge.models;

/**
 * Created by adil on 11/12/2017.
 */

public class Album {

    private String name;
    private String url;
    private String id;

    public Album(String name, String url, String id) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public Album() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
