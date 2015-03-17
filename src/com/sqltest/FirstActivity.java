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

public class FirstActivity extends Activity {

	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private TextView all_cnt;
	private Button btn_regist, btn_reload;
	private Spinner std_list;
	private String std_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);

		helper = new DatabaseHelper(this);
		db = helper.getWritableDatabase(); // WritableなDBのインスタンス生成

		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_reload = (Button) findViewById(R.id.btn_reload);
		std_list = (Spinner) findViewById(R.id.student_list);
		all_cnt = (TextView) findViewById(R.id.all_cnt_text);

		setAllScore(db);

		std_list.setAdapter(setStudentList());
		std_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				std_name = (String) spinner.getSelectedItem();
				if (std_name.equals(getString(R.string.regist))) {
					btn_regist.setText(getString(R.string.regist));
				} else {
					btn_regist.setText(getString(R.string.update));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		btn_regist.setOnClickListener(new ButtonClickListener());
		btn_reload.setOnClickListener(new ButtonClickListener());
	}

	private void setAllScore(SQLiteDatabase db) {
		Cursor c = helper.getCount(db);
		if (c.moveToFirst() == true) {
			if(c.getString(0).equals("0")){
				all_cnt.setText(getString(R.string.no_data));				
			}else{
				all_cnt.setText(c.getString(0));				
			}
		}
	}

	private ArrayAdapter<String> setStudentList() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add(getString(R.string.regist));
		Cursor c = helper.getStudentName(db);
		while (c.moveToNext()) {
			adapter.add(c.getString(0));
		}

		c.close();
		return adapter;
	}

	class ButtonClickListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(FirstActivity.this, MainActivity.class);
			if (v == btn_regist) {
				intent.putExtra("SELECTED_STD", std_name);
			} else if (v == btn_reload) {
				intent = getIntent();
				overridePendingTransition(0, 0);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				finish();

				overridePendingTransition(0, 0);
			}

			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first, menu);
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
}
