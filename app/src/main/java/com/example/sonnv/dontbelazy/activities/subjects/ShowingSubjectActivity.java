package com.example.sonnv.dontbelazy.activities.subjects;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.CustomAnimation;
import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.AddingSubjectActivity;
import com.example.sonnv.dontbelazy.activities.ContactActivity;
import com.example.sonnv.dontbelazy.activities.ShowingContactActivity;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;

public class ShowingSubjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Spinner spinnerDays;
    private ImageView ivBack;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabDelete;
    private FloatingActionButton fabEdit;
    private LinearLayout linearLayout;
    private AppBarLayout appBarLayout;
    private Subject subject;
    private Contact teacher;
    private TextView tvTeacher;
    private ImageView ivShowTeacher;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvStartDay;
    private TextView tvEndDay;
    private TextView tvRoom;
    private TextView tvAttendance;
    private TextView tvNote;
    private TextView tvEdit,tvTrash;

    private boolean isFabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_subject);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUI();
        loadData();
    }

    private void setUI() {
        // SET UP COLLAPSINGTOOLBAR
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_showing_subject));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_showing_subject);
//        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
//        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.titleTextColor));

        // SET UP SPINNER DAYS
        spinnerDays = (Spinner) findViewById(R.id.spinner_show_day_list);
        spinnerDays.setOnItemSelectedListener(this);

        // SET UP COMPONENTS
        ivBack = (ImageView) findViewById(R.id.iv_back_showing_subject);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowingSubjectActivity.super.onBackPressed();
            }
        });
        fabMain = (FloatingActionButton) findViewById(R.id.fab_main_showing_subject);
        fabMain.setOnClickListener(this);

        fabDelete = (FloatingActionButton) findViewById(R.id.fab_subject_trash);
        fabDelete.setOnClickListener(this);

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit_subject);
        fabEdit.setOnClickListener(this);

        linearLayout = (LinearLayout) findViewById(R.id.ll_showing_subject);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout_showing_subject);

        // SET UP OTHERS
        tvTeacher = (TextView) findViewById(R.id.tv_show_teachers_of_subject);
        ivShowTeacher = (ImageView) findViewById(R.id.iv_show_teacher_subject);
        ivShowTeacher.setOnClickListener(this);
        tvStartTime = (TextView) findViewById(R.id.tv_hour_start_showing_subject);
        tvEndTime = (TextView) findViewById(R.id.tv_hour_end_showing_subject);
        tvStartDay = (TextView) findViewById(R.id.tv_start_day_showing_subject);
        tvEndDay = (TextView) findViewById(R.id.tv_end_day_showing_subject);
        tvRoom = (TextView) findViewById(R.id.tv_room_showing_subject);
        tvAttendance = (TextView) findViewById(R.id.tv_attendance_showing_subject);
        tvNote = (TextView) findViewById(R.id.tv_note_showing_subject);
        tvEdit = (TextView) findViewById(R.id.tv_edit_subject);
        tvTrash = (TextView) findViewById(R.id.tv_trash_subject);
    }

    private void loadData() {
        subject = (Subject) getIntent().getExtras().getSerializable(RVSubjectAdapter.KEY_SUB);
        subject = SubjectHandleDB.getInstance(this).getSubjectAtId(subject.getId());
        if (subject.getIdTeacher() != -1) {
            teacher = ContactHandleDB.getInstance(this).getContactAtId(subject.getIdTeacher());
        }
        collapsingToolbarLayout.setTitle(subject.getName());
        if (teacher != null) {
            tvTeacher.setText(teacher.getName());
            ivShowTeacher.setClickable(true);
            ivShowTeacher.setAlpha(1f);
        } else {
            tvTeacher.setText("");
            ivShowTeacher.setClickable(false);
            ivShowTeacher.setAlpha(0.3f);
        }
        ArrayAdapter<String> adapterDayPicker = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, subject.getDaysPicked());
        adapterDayPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapterDayPicker);
        tvNote.setText(subject.getNote());
        tvAttendance.setText(subject.getAttendance() + "/" + subject.getMaxAttendance());
        tvStartDay.setText(subject.getDayStart());
        tvEndDay.setText(subject.getDayEnd());
        tvStartTime.setText("From "  + subject.getTimeStartList().get(0));
        tvEndTime.setText("To " + subject.getTimeEndList().get(0));
        tvRoom.setText(subject.getRoomList().get(0));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tvStartTime.setText("From "  + subject.getTimeStartList().get(i));
        tvEndTime.setText("To " + subject.getTimeEndList().get(i));
        tvRoom.setText(subject.getRoomList().get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_main_showing_subject:
                animateFAB();
                break;
            case R.id.iv_show_teacher_subject:
                Intent intent = new Intent(this, ShowingContactActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RVContactAdapter.KEY_CONTACT,teacher);
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case R.id.fab_subject_trash:
                SubjectHandleDB.getInstance(this).deleteSubject(subject.getId());
                AlarmUtils.cancelAlarm(this, 1);
                AlarmUtils.create(this, 1);
                super.onBackPressed();
                break;
            case R.id.fab_edit_subject:
                ContactActivity.flag = 1;
                Intent intentEdit = new Intent(this, AddingSubjectActivity.class);
                Bundle bundleEdit = new Bundle();
                bundleEdit.putSerializable(RVSubjectAdapter.KEY_SUB, subject);
                intentEdit.putExtras(bundleEdit);
                this.startActivity(intentEdit);
                break;
        }
    }

    private void animateFAB() {
        if(isFabOpen){
            linearLayout.setAlpha(1);
            appBarLayout.setAlpha(1);

            fabMain.startAnimation(CustomAnimation.fabRotateClockwise(getApplicationContext()));
            fabEdit.startAnimation(CustomAnimation.fabClose(getApplicationContext()));
            fabDelete.startAnimation(CustomAnimation.fabClose(getApplicationContext()));
            tvEdit.startAnimation(CustomAnimation.fabClose(getApplicationContext()));
            tvTrash.startAnimation(CustomAnimation.fabClose(getApplicationContext()));

            fabEdit.setClickable(false);
            fabDelete.setClickable(false);

            isFabOpen = false;
            Log.d("Start", "close");

        } else {
            linearLayout.setAlpha(0.2f);
            appBarLayout.setAlpha(0.2f);

            fabMain.startAnimation(CustomAnimation.fabRotateAntiClockwise(getApplicationContext()));
            fabEdit.startAnimation(CustomAnimation.fabOpen(getApplicationContext()));
            fabDelete.startAnimation(CustomAnimation.fabOpen(getApplicationContext()));
            tvEdit.startAnimation(CustomAnimation.fabOpen(getApplicationContext()));
            tvTrash.startAnimation(CustomAnimation.fabOpen(getApplicationContext()));

            fabEdit.setClickable(true);
            fabDelete.setClickable(true);

            isFabOpen = true;
        }
    }
}
