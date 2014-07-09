package sb.hangsearch;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class SearchIntentService extends IntentService {
    //the action our intents can specify
    public static final String ACTION_USER_SEARCH = "sb.hangsearch.action.USER_SEARCH";

    //the extras that come with our intents
    public static final String EXTRA_USER_NAME = "sb.hangsearch.extra.EXTRA_USER_NAME";
    public static final String baseUserSearchURL = "https://api-v1.hangwith.com/v1/users?username=";

    public SearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_USER_SEARCH.equals(action)) {
                final String userName = intent.getStringExtra(EXTRA_USER_NAME);
                handleActionUserSearch(userName);
            }
        }
    }

    /**
     * Handle user search in the background thread of the service
     */
    private void handleActionUserSearch(final String userName ) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = baseUserSearchURL + userName;
        JsonArrayRequest usersRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());

                List<User> userList = new ArrayList<User>();
                User u;
                JSONObject userJSON;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        u = new User();
                        userJSON = response.getJSONObject(i);
                        u.setBroadcast_count(userJSON.getInt("broadcast_count"));
                        u.setBroadcasting(userJSON.getBoolean("broadcasting"));
                        u.setFollower_count(userJSON.getInt("follower_count"));
                        u.setFollowing_count(userJSON.getInt("following_count"));
                        u.setName(userJSON.getString("name"));
                        u.setUsername(userJSON.getString("username"));
                        u.setAvatarURL((userJSON.getString("avatar_url")));

                        userList.add(u);
                    }
                }
                catch (JSONException e){
                    Log.d("JSON parse error", e.toString());

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                // TODO Auto-generated method stub

            }
        });
        queue.add(usersRequest);

    }

}
