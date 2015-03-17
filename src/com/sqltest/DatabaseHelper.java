package com.sqltest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.logging.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

	Logger logger = Logger.getLogger("MyLogger");

	private Context context;

	public DatabaseHelper(Context context) {
		super(context, "student_score", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) { // DatabaseHelper�N���X�Ăяo�����Ɏ��s
		db.beginTransaction(); // create�����s�̂��߂̃g�����U�N�V�������͂�
		try {
			db.execSQL("create table score (_id integer primary key autoincrement, name text, english integer, japanese integer, math integer);");
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // DB�̃o�[�W�������オ�������̏���
	}

	private boolean existsStudent(SQLiteDatabase db, String name) {
		Cursor c;
		if (name == null) {
			c = db.rawQuery("select _id from score;", null);
		} else {
			c = db.rawQuery("select _id from score where name = ?;",
					new String[] { name });
		}
		if (c.moveToFirst() == false) {
			return false;
		} else {
			return true;
		}
	}

	public Cursor getStudentName(SQLiteDatabase db) {
		Cursor c = db
				.rawQuery("select name from score order by _id asc;", null);
		return c;
	}

	public Cursor getCount(SQLiteDatabase db) {
		Cursor c = db.rawQuery("select count(*) from score;", null);
		return c;
	}

	public Cursor getScore(SQLiteDatabase db, String name) {
		Cursor c = db
				.rawQuery(
						"select _id, name, english, japanese, math from score where name= ?;",
						new String[] { name });
		return c;
	}

	public void doDelete(SQLiteDatabase db, int _id) {
		db.beginTransaction(); // delete�����s�̂��߂̃g�����U�N�V�������͂�
		try {
			db.execSQL("delete from score where _id = ?;",
					new String[] { String.valueOf(_id) });
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

	public void doInsert(SQLiteDatabase db, String[] score, String name) {
		if (existsStudent(db, name) == false) {
			db.beginTransaction(); // update�����s�̂��߂̃g�����U�N�V�������͂�
			try {
				db.execSQL(
						"insert into score (name, english, japanese, math) values (?, ?, ?, ?);",
						new String[] { name, score[0], score[1], score[2] });
				db.setTransactionSuccessful(); // ��������΃R�~�b�g
			} finally {
				db.endTransaction(); // ��O�������̓��[���o�b�N
			}
		}
	}

	public void doUpdate(SQLiteDatabase db, String[] score, String name, int _id) {
		db.beginTransaction(); // update�����s�̂��߂̃g�����U�N�V�������͂�
		try {
			db.execSQL(
					"update score set name = ?, english = ?, japanese = ?, math = ? where _id = ?",
					new String[] { name, score[0], score[1], score[2],
							String.valueOf(_id) });
			db.setTransactionSuccessful(); // ��������΃R�~�b�g
		} finally {
			db.endTransaction(); // ��O�������̓��[���o�b�N
		}
	}

}
