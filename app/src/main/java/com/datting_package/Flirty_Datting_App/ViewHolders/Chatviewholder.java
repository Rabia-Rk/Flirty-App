package com.datting_package.Flirty_Datting_App.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;

public class Chatviewholder extends RecyclerView.ViewHolder {

    public TextView message,datetxt, messageSeen, msgDate;
    public View view;
    public TextView userName;
    public LinearLayout upperlayout;

    public Chatviewholder(View itemView) {
        super(itemView);
        view = itemView;

        this.message = view.findViewById(R.id.msgtxt);
        this.datetxt=view.findViewById(R.id.datetxt);
        messageSeen =view.findViewById(R.id.message_seen);
        msgDate =view.findViewById(R.id.msg_date);

        this.upperlayout=view.findViewById(R.id.upperlayout);
    }

    public void bind(final  int pos,final ChatModel item,
                     final AdapterClickListener adapter_clickListener) {
        message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapter_clickListener.onLongItemClick(pos,item,v);
                return false;
            }
        });
    }


}
