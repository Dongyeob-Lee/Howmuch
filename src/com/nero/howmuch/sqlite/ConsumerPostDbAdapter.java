package com.nero.howmuch.sqlite;

import java.util.ArrayList;

import com.nero.howmuch.Utils;
import com.nero.howmuch.consumer.ConsumerPost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConsumerPostDbAdapter {

	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE = "date";
	public static final String KEY_CONSUMER = "consumer_name";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_CITY = "city";
	public static final String KEY_CONTENTS = "contents";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_ROWID = "_id";
	private static final String TAG = "ConsumerPostDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table consumer_posts (_id integer primary key, "
			+ "title text not null, date text not null, consumer_name text not null, category text not null, city text not null, contents text not null, image text);";
	private static final String DATABASE_NAME = "consumer";
	private static final String DATABASE_TABLE = "consumer_posts";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("database", "oncreate");
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS consumer_posts");
			onCreate(db);
		}
	}

	public ConsumerPostDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public ConsumerPostDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long addPost(ConsumerPost consumerPost) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID, consumerPost.getId());
		initialValues.put(KEY_TITLE, consumerPost.getTitle());
		initialValues.put(KEY_DATE, consumerPost.getDate());
		initialValues.put(KEY_CONSUMER, consumerPost.getConsumer_name());
		initialValues.put(KEY_CITY, consumerPost.getCity());
		initialValues.put(KEY_CATEGORY, consumerPost.getCategory());
		initialValues.put(KEY_CONTENTS, consumerPost.getContents());
		if(consumerPost.getPost_images_name().size()>0){
			initialValues.put(KEY_IMAGE, Utils.sumImgName(consumerPost.getPost_images_name()));
		}
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deletePost(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deleteAll(){
		return mDb.delete(DATABASE_TABLE, null, null)>0;
	}
	public ArrayList<ConsumerPost> fetchAllPosts() {
		ArrayList<ConsumerPost> postList = new ArrayList<ConsumerPost>();
		Cursor cursor = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_TITLE, KEY_DATE, KEY_CATEGORY, KEY_CONSUMER }, null, null,
				null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			ConsumerPost consumerPost = new ConsumerPost();
			consumerPost.setId(cursor.getInt(cursor.getColumnIndex(KEY_ROWID)));
			System.out.println("fetchAllPost id:" + consumerPost.getId());
			consumerPost.setTitle(cursor.getString(cursor
					.getColumnIndex(KEY_TITLE)));
			consumerPost.setCategory(cursor.getString(cursor
					.getColumnIndex(KEY_CATEGORY)));
			consumerPost.setDate(cursor.getString(cursor
					.getColumnIndex(KEY_DATE)));
			consumerPost.setConsumer_name(cursor.getString(cursor
					.getColumnIndex(KEY_CONSUMER)));
			consumerPost.setReplyCnt(getReplyCnt(consumerPost.getId()));

			postList.add(consumerPost);
			cursor.moveToNext();
		}
		return postList;
	}

	public int getReplyCnt(int post_id) {
		ConsumerReplyDbAdapter adapter = new ConsumerReplyDbAdapter(mCtx);
		int result = 0;
		adapter.open();
		result = adapter.fetchReplyCnt(post_id);
		adapter.close();
		return result;
	}

	

	public ConsumerPost fetchPost(long rowId) throws SQLException {

		ConsumerPost consumerPost =null;
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_TITLE, KEY_DATE, KEY_CONSUMER, KEY_CATEGORY,
				KEY_CITY, KEY_CONTENTS, KEY_IMAGE }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		mCursor.moveToFirst();
		while (!mCursor.isAfterLast()) {
			consumerPost = new ConsumerPost();
			consumerPost
					.setId(mCursor.getInt(mCursor.getColumnIndex(KEY_ROWID)));
			consumerPost.setTitle(mCursor.getString(mCursor
					.getColumnIndex(KEY_TITLE)));
			System.out.println("title :" + consumerPost.getTitle());
			consumerPost.setCategory(mCursor.getString(mCursor
					.getColumnIndex(KEY_CATEGORY)));
			consumerPost.setDate(mCursor.getString(mCursor
					.getColumnIndex(KEY_DATE)));
			consumerPost.setConsumer_name(mCursor.getString(mCursor
					.getColumnIndex(KEY_CONSUMER)));
			consumerPost.setCity(mCursor.getString(mCursor
					.getColumnIndex(KEY_CITY)));
			consumerPost.setContents(mCursor.getString(mCursor
					.getColumnIndex(KEY_CONTENTS)));
			if (mCursor.getString(mCursor.getColumnIndex(KEY_IMAGE)) != null) {
				consumerPost.setPost_images_name(Utils.getImgNameToken(mCursor.getString(mCursor.getColumnIndex(KEY_IMAGE))));
			}
			consumerPost.setReplyCnt(getReplyCnt(consumerPost.getId()));
			mCursor.moveToNext();
		}
		return consumerPost;
	}

	// public boolean updateNote(long rowId, String title, String body, String
	// date) {
	// ContentValues args = new ContentValues();
	// args.put(KEY_TITLE, title);
	// args.put(KEY_BODY, body);
	//
	// args.put(KEY_DATE, date);
	//
	// return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) >
	// 0;
	// }
}
