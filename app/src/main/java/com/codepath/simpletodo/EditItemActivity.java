package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Zhisheng Zhou
 */

public class EditItemActivity extends AppCompatActivity {


    private MenuItem update;
    private MenuItem edit;
    private MenuItem delete;
    private TextView taskNameEditTxt;
    private TextView dueDatePickerTxtView;
    private TextView notesEditTxt;
    private Spinner prioritylevelsSpinner;
    private Spinner statusSpinner;
    private Task selectedTask;
    private DatabaseHelper dbHelper;
    private DatePickerDialog datePickerDialog;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        dbHelper = new DatabaseHelper(this);
        selectedTask = (Task) getIntent().getSerializableExtra("taskObject");
        if (selectedTask != null)
            init();
    }

    private void init() {

        taskNameEditTxt = (EditText) findViewById(R.id.task_name_editview);
        taskNameEditTxt.setText(selectedTask.getName());
        taskNameEditTxt.setEnabled(false);

        notesEditTxt = (EditText) findViewById(R.id.notes_editview);
        notesEditTxt.setText(selectedTask.getNotes());
        notesEditTxt.setEnabled(false);

        prioritylevelsSpinner = (Spinner) findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritylevelsSpinner.setAdapter(pAdapter);
        prioritylevelsSpinner.setSelection(getIndex(prioritylevelsSpinner, selectedTask.getPriority_level()));
        prioritylevelsSpinner.setEnabled(false);


        statusSpinner = (Spinner) findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this,
                R.array.status_levels, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(sAdapter);
        statusSpinner.setSelection(getIndex(statusSpinner, selectedTask.getStatus()));
        statusSpinner.setEnabled(false);

        dueDatePickerTxtView = (TextView) findViewById(R.id.due_date_picker);
        if (selectedTask.getDuedate() != null) {
            try {
                Date date = dateFormatter.parse(selectedTask.getDuedate());
                dueDatePickerTxtView.setText(dateFormatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dueDatePickerTxtView.setEnabled(false);

        dueDatePickerTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(EditItemActivity.this, new DatePickerDialog.OnDateSetListener() {

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

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        delete = menu.findItem(R.id.delete_task);
        edit = menu.findItem(R.id.edit_task);
        update = menu.findItem(R.id.update_task);
        if(!isEditMode) {
            edit.setVisible(true);
            delete.setVisible(true);
            update.setVisible(false);
        } else {
            edit.setVisible(false);
            delete.setVisible(false);
            update.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.edit_task:
                isEditMode = true;
                invalidateOptionsMenu();
                taskNameEditTxt.setEnabled(true);
                notesEditTxt.setEnabled(true);
                prioritylevelsSpinner.setEnabled(true);
                statusSpinner.setEnabled(true);
                dueDatePickerTxtView.setEnabled(true);
                break;
            case R.id.delete_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final Intent poliyListIntent = new Intent(this, MainActivity.class);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure to delete this task?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                //Boolean boolean1 = dbHelper.deletePolicy(selectedPolicyInfo.getUserId(), selectedPolicyInfo.getPolicyId());
                                Boolean isDeletedSuccessful = dbHelper.deleteTask(selectedTask.getId());
                                if (isDeletedSuccessful) {
                                    Toast.makeText(EditItemActivity.this, "Task Deleted Successfully.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(EditItemActivity.this, "Failed to Delete the Task.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                final Intent intent = new Intent(EditItemActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.update_task:
                Task task = new Task();
                task.setId(selectedTask.getId());
                task.setName(taskNameEditTxt.getText().toString());
                task.setDuedate(dueDatePickerTxtView.getText().toString());
                task.setNotes(notesEditTxt.getText().toString());
                task.setPriority_level(((TextView)prioritylevelsSpinner.getSelectedView()).getText().toString());
                task.setStatus(((TextView)statusSpinner.getSelectedView()).getText().toString());
                dbHelper.updateTask(task);
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            default:
                break;
        }
        return true;
    }
}
