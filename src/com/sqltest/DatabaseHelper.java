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
	public void onCreate(SQLiteDatabase db) { // DatabaseHelper�N���X�Ăяo�����Ɏ��s
		db.beginTransaction(); // create�����s�̂��߂̃g�����U�N�V�������͂�
		try {
			db.execSQL("create table score (_id integer primary key autoincrement, english integer, japanese integer, math integer);");
			db.execSQL("insert into score (english, japanese, math) values (0, 0, 0);");
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // DB�̃o�[�W�������オ�������̏���
	}

	public Cursor getScore(SQLiteDatabase db) {
		Cursor c = db.rawQuery(
				"select english, japanese, math from score where _id = ?;",
				new String[] { "1" });
		return c;
	}

	public void doDelete(SQLiteDatabase db) {
		db.beginTransaction(); // delete�����s�̂��߂̃g�����U�N�V�������͂�
		try {
			db.execSQL("delete from score where _id = 1;");
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

	public void doUpdate(SQLiteDatabase db, String[] score) {
		db.beginTransaction(); // update�����s�̂��߂̃g�����U�N�V�������͂�

		try {
			db.execSQL(
					"update score set english = ?, japanese = ?, math = ? where _id = 1",
					new String[] { score[0], score[1], score[2] });
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

}
