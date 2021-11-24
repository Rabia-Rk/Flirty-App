package com.datting_package.Flirty_Datting_App.Chat;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.Models.InboxModel;
import com.datting_package.Flirty_Datting_App.R;

import java.util.ArrayList;
import java.util.Calendar;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.CustomViewHolder> implements Filterable {

    public Context context;
    ArrayList<InboxModel> inboxDataList = new ArrayList<>();
    ArrayList<InboxModel> inboxDataListFilter = new ArrayList<>();
    Integer todayDay = 0;

    AdapterClickListener adapterClicklistener;

    public InboxAdapter(Context context, ArrayList<InboxModel> userDatalist, AdapterClickListener adapterClicklistener) {
        this.context = context;
        this.inboxDataList = userDatalist;
        this.inboxDataListFilter = userDatalist;
        this.adapterClicklistener = adapterClicklistener;

        Calendar cal = Calendar.getInstance();
        todayDay = cal.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public InboxAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_layout, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        InboxAdapter.CustomViewHolder viewHolder = new InboxAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return inboxDataListFilter.size();
    }

    @Override
    public void onBindViewHolder(final InboxAdapter.CustomViewHolder holder, final int i) {
        final InboxModel item = inboxDataListFilter.get(i);

        holder.bind(i, item, adapterClicklistener);

        String check = Functions.parseDateToddMMyyyy(item.getDate());

        holder.dateCreated.setText(Functions.getTimeAgoOrg(check));

        String msg = item.getMsg();

        holder.lastMessage.setText(msg);
        if (item.getStatus() != null & item.getStatus().equalsIgnoreCase("0")) {
            holder.lastMessage.setTypeface(null, Typeface.BOLD);
            holder.lastMessage.setTextColor(ContextCompat.getColor(context,R.color.black));
        } else {
            holder.lastMessage.setTypeface(null, Typeface.NORMAL);
            holder.lastMessage.setTextColor(ContextCompat.getColor(context,R.color.gray));
        }

        String name = item.getName();

        if (name.length() > 10) {
            name = name.substring(0, 10) + " ...";
        }

        holder.userName.setText(name);


        item.getLike();

        if (item.getLike().equals("0")) {
            holder.icStar.setTag("unlike");
            holder.icStar.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_star_unfilled));
        } else {

            holder.icStar.setTag("like");
            holder.icStar.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_star_filled));


        }
        if (item.getPic() != null && !item.getPic().equals("")) {
            Uri uri = Uri.parse(item.getPic());
            holder.userImage.setImageURI(uri);
        }


        holder.icStar.setOnClickListener(v -> {

            if (holder.icStar.getTag().equals("unlike")) {

                holder.icStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_filled));
                holder.icStar.setTag("like");
                Inbox_F.likeChat("" + item.getRid(), context, "1");


            } else {
                holder.icStar.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_unfilled));
                holder.icStar.setTag("unlike");
                Inbox_F.likeChat("" + item.getRid(), context, "0");
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inboxDataListFilter = inboxDataList;
                } else {
                    ArrayList<InboxModel> filteredList = new ArrayList<>();
                    for (InboxModel row : inboxDataList) {

                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inboxDataListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inboxDataListFilter;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inboxDataListFilter = (ArrayList<InboxModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

        public void bind(final int pos, final InboxModel item, final AdapterClickListener listener) {

            itemView.setOnClickListener(v -> {

                listener.onItemClick(pos, item, v);

            });

            mainlayout.setOnLongClickListener(v -> {

                adapterClicklistener.onLongItemClick(pos, item, v);
                return false;

            });
        }

    }

}
