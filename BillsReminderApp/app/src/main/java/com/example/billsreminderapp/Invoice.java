package com.example.billsreminderapp;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import org.joda.time.DateTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Invoice implements Comparable {
    //variables
    private String receiver;
    private Double charge;
    private Date term;
    private String comments = "";
    private Integer howManyTimesPayment = 0;
    private boolean isRepeated;
    private String repetitionPeriod = "";
    private Integer whenInform;
    private boolean isPaid;

    //Constructor with basic parameters
    Invoice(String receiver, Double charge, String term, String comments){
        this.receiver = receiver;
        this.charge = charge;
        this.comments = comments;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl", "PL"));
        // try-catch parse error
        try {
            this.term =  df.parse(term);
        } catch (ParseException e) {
            this.term = null;
        }
    }

    Invoice(String receiver, Double charge, String term, Integer whenInform){
        this.receiver = receiver;
        this.charge = charge;
        this.comments = comments;
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl", "PL"));
        // try-catch parse error
        try {
            this.term =  df.parse(term);
        } catch (ParseException e) {
            this.term = null;
        }
        this.setWhenInform(whenInform);
    }

    //function to set how often you have to pay the invoice
    void extendTime() {
        DateTime originalDate = new DateTime(this.getTerm());
        DateTime extendDate;
        switch (this.repetitionPeriod) {
            case "DZIEŃ":
                extendDate = originalDate.plusDays(this.getHowManyTimesPayment());
                break;
            case "TYDZIEŃ":
                extendDate = originalDate.plusWeeks(this.getHowManyTimesPayment());
                break;
            case "MIESIĄC":
                extendDate = originalDate.plusMonths(this.getHowManyTimesPayment());
                break;
            case "ROK":
                extendDate = originalDate.plusYears(this.getHowManyTimesPayment());
                break;
            default:
                extendDate = originalDate;
        }
        this.setTerm(extendDate.toDate());
    }

    //Getters and Setters
    String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    Date getTerm() {
        return term;
    }

    String getTermString() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(this.term);
    }

    String getTimeToDisplay() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(this.term);
    }

    Integer getHowManyTimesPayment() {
        return howManyTimesPayment;
    }

    boolean isRepeated() {
        return isRepeated;
    }

    String getRepetitionPeriod() {
        return repetitionPeriod;
    }

    Integer getWhenInform() {
        return whenInform;
    }

    public boolean isisPaid() {
        return isPaid;
    }

    private void setTerm(Date term) {
        this.term = term;
    }

    String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    void setHowManyTimesPayment(Integer howManyTimesPayment) {
        this.howManyTimesPayment = howManyTimesPayment;
    }

    void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    void setRepetitionPeriod(String repetitionPeriod) {
        this.repetitionPeriod = repetitionPeriod;
    }

    void setWhenInform(Integer whenInform) {
        this.whenInform = whenInform;
    }

    //comparing object types.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (isRepeated != invoice.isRepeated) return false;
        if (isPaid != invoice.isPaid) return false;
        if (!receiver.equals(invoice.receiver)) return false;
        if (!charge.equals(invoice.charge)) return false;
        if (!term.equals(invoice.term)) return false;
        if (!comments.equals(invoice.comments)) return false;
        if (!howManyTimesPayment.equals(invoice.howManyTimesPayment)) return false;
        if (!repetitionPeriod.equals(invoice.repetitionPeriod)) return false;
        return whenInform.equals(invoice.whenInform);
    }

    //??????????????????????????????????????????
    @Override
    public int hashCode() {
        int result = receiver.hashCode();
        result = 31 * result + charge.hashCode();
        result = 31 * result + term.hashCode();
        result = 31 * result + comments.hashCode();
        result = 31 * result + howManyTimesPayment.hashCode();
        result = 31 * result + (isRepeated ? 1 : 0);
        result = 31 * result + repetitionPeriod.hashCode();
        result = 31 * result + whenInform.hashCode();
        result = 31 * result + (isPaid ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.term.compareTo(((Invoice) o).getTerm());
    }
}
