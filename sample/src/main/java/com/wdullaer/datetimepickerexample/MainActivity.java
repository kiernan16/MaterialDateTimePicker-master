package com.wdullaer.datetimepickerexample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.Utils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener
{
    private TextView timeTextView;
    private TextView dateTextView;
    private CheckBox mode24Hours;
    private CheckBox modeDarkTime;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentTime;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateTime;
    private CheckBox vibrateDate;
    private CheckBox dismissTime;
    private CheckBox dismissDate;
    private CheckBox titleTime;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox enableSeconds;

    private int year, month, day, hour, minute;
    boolean isBlockedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find our View instances
        timeTextView = (TextView)findViewById(R.id.time_textview);
        dateTextView = (TextView)findViewById(R.id.date_textview);
        Button timeButton = (Button)findViewById(R.id.time_button);
        Button dateButton = (Button)findViewById(R.id.date_button);
        mode24Hours = (CheckBox)findViewById(R.id.mode_24_hours);
        modeDarkTime = (CheckBox)findViewById(R.id.mode_dark_time);
        modeDarkDate = (CheckBox)findViewById(R.id.mode_dark_date);
        modeCustomAccentTime = (CheckBox) findViewById(R.id.mode_custom_accent_time);
        modeCustomAccentDate = (CheckBox) findViewById(R.id.mode_custom_accent_date);
        vibrateTime = (CheckBox) findViewById(R.id.vibrate_time);
        vibrateDate = (CheckBox) findViewById(R.id.vibrate_date);
        dismissTime = (CheckBox) findViewById(R.id.dismiss_time);
        dismissDate = (CheckBox) findViewById(R.id.dismiss_date);
        titleTime = (CheckBox) findViewById(R.id.title_time);
        titleDate = (CheckBox) findViewById(R.id.title_date);
        showYearFirst = (CheckBox) findViewById(R.id.show_year_first);
        enableSeconds = (CheckBox) findViewById(R.id.enable_seconds);

        // check if picker mode is specified in Style.xml
        modeDarkTime.setChecked(Utils.isDarkTheme(this, modeDarkTime.isChecked()));
        modeDarkDate.setChecked(Utils.isDarkTheme(this, modeDarkDate.isChecked()));

        // Show a timepicker when the timeButton is clicked
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        mode24Hours.isChecked()
                );
                tpd.setThemeDark(modeDarkTime.isChecked());
                tpd.vibrate(vibrateTime.isChecked());
                tpd.dismissOnPause(dismissTime.isChecked());
                tpd.enableSeconds(enableSeconds.isChecked());
                if (modeCustomAccentTime.isChecked()) {
                    tpd.setAccentColor(Color.parseColor("#9C27B0"));
                }
                if (titleTime.isChecked()) {
                    tpd.setTitle("TimePicker Title");
                }
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateTimeData();
                Calendar cDefault = Calendar.getInstance();
                cDefault.set(year, month, day);

                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        MainActivity.this,
                        cDefault.get(Calendar.YEAR),
                        cDefault.get(Calendar.MONTH),
                        cDefault.get(Calendar.DAY_OF_MONTH)
                );

                Calendar cMin = Calendar.getInstance();
                Calendar cMax = Calendar.getInstance();
                cMax.set( cDefault.get(Calendar.YEAR)+1, cDefault.get(Calendar.MONTH), 28 );
                cMin.set(cDefault.get(Calendar.YEAR), cDefault.get(Calendar.MONTH), cDefault.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setMinDate(cMin);
                datePickerDialog.setMaxDate(cMax);

                List<Calendar> daysList = new LinkedList<>();
                Calendar[] daysArray;
                Calendar cAux = Calendar.getInstance();

                List<Integer> listBlockYear = new LinkedList<>();
                List<Integer> listBlockMonth = new LinkedList<>();
                List<Integer> listBlockDayOfMonth = new LinkedList<>();

                for(int x = 0; x < 10; x++){
                    listBlockYear.add(2016);
                    listBlockMonth.add(1);
                    listBlockDayOfMonth.add(x + 12);
                }

                while(cAux.getTimeInMillis() <= cMax.getTimeInMillis()){
                  //  if( cAux.get( Calendar.DAY_OF_WEEK ) != 2 && cAux.get( Calendar.DAY_OF_WEEK ) != 4 && cAux.get(Calendar.DAY_OF_WEEK) !=6 && cAux.get(Calendar.DAY_OF_MONTH) != 20 ){
                    isBlockedDate = false;
                    for(int x = 0; x < 10; x++) {
                        if (cAux.get(Calendar.DAY_OF_MONTH) != listBlockDayOfMonth.get(x) || cAux.get(Calendar.MONTH) != listBlockMonth.get(x) || cAux.get(Calendar.YEAR) != listBlockYear.get(x)) {
                            isBlockedDate = false;
                        } else {
                            isBlockedDate = true;
                            x = 10000;
                        }
                    }

                    if(!isBlockedDate) {
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(cAux.getTimeInMillis());
                        daysList.add(c);
                    }
                    cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
                }
                daysArray = new Calendar[ daysList.size() ];
                for( int i = 0; i < daysArray.length; i++ ){
                    daysArray[i] = daysList.get(i);
                }

                datePickerDialog.setSelectableDays( daysArray );

                datePickerDialog.setThemeDark(modeDarkDate.isChecked());
                datePickerDialog.vibrate(vibrateDate.isChecked());
                datePickerDialog.dismissOnPause(dismissDate.isChecked());
                datePickerDialog.showYearPickerFirst(showYearFirst.isChecked());
                if (modeCustomAccentDate.isChecked()) {
                    datePickerDialog.setAccentColor(Color.parseColor("#9C27B0"));
                }
                if (titleDate.isChecked()) {
                    datePickerDialog.setTitle("DatePicker Title");
                }
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });

    }

    private void initDateTimeData(){
        if( year == 0 ){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd != null) tpd.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String secondString = second < 10 ? "0"+second : ""+second;
        String time = "You picked the following time: "+hourString+"h"+minuteString+"m"+secondString+"s";
        timeTextView.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }
}
