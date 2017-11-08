package com.example.sonnv.dontbelazy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.CourseAdapter;
import com.example.sonnv.dontbelazy.databases.CourseMode;

public class CourseActivityView extends AppCompatActivity {

    private static final String TAG = CourseAdapter.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        CourseMode courseMode = (CourseMode) getIntent().getExtras().getSerializable(CourseAdapter.KEY_COURSE);

        Log.d(TAG, "onCreate: " + courseMode);
    }
}
