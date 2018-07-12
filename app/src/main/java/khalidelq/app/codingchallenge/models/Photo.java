package khalidelq.app.codingchallenge.models;



public class Photo {

    private String lowResUrl;
    private String highResUrl;
    private String id;


    public Photo(String lowResUrl, String highResUrl, String id) {
        this.lowResUrl = lowResUrl;
        this.highResUrl = highResUrl;
        this.id = id;
    }

    public Photo(String lowResUrl, String id) {
        this.lowResUrl = lowResUrl;
        this.id = id;
    }

    public Photo() {
    }

    public String getLowResUrl() {
        return lowResUrl;
    }

    public void setLowResUrl(String lowResUrl) {
        this.lowResUrl = lowResUrl;
    }

    public String getHighResUrl() {
        return highResUrl;
    }

    public void setHighResUrl(String highResUrl) {
        this.highResUrl = highResUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
