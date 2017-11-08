package com.example.sonnv.dontbelazy.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    public static int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Gabriola.ttf");
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_contact));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_contact);
        collapsingToolbarLayout.setTitle("Teacher List");
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.titleTextColor));
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_contact_add);

        recyclerView = (RecyclerView) findViewById(R.id.rv_contact);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        ivBack = (ImageView) findViewById(R.id.iv_contact_back);

        ivBack.setOnClickListener(this);
        fabAdd.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        RVContactAdapter rvContactAdapter = new RVContactAdapter(ContactHandleDB.getInstance(this).getListContact());
        recyclerView.setAdapter(rvContactAdapter);

        ContactHandleDB.getInstance(this).getListContact();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_contact_add:
                flag = 1;
                Intent intent = new Intent(this,AddingContactActivity.class);
                Bundle bundleEdit = new Bundle();
                bundleEdit.putSerializable(RVContactAdapter.KEY_CONTACT, null);
                intent.putExtras(bundleEdit);
                this.startActivity(intent);
                break;
            case R.id.iv_contact_back:
                super.onBackPressed();
                break;
        }
    }
}