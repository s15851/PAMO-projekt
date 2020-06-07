package com.example.billsreminderapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AddActivity extends AppCompatActivity {

    //variables
    EditText charge, receiver, comments, day;
    RadioButton repeatPayment, noRepeatPayment;
    RadioGroup repeatRadio;
    Spinner repeats, howManyTimes;
    Button accept;
    Bundle bundle = new Bundle();
    boolean chooseDate = false;

    private static final String TAG = "AddActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Invoice editedInvoice;

    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        //get editedInvoice
        editedInvoice = Cache.getInstance().getEditedInvoice();
        //set editedInvoice
        Cache.getInstance().setEditedInvoice(null);
        if(editedInvoice != null) {
            Cache.getInstance().setMarkedInvoice(null);
        }
        //component support in layout
        Log.d("aktywność", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        charge = (EditText) findViewById(R.id.charge);
        receiver = (EditText) findViewById(R.id.receiver);
        comments = (EditText) findViewById(R.id.comments);
        day = (EditText) findViewById(R.id.day);
        repeatPayment = (RadioButton) findViewById(R.id.repeatPayment);
        noRepeatPayment = (RadioButton) findViewById(R.id.noRepeatPayment);
        repeatRadio = (RadioGroup) findViewById(R.id.radio_group);
        repeats = (Spinner) findViewById(R.id.repeats);
        howManyTimes = (Spinner) findViewById(R.id.howManyTimes);
        accept = (Button) findViewById(R.id.accept);

        //set date (payment time)
        mDisplayDate = (TextView) findViewById(R.id.tvdate);
        //default display current date
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", new Locale("pl", "PL"));
        mDisplayDate.setText(df.format(new Date()));
        //set payment time
        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){

                chooseDate = true;
                //set date format
                DateTimeFormatter format = DateTimeFormat.forPattern("dd-MM-yyyy");
                // dateTime equal parsed mDisplayDate to string
                DateTime dateTime = format.parseDateTime(mDisplayDate.getText().toString());
                int year = dateTime.getYear();
                int month = dateTime.getMonthOfYear() - 1;
                int day = dateTime.getDayOfMonth();
                //choosing what the date panel will look like
                DatePickerDialog dialog = new DatePickerDialog(
                        AddActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                //Checks that the specified object reference is not null and set background color
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //??????????????????????????????
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd//yy:" + month + "-" + dayOfMonth+ "-" + year);
                String date = dayOfMonth + "-" + month + "-" + year;

                DateTimeFormatter format = DateTimeFormat.forPattern("dd-MM-yyyy");
                DateTime time = format.parseDateTime(date);
                mDisplayDate.setText(time.toString("dd-MM-yyyy"));
                buttonIsAvailable();
            }
        };
        //if is nothing entered
        if(editedInvoice == null) {
            charge.setText(bundle.getString("charge"));
            receiver.setText(bundle.getString("receiver"));
            comments.setText(bundle.getString("comments"));
            day.setText(bundle.getString("dzien"));

            //else go to layout with entered data
        } else {
            setTitle("Edytuj Przypomnienie");
            chooseDate = true;
            charge.setText(editedInvoice.getCharge().toString());
            receiver.setText(editedInvoice.getReceiver());
            comments.setText(editedInvoice.getComments());
            day.setText(editedInvoice.getHowManyTimesPayment().toString());

            //spinner to choose cyclical payments
            switch (editedInvoice.getRepetitionPeriod()) {
                case "DZIEŃ":
                    repeats.setSelection(0);
                    break;
                case "TYDZIEŃ":
                    repeats.setSelection(1);
                    break;
                case "MIESIĄC":
                    repeats.setSelection(2);
                    break;
                case "ROK":
                    repeats.setSelection(3);
                    break;
                default:
                    repeats.setSelection(0);
            }

            howManyTimes.setSelection(editedInvoice.getWhenInform());
            //if user choose the payment is repeated perform button repeatPayment
            if(editedInvoice.isRepeated()) {
                repeatPayment.performClick();
                //else another button - noRepeatPayment
            } else {
                noRepeatPayment.performClick();
            }
            //?????????????????????????????????????
            mDisplayDate.setText(editedInvoice.getTimeToDisplay());
            buttonIsAvailable();
        }

        setValidate();
    }

    private void setValidate() {
        charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonIsAvailable();
            }
        });

        receiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonIsAvailable();
            }
        });
    }
    //if areDateCorrect()=true, button "Accept" is available
    private void buttonIsAvailable() {
        if(areDateCorrect()) {
            accept.setEnabled(true);
        } else {
            accept.setEnabled(false);
        }
    }

    //function to check are data correct.
    private boolean areDateCorrect() {
        String receiver = this.receiver.getText().toString();
        double charge;
        if(this.charge.getText().toString().equals("")) {
            charge = -1D;
        } else {
            charge = Double.parseDouble(this.charge.getText().toString());
        }

        return !receiver.equals("") && charge != -1 && chooseDate;
    }

    // operation life cycle methods
    @Override
    protected void onStart() {
        Log.d("aktywność", "onStart");
        super.onStart();
    }
    protected void onResume() {
        Log.d("aktywność", "onResume");
        super.onResume();
    }
    @Override
    protected void onPause() {
        Log.d("aktywność", "onPause");
        bundle.putString( "receiver", receiver.getText().toString() );

        super.onPause();
    }
    @Override
    protected void onRestart() {
        Log.d("aktywność", "onRestart");
        super.onRestart();
    }
    @Override
    protected void onStop() {
        Log.d("aktywność", "onStop");
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d("aktywność", "onDestroy");
        super.onDestroy();
    }

    public void accept(View view){

        //new object invoicesManager
        InvoicesManager invoicesManager = new InvoicesManager(getSharedPreferences("zapisaneFaktury", Context.MODE_PRIVATE));

        //local variables
        String receiver = this.receiver.getText().toString();
        Double charge = Double.parseDouble(this.charge.getText().toString());
        String comments = this.comments.getText().toString();
        String tvdate = this.mDisplayDate.getText().toString();
        String day = this.day.getText().toString();
        String repeats = this.repeats.getSelectedItem().toString();
        String howManyTimes = this.howManyTimes.getSelectedItem().toString().replaceAll("\\D+", "");

        //object invoice with parameters to display
        Invoice invoice = new Invoice(receiver, charge, tvdate, comments);

        //Check payment periodicity
        if(!day.equals("")) {
            invoice.setHowManyTimesPayment(Integer.valueOf(day));
        } else {
            invoice.setHowManyTimesPayment(0);
        }

        boolean repeatCheck = false;

        //check the radio is clicked
        if(repeatRadio.getCheckedRadioButtonId() == R.id.repeatPayment) {
            repeatCheck = true;
        }

        //set sellection (recurrence and periodicity)
        invoice.setIsRepeated(repeatCheck);
        invoice.setRepetitionPeriod(repeats);

        //Check is notification set or not
        if(howManyTimes.equals("")) {
            invoice.setWhenInform(0);
        } else {
            invoice.setWhenInform(Integer.valueOf(howManyTimes));
        }
        //??????????????????????????????????
        if(editedInvoice != null) {
            invoicesManager.deleteInvoice(editedInvoice);
        }
        //when invoice is filled and clicked "Akceptuj"
        invoicesManager.addInvoice(invoice);
        //show message
        Toast.makeText(getApplicationContext(), "faktura została zapisana", Toast.LENGTH_LONG).show();
        //set empty field
        Cache.getInstance().setEditedInvoice(null);
        //go to main layout
        cancel(view);
    }
    //function leading to the main activity
    public void cancel(View view){
        startActivity(new Intent(AddActivity.this,MainActivity.class));
    }
    //function setting the periodically false
    public void disableHowManyTimesPayment(View view) {
        repeats.setEnabled(false);
        day.setEnabled(false);
    }

    //function setting the periodically true
    public void ableHowManyTimesPayment(View view) {
        repeats.setEnabled(true);
        day.setEnabled(true);
    }
}
