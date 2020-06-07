package com.example.billsreminderapp;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.hamcrest.core.IsEqual;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import java.util.HashSet;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvoicesManagerTest {

    private static SharedPreferences sharedPreferences;
    private static InvoicesManager invoicesManager;

    private static String produceInvoiceAsJson(Invoice invoice) {
        Gson gson = new Gson();
        return gson.toJson(invoice);
    }

    @BeforeClass
    public static void setUp() {
        LocalDate now = LocalDate.now();
        Set<Invoice> invoices = new HashSet<>();
        Set<String> invoicesAsJsons = new HashSet<>();
        invoices.add(new Invoice("Play", new Double("49.00"), now.minusDays(10).toString(), 10));
        invoices.add(new Invoice("Orange", new Double("29.00"), now.minusDays(5).toString(), 5));
        invoices.add(new Invoice("Energa", new Double("149.00"), now.toString(), 2));
        invoices.add(new Invoice("Mieszkanie", new Double("549.00"), now.plusDays(5).toString(), 5));
        invoices.add(new Invoice("UPC", new Double("39.00"), now.plusDays(10).toString(), 0));
        for (Invoice invoice : invoices) {
            invoicesAsJsons.add(produceInvoiceAsJson(invoice));
        }
        sharedPreferences = mock(SharedPreferences.class);
        when(sharedPreferences.getStringSet(eq("listaFaktur"), any(Set.class))). thenReturn(invoicesAsJsons);

        invoicesManager = new InvoicesManager(sharedPreferences);
    }

    @Test
    public void takeOverdue() {
        Set<Invoice> overdueInvoices = invoicesManager.takeOverdue();

        assertEquals(3, overdueInvoices.size());
    }

    @Test
    public void takeFuture() {
        Set<Invoice> futureInvoices = invoicesManager.takeFuture();

        assertEquals(2, futureInvoices.size());
    }

    @Test
    public void takeIncoming() {
        Set<Invoice> incomingInvoices = invoicesManager.takeIncoming();
        ArrayList<Invoice> test1 = (ArrayList)invoicesManager.takeIncoming();
        assertEquals(0, incomingInvoices.size());
       // assertEquals("Play", receiver);
        //assertThat("Play", incomingInvoices.);

    }

    @Test
    public void deleteInvoice() {
    }

    @Test
    public void addInvoice() {
    }
}