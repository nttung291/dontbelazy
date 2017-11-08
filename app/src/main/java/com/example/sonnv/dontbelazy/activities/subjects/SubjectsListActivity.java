package com.example.sonnv.dontbelazy.activities.subjects;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.activities.AddingSubjectActivity;
import com.example.sonnv.dontbelazy.adapter.RVSubjectAdapter;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubjectsListActivity extends AppCompatActivity implements View.OnClickListener{
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView image;
    private RecyclerView rvSubjectsList;
    private ImageView ivBack;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Subjects List");
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.titleTextColor));

        rvSubjectsList = (RecyclerView) findViewById(R.id.rv_subjects_list);
        rvSubjectsList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvSubjectsList.setLayoutManager(llm);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_subjects_list_add);
        fabAdd.setOnClickListener(this);

        ivBack = (ImageView) findViewById(R.id.iv_subjects_list_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_subjects_list_add:
                Intent intent = new Intent(this, AddingSubjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RVSubjectAdapter.KEY_SUB, null);
                intent.putExtras(bundle);
               
                this.startActivity(intent);
                break;
            case R.id.iv_subjects_list_back:
                super.onBackPressed();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        RVSubjectAdapter rvSubjectAdapter = new RVSubjectAdapter(SubjectHandleDB.getInstance(this).getListSubject());
        rvSubjectsList.setAdapter(rvSubjectAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
