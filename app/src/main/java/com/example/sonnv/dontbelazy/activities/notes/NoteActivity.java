package com.example.sonnv.dontbelazy.activities.notes;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionValues;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.notes.NoteAdapter;
import com.example.sonnv.dontbelazy.databases.Note;
import com.example.sonnv.dontbelazy.databases.NoteHandleDB;
import com.example.sonnv.dontbelazy.utils.Utils;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = NoteActivity.class.toString();
    private RecyclerView recyclerView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        recyclerView = (RecyclerView) findViewById(R.id.lv_note);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        FloatingActionButton addButon = (FloatingActionButton) findViewById(R.id.fab_note_add);
        addButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, EditNote.class);
                Bundle bundle = new Bundle();
                Note note = new Note(-1, null, null, null, false);
                bundle.putSerializable(NoteAdapter.Key_NOTE, note);
                intent.putExtras(bundle);
                NoteActivity.this.startActivity(intent);
            }
        });
        Log.d(TAG, "onCreate: ");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_note);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_note));
        collapsingToolbarLayout.setTitle("My Note");
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.titleTextColor));
        ImageView back = (ImageView) findViewById(R.id.iv_note_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NoteActivity.super.onBackPressed();
//                NoteActivity.super.finishAfterTransition();
//                finishAfterTransition();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        NoteHandleDB.getInstence(this).getList();
        recyclerView.setAdapter(new NoteAdapter(this, Utils.sortNoteByDate(NoteHandleDB.getInstence(this).getList()), NoteActivity.this));

        List<Note> list = Utils.sortNoteByDate(NoteHandleDB.getInstence(this).getList());

        for (Note  note : list){
            Log.d(TAG, "onStart: " + note);
        }
    }
}
