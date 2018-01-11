package com.ashishlakhmani.youthopia.adapter;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ashishlakhmani.youthopia.R;
import com.ashishlakhmani.youthopia.activity.Home;
import com.ashishlakhmani.youthopia.fragment.EventCommonFragment;
import com.ashishlakhmani.youthopia.services.NotificationReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;


public class EventsAdapter extends RecyclerView.Adapter {
    private Context context;
    private JSONArray jsonArray;
    private ArrayList<String> list;

    private Calendar myCalendar = Calendar.getInstance();

    public EventsAdapter(Context context, JSONArray jsonArray, ArrayList<String> list) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cardview_layout, parent, false);
        return new EventsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            JSONObject jasonObject = jsonArray.getJSONObject(position);
            //initialize all components..
            final String eventName = jasonObject.getString("eventname");
            final String eventSubheading = jasonObject.getString("subheading");
            final String picLink = jasonObject.getString("piclink");
            final String detailsLink = jasonObject.getString("detailslink");

            Picasso.with(context).load(picLink).
                    placeholder(R.drawable.placeholder_album).
                    into(((MyViewHolder) holder).imageView);

            ((MyViewHolder) holder).heading.setText(eventName.toUpperCase());
            ((MyViewHolder) holder).subheading.setText(eventSubheading.toUpperCase());

            ((MyViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventCommonFragment eventCommonFragment = new EventCommonFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("eventName", eventName);
                    bundle.putString("detailsLink", detailsLink);
                    eventCommonFragment.setArguments(bundle);
                    ((Home) context).loadFragment(eventCommonFragment, eventName);
                }
            });

            //Check whether the event is registered or not..
            for (String s : list) {
                if (s.toLowerCase().equals(eventName.toLowerCase())) {
                    ((MyViewHolder) holder).registeredPic.setVisibility(View.VISIBLE);
                }
            }


            SharedPreferences sp = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
            if (sp.contains(((MyViewHolder) holder).heading.getText().toString())) {
                ((MyViewHolder) holder).notification.setChecked(true);
            } else {
                ((MyViewHolder) holder).notification.setChecked(false);
            }

            ((MyViewHolder) holder).notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        dateTimeTask(((MyViewHolder) holder).heading.getText().toString(), ((MyViewHolder) holder).notification, picLink);
                    } else {
                        Toast.makeText(context, "Alarm Cancelled.", Toast.LENGTH_SHORT).show();
                        editor.remove(((MyViewHolder) holder).heading.getText().toString());
                        editor.apply();
                    }
                }
            });


        } catch (JSONException e) {
            Toast.makeText(context, "Sorry..Something went wrong..!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    private class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        ImageView imageView;
        ImageView registeredPic;
        ToggleButton notification;
        TextView heading;
        TextView subheading;
        CardView cardView;

        private MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            imageView = itemView.findViewById(R.id.card_pic);
            registeredPic = itemView.findViewById(R.id.registered);
            notification = itemView.findViewById(R.id.toggle_notification);
            heading = itemView.findViewById(R.id.card_heading);
            subheading = itemView.findViewById(R.id.card_subheading);
            cardView = itemView.findViewById(R.id.event_cardView);
        }
    }


    private void dateTimeTask(final String heading, final ToggleButton toggleButton, final String picLink) {

        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(year, month, dayOfMonth, selectedHour, selectedMinute);
                        String myFormat = "dd MMMM yyyy : HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                        Toast.makeText(context, "You will be Notified on : " + sdf.format(myCalendar.getTime()), Toast.LENGTH_LONG).show();
                        createAlarm(heading, picLink);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.setCancelable(false);

                mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        toggleButton.setChecked(false);
                    }
                });
                mTimePicker.show();
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCancelable(false);
        dialog.setTitle("Set Date");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                toggleButton.setChecked(false);
            }
        });
        dialog.show();

    }

    private void createAlarm(String heading, String picLink) {

        Random random = new Random();
        int num = random.nextInt(999999999);

        long alarm = myCalendar.getTimeInMillis();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("heading", heading.trim());
        intent.putExtra("picture_link", picLink);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, num, intent, 0);
        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager1 != null) {
            alarmManager1.set(AlarmManager.RTC_WAKEUP, alarm, pendingIntent1);
        }

        SharedPreferences sp = context.getSharedPreferences("notification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(heading.trim(), true);
        editor.apply();
    }


}
