package com.craining.book.DataBase;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnderCtrlDB {

	// 数据库名称
	private static final String DB_NAME = "CtrlPhones.db";

	// 数据库表名
	private static final String DB_TABLE = "under_ctrl";

	// 数据库版本
	private static final int DB_VERSION = 1;

	// 数据库的三个列名
	private static final String KEY_ID = "_id";
	public static final String KEY_NUM = "number";
	public static final String KEY_NAME = "name";
	public static final String KEY_CTRLPWD = "ctrlpwd";

	// 本地Context对象
	private Context mContext = null;

	// 创建一个表
	private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUM + " VARCHAR," + KEY_NAME + " VARCHAR," + KEY_CTRLPWD + " VARCHAR)";

	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase mSQLiteDatabase = null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper mDatabaseHelper = null;

	/* 构造函数-取得Context */
	public UnderCtrlDB(Context context) {
		mContext = context;
	}

	// 打开数据库，返回数据库对象
	public void open() throws SQLException {
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

	}

	// 关闭数据库
	public void close() {
		mDatabaseHelper.close();
	}

	/* 插入一条数据 */
	public long insertData(String num, String name, String pwd) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NUM, num);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_CTRLPWD, pwd);
		long toforReturn = mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
		return toforReturn;
	}

	/* 根据num列的值删除一条数据 */
	public boolean deleteNum(String num) {
		// long toforReturn = mSQLiteDatabase.delete(DB_TABLE, KEY_NUM + "=" +
		// num, null);
		// return toforReturn > 0;

		final String DELETE_THING = "DELETE FROM " + DB_TABLE + " WHERE(" + KEY_NUM + "='" + num + "')";
		mSQLiteDatabase.execSQL(DELETE_THING);

		return true;

	}

	/* 通过Cursor查询某列数据 */
	public Cursor fetchColumnData(String column) {

		Cursor forReturn = mSQLiteDatabase.query(false, DB_TABLE, new String[]{column}, null, null, null, null, null, null);

		return forReturn;
	}

	/* 通过Cursor查询所有数据 */
	public Cursor fetchAllData() {

		return mSQLiteDatabase.query(DB_TABLE, new String[]{KEY_ID, KEY_NUM, KEY_NAME, KEY_CTRLPWD}, null, null, null, null, null);

	}

	/**
	 * 读取表格中 column 列的所有数据放到一个Vector中
	 * 
	 * @param column
	 * @return
	 */
	public Vector<String> getColumnUnderCtrlVector(String column) {

		Vector<String> getvector = new Vector<String>();
		Cursor findColumDate = fetchAllData();
		if ( findColumDate != null && findColumDate.getCount() != 0 ) {
			findColumDate.moveToFirst();
			final int Index = findColumDate.getColumnIndex(column);

			for (findColumDate.moveToFirst();!findColumDate.isAfterLast();findColumDate.moveToNext()) {
				String getOneItem = findColumDate.getString(Index);
				getvector.addElement(getOneItem);
			}
		}

		return getvector;
	}

	/**
	 * 操作数据库的内部类
	 * 
	 * @author Ruin
	 * 
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context) {
			// 当调用getWritableDatabase()
			// 或 getReadableDatabase()方法时
			// 则创建一个数据库
			super(context, DB_NAME, null, DB_VERSION);

		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// 数据库没有表时创建一个
			db.execSQL(DB_CREATE);
		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
}
