package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.List;

public class CardAdapter extends ArrayAdapter<NearbyUsersModel> {


    AdapterClickListener adapterClicklistener;
    List<NearbyUsersModel> nearbyList;
    Context context;

    public CardAdapter(Context context, int resources, AdapterClickListener adapterClicklistener, List<NearbyUsersModel> nearbyList) {
        super(context, resources);
        this.adapterClicklistener = adapterClicklistener;
        this.nearbyList = nearbyList;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {


        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_card_layout, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        final NearbyUsersModel item = getItem(position);

        holder.image.setImageResource(android.R.color.transparent);
        try {

            Uri uri = Uri.parse(item.getImage1());
            holder.image.setImageURI(uri);
        } catch (Exception v) {
            Functions.logDMsg("Error In Fresco " + getItem(position) + " Err " + v.toString());
        }

        holder.onbind(position, item, adapterClicklistener);
        return contentView;
    }

    private static class ViewHolder {

        public SimpleDraweeView image;
        ImageView like, dislike;

        public ViewHolder(View view) {
            image = view.findViewById(R.id.card_item_userimage_id);
            like = view.findViewById(R.id.right_overlay);
            dislike = view.findViewById(R.id.left_overlay);

        }

        public void onbind(final int pos, final NearbyUsersModel model, final AdapterClickListener clickListener) {

            like.setOnClickListener(view -> clickListener.onItemClick(pos, model, view));

            dislike.setOnClickListener(view -> clickListener.onItemClick(pos, model, view));

        }
    }


}
