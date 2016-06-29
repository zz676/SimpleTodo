package com.codepath.simpletodo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Zhisheng Zhou
 */

public class TaskItemsAdapater extends BaseAdapter {
    /**
     * The LayoutInflater.
     */
    private LayoutInflater mInflater;

    /**
     * The context.
     */
    Context context;

    /**
     * The holder.
     */
    ViewHolder holder;

    String isPolicyStatus;

    /**
     * The data ArrayList.
     */
    private ArrayList<Task> tasks;

    public TaskItemsAdapater(Context context,
                             ArrayList<Task> tasks) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.tasks = tasks;

        LayoutInflater layoutInflater = (LayoutInflater) ((Activity) context)
                .getBaseContext().getSystemService(
                        context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        if (tasks != null) {
            return tasks.size();

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (tasks != null) {
            try {
                return tasks.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.custom_list_item, null);
        holder = new ViewHolder();

        final Task task = tasks.get(position);

        final int id = task.getId();
        final String name = task.getName();
        final String dueDate = task.getDuedate();
        final String notes = task.getNotes();
        final String priorityLevel = task.getPriority_level();
        final String status = task.getStatus();

        holder.txtViewName = (TextView) convertView
                .findViewById(R.id.list_txtview_task_name);
        holder.txtViewPriorityLevel = (TextView) convertView
                .findViewById(R.id.list_txtview_task_priority_level);
/*        holder.progressBtn = (ImageView) convertView
                .findViewById(R.id.list_claim_detail_progressbtnID);
        if (claims.isCompleted()) {
            holder.progressBtn.setBackgroundResource(R.drawable.complete);
        } else {
            holder.progressBtn.setBackgroundResource(R.drawable.progresss);
        }*/
        holder.txtViewName.setText(name);
        holder.txtViewPriorityLevel.setText(priorityLevel);
        convertView.setTag(holder);
        return convertView;
    }

    public void showProgress() {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = 1000;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
            }
        }, delayInMillis);
    }

    /**
     * The Class ViewHolder.
     */
    static class ViewHolder {

        TextView txtViewName, txtViewDueDate, txtViewNotes, txtViewPriorityLevel, txtViewStatus;
    }
}
