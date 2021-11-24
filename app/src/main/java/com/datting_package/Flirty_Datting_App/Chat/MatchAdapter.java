package com.datting_package.Flirty_Datting_App.Chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.Activities.Chat_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.Models.MatchModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    Context context;
    List<MatchModel> listMyMatch;


    public MatchAdapter(Context context, List<MatchModel> listMyMatch) {
        this.context = context;
        this.listMyMatch = listMyMatch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match_layout, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final MatchModel myMatch = listMyMatch.get(i);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.iv.setClipToOutline(true);
        }

        try {
            Uri uri = Uri.parse(myMatch.getPicture());
            viewHolder.iv.setImageURI(uri);


        } catch (Exception v) {
            Functions.toastMsg(context, "Error Adp image " + v.toString());
        }


        viewHolder.iv.setOnClickListener(v -> {


            Intent myIntent = new Intent(context, Chat_A.class);
            myIntent.putExtra("receiver_id", myMatch.getU_id());
            myIntent.putExtra("receiver_name", myMatch.getUsername());
            myIntent.putExtra("receiver_pic", myMatch.getPicture());
            myIntent.putExtra("match_api_run", "1");
            myIntent.putExtra("position_to_remove", "" + i);
            myIntent.putExtra("is_block", "0");


            context.startActivity(myIntent);

        });

    }

    @Override
    public int getItemCount() {
        return listMyMatch.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView addPhoto;
        SimpleDraweeView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.chat_item_IV_id);
            addPhoto = itemView.findViewById(R.id.add_photo_img_id);

        }
    }
}
