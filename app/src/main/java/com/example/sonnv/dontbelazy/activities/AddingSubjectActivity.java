package com.example.sonnv.dontbelazy.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.subjects.ShowingSubjectActivity;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;
import com.example.sonnv.dontbelazy.databases.Subject;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;
import com.example.sonnv.dontbelazy.services.SchedulingService;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddingSubjectActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView ivBack;
    private TextView tvPickDaysWeek;
    private AlertDialog dialogDaysWeekPicker;
    private TextView tvDaysWeekList[];
    private ImageView ivAddTeacher;
    private Spinner spinnerTeachers;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvStartDay;
    private TextView tvEndDay;
    private Button btDayPicker;
    private Spinner spinnerDays;
    private TextView tvSave;
    private TextView subjectName;
    private EditText etRoomName;
    private EditText etMaxAttendance;
    private EditText etNote;

    private int previousDayPicked;
    private List<String> teacherNameList;
    private List<Contact> contactList;
    private int year_x, month_x, day_x;
    private int hour_x, minute_x;
    private boolean pickingStartDay;
    private boolean pickingStartTime;
    private String dayStart;
    private String dayEnd;
    private boolean isPickedDaysWeek[];
    private List<String> daysPicked;
    private String[] timeStartList;
    private String[] timeEndList;
    private String[] roomList;
    public static boolean isSaved = false;
    private Subject subjectBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        subjectBundle = (Subject) getIntent().getExtras().getSerializable(RVSubjectAdapter.KEY_SUB);
        setUI();
        if (subjectBundle != null) {
            setUIEdit();
        }
    }

    private void setUIEdit() {
        if (subjectBundle.getIdTeacher() != -1) {
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getId() == subjectBundle.getIdTeacher()) {
                    Log.d("YES", "setUIEdit: " + i);
                    spinnerTeachers.setSelection(i+1);
                    break;
                }
            }
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < subjectBundle.getDaysPicked().size(); j++) {
                if (tvDaysWeekList[i].getText().equals(subjectBundle.getDaysPicked().get(j))) {
                    isPickedDaysWeek[i] = true;
                    timeStartList[i] = subjectBundle.getTimeStartList().get(j);
                    timeEndList[i] = subjectBundle.getTimeEndList().get(j);
                    try {
                        roomList[i] = subjectBundle.getRoomList().get(j);
                    } catch (Exception ex) {
                        Log.d("Exception", "setUIEdit: ");
                    }
                    break;
                }
            }
        }
        daysPicked = subjectBundle.getDaysPicked();
        ArrayAdapter<String> adapterDayPicker = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, daysPicked);
        adapterDayPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapterDayPicker);

        tvStartTime.setText("From " + timeStartList[0]);
        tvEndTime.setText("To " + timeEndList[0]);

        tvStartDay.setText(subjectBundle.getDayStart());
        tvEndDay.setText(subjectBundle.getDayEnd());

        etRoomName.setText(roomList[0]);
        etMaxAttendance.setText("" + subjectBundle.getMaxAttendance());
        etNote.setText(subjectBundle.getNote());

        dayStart = subjectBundle.getDayStart();
        dayEnd = subjectBundle.getDayEnd();

        subjectName.setText(subjectBundle.getName());
    }

    private void setUI() {
        //SET UP TOOL BAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_subject);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ivBack = (ImageView) findViewById(R.id.ivBack_add_subject);
        ivBack.setOnClickListener(this);

        //SET UP DIALOG DAYS WEEK PICKER
        tvPickDaysWeek = (TextView) findViewById(R.id.tv_pick_days_of_week);
        tvPickDaysWeek.setOnClickListener(this);

        isPickedDaysWeek = new boolean[7];

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddingSubjectActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_weekdays, null);
        mBuilder.setView(mView);
        dialogDaysWeekPicker = mBuilder.create();
        tvDaysWeekList = getTvDaysArr(mView);

        btDayPicker = mView.findViewById(R.id.bt_dialog_day_picker);
        btDayPicker.setOnClickListener(this);

        // SET UP SPINNER TEACHER
        contactList = ContactHandleDB.getInstance(this).getListContact();
        teacherNameList = new ArrayList<>();
        teacherNameList.add("Add teacher");
        for (int i = 0; i < contactList.size(); i++) {
            teacherNameList.add(contactList.get(i).getName());
        }

        spinnerTeachers = (Spinner) findViewById(R.id.spinner_teachers_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, teacherNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeachers.setAdapter(adapter);
        spinnerTeachers.setOnItemSelectedListener(this);

        // ADD TEACHER
        ivAddTeacher = (ImageView) findViewById(R.id.iv_add_teacher_subject);
        ivAddTeacher.setOnClickListener(this);

        // SET UP SPINNER PICKED DAYS
        daysPicked = new ArrayList<>();
        daysPicked.add("Pick days before");
        spinnerDays = (Spinner) findViewById(R.id.spinner_picked_day_list);
        ArrayAdapter<String> adapterDayPicker = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, daysPicked);
        adapterDayPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapterDayPicker);
        spinnerDays.setOnItemSelectedListener(this);

        // SET UP DIALOG DATE PICKER (REPEAT)
        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);

        dayStart = day_x + "/" + (month_x + 1) + "/" + year_x;
        dayEnd = day_x + "/" + (month_x + 1) + "/" + year_x;

        tvStartDay = (TextView) findViewById(R.id.tv_start_day);
        tvStartDay.setText(day_x + "/" + (month_x + 1) + "/" + year_x);

        tvEndDay = (TextView) findViewById(R.id.tv_end_day);
        tvEndDay.setText(day_x + "/" + (month_x + 1) + "/" + year_x);

        tvStartDay.setOnClickListener(this);
        tvEndDay.setOnClickListener(this);

        // SET UP DIALOG TIME PICKER
        timeStartList = new String[7];
        timeEndList = new String[7];

        hour_x = calendar.get(Calendar.HOUR_OF_DAY);
        minute_x = calendar.get(Calendar.MINUTE);

        for (int i = 0; i < 7; i++) {
            if (minute_x >= 10) {
                timeStartList[i] = hour_x + ":" + minute_x;
                timeEndList[i] = hour_x + ":" + minute_x;
            } else {
                timeStartList[i] = hour_x + ":0" + minute_x;
                timeEndList[i] = hour_x + ":0" + minute_x;
            }
        }

        tvStartTime = (TextView) findViewById(R.id.tv_hour_start);
        tvStartTime.setText("From " + timeStartList[0]);

        tvEndTime = (TextView) findViewById(R.id.tv_hour_end);
        tvEndTime.setText("To " + timeEndList[0]);

        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

        tvSave = (TextView) findViewById(R.id.tv_save_add_subject);
        tvSave.setOnClickListener(this);
        subjectName = (TextView) findViewById(R.id.toolbar_subject_name);

        // ROOM
        previousDayPicked = 0;
        etRoomName = (EditText) findViewById(R.id.et_add_subject_room);
        roomList = new String[7];
        for (int i = 0; i < 7; i++) {
            roomList[i] = "";
        }

        etMaxAttendance = (EditText) findViewById(R.id.et_add_subject_attendance);
        etNote = (EditText) findViewById(R.id.et_add_subject_note);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSaved) {
            contactList = ContactHandleDB.getInstance(this).getListContact();
            teacherNameList = new ArrayList<>();
            teacherNameList.add("Add teacher");
            for (int i = 0; i < contactList.size(); i++) {
                teacherNameList.add(contactList.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, teacherNameList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTeachers.setAdapter(adapter);
            spinnerTeachers.setSelection(spinnerTeachers.getCount() - 1);
        }
        isSaved = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack_add_subject:
                super.onBackPressed();
                break;
            case R.id.iv_add_teacher_subject:
                if (spinnerTeachers.getSelectedItemPosition() == 0) {
                    ContactActivity.flag = 2;
                    Intent intent = new Intent(this,AddingContactActivity.class);
                    Bundle bundleEdit = new Bundle();
                    bundleEdit.putSerializable(RVContactAdapter.KEY_CONTACT, null);
                    intent.putExtras(bundleEdit);
                    this.startActivity(intent);
                }
                break;
            case R.id.tv_pick_days_of_week:
                for (int i = 0; i < 7; i++) {
                    if (isPickedDaysWeek[i]) {
                        tvDaysWeekList[i].setBackgroundResource(R.drawable.shape_blue_circle_50dp);
                        tvDaysWeekList[i].setTextColor(Color.WHITE);
                    } else {
                        tvDaysWeekList[i].setBackgroundResource(R.drawable.shape_white_circle_50dp);
                        tvDaysWeekList[i].setTextColor(Color.GRAY);
                    }
                }
                dialogDaysWeekPicker.show();
                break;
            case R.id.tv_start_day:
                pickingStartDay = true;
                onCreateDialog(0).show();
                break;
            case R.id.tv_end_day:
                pickingStartDay = false;
                onCreateDialog(0).show();
                break;
            case R.id.tv_hour_start:
                pickingStartTime = true;
                if (!daysPicked.get(0).equals("Pick days before")) {
                    onCreateDialog(1).show();
                }
                break;
            case R.id.tv_hour_end:
                pickingStartTime = false;
                if (!daysPicked.get(0).equals("Pick days before")) {
                    onCreateDialog(1).show();
                }
                break;
            case R.id.bt_dialog_day_picker:
                daysPicked.clear();
                for (int i = 0; i < 7; i++) {
                    if (isPickedDaysWeek[i]) {
                        daysPicked.add(tvDaysWeekList[i].getText().toString());
                    } else {
                        roomList[i] = "";
                    }
                }
                if (daysPicked.isEmpty()) {
                    daysPicked.add("Pick days before");
                    if (minute_x >= 10) {
                        tvStartTime.setText("From " + hour_x + ":" + minute_x);
                        tvEndTime.setText("To " + hour_x + ":" + minute_x);
                    } else {
                        tvStartTime.setText("From " + hour_x + ":0" + minute_x);
                        tvEndTime.setText("To " + hour_x + ":0" + minute_x);
                    }
                    etRoomName.setText("");
                }
                ArrayAdapter<String> adapterDayPicker = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_dropdown_item, daysPicked);
                adapterDayPicker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDays.setAdapter(adapterDayPicker);
                dialogDaysWeekPicker.hide();
                break;
            case R.id.tv_save_add_subject:
                save();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            if (pickingStartDay) {
                int date[] = Utils.getDate(tvStartDay.getText().toString());
                return new DatePickerDialog(this, dpickerListner, date[2], date[1] - 1, date[0]);
            } else {
                int date[] = Utils.getDate(tvEndDay.getText().toString());
                return new DatePickerDialog(this, dpickerListner, date[2], date[1] - 1, date[0]);
            }
        } else {
            if (pickingStartTime) {
                int hour = hour_x;
                int minute = minute_x;
                for (int i = 0; i < 7; i++) {
                    if (spinnerDays.getItemAtPosition(spinnerDays.getSelectedItemPosition()).toString().equals(tvDaysWeekList[i].getText())) {
                        hour = Utils.getTime(timeStartList[i])[0];
                        minute = Utils.getTime(timeStartList[i])[1];
                        break;
                    }
                }
                return new TimePickerDialog(this, tpickerListner, hour, minute, true);
            }
            else {
                int hour = hour_x;
                int minute = minute_x;
                for (int i = 0; i < 7; i++) {
                    if (spinnerDays.getItemAtPosition(spinnerDays.getSelectedItemPosition()).toString().equals(tvDaysWeekList[i].getText())) {
                        hour = Utils.getTime(timeEndList[i])[0];
                        minute = Utils.getTime(timeEndList[i])[1];
                        break;
                    }
                }
                return new TimePickerDialog(this, tpickerListner, hour, minute, true);
            }
        }
    }


    private DatePickerDialog.OnDateSetListener dpickerListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;
                    if (pickingStartDay) {
                        dayStart = day_x + "/" + month_x + "/" + year_x;
                        tvStartDay.setText(day_x + "/" + month_x + "/" + year_x);
                    } else {
                        dayEnd = day_x + "/" + month_x + "/" + year_x;
                        tvEndDay.setText(day_x + "/" + month_x + "/" + year_x);
                    }
                }
            };

    private TimePickerDialog.OnTimeSetListener tpickerListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    hour_x = i;
                    minute_x = i1;

                    if (pickingStartTime) {
                        for (int j = 0; j < 7; j++) {
                            if (spinnerDays.getSelectedItem().toString().equals(tvDaysWeekList[j].getText())) {
                                if (minute_x >= 10) {
                                    timeStartList[j] = "" + hour_x + ":" + minute_x;
                                } else {
                                    timeStartList[j] = "" + hour_x + ":0" + minute_x;
                                }
                                break;
                            }
                        }
                        if (minute_x >= 10) {
                            tvStartTime.setText("From " + hour_x + ":" + minute_x);
                        } else {
                            tvStartTime.setText("From " + hour_x + ":0" + minute_x);
                        }
                    } else {
                        for (int j = 0; j < 7; j++) {
                            if (spinnerDays.getSelectedItem().toString().equals(tvDaysWeekList[j].getText())) {
                                if (minute_x >= 10) {
                                    timeEndList[j] = "" + hour_x + ":" + minute_x;
                                } else {
                                    timeEndList[j] = "" + hour_x + ":0" + minute_x;
                                }
                                break;
                            }
                        }
                        if (minute_x >= 10) {
                            tvEndTime.setText("To " + hour_x + ":" + minute_x);
                        } else {
                            tvEndTime.setText("To " + hour_x + ":0" + minute_x);
                        }
                    }
                }
            };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_teachers_list:
                if (i == 0) {
                    ivAddTeacher.setAlpha(1f);
                } else {
                    ivAddTeacher.setAlpha(0.3f);
                }
                break;
            case R.id.spinner_picked_day_list:
                for (int j = 0; j < 7; j++) {
                    if (spinnerDays.getItemAtPosition(i).toString().equals(tvDaysWeekList[j].getText())) {
                        tvStartTime.setText("From " + timeStartList[j]);
                        tvEndTime.setText("To " + timeEndList[j]);
                        roomList[previousDayPicked] = String.valueOf(etRoomName.getText());
                        etRoomName.setText(roomList[j]);
                        previousDayPicked = j;
                        break;
                    }
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private TextView[] getTvDaysArr(View mView) {
        final TextView[] tvList = new TextView[7];
        tvList[0] = mView.findViewById(R.id.tv_dayWeeks_sun);
        tvList[1] = mView.findViewById(R.id.tv_dayWeeks_mon);
        tvList[2] = mView.findViewById(R.id.tv_dayWeeks_tue);
        tvList[3] = mView.findViewById(R.id.tv_dayWeeks_wed);
        tvList[4] = mView.findViewById(R.id.tv_dayWeeks_thu);
        tvList[5] = mView.findViewById(R.id.tv_dayWeeks_fri);
        tvList[6] = mView.findViewById(R.id.tv_dayWeeks_sat);
        for (int i = 0; i < 7; i++) {
            tvList[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = (TextView) view;
                    for (int j = 0; j < 7; j++) {
                        if (textView == tvList[j]) {
                            if (isPickedDaysWeek[j]) {
                                tvList[j].setBackgroundResource(R.drawable.shape_white_circle_50dp);
                                tvList[j].setTextColor(Color.GRAY);
                                isPickedDaysWeek[j] = false;
                            } else {
                                isPickedDaysWeek[j] = true;
                                tvList[j].setBackgroundResource(R.drawable.shape_blue_circle_50dp);
                                tvList[j].setTextColor(Color.WHITE);
                            }
                            break;
                        }
                    }
                }
            });
        }
        return tvList;
    }

    private void save() {
        if (subjectName.getText().length() == 0) {
            Utils.showWarningDialog(this, "Enter subject name");
            return ;
        }

        if (spinnerDays.getItemAtPosition(0).toString().equals("Pick days before")) {
            Utils.showWarningDialog(this, "Pick days to study");
            return ;
        }

        if (Utils.date1CompareDate2(Utils.getDate(dayStart), Utils.getDate(dayEnd)) == 1) {
            Utils.showWarningDialog(this, "Start day repeat must be smaller than end day");
            return ;
        }

        for (int i = 0; i < 7; i++) {
            if (isPickedDaysWeek[i] && Utils.time1CompareTime2(Utils.getTime(timeStartList[i]), Utils.getTime(timeEndList[i])) == 1) {
                Utils.showWarningDialog(this, "Start time of " + tvDaysWeekList[i].getText() + " must be smaller than time end");
                return ;
            }
        }

        int attendance = 0;
        if (!etMaxAttendance.getText().toString().equals("")) {
            attendance = Integer.parseInt(etMaxAttendance.getText().toString());
        }
        Subject subject;
        if (spinnerTeachers.getSelectedItemPosition() > 0) {
            subject = new Subject(1, subjectName.getText().toString(), contactList.get(spinnerTeachers.getSelectedItemPosition() - 1).getId(),
                    dayStart, dayEnd, true, 0, attendance, etNote.getText().toString());
        } else {
            subject = new Subject(1, subjectName.getText().toString(), -1,
                    dayStart, dayEnd, true, 0, attendance, etNote.getText().toString());
        }
        List<String> newRoomList = new ArrayList<>();
        List<String> newTimeStart = new ArrayList<>();
        List<String> newTimeEnd = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            if (isPickedDaysWeek[i]) {
                roomList[previousDayPicked] = String.valueOf(etRoomName.getText());
                newRoomList.add(roomList[i]);
                newTimeStart.add(timeStartList[i]);
                newTimeEnd.add(timeEndList[i]);
            }
        }

        subject.setDetail(newRoomList, daysPicked, newTimeStart, newTimeEnd);
        dialogDaysWeekPicker.dismiss();
        if (subjectBundle != null) {
            subject.setId(subjectBundle.getId());
            SubjectHandleDB.getInstance(this).updateSubject(subject);
            AlarmUtils.cancelAlarm(this, 1);
            AlarmUtils.create(this, 1);
            super.onBackPressed();
        } else {
            SubjectHandleDB.getInstance(this).insertSubject(subject);
            List<Subject> list = SubjectHandleDB.getInstance(this).getListSubject();
            subject.setId(list.get(list.size()-1).getId());
            Intent intent = new Intent(this, ShowingSubjectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RVSubjectAdapter.KEY_SUB ,subject);
            intent.putExtras(bundle);
            AlarmUtils.cancelAlarm(this, 1);
            AlarmUtils.create(this , 1);
            this.startActivity(intent);
            this.finish();
        }


    }



}
