package com.example.sonnv.dontbelazy.adapter.notes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.id.text1;

/**
 * Created by cuonghx2709 on 9/29/2017.
 */

public class SpinnerDateAdapter extends ArrayAdapter<DateSpinner> {

    private Context context;
    private int resource;
    private ArrayList<DateSpinner> list = new ArrayList<>();
    public static DateSpinner current = new DateSpinner(0, null);

    public SpinnerDateAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DateSpinner> objects) {
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

        final CheckedTextView textView = convertView.findViewById(text1);
        DateSpinner dateSpinner = list.get(position);
        Calendar date = Calendar.getInstance();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");


        switch (dateSpinner.getId()){
            case 1:
                Date need1 = date.getTime();
                int month = need1.getMonth();
                if (need1.getYear() > new Date().getYear()){
                    textView.setText(Utils.getNowMonth(month) + " " + need1.getDate() + "," + need1.getYear() );
                }else {
                    textView.setText(Utils.getNowMonth(month) +  " " + need1.getDate());
                }
                break;
            case 2:
                date.add(Calendar.DAY_OF_MONTH, +1);
                Date need2 = date.getTime();
                int month2 = need2.getMonth();
                if (need2.getYear() > new Date().getYear()){
                    textView.setText(Utils.getNowMonth(month2) + " " + need2.getDate() + "," + need2.getYear() );
                }else {
                    textView.setText(Utils.getNowMonth(month2) +  " " + need2.getDate());
                }
                break;
            case 3:
                date.add(Calendar.DAY_OF_MONTH, +7);
                Date need = date.getTime();
                int month3 = need.getMonth();
                if (need.getYear() > new Date().getYear()){
                    textView.setText(Utils.getNowMonth(month3) + " " + need.getDate() + "," + need.getYear() );
                }else {
                    textView.setText(Utils.getNowMonth(month3) +  " " + need.getDate());
                }
                break;
            case 4:
                break;

        }


        return convertView;


    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource, parent, false);

        DateSpinner dateSpinner = list.get(position);

        CheckedTextView textView = convertView.findViewById(text1);
        textView.setText(dateSpinner.getName());
        String day = Utils.getNowDayofWeek();
        if (dateSpinner.getId() == 3){
            textView.setText("Next " + day);
        }

        return convertView;
    }
}
