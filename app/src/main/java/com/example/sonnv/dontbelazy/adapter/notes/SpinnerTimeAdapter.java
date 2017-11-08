package com.example.sonnv.dontbelazy.adapter.notes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.notes.EditNote;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by cuonghx2709 on 9/29/2017.
 */

public class SpinnerTimeAdapter extends ArrayAdapter<TimeSpiner> {

    private Context context;
    private int resource;
    public static ArrayList<TimeSpiner> list = new ArrayList<>();
    public static TextView tvView ;
    public boolean created;
    public static TimeSpiner choose = new TimeSpiner(null, null);


    public SpinnerTimeAdapter (@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TimeSpiner> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);


        tvView = convertView.findViewById(R.id.tv_first_spinner);

        TimeSpiner timeSpiner = list.get(position);
        tvView.setText(timeSpiner.getTime());

        return convertView;
    }



    @Override
    public View getDropDownView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        final TimeSpiner timeSpiner = list.get(position);
        final TextView textView = convertView.findViewById(R.id.tv_first_spinner);
        TextView textViewSecond = convertView.findViewById(R.id.tv_second_spinner);

        textView.setText(timeSpiner.getName());
        textViewSecond.setText(timeSpiner.getTime());

        return convertView;
    }

}
