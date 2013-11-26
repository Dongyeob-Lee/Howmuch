package com.nero.howmuch.sqlite;

import java.util.ArrayList;

import com.nero.howmuch.Reply;
import com.nero.howmuch.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SellerReplyDbAdapter {

	public static final String KEY_DATE = "date";
	public static final String KEY_CONSUMER = "consumer_name";
	public static final String KEY_SELLER = "seller_name";
	public static final String KEY_SELLER_NUM = "seller_num";
	public static final String KEY_SELLER_PHONE = "seller_phone";
	public static final String KEY_CONTENTS = "contents";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_PFLAG = "pflag";
	public static final String KEY_PARENT = "parent";
	public static final String KEY_POSTID = "post_id";
	public static final String KEY_REPLYID = "reply_id";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_SEND_FLAG = "send_flag";

	private static final String TAG = "ConsumerReplyDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table seller_reply (_id integer primary key, reply_id integer not null, "
			+ "post_id integer not null, consumer_name text not null, seller_name text not null, seller_num text not null, seller_phone text not null, date text not null, contents text not null, image text, pflag integer not null, parent integer not null,send_flag integer not null);";
	private static final String DATABASE_NAME = "sellerreply";
	private static final String DATABASE_TABLE = "seller_reply";
	private static final int DATABASE_VERSION = 4;

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
			db.execSQL("DROP TABLE IF EXISTS consumer_reply");
			onCreate(db);
		}
	}

	public SellerReplyDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public SellerReplyDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}
	public boolean deleteAll(){
		return mDb.delete(DATABASE_TABLE, null, null)>0;
	}
	public long addReply(Reply reply) {
		Log.d("addReply", reply.getConsumer_name()+","+reply.getContents()+","+reply.getDate()+","+reply.getId()+","+reply.getPost_id()+","+reply.getSend_flag()+","+reply.getSeller_name()+","+reply.getReply_images_name().size());
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_REPLYID, reply.getId());
		initialValues.put(KEY_POSTID, reply.getPost_id());
		initialValues.put(KEY_DATE , reply.getDate());
		initialValues.put(KEY_CONSUMER, reply.getConsumer_name());
		initialValues.put(KEY_SELLER, reply.getSeller_name());
		initialValues.put(KEY_SELLER_NUM, reply.getSeller_num());
		initialValues.put(KEY_SELLER_PHONE, reply.getSeller_phone());
		initialValues.put(KEY_CONTENTS, reply.getContents());
		if(reply.getReply_images_name().size()>0){
			initialValues.put(KEY_IMAGE, Utils.sumImgName(reply.getReply_images_name()));
		}
		initialValues.put(KEY_PFLAG, reply.getPflag());
		initialValues.put(KEY_PARENT, reply.getParent());
		initialValues.put(KEY_SEND_FLAG, reply.getSend_flag());
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deletePost(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public ArrayList<Reply> fetchAllreply(int post_id) {
		ArrayList<Reply> replyList = new ArrayList<Reply>();
		Cursor cursor =  mDb.query(DATABASE_TABLE, new String[] { KEY_REPLYID, KEY_POSTID,
				KEY_DATE,KEY_SELLER,KEY_SELLER_NUM,KEY_SELLER_PHONE,KEY_CONSUMER,KEY_CONTENTS,KEY_IMAGE, KEY_PFLAG,KEY_PARENT, KEY_SEND_FLAG}, KEY_POSTID+"="+post_id, null, null, null, null);
		cursor.moveToFirst();
		
		while(!cursor.isAfterLast()){
			Reply reply = new Reply();
			reply.setId(cursor.getInt(cursor.getColumnIndex(KEY_REPLYID)));
			reply.setPost_id(cursor.getInt(cursor.getColumnIndex(KEY_POSTID)));
			reply.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
			reply.setSeller_name(cursor.getString(cursor.getColumnIndex(KEY_SELLER)));
			reply.setSeller_num(cursor.getString(cursor.getColumnIndex(KEY_SELLER_NUM)));
			reply.setSeller_phone(cursor.getString(cursor.getColumnIndex(KEY_SELLER_PHONE)));
			reply.setConsumer_name(cursor.getString(cursor.getColumnIndex(KEY_CONSUMER)));
			reply.setContents(cursor.getString(cursor.getColumnIndex(KEY_CONTENTS)));
			String image = cursor.getString(cursor.getColumnIndex(KEY_IMAGE));
			if(image!=null){
				reply.setReply_images_name(Utils.getImgNameToken(image));
			}
			reply.setPflag(cursor.getInt(cursor.getColumnIndex(KEY_PFLAG)));
			reply.setParent(cursor.getInt(cursor.getColumnIndex(KEY_PARENT)));
			reply.setSend_flag(cursor.getInt(cursor.getColumnIndex(KEY_SEND_FLAG)));
			replyList.add(reply);
			cursor.moveToNext();
		}
		return replyList;
	}
	public int fetchReplyCnt(int post_id){
		Cursor cursor =  mDb.query(DATABASE_TABLE, new String[] { KEY_REPLYID, KEY_POSTID,
				KEY_DATE,KEY_SELLER,KEY_CONSUMER,KEY_CONTENTS,KEY_IMAGE, KEY_SEND_FLAG}, KEY_POSTID+"="+post_id, null, null, null, null);
		cursor.moveToFirst();
		int cnt=0;
		while(!cursor.isAfterLast()){
			cnt++;
			cursor.moveToNext();
		}
		return cnt;
	}
//	public boolean updateNote(long rowId, String title, String body, String date) {
//		ContentValues args = new ContentValues();
//		args.put(KEY_TITLE, title);
//		args.put(KEY_BODY, body);
//
//		args.put(KEY_DATE, date);
//
//		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
//	}
}
