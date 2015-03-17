package com.sqltest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends Activity {

	Logger logger = Logger.getLogger("MyLogger");

	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private EditText english, japanese, math, name;
	private TextView sum, average, all_sum, all_average;
	private Button btn_delete, btn_update, btn_regist;
	private Spinner std_list;
	private String std_name;
	private int _id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		name = (EditText) findViewById(R.id.name_edit);
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

		Intent data = getIntent();
		Bundle extras = data.getExtras();
		std_name = extras != null ? extras.getString("SELECTED_STD") : "";
		// 新規作成
		if (std_name.equals(getString(R.string.regist))) {
			btn_delete.setEnabled(false);
			btn_update.setText(getString(R.string.regist));
		} else {
			// 存在しない氏名を受け取った場合はボタンを無効化
			if (setScore(db) == false) {
				btn_delete.setEnabled(false);
				btn_update.setEnabled(false);
				name.setText(getString(R.string.no_data));
			}
		}

		// 各データセット
		// setScore(db);
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

	private boolean setScore(SQLiteDatabase db) {
		Cursor c = helper.getScore(db, std_name);
		if (c.moveToFirst()) {
			_id = c.getInt(0);
			name.setText(c.getString(1));
			english.setText(c.getString(2));
			japanese.setText(c.getString(3));
			math.setText(c.getString(4));

			sum.setText(Integer.toString(sum(c)));
			average.setText(Double.toString(average(c)));
			c.close();

			return true;
		}

		return false;
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
				helper.doDelete(db, _id);
				Toast.makeText(getBaseContext(), "削除が完了しました。",
						Toast.LENGTH_LONG).show();
				finish();
			} else if (v == btn_update) {
				String score[] = { english.getText().toString(),
						japanese.getText().toString(),
						math.getText().toString() };
				if (checkScore(score)) {
					// 新規登録の場合
					if (std_name.equals(getString(R.string.regist))) {
						helper.doInsert(db, score, name.getText().toString());
						Toast.makeText(getBaseContext(), "登録が完了しました。",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(MainActivity.this, MainActivity.class);
						intent.putExtra("SELECTED_STD", name.getText().toString());
						startActivity(intent);
					} else {
						helper.doUpdate(db, score, name.getText().toString(),
								_id);
						Toast.makeText(getBaseContext(), "更新が完了しました。",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getBaseContext(), "入力された値が正しくありません",
							Toast.LENGTH_LONG).show();
				}

				// setScore(db);
			}
		}
	}
}
