package com.example.proyecto_final.db_reminders;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterReminders extends RecyclerView.Adapter<AdapterReminders.MyViewHolder>{
    private List<Reminders> allReminders;
    private TextView message,time;
    Context context;

    public AdapterReminders(Context context, List<Reminders> allReminders) {
        this.allReminders = allReminders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alerta_modelo,viewGroup,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        Reminders reminders = allReminders.get(i);
        if(!reminders.getMessage().equals(""))
            message.setText(reminders.getMessage());
        else
            message.setHint("No Message");

        //time.setText(reminders.getRemindDate().toString());
        try {
            time.setText(humanDate(reminders.getRemindDate().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return allReminders.size();
    }

    public String humanDate(String date) throws ParseException {
        String cutted = " ";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM d HH:mm:ss z yyyy", Locale.ENGLISH);
        Log.d("DATE_TEST", String.valueOf(dateFormat.parse(date)));
        calendar.setTime(dateFormat.parse(date));
        String fecha = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.YEAR);
        String hora = String.format("%02d:%02d",calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
        cutted = fecha + " a las " + hora;
        return cutted;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.descripcion);
            time = itemView.findViewById(R.id.fecha);
        }
    }
}
