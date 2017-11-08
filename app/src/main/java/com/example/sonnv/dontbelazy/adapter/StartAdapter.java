package com.example.sonnv.dontbelazy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.subjects.ShowingSubjectActivity;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectOfstart;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by SONNV on 9/18/2017.
 */

public class StartAdapter extends ArrayAdapter<SubjectOfstart> {

    private Context context;
    private int resource;
    private List<SubjectOfstart> subjects;

    public StartAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SubjectOfstart> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.subjects = objects;
        Log.d(TAG, "StartAdapter: " + subjects.size());
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        TextView time = convertView.findViewById(R.id.tv_time_start);
        TextView room = convertView.findViewById(R.id.tv_room_start);
        TextView name = convertView.findViewById(R.id.tv_name_start);
        LinearLayout linearLayout = convertView.findViewById(R.id.item_start);

        SubjectOfstart subjectOfstart = this.subjects.get(position);
        time.setText(subjectOfstart.getTime());
        room.setText(subjectOfstart.getRoom());
        name.setText(subjectOfstart.getName());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowingSubjectActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(RVSubjectAdapter.KEY_SUB, subjects.get(position).getSubject());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
