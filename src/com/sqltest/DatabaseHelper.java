package com.sqltest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.logging.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

	Logger logger = Logger.getLogger("MyLogger");

	public DatabaseHelper(Context context) {
		super(context, null, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) { // DatabaseHelperクラス呼び出し時に実行
		db.beginTransaction(); // create文実行のためのトランザクションをはる
		try {
			db.execSQL("create table score (_id integer primary key autoincrement, english integer, japanese integer, math integer);");
			db.execSQL("insert into score (english, japanese, math) values (0, 0, 0);");
			db.setTransactionSuccessful(); // 成功すればコミット
		} finally {
			db.endTransaction(); // 例外発生時はロールバック
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // DBのバージョンが上がった時の処理
	}

	public Cursor getScore(SQLiteDatabase db) {
		Cursor c = db.rawQuery(
				"select english, japanese, math from score where _id = ?;",
				new String[] { "1" });
		return c;
	}

	public void doDelete(SQLiteDatabase db) {
		db.beginTransaction(); // delete文実行のためのトランザクションをはる
		try {
			db.execSQL("delete from score where _id = 1;");
			db.setTransactionSuccessful(); // 成功すればコミット
		} finally {
			db.endTransaction(); // 例外発生時はロールバック
		}
	}

	public void doUpdate(SQLiteDatabase db, String[] score) {
		db.beginTransaction(); // update文実行のためのトランザクションをはる

		try {
			db.execSQL(
					"update score set english = ?, japanese = ?, math = ? where _id = 1",
					new String[] { score[0], score[1], score[2] });
			db.setTransactionSuccessful(); // 成功すればコミット
		} finally {
			db.endTransaction(); // 例外発生時はロールバック
		}
	}

}
