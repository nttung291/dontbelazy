package com.example.sonnv.dontbelazy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.subjects.ShowingSubjectActivity;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;
import com.example.sonnv.dontbelazy.databases.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SONNV on 9/23/2017.
 */

public class RVSubjectAdapter extends RecyclerView.Adapter<RVSubjectAdapter.SubjectViewHolder>  {
    private Context context;
    public static final String KEY_SUB = "key_subject";
    private Contact teacher;

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvTeacher;
        TextView tvSubject;
        ImageView ivBackground;

        public SubjectViewHolder(View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cv_item_subject);
            tvTeacher = itemView.findViewById(R.id.tv_cvSubject_teacher);
            tvSubject = itemView.findViewById(R.id.tv_cvSubject_name);
            ivBackground = itemView.findViewById(R.id.iv_cvSubject);

        }
    }

    private List<Subject> subjectList;

    public RVSubjectAdapter(List<Subject> subjectListList) {
        subjectList = new ArrayList<>();
        this.subjectList = subjectListList;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_subject, viewGroup, false);
        SubjectViewHolder svh = new SubjectViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, final int position) {
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowingSubjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_SUB,subjectList.get(position));
//                bundle.putSerializable(RVContactAdapter.KEY_CONTACT, teacher);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        teacher = ContactHandleDB.getInstance(context).getContactAtId(subjectList.get(position).getIdTeacher());
        if (teacher != null) {
            holder.tvTeacher.setText(teacher.getName());
        } else {
            holder.tvTeacher.setText("");
        }
        holder.tvSubject.setText(subjectList.get(position).getName());
        holder.ivBackground.setImageResource(R.drawable.bg_subject_2);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

}
