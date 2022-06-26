package com.example.proyecto_final;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final.db_reminders.AdapterReminders;
import com.example.proyecto_final.db_reminders.BBDD_Helper;
import com.example.proyecto_final.db_reminders.FuncionesDB;
import com.example.proyecto_final.db_reminders.Reminders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DatosControlActivity extends AppCompatActivity {

    private Dialog dialog;
    private List<Reminders> temp;
    private AdapterReminders adapter;
    private TextView empty;
    private RecyclerView recyclerView;

    public static Activity dca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dca = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_control);
        BBDD_Helper dbHelper = new BBDD_Helper(DatosControlActivity.this);

        FloatingActionButton fab_agregar = (FloatingActionButton) findViewById(R.id.agregar_alerta);

        fab_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReminder();
            }
        });
        empty = findViewById(R.id.empty);
        recyclerView = findViewById(R.id.recycler_view);
        setItemsInView();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                FuncionesDB db = new FuncionesDB(DatosControlActivity.this);
                List<Reminders> recyclerDataList = db.getAll();
                AdapterReminders ar = new AdapterReminders(DatosControlActivity.this,recyclerDataList);
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                Reminders deletedReminder = recyclerDataList.get(viewHolder.getAbsoluteAdapterPosition());
                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAbsoluteAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                recyclerDataList.remove(viewHolder.getAbsoluteAdapterPosition());
                db.Delete(deletedReminder.getId());
                if (recyclerDataList.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                Intent intent = new Intent(DatosControlActivity.this,NotifierAlarm.class);
                PendingIntent.getBroadcast(DatosControlActivity.this, deletedReminder.getId(), intent,PendingIntent.FLAG_UPDATE_CURRENT).cancel();
                NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
                // below line is to notify our item is removed from adapter.
                ar.notifyItemRemoved(viewHolder.getAbsoluteAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, deletedReminder.getMessage(), Snackbar.LENGTH_LONG).setAction("Deshacer", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        // below line is to notify item is
                        // added to our adapter class.
                        //ar.notifyItemInserted(position);
                        configurarReminder(deletedReminder.getMessage(),deletedReminder.getRemindDate().toString());
                        setItemsInView();
                    }
                }).show();

                recyclerView.setAdapter(ar);
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(recyclerView);
    }

    public void addReminder(){

        final String[] uglyDate = new String[1];

        dialog = new Dialog(DatosControlActivity.this);
        dialog.setContentView(R.layout.popup_crear_alerta);

        final TextView textView = dialog.findViewById(R.id.fecha);
        ImageButton select;
        Button add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.titulo);


        final Calendar newCalender = Calendar.getInstance();
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(DatosControlActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        TimePickerDialog time = new TimePickerDialog(DatosControlActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                                Calendar tem = Calendar.getInstance();
                                Log.w("TIME",System.currentTimeMillis()+"");
                                if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0) {
                                    String humanDate = "";
                                    try {
                                        humanDate = adapter.humanDate(newDate.getTime().toString());
                                        uglyDate[0] = newDate.getTime().toString();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    textView.setText(humanDate);
                                }else {
                                    Toast.makeText(DatosControlActivity.this, "Esa fecha/hora ya pasó.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE),true);
                        time.show();

                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configurarReminder(message.getText().toString(), uglyDate[0]);

                Toast.makeText(DatosControlActivity.this,"Inserted Successfully",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                setItemsInView();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void setItemsInView(){
        FuncionesDB db = new FuncionesDB(DatosControlActivity.this);
        temp = db.getAll();
        if(temp.size()>0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        adapter = new AdapterReminders(DatosControlActivity.this,temp);
        recyclerView.setAdapter(adapter);
    }

    public void configurarReminder(String message,String date){

        FuncionesDB db = new FuncionesDB(DatosControlActivity.this);
        Reminders reminders = new Reminders();
        reminders.setMessage(message);
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date remint = null;
        try {
            remint = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reminders.setRemindDate(remint);
        db.Insert(reminders);
        List<Reminders> l = db.getAll();
        reminders = l.get(l.size()-1);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(remint);
        calendar.set(Calendar.SECOND,0);
        Intent intent = new Intent(DatosControlActivity.this,NotifierAlarm.class);
        intent.putExtra("Message",reminders.getMessage());
        intent.putExtra("RemindDate",reminders.getRemindDate().toString());
        intent.putExtra("idRem",reminders.getId());

        PendingIntent intent1 = PendingIntent.getBroadcast(DatosControlActivity.this,reminders.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),intent1);
    }

}