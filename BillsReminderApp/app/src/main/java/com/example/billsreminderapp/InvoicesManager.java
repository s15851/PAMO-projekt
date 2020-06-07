package com.example.billsreminderapp;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//Class responsible for write date to Json
class InvoicesManager {

    private SharedPreferences sharedPreferences;

    //constructor
    InvoicesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    // function to take invoices ( type set<> collections)
    private Set<Invoice> takeInvoices() {
        Set<String> invoicesJson = sharedPreferences.getStringSet("listaFaktur", new HashSet<String>());
        final Gson gson = new Gson();
        Set<Invoice> invoices = new HashSet<>();

        for (String invoiceString: invoicesJson) {
            invoices.add(gson.fromJson(invoiceString, Invoice.class));
        }
        return invoices;
    }

    //function to take invoices which was't paid
    Set<Invoice> takeOverdue() {
        Set<Invoice> allInvoices = this.takeInvoices();

        Date now = new Date();
        Set<Invoice> invoices = new HashSet<>();

        for (Invoice inv: allInvoices) {
            if(inv.getTerm().compareTo(now) <= 0){
                invoices.add(inv);
            }
        }
        return invoices;
    }

    //take invoices to be paid in the future
    Set<Invoice> takeFuture() {
        Set<Invoice> allInvoices = this.takeInvoices();

        Date now = new Date();
        Set<Invoice> invoices = new HashSet<>();

        for (Invoice inv: allInvoices) {
            if(inv.getTerm().compareTo(now) > 0){
                invoices.add(inv);
            }
        }
        return invoices;
    }

    //take invoices to be paid in the future, but the time has come
    Set<Invoice> takeIncoming() {
        Set<Invoice> allInvoices = this.takeInvoices();
        Set<Invoice> invoices = new HashSet<>();

        for (Invoice inv: allInvoices) {
            Integer whenInform = inv.getWhenInform();
            if(whenInform != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.add(Calendar.DAY_OF_MONTH, whenInform);

                Calendar today = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                if(inv.getTerm().after(today.getTime()) && inv.getTerm().compareTo(calendar.getTime()) <= 0){
                    invoices.add(inv);
                }
            }
        }
        return invoices;
    }

    //delete invoice from json
    void deleteInvoice(Invoice invoice) {
        Set<String> invoicesJson = sharedPreferences.getStringSet("listaFaktur", new HashSet<String>());
        final Gson gson = new Gson();

        //delete invoice
        Set<Invoice> invoices = new HashSet<>();

        for (String invoiceString: invoicesJson) {
            invoices.add(gson.fromJson(invoiceString, Invoice.class));
        }
        invoices.remove(invoice);

        //take new (nit deleted) invoices after delete
        Set<String> newInvoices = new HashSet<>();
        for (Invoice invoice1: invoices) {
            newInvoices.add(gson.toJson(invoice1));
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("listaFaktur", newInvoices).apply();
    }

    //add new invoice to json
    void addInvoice(Invoice invoice) {
        Set<String> writedInvoices = sharedPreferences.getStringSet("listaFaktur", new HashSet<String>());
        Set<String> newInvoices = new HashSet<>(writedInvoices);
        Gson gson = new Gson();

        String invoiceString = gson.toJson(invoice);
        newInvoices.add(invoiceString);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("listaFaktur", newInvoices).apply();
    }
}
