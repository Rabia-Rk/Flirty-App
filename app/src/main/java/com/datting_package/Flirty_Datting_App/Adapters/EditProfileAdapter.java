package com.datting_package.Flirty_Datting_App.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;

import java.util.List;

public class EditProfileAdapter extends RecyclerView.Adapter<EditProfileAdapter.ViewHolder> {

    public final int imageType = 1;
    public final int browseType = 2;
    Context context;
    Activity activity;
    List<String> listUserImage;
    AdapterClickListener adapterClickListener;

    public EditProfileAdapter(Context context, Activity activity, List<String> listUserImage, AdapterClickListener adapterClickListener) {
        this.context = context;
        this.activity = activity;
        this.adapterClickListener = adapterClickListener;
        this.listUserImage = listUserImage;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        View v;
        if (viewtype == imageType) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_edit_prof, null);
            return new ViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_browse_profile_image, null);
            return new ViewHolder(v);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final String images = listUserImage.get(i);
        viewHolder.bind(i, images, adapterClickListener);

        if (viewHolder.cancel != null) {

            viewHolder.iv.setClipToOutline(true);
            if (i == 0) {
                viewHolder.cancel.setVisibility(View.GONE);
            } else {
                viewHolder.cancel.setVisibility(View.VISIBLE);
            }

            try {
                Uri uri = Uri.parse(images);
                viewHolder.iv.setImageURI(uri);
            } catch (Exception v) {
                v.printStackTrace();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listUserImage.get(position).equals("0") || listUserImage.get(position).equals("")) {
            return browseType;
        } else {
            return imageType;
        }

    }

    @Override
    public int getItemCount() {
        return listUserImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv;
        ImageView cancel;
        RelativeLayout rlAddImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.edit_prof_IV_id);
            cancel = itemView.findViewById(R.id.cancel);


            rlAddImg = itemView.findViewById(R.id.RL_add_img);

        }

        public void bind(final int pos, final String item, final AdapterClickListener onClickListner) {

            itemView.setOnClickListener(view -> onClickListner.onItemClick(pos, item, view));


            if (rlAddImg != null)
                rlAddImg.setOnClickListener(v -> onClickListner.onItemClick(pos, item, v));

            if (cancel != null)
                cancel.setOnClickListener(v -> onClickListner.onItemClick(pos, item, v));
        }

    }


}
