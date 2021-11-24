package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import com.datting_package.Flirty_Datting_App.Models.ImagesModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;

public class ImagesAdp extends RecyclerView.Adapter<ImagesAdp.ViewHolder> {

    Context context;
    List<ImagesModel> wallPaper;
    AdapterClickListener adapterClicklistener;


    public ImagesAdp(Context context, List<ImagesModel> listWall, AdapterClickListener adapterClicklistener) {
        this.context = context;
        this.wallPaper = listWall;
        this.adapterClicklistener = adapterClicklistener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_img_in_profile, null);
        return new ViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ImagesModel check = wallPaper.get(i);

        viewHolder.iv.setClipToOutline(true);

        if(!check.getImgUrl().equals("") &&  !check.getImgUrl().equals("0")){
            try{
                Uri uri = Uri.parse(check.getImgUrl());
                viewHolder.iv.setImageURI(uri);  // Attachment
            }catch (Exception v){
                v.printStackTrace();
            }
        }
        viewHolder.onbind(i,check, adapterClicklistener);
    }


    @Override
    public int getItemCount() {
        return wallPaper.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.IV_id);

        }

        public void onbind(final int pos, final ImagesModel imagesmodel, final AdapterClickListener listner) {
            itemView.setOnClickListener(v-> {
                    listner.onItemClick(pos, imagesmodel,v);

            });

        }

    }

}
