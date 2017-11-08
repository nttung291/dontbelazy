package com.example.sonnv.dontbelazy.activities;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.notes.EditNote;
import com.example.sonnv.dontbelazy.activities.notes.NoteActivity;
import com.example.sonnv.dontbelazy.activities.subjects.SubjectsListActivity;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.adapter.StartAdapter;
import com.example.sonnv.dontbelazy.adapter.notes.NoteAdapter;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.SubjectOfstart;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.sonnv.dontbelazy.R.transition.fade;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = StartActivity.class.toString();
    private ListView lvStart;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabAdd,fabContact,fabSubject, fabNote;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private LinearLayout linearLayoutStart;
    private TextView tvTeacher,tvSubject,tvTime,tvNote,tvNext;
    private ImageView imageView, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText("Next classes !");
        tv.setTextSize(25);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        tv.setTypeface(tf);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);

        linearLayoutStart = (LinearLayout) findViewById(R.id.lnl_start);

        lvStart = (ListView) findViewById(R.id.lv_start);
        tvTeacher = (TextView) findViewById(R.id.tv_teacher_start);
        tvSubject = (TextView) findViewById(R.id.tv_subject_start);
        tvTime = (TextView) findViewById(R.id.tv_time_start);
        tvNote = (TextView) findViewById(R.id.tv_note_start);
        imageView = (ImageView) findViewById(R.id.iv_note);


        tvTeacher.setTypeface(typeface);
        tvSubject.setTypeface(typeface);
        tvTime.setTypeface(typeface);
        tvNote.setTypeface(typeface);

        fabAdd = (FloatingActionButton)findViewById(R.id.fab_start_add);
        fabSubject = (FloatingActionButton)findViewById(R.id.fab_start_subject);
        fabContact = (FloatingActionButton)findViewById(R.id.fab_start_contact);
        fabNote = (FloatingActionButton) findViewById(R.id.fab_start_note);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        fabAdd.setOnClickListener(this);
        fabSubject.setOnClickListener(this);
        fabContact.setOnClickListener(this);
        fabNote.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab_start_add:
                animateFAB();
                break;
            case R.id.fab_start_contact:
                ContactActivity.flag = 1;
                Intent intent = new Intent(this,AddingContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RVContactAdapter.KEY_CONTACT, null);
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case R.id.fab_start_subject:
                Intent intentStart = new Intent(this, AddingSubjectActivity.class);
                Bundle bundleStart = new Bundle();
                bundleStart.putSerializable(RVSubjectAdapter.KEY_SUB, null);
                intentStart.putExtras(bundleStart);
                this.startActivity(intentStart);
                break;
            case R.id.fab_start_note:
                Intent i = new Intent(this, EditNote.class);
                Bundle b = new Bundle();
                Note note = new Note(-1, null, null, null, false);
                b.putSerializable(NoteAdapter.Key_NOTE, note);
                i.putExtras(b);
                this.startActivity(i);
                break;
            case R.id.cv_start_contact:
                Intent intentContact = new Intent(this, ContactActivity.class);
                ImageView ivcontact = (ImageView) findViewById(R.id.iv_teacher_list_start);
                ActivityOptionsCompat optionContact = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(ivcontact, ViewCompat.getTransitionName(ivcontact)),
                        new Pair<View, String>(fabAdd,  ViewCompat.getTransitionName(fabAdd)) );
                this.startActivity(intentContact, optionContact.toBundle());
                break;
            case R.id.cv_start_timetable:
                Intent intentTimetable = new Intent(this, CalendarActivity.class);
                ImageView ivtime = (ImageView) findViewById(R.id.iv_time_table_start);
                ActivityOptionsCompat optionTime = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(ivtime, ViewCompat.getTransitionName(ivtime)),
                        new Pair<View, String>(fabAdd,  ViewCompat.getTransitionName(fabAdd)) );
                this.startActivity(intentTimetable, optionTime.toBundle());
                break;
            case R.id.cv_start_subject:
                Intent intentsubject = new Intent(this, SubjectsListActivity.class);
                ImageView iv = (ImageView) findViewById(R.id.iv_subject_start);
                ActivityOptionsCompat optionsubject = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(iv, ViewCompat.getTransitionName(iv)),
                        new Pair<View, String>(fabAdd,  ViewCompat.getTransitionName(fabAdd)) );
                this.startActivity(intentsubject, optionsubject.toBundle());
                break;
            case R.id.cv_start_note:

                Intent intentNote = new Intent(this, NoteActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
//                        imageView,
//                        ViewCompat.getTransitionName(imageView), fabAdd , ViewCompat.getTransitionName(fabAdd));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(imageView, ViewCompat.getTransitionName(imageView)),
                        new Pair<View, String>(fabAdd,  ViewCompat.getTransitionName(fabAdd)) );

                this.startActivity(intentNote, options.toBundle());
                break;
        }
    }

    private void animateFAB() {
        if(isFabOpen){
            linearLayoutStart.setAlpha(1);

            fabAdd.startAnimation(rotate_backward);
            fabContact.startAnimation(fab_close);
            fabSubject.startAnimation(fab_close);
            fabNote.startAnimation(fab_close);

            fabSubject.setClickable(false);
            fabContact.setClickable(false);
            fabNote.setClickable(false);

            isFabOpen = false;
            Log.d("Start", "close");

        } else {
            linearLayoutStart.setAlpha(0.5f);

            fabAdd.startAnimation(rotate_forward);
            fabSubject.startAnimation(fab_open);
            fabContact.startAnimation(fab_open);
            fabNote.startAnimation(fab_open);

            fabSubject.setClickable(true);
            fabContact.setClickable(true);
            fabNote.setClickable(true);

            isFabOpen = true;
            Log.d("Start","open");
        }
    }
    @Override
    protected void onStart() {
        super.onStart();


        List<SubjectOfstart> list = Utils.sortSubjectOfStartByDate(Utils.getListStart(this));
        List<SubjectOfstart> listDelete = Utils.sortSubjectOfStartByDate(Utils.getListStart(this));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date d = new Date();
        for(SubjectOfstart subjectOfstart : list){
            d = Utils.getDate(dateFormat, subjectOfstart.getTime());
            if (date.getHours() > d.getHours() && date.getMinutes() > d.getMinutes() ){
                listDelete.add(subjectOfstart);
            }
        }

        list.removeAll(listDelete);
        listDelete.clear();

        StartAdapter startAdapter = new StartAdapter(this,R.layout.item_start ,list);
        lvStart.setAdapter(startAdapter);



    }
}
