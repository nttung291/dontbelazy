package com.example.sonnv.dontbelazy.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;
import com.example.sonnv.dontbelazy.databases.SubjectHandleDB;

public class ShowingContactActivity extends AppCompatActivity implements View.OnClickListener{
    private Boolean isFabOpen = false;
    private FloatingActionButton fabPen,fabEdit,fabTrash;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private LinearLayout linearLayoutStart;
    private ImageView ivBack;
    private ImageView sex;
    private TextView name,mail,phoneNumber,address,subject,note,tvEdit,tvTrash;

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_contact);
        setUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab_contact_pen:
                animateFAB();
                break;
            case R.id.iv_back_showcontact:
                onBackPressed();
                break;
            case R.id.fab_contac_edit:
                ContactActivity.flag = 3;
                Intent intentEdit = new Intent(this,AddingContactActivity.class);
                Bundle bundleEdit = new Bundle();
                bundleEdit.putSerializable(RVContactAdapter.KEY_CONTACT, contact);
                intentEdit.putExtras(bundleEdit);
                this.startActivity(intentEdit);
                break;
            case R.id.fab_contact_trash:
                SubjectHandleDB.getInstance(this).updateTeacher(contact.getId());
                ContactHandleDB.getInstance(this).deleteContact(contact.getId());
                super.onBackPressed();
                break;

        }
    }

    private void setUI(){
        fabPen = (FloatingActionButton)findViewById(R.id.fab_contact_pen);
        fabEdit = (FloatingActionButton)findViewById(R.id.fab_contac_edit);
        fabTrash = (FloatingActionButton)findViewById(R.id.fab_contact_trash);
        tvEdit = (TextView) findViewById(R.id.tv_edit_showingcontact);
        tvTrash = (TextView) findViewById(R.id.tv_trash_showingcontact);

        linearLayoutStart = (LinearLayout) findViewById(R.id.lnl_contact);

        ivBack = (ImageView) findViewById(R.id.iv_back_showcontact);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);

        sex = (ImageView) findViewById(R.id.im_sex_showcontact);
        name = (TextView) findViewById(R.id.tv_name_showcontact);
        mail = (TextView) findViewById(R.id.tv_mail_showcontact);
        phoneNumber = (TextView) findViewById(R.id.tv_phone_showcontact);
        address = (TextView) findViewById(R.id.tv_address_showcontact);
        subject = (TextView) findViewById(R.id.tv_subject_showcontact);
        note = (TextView) findViewById(R.id.tv_note_showcontact);

        fabPen.setOnClickListener(this);
        fabEdit.setOnClickListener(this);
        fabTrash.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void loadData() {
        contact = (Contact) getIntent().getExtras()
                .getSerializable(RVContactAdapter.KEY_CONTACT);
        contact = ContactHandleDB.getInstance(this).getContactAtId(contact.getId());
        name.setText(contact.getName());
        mail.setText(contact.getMail());
        phoneNumber.setText(contact.getPhoneNumber());
        address.setText(contact.getAdress());
        subject.setText(contact.getSubject());
        note.setText(contact.getNote());

        if (contact.isMale()){
            sex.setImageResource(R.drawable.ic_male);
        }else{
            sex.setImageResource(R.drawable.ic_female);
        }
    }

    private void animateFAB() {
        if(isFabOpen){
            linearLayoutStart.setAlpha(1);

            fabPen.startAnimation(rotate_backward);
            fabEdit.startAnimation(fab_close);
            fabTrash.startAnimation(fab_close);
            tvEdit.startAnimation(fab_close);
            tvTrash.startAnimation(fab_close);

            fabEdit.setClickable(false);
            fabTrash.setClickable(false);

            isFabOpen = false;
            Log.d("Start", "close");

        } else {
            linearLayoutStart.setAlpha(0.2f);

            fabPen.startAnimation(rotate_forward);
            fabEdit.startAnimation(fab_open);
            fabTrash.startAnimation(fab_open);
            tvEdit.startAnimation(fab_open);
            tvTrash.startAnimation(fab_open);

            fabEdit.setClickable(true);
            fabTrash.setClickable(true);

            isFabOpen = true;
            Log.d("Start","open");
        }
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        this.finish();
    }
}
