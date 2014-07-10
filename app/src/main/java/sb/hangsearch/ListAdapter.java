package sb.hangsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.List;

/**
 * Created by ScottB on 7/10/2014.
 */
public class ListAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> mUserList;
    private ImageLoader mImageLoader;
    public ListAdapter(Context context, List<User> userList){
        super(context, R.layout.list_row,userList);
        mContext = context;
        mUserList = userList;

        ImageLoader.ImageCache imageCache = new BitmapLRUCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);

    }
    public class ViewHolder{
        String objectID;    //use this to compare equality
        NetworkImageView networkImageView;
        TextView topText;
        TextView bottomText;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final User user = mUserList.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row,parent, false);
            ViewHolder newHolder = new ViewHolder();
            newHolder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.list_image_view);
            newHolder.topText = (TextView) convertView.findViewById(R.id.top_text);
            newHolder.bottomText = (TextView) convertView.findViewById(R.id.bottom_text);
            convertView.setTag(newHolder);
        }
        ViewHolder vh = (ViewHolder) convertView.getTag();
        vh.topText.setText(user.getName());
        vh.bottomText.setText(user.getUsername());
        vh.networkImageView.setDefaultImageResId(R.drawable.ic_launcher);
        vh.networkImageView.setErrorImageResId(R.drawable.ic_launcher);
        vh.networkImageView.setImageResource(R.drawable.ic_launcher);

        //if there is an avatar url, load it
        if (!user.getAvatarURL().equals("")){
            vh.networkImageView.setImageUrl(user.getAvatarURL(), mImageLoader);
        }
        return convertView;
    }
}
