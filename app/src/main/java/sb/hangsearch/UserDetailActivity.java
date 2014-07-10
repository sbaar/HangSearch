package sb.hangsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import sb.hangsearch.R;

/**
 * Displays user details in a scrollview so it can be seen on all screen sizes
 */
public class UserDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent i = getIntent();
        String objectID = i.getStringExtra("objectID"); //grab objectID parameter
        User user = UserSQLHelper.getInstance(this).getUser(objectID);  //get User from db

        //identify views
        TextView nameText = (TextView) findViewById(R.id.name_text);
        TextView usernameText = (TextView) findViewById(R.id.username_text);
        TextView broadcastCountText = (TextView) findViewById(R.id.broadcast_count);
        TextView broadcastingText = (TextView) findViewById(R.id.broadcasting);
        TextView followerCountText = (TextView) findViewById(R.id.follower_count);
        TextView followingCountText = (TextView) findViewById(R.id.follwing_count);
        ImageView verifyImageView = (ImageView) findViewById(R.id.verifyImageView);
        NetworkImageView avatarView = (NetworkImageView) findViewById(R.id.detail_image_view);

        //load avatar if applicable
        ImageLoader.ImageCache imageCache = new BitmapLRUCache();
        ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);
        if (!user.getAvatarURL().equals("")){
            avatarView.setImageUrl(user.getAvatarURL(), imageLoader);
        }

        //set fields
        nameText.setText(nameText.getText() + user.getName());
        usernameText.setText(usernameText.getText() + user.getUsername());
        broadcastCountText.setText(broadcastCountText.getText() + String.valueOf(user.getBroadcast_count()));
        broadcastingText.setText(broadcastingText.getText() + String.valueOf(user.isBroadcasting()));
        followerCountText.setText(followerCountText.getText() + String.valueOf(user.getFollower_count()));
        followingCountText.setText(followingCountText.getText() + String.valueOf(user.getFollowing_count()));

        //color badge if verified
        if (user.isVerified()) verifyImageView.setImageResource(R.drawable.icon_verified);
        else verifyImageView.setImageResource(R.drawable.icon_verified_grey);
    }



}
