package com.example.billsreminderapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

//Class responsible for display invoice data in main layout
public class CustomAdapter extends ArrayAdapter<Invoice> implements View.OnClickListener{

    private ArrayList<Invoice> invoices;
    private Context mContext;
    private Activity mainActivity;

    // View lookup cache
    private static class ViewHolder {
        TextView receiver;
        TextView charge;
        TextView datePayment;
        TextView comments;
    }

    //class constructor
    CustomAdapter(ArrayList<Invoice> date, Context context, Activity mainActivity) {
        super(context, R.layout.row, date);
        this.invoices = date;
        this.mContext=context;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
    }

    int lastPosition = -1;

    @SuppressLint("SetTextI18n")
    @Override

    //function responsible for invoice view
    public View getView(int position, View convertView, ViewGroup parent) {
        final Invoice dateModel = getItem(position);

        //creative template. Its task is to optimize the rendering process of the element collection view
        ViewHolder viewHolder;

        final View result;

        // create the view that will appear in list
        if (convertView == null) {
            viewHolder = new ViewHolder();

            //Instantiates a layout XML file into its corresponding View objects.
            LayoutInflater inflater = LayoutInflater.from(getContext());

            //We must create a View:
            convertView = inflater.inflate(R.layout.row, parent, false);
            viewHolder.receiver = (TextView) convertView.findViewById(R.id.receiver_value);
            viewHolder.charge = (TextView) convertView.findViewById(R.id.charge_value);
            viewHolder.datePayment = (TextView) convertView.findViewById(R.id.date_value);
            viewHolder.comments = (TextView) convertView.findViewById(R.id.comments_value);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        assert dateModel != null;

        // display in R.layout.row
        viewHolder.receiver.setText(dateModel.getReceiver());
        viewHolder.charge.setText(dateModel.getCharge().toString() + " PLN");
        viewHolder.datePayment.setText(dateModel.getTermString());
        viewHolder.comments.setText(dateModel.getComments());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cache.getInstance().setMarkedInvoice(dateModel);
                //The view that should show a context menu
                mainActivity.registerForContextMenu(v);
                //The view to show the context menu for
                mainActivity.openContextMenu(v);
                //The view that should stop showing a context menu
                mainActivity.unregisterForContextMenu(v);
            }
        });

        return convertView;
    }


}