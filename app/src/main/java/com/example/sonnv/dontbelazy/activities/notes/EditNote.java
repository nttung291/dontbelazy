package com.example.sonnv.dontbelazy.activities.notes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.notes.DateSpinner;
import com.example.sonnv.dontbelazy.adapter.notes.NoteAdapter;
import com.example.sonnv.dontbelazy.adapter.notes.SpinnerDateAdapter;
import com.example.sonnv.dontbelazy.adapter.notes.SpinnerTimeAdapter;
import com.example.sonnv.dontbelazy.adapter.notes.TimeSpiner;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.NoteHandleDB;
import com.example.sonnv.dontbelazy.utils.AlarmUtils;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.id.candidatesArea;
import static android.R.id.text1;

public class EditNote extends AppCompatActivity implements View.OnClickListener {

    private EditText edtMain;
    private EditText edtTitle;
    private Note note;
    private RelativeLayout relativeLayout;
    private AlertDialog dialogAdd;
    private Spinner spinnerDate;
    private TextView textView;
    private boolean changeTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition enterTansition = TransitionInflater.from(this).inflateTransition(R.transition.fade);
//        getWindow().setEnterTransition(enterTansition);

        setContentView(R.layout.activity_edit_note);

//        if (Build.VERSION.SDK_INT >= 21 ){
//            Explode enterTransition = new Explode();
//            enterTransition.setDuration(getResources().getInteger(500));
//            getWindow().setEnterTransition(enterTransition);
//            getWindow().setAllowEnterTransitionOverlap(false);
//        }

//        Slide slide = (Slide) TransitionInflater.from(this).inflateTransition(R.transition.slide);
//        getWindow().setExitTransition(slide);

        ImageView add = (ImageView) findViewById(R.id.iv_add_time_note);


        edtMain = (EditText) findViewById(R.id.edt_main_note);
        edtTitle = (EditText) findViewById(R.id.edt_note_title);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_time_note_edit);
        TextView ivSave = (TextView) findViewById(R.id.iv_save_note);
        ImageView ivback = (ImageView) findViewById(R.id.iv_back_editNote);
        textView = (TextView) findViewById(R.id.tv_date_edit);
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.fab_delete_note);


        ivback.setOnClickListener(this);
//        relativeLayout.setVisibility(View.INVISIBLE);
        ivSave.setOnClickListener(this);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        note = (Note) getIntent().getExtras().getSerializable(NoteAdapter.Key_NOTE);



        edtTitle.setText(note.getText());
        edtMain.setText(note.getMainText());

        if (!note.isNotifi()){
            relativeLayout.setVisibility(View.INVISIBLE);
        }else {
            relativeLayout.setVisibility(View.VISIBLE);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date d = Utils.getDate(s, note.getTime());
            int date = d.getDate();
            int month = d.getMonth() + 1;
            SimpleDateFormat out = new SimpleDateFormat("HH:mm");

            Log.d("cuonghx", "onStart: " + d.getMonth());
            textView.setText(date + " thg " + month +"," + out.format(d));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_save_note:

                if (edtTitle.getText().length() == 0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Warning !!!");
                    alertDialogBuilder
                            .setMessage("Set the title before saving!")
                            .setCancelable(false)
                            .setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }else {

                    if (note.getId() < 0) {
                        if (changeTime) {
                            note.setNotifi(true);
                        } else {
                            note.setNotifi(false);
                        }
                        if (note.isNotifi()) {
                            NoteHandleDB.getInstence(this).setNote(edtTitle.getText().toString(), edtMain.getText().toString(), SpinnerDateAdapter.current.getDate() + " " + SpinnerTimeAdapter.choose.getTime(), note.isNotifi());
                            AlarmUtils.cancelAlarm(this, 2);
                            AlarmUtils.create(this , 2);
                        } else {
                            NoteHandleDB.getInstence(this).setNote(edtTitle.getText().toString(), edtMain.getText().toString(), "00/00/5555 00:00", note.isNotifi());

                        }
                    }
                    if (changeTime) {
                        note.setNotifi(true);
                        NoteHandleDB.getInstence(this).editNote(note, edtTitle.getText().toString(), edtMain.getText().toString(), SpinnerDateAdapter.current.getDate() + " " + SpinnerTimeAdapter.choose.getTime(), true);
                        AlarmUtils.cancelAlarm(this, 2);
                        AlarmUtils.create(this , 2);
                    } else {
                        Log.d("cuonghx", "onClick: " + note.isNotifi());
                        if (!note.isNotifi()) {
                            NoteHandleDB.getInstence(this).editNote(note, edtTitle.getText().toString(), edtMain.getText().toString(), "00/00/5555 00:00", false);
                        } else {
                            AlarmUtils.cancelAlarm(this, 2);
                            AlarmUtils.create(this , 2);
                            Log.d("cuonghx", "onClick: " + note.getTime());
                            NoteHandleDB.getInstence(this).editNote(note, edtTitle.getText().toString(), edtMain.getText().toString(), note.getTime(), true);
                        }
                    }
                    Toast.makeText(EditNote.this, "success",
                            Toast.LENGTH_SHORT).show();
                    super.onBackPressed();
                }
                break;
            case R.id.iv_add_time_note:
                SetUpDialog();
                dialogAdd.show();

                break;
            case R.id.tv_save_edit:
                changeTime = true;
                relativeLayout.setVisibility(View.VISIBLE);
                SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                Date d = Utils.getDate(s,  SpinnerDateAdapter.current.getDate());
                int date = d.getDate();
                int month = d.getMonth() + 1;
                SimpleDateFormat out = new SimpleDateFormat("HH:mm");

                Log.d("cuonghx", "onStart: " + d.getMonth());
                textView.setText(date + " thg " + month +" , " + SpinnerTimeAdapter.choose.getTime());
                Log.d("cuonghx123", "onClick: " + SpinnerTimeAdapter.choose.getTime());
                Log.d("cuonghx123", "onClick: " + SpinnerDateAdapter.current.getDate());
                dialogAdd.hide();
                break;
            case R.id.tv_huy_edit:
                changeTime = false;
                dialogAdd.hide();
                break;
            case R.id.tv_delete_edit:
                relativeLayout.setVisibility(View.INVISIBLE);
                changeTime = false;
                note.setNotifi(false);
                dialogAdd.hide();
                break;
            case  R.id.iv_back_editNote:
                super.onBackPressed();
                break;
            case R.id.fab_delete_note:
                NoteHandleDB.getInstence(this).deleteNote(note.getId());
                super.onBackPressed();
                break;
        }
    }

    private void SetUpDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_note_seting, null);
        mBuilder.setView(mView);
        dialogAdd = mBuilder.create();
        final Spinner spinnerDate = mView.findViewById(R.id.spinner_date);
        final Spinner spinnerTime = mView.findViewById(R.id.spinner_time);

        final ArrayList<DateSpinner> list = new ArrayList<>();

        list.add(new DateSpinner(1, "Today"));
        list.add(new DateSpinner(2, "Tomorrow"));
        list.add(new DateSpinner(3, "Next day"));
        list.add(new DateSpinner(4, "Pick a date..."));

        spinnerDate.setAdapter(new SpinnerDateAdapter(this, android.R.layout.simple_spinner_dropdown_item, list));

        final ArrayList<TimeSpiner> listTime = new ArrayList<>();
//        listTime.add(new TimeSpiner(null, "----------------select Time--------------"));
        listTime.add(new TimeSpiner("08:00", "Morning"));
        listTime.add(new TimeSpiner("13:00", "Afternoon"));
        listTime.add(new TimeSpiner("18:00", "Evening"));
        listTime.add(new TimeSpiner("20:00", "Night"));
        listTime.add(new TimeSpiner(null, "Pick a time...."));

        spinnerTime.setAdapter(new SpinnerTimeAdapter(this, R.layout.spinner_item, listTime));




        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, int pos, long id) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                if (pos == 4){
                    Date date = Utils.getDate(simpleDateFormat, SpinnerTimeAdapter.choose.getTime());
                    int hour = date.getHours();
                    int minute = date.getMinutes();
                    final TimePickerDialog mTimePicker ;
                    mTimePicker = new TimePickerDialog(EditNote.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            TextView textView = view.findViewById(R.id.tv_first_spinner);
                            if (selectedMinute < 10) {
                                textView.setText(selectedHour + ":0" + selectedMinute);
                                SpinnerTimeAdapter.choose.setTime(selectedHour + ":0" + selectedMinute);
                            }
                            else {
                                textView.setText(selectedHour + ":" + selectedMinute);
                                SpinnerTimeAdapter.choose.setTime(selectedHour + ":" + selectedMinute);
                            }
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }else {
                    SpinnerTimeAdapter.choose.setTime(listTime.get(pos).getTime());
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("cuonghx", "onItemSelected: ");
            }
        });

        final TextView textView = mView.findViewById(R.id.tv_save_edit);
        final TextView tvcancel = mView.findViewById(R.id.tv_huy_edit);
        final TextView tvdelete = mView.findViewById(R.id.tv_delete_edit);

        textView.setOnClickListener(this);
        tvcancel.setOnClickListener(this);
        tvdelete.setOnClickListener(this);

        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {

                SimpleDateFormat datefomat = new SimpleDateFormat("dd/MM/yyyy");


                if (i == 3){
                    Log.d("cuonghx1", "onItemSelected: " + SpinnerDateAdapter.current.getDate());
                    Date date = Utils.getDate(datefomat, SpinnerDateAdapter.current.getDate());

                    Log.d("cuonghx1", "onItemSelected: " + date);
                    final Calendar mcurrentDate = Calendar.getInstance();
                    mcurrentDate.setTime(date);

                    final int year = mcurrentDate.get(Calendar.YEAR);
                    final int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    final DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(EditNote.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            // TODO Auto-generated method stub
                            CheckedTextView textView = view.findViewById(text1);
                    /*      Your code   to get date and time    */
                            selectedmonth = selectedmonth + 1;
                            if (selectedyear > year){

                                textView.setText(Utils.getNowMonth(selectedmonth - 1) + " " +  selectedday + ", " + selectedyear );
                            }else {
                                textView.setText(Utils.getNowMonth(selectedmonth - 1) + " " + selectedday);
                            }

                            if (selectedmonth < 10 && selectedday < 10){
                                SpinnerDateAdapter.current.setDate("0" + selectedday + "/" + "0"+ selectedmonth + "/" + selectedyear);
                            }else if (selectedday < 10){
                                SpinnerDateAdapter.current.setDate("0" + selectedday + "/" +  selectedmonth + "/" + selectedyear);
                            }else if (selectedmonth < 10){
                                SpinnerDateAdapter.current.setDate( selectedday + "/" + "0"+ selectedmonth + "/" + selectedyear);
                            }else {
                                SpinnerDateAdapter.current.setDate(selectedday + "/" + selectedmonth + "/" + selectedyear);
                            }
                        }
                    }, mYear, mMonth, mDay);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }else {
                    SpinnerDateAdapter.current.setDate(Utils.getDate(list.get(i).getId()));
                    Log.d("cuonghx", "onItemSelected: " + Utils.getDate(list.get(i).getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
