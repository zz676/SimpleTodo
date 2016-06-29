package com.codepath.simpletodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Zhisheng Zhou
 */

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    public final String PREFS_NAME = "MyPrefsFile";
    public static final String POSITION_EXTRA_STRING = "com.codepath.simpletodo.position";
    public static final String ITEMS_EXTRA_STRING = "com.codepath.simpletodo.items";
    public static final String IS_FIRST_TIME = "com.codepath.simpletodo.firsttime";
    private SharedPreferences mPrefs;
    private boolean isFirstTime = true;
    private DatabaseHelper dbHelper;
    private TaskItemsAdapater taskItemsAdapater;
    private ListView tasksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(mPrefs.contains(IS_FIRST_TIME)){
            items = new ArrayList<>(mPrefs.getStringSet(ITEMS_EXTRA_STRING, new HashSet<String>()));
        } else {
            //if this is the first time the app is launched.
            items = new ArrayList<String>(Arrays.asList(new String[]{"First Item", "Second Item"}));
        }*/
        dbHelper = new DatabaseHelper(this);
        tasksListView = (ListView) findViewById(R.id.tasks_list_id);
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task selectedTask = tasks.get(position);
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("taskObject", selectedTask);
                startActivity(intent);
            }
        });
        fetchtasks();
    }

    private void fetchtasks() {
        // DatabaseHelper dbHelper = new DatabaseHelper(ClaimDetailActivity.this);
        // claimDetailList = dbHelper.getPolicyDetails();
        //claimDetailList = getClaims();
        tasks = dbHelper.getTasks();
        if (tasks != null) {
            taskItemsAdapater = new TaskItemsAdapater(this, tasks);
            tasksListView.setAdapter(taskItemsAdapater);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.add_task:
                final Intent intent = new Intent(this, AddItemActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
