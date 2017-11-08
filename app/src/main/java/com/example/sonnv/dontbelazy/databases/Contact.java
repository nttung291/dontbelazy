package com.example.sonnv.dontbelazy.databases;

import java.io.Serializable;

/**
 * Created by Nttung PC on 9/21/2017.
 */

public class Contact implements Serializable {
    private String name;
    private String mail;
    private String phoneNumber;
    private String adress;
    private String subject;
    private String note;
    private int id;
    private boolean isMale;

    public Contact(int id,String name, String mail, String phoneNumber, String adress, String subject, String note,boolean sex) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.subject = subject;
        this.note = note;
        this.isMale = sex;
    }

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public boolean isMale() {
        return isMale;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public String getSubject() {
        return subject;
    }

    public String getNote() {
        return note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", adress='" + adress + '\'' +
                ", subject='" + subject + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
