package com.example.billsreminderapp;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setNotification();
        this.showInvoices();

        //go to add invoice button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
            }

        });
    }

    //set notification on phone
    private void setNotification() {
        NotificationSetter notificationSetter = new NotificationSetter(getApplicationContext(),
                (AlarmManager) getSystemService(ALARM_SERVICE));
        notificationSetter.createRepeatNotification();
    }

    public void switchMode(View view) {
        this.showInvoices();
    }

    // function responsible for show invoices with incoming pay time or overdue time
    private void showInvoices() {
        InvoicesManager invoicesManager = new InvoicesManager(getSharedPreferences("zapisaneFaktury", Context.MODE_PRIVATE));
        Set<Invoice> invoices;
        ToggleButton toggleToShowInvoices = (ToggleButton) findViewById(R.id.whatToDisplay);

        // if pressed with text "BLIŻAJĄCE SIĘ FAKTURY"...
        if(toggleToShowInvoices.getText().toString().equals("ZBLIŻAJĄCE SIĘ FAKTURY")){
            invoices = invoicesManager.takeFuture();
        } else {
            invoices = invoicesManager.takeOverdue();
        }
        ArrayList<Invoice> whatToDisplay = new ArrayList<>(invoices);

        ListView listView = (ListView)findViewById(R.id.list);

        //sort invoices
        Collections.sort(whatToDisplay);
        CustomAdapter adapter = new CustomAdapter(whatToDisplay, getApplicationContext(), this);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //variables to choose what we want to do with invoice
    final int CONTEXT_MENU_DELETE = 1;
    final int CONTEXT_MENU_EDIT = 2;
    final int CONTEXT_MENU_MARK_AS_PAID = 3;

    //after click on invoice we show option delete,edit or mark as paid
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, CONTEXT_MENU_DELETE, 0, "Usuń");
        menu.add(0, CONTEXT_MENU_EDIT, 1, "Edytuj");
        menu.add(0, CONTEXT_MENU_MARK_AS_PAID, 2, "Oznacz jako zapłacona");
    }

    //three options implements
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Invoice invoice = Cache.getInstance().getMarkedInvoice();
        InvoicesManager invoicesManager = new InvoicesManager(getSharedPreferences("zapisaneFaktury", Context.MODE_PRIVATE));
        switch (item.getItemId()) {
            //delete
            case 1:
                invoicesManager.deleteInvoice(invoice);
                showInvoices();
                Cache.getInstance().setMarkedInvoice(null);
                break;
            //edit
            case 2:
                Cache.getInstance().setEditedInvoice(invoice);
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
            //mark as paid
            case 3:
                if(invoice.isRepeated()) {
                    invoicesManager.deleteInvoice(invoice);
                    invoice.extendTime();
                    invoicesManager.addInvoice(invoice);
                    Toast.makeText(this, "Faktura została oznaczona jako opłacona", Toast.LENGTH_LONG).show();

                //if is only one more paid
                } else {
                    Toast.makeText(this, "Faktura do opłacenia jednorazowo", Toast.LENGTH_LONG).show();
                }
                showInvoices();
            default:

        }
        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        Cache.getInstance().setMarkedInvoice(null);
        super.onContextMenuClosed(menu);
    }
}
