package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Zhisheng Zhou
 */

public class AddItemActivity extends AppCompatActivity {

    private TextView taskNameEditTxt;
    private TextView dueDatePickerTxtView;
    private TextView notesEditTxt;
    private Spinner prioritylevelsSpinner;
    private Spinner statusSpinner;

    private DatabaseHelper dbHelper;
    private DatePickerDialog datePickerDialog;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        dbHelper = new DatabaseHelper(this);
        init();
    }

    private void init() {

        taskNameEditTxt = (EditText) findViewById(R.id.task_name_editview);
        notesEditTxt = (EditText) findViewById(R.id.notes_editview);

        prioritylevelsSpinner = (Spinner) findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritylevelsSpinner.setAdapter(pAdapter);

        statusSpinner = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_levels, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(sAdapter);

        dueDatePickerTxtView = (TextView) findViewById(R.id.due_date_picker);
        dueDatePickerTxtView.setText(dateFormatter.format(Calendar.getInstance().getTime()));
        dueDatePickerTxtView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        dueDatePickerTxtView.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_item_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.save_task:
                Task task = new Task();
                task.setName(taskNameEditTxt.getText().toString());
                task.setDuedate(dueDatePickerTxtView.getText().toString());
                task.setNotes(notesEditTxt.getText().toString());
                task.setPriority_level(((TextView)prioritylevelsSpinner.getSelectedView()).getText().toString());
                task.setStatus(((TextView)statusSpinner.getSelectedView()).getText().toString());
                dbHelper.insertTask(task);
                break;
            case R.id.discard_task:
                break;
            default:
                break;
        }
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}
