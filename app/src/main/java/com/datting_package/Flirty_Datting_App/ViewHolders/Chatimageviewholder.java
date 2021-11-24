package com.datting_package.Flirty_Datting_App.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.R;

public class Chatimageviewholder extends RecyclerView.ViewHolder {

    public ImageView chatimage;
    public TextView datetxt, messageSeen;
    public ProgressBar progressBar;
    public ImageView notSendMessageIcon;
    public View view;

    public TextView userName;
    public LinearLayout upperlayout;

    public Chatimageviewholder(View itemView) {
        super(itemView);
        view = itemView;

        this.chatimage = view.findViewById(R.id.chatimage);
        this.datetxt = view.findViewById(R.id.datetxt);
        messageSeen = view.findViewById(R.id.message_seen);
        notSendMessageIcon = view.findViewById(R.id.not_send_messsage);
        progressBar = view.findViewById(R.id.p_bar);


        // user_name=view.findViewById(R.id.user_name);
        this.upperlayout = view.findViewById(R.id.upperlayout);


    }

    public void bind(final int pos, final ChatModel item, final AdapterClickListener adapterClickListener) {

        chatimage.setOnClickListener(v -> adapterClickListener.onItemClick(pos, item, v));

        chatimage.setOnLongClickListener(v -> {
            adapterClickListener.onLongItemClick(pos, item, v);
            return false;
        });
    }
}
