package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    List<Task> allTaskData = new ArrayList<>();

    public TaskAdapter(List<Task> allTaskData) {
        this.allTaskData = allTaskData;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
        TaskViewHolder std = new TaskViewHolder(view);
        return std;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = allTaskData.get(position);
        System.out.print("Hello");
        TextView title = holder.itemView.findViewById(R.id.title);
        TextView body = holder.itemView.findViewById(R.id.body);
        TextView state = holder.itemView.findViewById(R.id.state);

        title.setText(holder.task.title);
        body.setText(holder.task.body);
        state.setText(holder.task.state);

        holder.itemView.findViewById(R.id.constraint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDetailsPage = new Intent(v.getContext(), TaskDetailPage.class);
                goToDetailsPage.putExtra("title",allTaskData.get(position).title );
                goToDetailsPage.putExtra("body", allTaskData.get(position).body);
                goToDetailsPage.putExtra("state",allTaskData.get(position).state);
                v.getContext().startActivity(goToDetailsPage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTaskData.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{

        public Task task;

       public View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
