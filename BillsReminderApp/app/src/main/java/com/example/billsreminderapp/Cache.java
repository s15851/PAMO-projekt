package com.example.billsreminderapp;

//cache-holding class. To handle invoices

class Cache {

    //variable to mark invoice
    private Invoice markedInvoice = null;

    //variable to edit invoice
    private Invoice editedInvoice = null;

    // new object cache
    private static final Cache cache = new Cache();

    //getter of cache object
    static Cache getInstance() {return cache;}

    //getters, setters
    Invoice getMarkedInvoice() {
        return markedInvoice;
    }

    void setMarkedInvoice(Invoice markedInvoice) {
        this.markedInvoice = markedInvoice;
    }
    Invoice getEditedInvoice() {
        return editedInvoice;
    }
    void setEditedInvoice(Invoice editedInvoice) {
        this.editedInvoice = editedInvoice;
    }
}
