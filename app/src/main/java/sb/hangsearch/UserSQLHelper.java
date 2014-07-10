package sb.hangsearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ScottB on 7/9/2014.
 */
public class UserSQLHelper  extends SQLiteOpenHelper{
    public static UserSQLHelper mInstance = null;
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_USERS = "USERS";

    public static final String COL_ID = "_ID";
    public static final  String COL_OBJECT_ID = "OBJECT_ID";
    public static final  String COL_BROADCAST_COUNT = "BROADCAST_COUNT";
    public static final  String COL_BROADCASTING = "BROADCASTING"; //0 for false, 1 for true
    public static final  String COL_FOLLOWER_COUNT = "FOLLOWER_COUNT";
    public static final  String COL_FOLLOWING_COUNT = "FOLLOWING_COUNT";
    public static final  String COL_NAME = "NAME";
    public static final String COL_USERNAME = "USERNAME";
    public static final  String COL_AVATAR_URL = "AVATAR_URL";
    public static final String COL_VERIFIED = "VERIFIED";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_USERS + "(" + COL_ID + " integer primary key AUTOINCREMENT, "
            + COL_OBJECT_ID + " TEXT, "
            + COL_BROADCAST_COUNT + " INTEGER, "
            + COL_BROADCASTING + " INTEGER, " //0 for false, 1 for true
            + COL_FOLLOWER_COUNT + " INTEGER, "
            + COL_FOLLOWING_COUNT + " INTEGER, "
            + COL_NAME + " TEXT, "
            + COL_USERNAME + " TEXT, "
            + COL_AVATAR_URL + " TEXT, "
            + COL_VERIFIED + " INTEGER"
            + ")";

    private UserSQLHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //ensures there's only one Helper at a time
    public static UserSQLHelper getInstance(Context context){
        if (mInstance == null)
            mInstance = new UserSQLHelper(context.getApplicationContext());
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            Log.d("Users.db create", DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);

    }

    /**
     * Helper to create content values to insert new user into db
     * @param user User to insert
     * @return ContentValues ready to insert
     */
    private ContentValues addValues(User user){
        ContentValues values = new ContentValues();
        values.put(COL_OBJECT_ID, user.getObjectID());
        values.put(COL_BROADCAST_COUNT, user.getBroadcast_count());
        values.put(COL_BROADCASTING, user.isBroadcasting() ? 1 : 0);// 1 for true, 0 for false
        values.put(COL_FOLLOWER_COUNT, user.getFollower_count());
        values.put(COL_FOLLOWING_COUNT, user.getFollowing_count());
        values.put(COL_NAME, user.getName());
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_AVATAR_URL, user.getAvatarURL());
        values.put(COL_VERIFIED, user.isVerified() ? 1 : 0);

        return values;
    }


    /**
     * Delete all users from db, then insert new ones. We don't have to worry about synchroinzation here because all writes come from
     * SearchIntentService, which handles intents one at a time.
     * @param userList users to insert
     * @return the error string, or null if succesful
    **/

    public  String overWriteUsers(List<User> userList){
       SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            db.delete(TABLE_USERS, null, null);
            for (User u : userList){
                ContentValues contentValues = addValues(u);
                db.insert(TABLE_USERS,null,contentValues);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        catch (Exception e){
            return e.toString();
        }
        return null;


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Queries for all columns of all entries.
     * @return all list of users in the database
     */
    public List<User> getUserList() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        List<User> userList = new ArrayList<User>();
        User user;
        try{
            cursor = db.query(TABLE_USERS,null,null,null,null,null,null);   //get all users
            if (cursor != null)
            while (cursor.moveToNext()) {       //iterate over cursor
                user = new User();
                user.setObjectID(cursor.getString(1));
                user.setBroadcast_count(cursor.getInt(2));
                user.setBroadcasting(cursor.getInt(3) == 1);    //yes, this is the correct way to store booleans
                user.setFollower_count(cursor.getInt(4));
                user.setFollowing_count(cursor.getInt(5));
                user.setName(cursor.getString(6));
                user.setUsername(cursor.getString(7));
                user.setAvatarURL(cursor.getString(8));
                user.setVerified(cursor.getInt(9) == 1);
                userList.add(user);
            }

        }
        catch (Exception e){
            Log.e("DB get users error: ", e.toString());

        }
        finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return userList;
    }

    /**
     * Get a single user based on objectid
     * @param objectID the paramter to be compared in the where clause
     * @return the corresponding User
     */
    public User getUser(String objectID) {
        SQLiteDatabase db = getReadableDatabase();
        User user = new User();
        Cursor cursor = null;
        try{
            cursor = db.query(TABLE_USERS,null,COL_OBJECT_ID + " = '" +objectID+ "'" ,null,null,null,null);   //get user from objectID
            if (cursor != null)
                while (cursor.moveToNext()) {
                    user.setObjectID(cursor.getString(1));
                    user.setBroadcast_count(cursor.getInt(2));
                    user.setBroadcasting(cursor.getInt(3) == 1);    //this is the correct way to store booleans
                    user.setFollower_count(cursor.getInt(4));
                    user.setFollowing_count(cursor.getInt(5));
                    user.setName(cursor.getString(6));
                    user.setUsername(cursor.getString(7));
                    user.setAvatarURL(cursor.getString(8));
                    user.setVerified(cursor.getInt(9) == 1);
                }

        }
        catch (Exception e){
            Log.e("DB get user error: ", e.toString());

        }
        finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return user;
    }
}
