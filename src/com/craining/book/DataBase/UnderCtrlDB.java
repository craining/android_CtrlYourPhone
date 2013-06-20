package com.craining.book.DataBase;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UnderCtrlDB {

	// ���ݿ�����
	private static final String DB_NAME = "CtrlPhones.db";

	// ���ݿ����
	private static final String DB_TABLE = "under_ctrl";

	// ���ݿ�汾
	private static final int DB_VERSION = 1;

	// ���ݿ����������
	private static final String KEY_ID = "_id";
	public static final String KEY_NUM = "number";
	public static final String KEY_NAME = "name";
	public static final String KEY_CTRLPWD = "ctrlpwd";

	// ����Context����
	private Context mContext = null;

	// ����һ����
	private static final String DB_CREATE = "CREATE TABLE " + DB_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NUM + " VARCHAR," + KEY_NAME + " VARCHAR," + KEY_CTRLPWD + " VARCHAR)";

	// ִ��open���������ݿ�ʱ�����淵�ص����ݿ����
	private SQLiteDatabase mSQLiteDatabase = null;

	// ��SQLiteOpenHelper�̳й���
	private DatabaseHelper mDatabaseHelper = null;

	/* ���캯��-ȡ��Context */
	public UnderCtrlDB(Context context) {
		mContext = context;
	}

	// �����ݿ⣬�������ݿ����
	public void open() throws SQLException {
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

	}

	// �ر����ݿ�
	public void close() {
		mDatabaseHelper.close();
	}

	/* ����һ������ */
	public long insertData(String num, String name, String pwd) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NUM, num);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_CTRLPWD, pwd);
		long toforReturn = mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
		return toforReturn;
	}

	/* ����num�е�ֵɾ��һ������ */
	public boolean deleteNum(String num) {
		// long toforReturn = mSQLiteDatabase.delete(DB_TABLE, KEY_NUM + "=" +
		// num, null);
		// return toforReturn > 0;

		final String DELETE_THING = "DELETE FROM " + DB_TABLE + " WHERE(" + KEY_NUM + "='" + num + "')";
		mSQLiteDatabase.execSQL(DELETE_THING);

		return true;

	}

	/* ͨ��Cursor��ѯĳ������ */
	public Cursor fetchColumnData(String column) {

		Cursor forReturn = mSQLiteDatabase.query(false, DB_TABLE, new String[]{column}, null, null, null, null, null, null);

		return forReturn;
	}

	/* ͨ��Cursor��ѯ�������� */
	public Cursor fetchAllData() {

		return mSQLiteDatabase.query(DB_TABLE, new String[]{KEY_ID, KEY_NUM, KEY_NAME, KEY_CTRLPWD}, null, null, null, null, null);

	}

	/**
	 * ��ȡ����� column �е��������ݷŵ�һ��Vector��
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
	 * �������ݿ���ڲ���
	 * 
	 * @author Ruin
	 * 
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		/* ���캯��-����һ�����ݿ� */
		DatabaseHelper(Context context) {
			// ������getWritableDatabase()
			// �� getReadableDatabase()����ʱ
			// �򴴽�һ�����ݿ�
			super(context, DB_NAME, null, DB_VERSION);

		}

		/* ����һ���� */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// ���ݿ�û�б�ʱ����һ��
			db.execSQL(DB_CREATE);
		}

		/* �������ݿ� */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}
}
