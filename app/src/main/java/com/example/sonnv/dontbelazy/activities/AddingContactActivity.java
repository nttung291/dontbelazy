package com.example.sonnv.dontbelazy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonnv.dontbelazy.R;
import com.example.sonnv.dontbelazy.adapter.RVContactAdapter;
import com.example.sonnv.dontbelazy.databases.Contact;
import com.example.sonnv.dontbelazy.databases.ContactHandleDB;

public class AddingContactActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private ImageView imMale;
    private ImageView imFemale;
    private ImageView imBack;
    private TextView tvSave;
    private boolean isMale;
    private EditText etName,etMail,etPhone,etAddress,etSubject,etNote;
    private Contact contact;
    private Contact contactBundle;
    String name,mail,address,subject,note;
    String phoneNumber;

    private Animation scaleZoomin,scaleZoomout;
    private boolean isClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_contact);
        contactBundle = (Contact) getIntent().getExtras().getSerializable(RVContactAdapter.KEY_CONTACT);
        setUI();
        if (contactBundle != null) {
            setUIEdit();
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.im_male_addcontact:
                imMale.startAnimation(scaleZoomin);
                isMale = true;
                imMale.setClickable(false);
                if (isClick){
                    imFemale.startAnimation(scaleZoomout);
                    imFemale.setClickable(true);
                }
                isClick = true;
                break;
            case R.id.im_female_addcontact:
                imFemale.startAnimation(scaleZoomin);
                isMale = false;
                imFemale.setClickable(false);
                if (isClick){
                    imMale.startAnimation(scaleZoomout);
                    imMale.setClickable(true);
                }
                isClick = true;
                break;
            case R.id.im_back_addcontact:
                this.finish();
                super.onBackPressed();
                break;
            case R.id.tv_save_addcontact:
                if (etName.getText().length() == 0){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Warning !!!");
                    alertDialogBuilder
                            .setMessage("Enter Full Name")
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
                }else{
                    if (ContactActivity.flag == 1){
                        insertContact();
                        Intent intent = new Intent(this, ShowingContactActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RVContactAdapter.KEY_CONTACT,contact);
                        intent.putExtras(bundle);
                        this.startActivity(intent);
                    }else if (ContactActivity.flag == 2){
                        AddingSubjectActivity.isSaved = true;
                        insertContact();
                        super.onBackPressed();
                    }else if (ContactActivity.flag == 3){
                        name = etName.getText().toString();
                        mail = etMail.getText().toString();
                        phoneNumber = etPhone.getText().toString();
                        address = etAddress.getText().toString();
                        subject = etSubject.getText().toString();
                        note = etNote.getText().toString();
                        contactBundle.setName(name);
                        contactBundle.setMail(mail);
                        contactBundle.setPhoneNumber(phoneNumber);
                        contactBundle.setAdress(address);
                        contactBundle.setSubject(subject);
                        contactBundle.setNote(note);
                        ContactHandleDB.getInstance(this).editContact(contactBundle,name,mail,phoneNumber,address,subject,note,isMale);
                        contact = contactBundle;
                    }
                    this.finish();
                }
                break;
        }
    }

    public void insertContact(){
        name = etName.getText().toString();
        mail = etMail.getText().toString();
        phoneNumber = etPhone.getText().toString();
        address = etAddress.getText().toString();
        subject = etSubject.getText().toString();
        note = etNote.getText().toString();
        ContactHandleDB.getInstance(this).setContact(name,mail,phoneNumber,address,subject,note,isMale);
        for (Contact newContact : ContactHandleDB.getInstance(this).getListContact()){
            contact = newContact;
        }
    }

    public void setUI(){
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        imMale = (ImageView) findViewById(R.id.im_male_addcontact);
        imFemale = (ImageView) findViewById(R.id.im_female_addcontact);
        imBack = (ImageView) findViewById(R.id.im_back_addcontact);
        etName = (EditText) findViewById(R.id.et_name_addcontact);
        etMail = (EditText) findViewById(R.id.et_mail_addcontact);
        etPhone = (EditText) findViewById(R.id.et_phonenumber_addcontact);
        etAddress = (EditText) findViewById(R.id.et_address_addcontact);
        etSubject = (EditText) findViewById(R.id.et_subject_addcontact);
        etNote = (EditText) findViewById(R.id.et_note_addcontact);
        tvSave = (TextView) findViewById(R.id.tv_save_addcontact);

        scaleZoomin = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scalezoomin);
        scaleZoomout = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scalezoomout);

        imMale.setOnClickListener(this);
        imFemale.setOnClickListener(this);
        imBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    private void setUIEdit() {
        etName.setText(contactBundle.getName());
        etMail.setText(contactBundle.getMail());
        etPhone.setText(contactBundle.getPhoneNumber());
        etAddress.setText(contactBundle.getAdress());
        etSubject.setText(contactBundle.getSubject());
        etNote.setText(contactBundle.getNote());
        isMale = contactBundle.isMale();
        if (isMale){
            imMale.startAnimation(scaleZoomin);
            isMale = true;
            imMale.setClickable(false);
            if (isClick){
                imFemale.startAnimation(scaleZoomout);
                imFemale.setClickable(true);
            }
            isClick = true;
        }else{
            imFemale.startAnimation(scaleZoomin);
            isMale = false;
            imFemale.setClickable(false);
            if (isClick){
                imMale.startAnimation(scaleZoomout);
                imMale.setClickable(true);
            }
            isClick = true;
        }
    }
}
