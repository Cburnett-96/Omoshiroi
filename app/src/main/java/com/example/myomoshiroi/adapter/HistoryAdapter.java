package com.example.myomoshiroi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myomoshiroi.R;
import com.example.myomoshiroi.model.UserHistory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context mCtx;
    private final List<UserHistory> userHistoryList;
    private final int limit = 20;

    public HistoryAdapter(Context mCtx, List<UserHistory> userHistoryList) {
        this.mCtx = mCtx;
        this.userHistoryList = userHistoryList;
    }
    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_history, parent, false);
        return new HistoryAdapter.HistoryViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        UserHistory user = userHistoryList.get(position);
        holder.textViewSubject.setText(user.Title);
        holder.textViewDescription.setText(user.Descriptions);
        DateFormat origDate = new SimpleDateFormat("yyyy MM dd HH:mm", Locale.ENGLISH);
        DateFormat customDate = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
        String newDateFormat = null;
        try {
            Date date = origDate.parse(user.CreatedTime);
            assert date != null;
            newDateFormat = customDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewDate.setText(newDateFormat);
    }
    @Override
    public int getItemCount() {
        if(userHistoryList.size() > limit){
            return limit;
        }
        else
        {
            return userHistoryList.size();
        }
    }
    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSubject, textViewDescription, textViewDate;
        LinearLayout showSubject, showDescription;
        Button showMore;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewDate = itemView.findViewById(R.id.textViewDate);

            showMore = itemView.findViewById(R.id.btn_showMore);
            showSubject = itemView.findViewById(R.id.layout_Subject);
            showDescription = itemView.findViewById(R.id.layout_description);

            showMore.setOnClickListener(v -> {
                if (showMore.getText().toString().equalsIgnoreCase("Show More..."))
                {
                    showSubject.setVisibility(View.VISIBLE);
                    showDescription.setVisibility(View.VISIBLE);
                    showMore.setText("Show Less...");
                }
                else
                {
                    showSubject.setVisibility(View.GONE);
                    showDescription.setVisibility(View.GONE);
                    showMore.setText("Show More...");
                }
            });

        }
    }
}
