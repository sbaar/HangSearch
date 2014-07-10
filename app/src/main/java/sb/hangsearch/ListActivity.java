package sb.hangsearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sb.hangsearch.R;

/**
 * Displays the search results for users. Leads to the User Detail Activity
 */
public class ListActivity extends Activity {

    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListView = (ListView) findViewById(R.id.list_view);

        //populate the list with all the users in the database
        mListView.setAdapter(new ListAdapter(this, UserSQLHelper.getInstance(this).getUserList()));

        //launch detail activity when we click on an item
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter.ViewHolder viewHolder = (ListAdapter.ViewHolder) view.getTag();
                String objectID = viewHolder.objectID;
                Intent i = new Intent(getApplicationContext(),UserDetailActivity.class);
                i.putExtra("objectID", objectID);   //put the unique objectID as a parameter
                startActivity(i);
            }
        });

    }

}
