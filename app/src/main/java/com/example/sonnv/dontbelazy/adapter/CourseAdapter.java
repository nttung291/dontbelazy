package com.example.sonnv.dontbelazy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.subjects.ShowingSubjectActivity;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;
import com.example.sonnv.dontbelazy.databases.CourseMode;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectCalendar;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by cuonghx2709 on 9/20/2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.StoryViewHolder> {

    private static final String TAG = CourseAdapter.class.toString() ;
    public static final String KEY_COURSE = "cuong";
    private List<Subject> list ;
    private List<SubjectCalendar> listTest;
    private Context context;
    private int dayofWeek;

    public CourseAdapter(Context context,List<Subject> list, int dayOfWeek,List<SubjectCalendar> listTest ){
        this.list = list;
        this.context = context;
        this.dayofWeek = dayOfWeek;
        this.listTest = listTest;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public StoryViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_course, viewGroup, false);

        StoryViewHolder svh = new StoryViewHolder(view);
        return svh;
    }

    @Override
    public void onBindViewHolder(final StoryViewHolder holder, final int position) {
        View view = holder.view;

        TextView tvTile = view.findViewById(R.id.tv_title_course);
        TextView tvTime = view.findViewById(R.id.tv_time_course);
        TextView tvRoom = view.findViewById(R.id.tv_location);
        CardView itemCourse = view.findViewById(R.id.cv_item_course);

        final Subject subject = list.get(position);
        final SubjectCalendar subjectCalendar = listTest.get(position);
        Log.d(TAG, "onBindViewHolder: " + subjectCalendar.getTimeStart());

        tvTile.setText(subjectCalendar.getName());

        SimpleDateFormat fullOption = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");

//        for (int i = 0; i < subject.getDaysPicked().size() ;i ++) {
//
//            if (Utils.formatDateOfWeek(subject.getDaysPicked().get(i)) == dayofWeek) {
//                tvTime.setText(subject.getTimeStartList().get(i) + " - " + subject.getTimeEndList().get(i));
//                tvRoom.setText(subject.getRoomList().get(i));
//                break;
//            }
//        }
        tvTime.setText(subjectCalendar.getTimeStart() + " - " + subjectCalendar.getTimeEnd());
        tvRoom.setText(subjectCalendar.getRoom());
        itemCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent = new Intent(context, ShowingSubjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RVSubjectAdapter.KEY_SUB,subject);
                Contact teacher = ContactHandleDB.getInstance(context).getContactAtId(subject.getIdTeacher());
                bundle.putSerializable(RVContactAdapter.KEY_CONTACT, teacher);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }
}