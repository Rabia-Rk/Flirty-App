package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.Models.BlockUserModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;

public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.CustomViewHolder> {

    public Context context;
    ArrayList<BlockUserModel> inboxDataList;
    AdapterClickListener adapterClicklistener;

    public BlockedUserAdapter(Context context, ArrayList<BlockUserModel> userDataList, AdapterClickListener adapterClicklistener) {
        this.context = context;
        this.inboxDataList = userDataList;
        this.adapterClicklistener = adapterClicklistener;
    }


    @Override
    public BlockedUserAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_block_user, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        BlockedUserAdapter.CustomViewHolder viewHolder = new BlockedUserAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return inboxDataList.size();
    }

    @Override
    public void onBindViewHolder(final BlockedUserAdapter.CustomViewHolder holder, final int i) {
        final BlockUserModel item = inboxDataList.get(i);

        holder.bind(i, item, adapterClicklistener);

        String name = item.getName();

        holder.userName.setText(name);

        if (item.getImgUrl() != null && !item.getImgUrl().equals("")) {
            Uri uri = Uri.parse(item.getImgUrl());
            holder.userImage.setImageURI(uri);
        }
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView userName, lastMessage, dateCreated;
        SimpleDraweeView userImage;
        RelativeLayout mainlayout;
        ImageView icStar;

        public CustomViewHolder(View view) {
            super(view);
            this.mainlayout = view.findViewById(R.id.mainlayout);
            this.userName = view.findViewById(R.id.username);
            this.lastMessage = view.findViewById(R.id.message);
            this.dateCreated = view.findViewById(R.id.datetxt);
            this.userImage = view.findViewById(R.id.userimage);
            this.icStar = view.findViewById(R.id.ic_star);


        }

        public void bind(final int pos, final BlockUserModel item, final AdapterClickListener listener) {

            itemView.setOnClickListener(v -> listener.onItemClick(pos, item, v));

            mainlayout.setOnLongClickListener(v -> {
                listener.onLongItemClick(pos, item, v);
                return false;
            });
        }

    }
}
