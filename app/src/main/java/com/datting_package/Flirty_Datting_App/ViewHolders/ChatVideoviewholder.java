package com.datting_package.Flirty_Datting_App.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;

public class ChatVideoviewholder extends RecyclerView.ViewHolder {

    public ImageView chatimage;
    public TextView datetxt, messageSeen;
    public ProgressBar progressBar;
    public ImageView notSendMessageIcon;
    public View view;
    public TextView user_name;

    public ChatVideoviewholder(View itemView) {
        super(itemView);
        view = itemView;
        this.chatimage = view.findViewById(R.id.chatimage);
        this.datetxt=view.findViewById(R.id.datetxt);
        messageSeen =view.findViewById(R.id.message_seen);
        notSendMessageIcon =view.findViewById(R.id.not_send_messsage);
        progressBar =view.findViewById(R.id.p_bar);

    }

    public void bind(final  int pos, final ChatModel item, final AdapterClickListener adapter_clickListener) {

        chatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_clickListener.onItemClick(pos,item,v);
            }
        });

        chatimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapter_clickListener.onLongItemClick(pos,item,v);
                return false;
            }
        });
    }

}
