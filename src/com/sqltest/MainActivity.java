package com.sqltest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends Activity {

	Logger logger = Logger.getLogger("MyLogger");

	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private EditText english, japanese, math;
	private TextView sum, average;
	private Button btn_delete, btn_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		english = (EditText) findViewById(R.id.en_edit);
		japanese = (EditText) findViewById(R.id.ja_edit);
		math = (EditText) findViewById(R.id.ma_edit);
		sum = (TextView) findViewById(R.id.sum_text);
		average = (TextView) findViewById(R.id.average_text);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_update = (Button) findViewById(R.id.btn_update);

		btn_delete.setOnClickListener(new ButtonClickListener());
		btn_update.setOnClickListener(new ButtonClickListener());

		helper = new DatabaseHelper(this);
		db = helper.getWritableDatabase(); // WritableなDBのインスタンス生成

		// 各データセット
		setScore(db);
		// db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private int sum(Cursor c) {
		int sum = 0;
		for (int i = 0; i <= 2; i++) {
			sum += c.getInt(i);
		}
		return sum;
	}

	private double average(Cursor c) {
		return sum(c) / 3.0;
	}

	private void setScore(SQLiteDatabase db) {
		Cursor c = helper.getScore(db);
		c.moveToFirst();

		english.setText(c.getString(0));
		japanese.setText(c.getString(1));
		math.setText(c.getString(2));

		sum.setText(Integer.toString(sum(c)));
		average.setText(Double.toString(average(c)));

		c.close();
	}

	private boolean checkScore(String[] score) {
		int num = 0;
		for (String data : score) {
			try {
				num = Integer.parseInt(data);
				if ((num < 0) || (num > 100)) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

	class ButtonClickListener implements OnClickListener {
		public void onClick(View v) {
			if (v == btn_delete) {
				helper.doDelete(db);
				finish();
				startActivity(getIntent());
			} else if (v == btn_update) {
				String score[] = { english.getText().toString(),
						japanese.getText().toString(),
						math.getText().toString() };
				if (checkScore(score)) {
					helper.doUpdate(db, score);
				} else {
					Toast.makeText(getBaseContext(), "入力された値が正しくありません", Toast.LENGTH_LONG).show();
				}

				setScore(db);
			}
		}
	}
}
