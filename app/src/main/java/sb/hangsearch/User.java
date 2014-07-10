package sb.hangsearch;

/**
 * Created by ScottB on 7/9/2014.
 */
public class User {

    private String objectID;
    private int broadcast_count;
    private boolean broadcasting;
    private int follower_count;
    private int following_count;
    private String name;
    private String username;
    private String avatarURL;


    //generated getters and setters
    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int getBroadcast_count() {
        return broadcast_count;
    }

    public void setBroadcast_count(int broadcast_count) {
        this.broadcast_count = broadcast_count;
    }

    public boolean isBroadcasting() {
        return broadcasting;
    }

    public void setBroadcasting(boolean broadcasting) {
        this.broadcasting = broadcasting;
    }

    public int getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(int follower_count) {
        this.follower_count = follower_count;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }
}
