package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

import com.datting_package.Flirty_Datting_App.Models.NearbyUsersModel;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.ViewHolders.UnifiedNativeAdViewHolder;

public class WorldWideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AdapterClickListener adapterClickListener;
    List<Object> nearbyList;
    Context context;

    public static final int MENU_ITEM_VIEW_TYPE = 0;
    StaggeredGridLayoutManager.LayoutParams layoutParams;

    public static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;


    public WorldWideAdapter(AdapterClickListener adapterClickListener, List<Object> nearbyList, Context context) {
        this.adapterClickListener =adapterClickListener;
        this.nearbyList = nearbyList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_unified_ad,
                        parent, false);

                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            default:
                View menuItemLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_world_wide, parent, false);
                return new MyViewHolder(menuItemLayoutView);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {


        int viewType = getItemViewType(i);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) nearbyList.get(i);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) viewHolder).getAdView());
                layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                break;
            default:
                NearbyUsersModel getNearby = (NearbyUsersModel) nearbyList.get(i);
                MyViewHolder holder = (MyViewHolder) viewHolder;
                holder.tvId.setText("" + getNearby.getFirst_name());
                holder.iv.setClipToOutline(true);

                layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(false);

                try {


                    if(getNearby.getImage1()!=null && !getNearby.getImage1().equals("")) {
                        Uri uri = Uri.parse(getNearby.getImage1());
                        holder.iv.setImageURI(uri);
                    }

                } catch (Exception v) {
                    v.printStackTrace();
                }

                holder.setIsRecyclable(false);


                holder.onbind(i, adapterClickListener);

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = nearbyList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {

            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return nearbyList.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv;
        TextView tvId;
        RelativeLayout adsRl;
        public MyViewHolder(View view) {
            super(view);
            iv = itemView.findViewById(R.id.CIV_id);
            tvId = itemView.findViewById(R.id.TV_id);
            adsRl = itemView.findViewById(R.id.Ads_RL);
        }

        protected void onbind(final int pos, final AdapterClickListener adapterClicklistener ){
            itemView.setOnClickListener(v -> adapterClicklistener.onItemClick(pos,null,v));
        }

    }




    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());


        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
