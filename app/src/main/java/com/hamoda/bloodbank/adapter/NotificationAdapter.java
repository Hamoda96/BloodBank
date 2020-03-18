package com.hamoda.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.getNotification.getNotificationData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    private AppCompatActivity activity;
    private Context context;
    private List<getNotificationData> notificationData = new ArrayList<>();
    private ClientData clientData;
    private NavController navController;

    public NotificationAdapter(AppCompatActivity activity, List<getNotificationData> notificationData, NavController navController) {
        this.activity = activity;
        this.notificationData = notificationData;
        this.navController = navController;
        this.context = activity;
        clientData = loadUserData(activity);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        setData(holder , position);
        setAction(holder,position);
    }

    private void setAction(NotificationViewHolder holder, int position) {

    }

    private void setData(NotificationViewHolder holder, int position) {
        if (notificationData.get(position).getPivot().getIsRead().equals("0")) {
            holder.itemNotificationIvIcon.setImageResource(R.drawable.ic_notifications);
        }else {
            holder.itemNotificationIvIcon.setImageResource(R.drawable.ic_notifications_none);
        }
        holder.itemNotificationTvTitle.setText(notificationData.get(position).getTitle());
        holder.itemNotificationTvTime.setText(notificationData.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return notificationData.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_notification_iv_icon)
        ImageView itemNotificationIvIcon;
        @BindView(R.id.item_notification_tv_title)
        TextView itemNotificationTvTitle;
        @BindView(R.id.item_notification_tv_time)
        TextView itemNotificationTvTime;

        private View view;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,view);

        }
    }
}
