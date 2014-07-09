package sb.hangsearch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.

 */
public class SearchActivity extends Activity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mUserBox;
    private View mProgressView;
    private View mSearchForm;
    private Button mSearchButton;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;

        mUserBox = (EditText) findViewById(R.id.user_text_box);
        mSearchForm = findViewById(R.id.search_form);
        mProgressView = findViewById(R.id.progressBar);
        mSearchButton = (Button)findViewById(R.id.search_button);

        mUserBox.addTextChangedListener(getSearchBoxChangedListener()); //add form validation function

        //if we're coming back to the activity like from a rotate, restore the user's form entry
        if (savedInstanceState != null)
            mUserBox.setText(savedInstanceState.getString("userBox"));
        else mUserBox.setText("");//set to nothing to trigger validation

        mSearchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mActivity, SearchIntentService.class);
                i.setAction(SearchIntentService.ACTION_USER_SEARCH);
                i.putExtra(SearchIntentService.EXTRA_USER_NAME,mUserBox.getText().toString());
                startService(i);
            }
        });


    }
    //save user input during configuration change
    protected void onSaveInstanceState(Bundle extra) {
        super.onSaveInstanceState(extra);
        extra.putString("userBox", mUserBox.getText().toString());
    }


    /**
     *
     * @return A listener that disables the search button when the form is invalid
     */
    public TextWatcher getSearchBoxChangedListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <2)
                    mSearchButton.setEnabled(false);
                else
                    mSearchButton.setEnabled(true);

            }
        };
        }

    /**
     * Shows the progress UI and hides the form.
     */
    public void showProgress(final boolean show) {
        // Show and animate the indeterminate progress bar while we search.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSearchForm.setVisibility(show ? View.GONE : View.VISIBLE);
        mSearchForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSearchForm.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}






