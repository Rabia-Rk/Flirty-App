package com.datting_package.Flirty_Datting_App.Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.datting_package.Flirty_Datting_App.Activities.Chat_A;
import com.datting_package.Flirty_Datting_App.CodeClasses.AdapterClickListener;
import com.datting_package.Flirty_Datting_App.CodeClasses.Functions;
import com.datting_package.Flirty_Datting_App.CodeClasses.Variables;
import com.datting_package.Flirty_Datting_App.Models.ChatModel;
import com.datting_package.Flirty_Datting_App.R;
import com.datting_package.Flirty_Datting_App.ViewHolders.Alertviewholder;
import com.datting_package.Flirty_Datting_App.ViewHolders.ChatVideoviewholder;
import com.datting_package.Flirty_Datting_App.ViewHolders.Chatimageviewholder;
import com.datting_package.Flirty_Datting_App.ViewHolders.Chatviewholder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MYCHAT = 1;
    private static final int FRIENDCHAT = 2;
    private static final int MYCHATIMAGE = 3;
    private static final int OTHERCHATIMAGE = 4;
    private static final int MYGIFIMAGE = 5;
    private static final int OTHERGIFIMAGE = 6;
    private static final int ALERT_MESSAGE = 7;
    private static final int MY_VIDEO_MESSAGE = 10;
    private static final int OTHER_VIDEO_MESSAGE = 11;
    String myID;
    Context context;
    Integer today_day = 0;
    AdapterClickListener adapterClickListener;
    private List<ChatModel> mDataSet;

    public MsgAdapter(List<ChatModel> dataSet, String id, Context context, AdapterClickListener adapterClickListener) {
        mDataSet = dataSet;
        this.myID = id;
        this.context = context;
        this.adapterClickListener = adapterClickListener;
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View v = null;

        switch (viewtype) {

            case MYCHAT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_my, viewGroup, false);
                Chatviewholder mychatHolder = new Chatviewholder(v);
                return mychatHolder;
            case FRIENDCHAT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_other, viewGroup, false);
                Chatviewholder friendchatHolder = new Chatviewholder(v);
                return friendchatHolder;
            case MYCHATIMAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_my, viewGroup, false);
                Chatimageviewholder mychatimageHolder = new Chatimageviewholder(v);
                return mychatimageHolder;
            case OTHERCHATIMAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_image_other, viewGroup, false);
                Chatimageviewholder otherchatimageHolder = new Chatimageviewholder(v);
                return otherchatimageHolder;


            case MYGIFIMAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_my, viewGroup, false);
                Chatimageviewholder mychatgigHolder = new Chatimageviewholder(v);
                return mychatgigHolder;

            case OTHERGIFIMAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_gif_other, viewGroup, false);
                Chatimageviewholder otherchatgifHolder = new Chatimageviewholder(v);
                return otherchatgifHolder;

            case MY_VIDEO_MESSAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_video_my, viewGroup, false);
                ChatVideoviewholder mychatVideoviewholder = new ChatVideoviewholder(v);
                return mychatVideoviewholder;

            case OTHER_VIDEO_MESSAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_video_other, viewGroup, false);
                ChatVideoviewholder otherchatVideoviewholder = new ChatVideoviewholder(v);
                return otherchatVideoviewholder;


            case ALERT_MESSAGE:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_alert, viewGroup, false);
                Alertviewholder alertviewholder = new Alertviewholder(v);
                return alertviewholder;

            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatModel chat = mDataSet.get(position);
        holder.setIsRecyclable(false);



        if (chat.getType().equals("text")) {
            Chatviewholder chatviewholder = (Chatviewholder) holder;
            // check if the message is from sender or receiver
            // changeDrawableColor
            if (chat.getSender_id().equals(myID)) {
                Functions.getRoundRect();
                if (chat.getStatus().equals("1"))
                    chatviewholder.messageSeen.setText(" Seen at " + changeDateToTime(chat.getTime()));

                else
                    chatviewholder.messageSeen.setText("Sent");
            } else {

                chatviewholder.messageSeen.setText("");
            }

            chatviewholder.message.setText(chat.getText());
            chatviewholder.msgDate.setText(showMessageTime(chat.getTimestamp()));


            if (position != 0) {
                ChatModel chat2 = mDataSet.get(position - 1);
                if (!chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {
                    chatviewholder.datetxt.setVisibility(View.VISIBLE);
                    chatviewholder.datetxt.setText(changeDate(chat.getTimestamp()));
                }

            } else {
                chatviewholder.datetxt.setVisibility(View.VISIBLE);
                chatviewholder.datetxt.setText(changeDate(chat.getTimestamp()));
            }

            chatviewholder.bind(position, chat, adapterClickListener);

        } else if (chat.getType().equals("image")) {

            final Chatimageviewholder chatimageholder = (Chatimageviewholder) holder;

            chatimageholder.messageSeen.setTextColor(ContextCompat.getColor(context, R.color.gray));
            chatimageholder.datetxt.setVisibility(View.VISIBLE);

            if (chat.getSender_id().equals(myID)) {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText("" + showMessageTime(chat.getTimestamp()));

                if (chat.getStatus().equals("1")) {
                    chatimageholder.messageSeen.setText("Seen at " + changeDateToTime(chat.getTime()));
                } else {
                    chatimageholder.messageSeen.setText("Sent");
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);

                }


            } else {

                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText("" + showMessageTime(chat.getTimestamp()));

                chatimageholder.messageSeen.setText("" + changeDateToTime(chat.getTime()));
            }

            if (chat.getPic_url().equals("none")) {
                if (Chat_A.uploading_image_id.equals(chat.getChat_id())) {
                    chatimageholder.progressBar.setVisibility(View.VISIBLE);
                    chatimageholder.messageSeen.setText("");
                } else {
                    chatimageholder.progressBar.setVisibility(View.GONE);
                    chatimageholder.notSendMessageIcon.setVisibility(View.VISIBLE);
                    chatimageholder.messageSeen.setText("Not delivered. This is bug");
                }
            } else {
                chatimageholder.notSendMessageIcon.setVisibility(View.GONE);
                chatimageholder.progressBar.setVisibility(View.GONE);
            }

            if (position != 0) {
                ChatModel chat2 = mDataSet.get(position - 1);
                if (chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                } else {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(changeDate(chat.getTimestamp()));
                }
                try {
                    Picasso.get().load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400, 400).centerCrop().into(chatimageholder.chatimage);
                } catch (Exception n) {
                    n.printStackTrace();
                }

            } else {

                chatimageholder.datetxt.setText(changeDate(chat.getTimestamp()) + " Bug ");
                try {
                    Picasso.get().load(chat.getPic_url()).placeholder(R.drawable.image_placeholder).resize(400, 400).centerCrop().into(chatimageholder.chatimage);
                } catch (Exception b) {
                    b.printStackTrace();
                }
            }
            chatimageholder.bind(position, mDataSet.get(position), adapterClickListener);
        } else if (chat.getType().equals("video")) {

            final ChatVideoviewholder viewholder = (ChatVideoviewholder) holder;
            viewholder.messageSeen.setTextColor(ContextCompat.getColor(context, R.color.gray));
            viewholder.datetxt.setVisibility(View.VISIBLE);
            if (chat.getSender_id().equals(myID)) {
                if (chat.getStatus().equals("1")) {
                    viewholder.messageSeen.setText("Seen at " + changeDateToTime(chat.getTime()));
                } else {
                    viewholder.messageSeen.setText("Sent");
                    viewholder.datetxt.setVisibility(View.VISIBLE);
                    viewholder.datetxt.setText(showMessageTime(chat.getTimestamp()));
                }
            } else {
                viewholder.messageSeen.setText("");
            }

            viewholder.datetxt.setText(showMessageTime(chat.getTimestamp()));

            if (position != 0) {

                ChatModel chat2 = mDataSet.get(position - 1);
                if (!chat2.getTimestamp().substring(14, 16).equals(chat.getTimestamp().substring(14, 16))) {
                    viewholder.datetxt.setVisibility(View.VISIBLE);
                }

            } else {
                viewholder.datetxt.setVisibility(View.VISIBLE);
            }

            viewholder.bind(position, mDataSet.get(position), adapterClickListener);
        } else if (chat.getType().equals("gif")) {
            final Chatimageviewholder chatimageholder = (Chatimageviewholder) holder;

            chatimageholder.messageSeen.setTextColor(ContextCompat.getColor(context, R.color.gray));
            chatimageholder.datetxt.setVisibility(View.VISIBLE);
            chatimageholder.datetxt.setText(showMessageTime(chat.getTimestamp()));
            // check if the message is from sender or receiver
            if (chat.getSender_id().equals(myID)) {
                if (chat.getStatus().equals("1")) {
                    chatimageholder.messageSeen.setText("Seen at " + changeDateToTime(chat.getTime()));
                } else {
                    chatimageholder.messageSeen.setText("Sent");
                    chatimageholder.datetxt.setText(showMessageTime(chat.getTimestamp()));
                }

            } else {
                chatimageholder.messageSeen.setText("" + changeDateToTime(chat.getTime()));
            }

            if (position != 0) {
                ChatModel chat2 = mDataSet.get(position - 1);
                if (!chat2.getTimestamp().substring(0, 2).equals(chat.getTimestamp().substring(0, 2))) {
                    chatimageholder.datetxt.setVisibility(View.VISIBLE);
                    chatimageholder.datetxt.setText(changeDate(chat.getTimestamp()));
                }
                Glide.with(context)
                        .load(Variables.gifFirstpartChat + chat.getPic_url() + Variables.gifSecondpartChat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);
            } else {
                chatimageholder.datetxt.setVisibility(View.VISIBLE);
                chatimageholder.datetxt.setText(changeDate(chat.getTimestamp()));
                Glide.with(context)
                        .load(Variables.gifFirstpartChat + chat.getPic_url() + Variables.gifSecondpartChat)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(chatimageholder.chatimage);

            }

            chatimageholder.bind(position, mDataSet.get(position), adapterClickListener);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (mDataSet.get(position).getType().equals("text")) {
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return MYCHAT;
            }
            return FRIENDCHAT;
        } else if (mDataSet.get(position).getType().equals("image")) {

            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return MYCHATIMAGE;
            }
            return OTHERCHATIMAGE;
        } else if (mDataSet.get(position).getType().equals("video")) {
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return MY_VIDEO_MESSAGE;
            }

            return OTHER_VIDEO_MESSAGE;
        } else if (mDataSet.get(position).getType().equals("gif")) {
            if (mDataSet.get(position).getSender_id().equals(myID)) {
                return MYGIFIMAGE;
            }

            return OTHERGIFIMAGE;
        } else {
            return ALERT_MESSAGE;
        }
    }

    public String changeDate(String date) {
        //current date in millisecond
        long currenttime = System.currentTimeMillis();

        //database date in millisecond
        long databasedate = 0;
        Date d = null;
        try {
            d = Variables.df.parse(date);
            databasedate = d.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = currenttime - databasedate;
        if (difference < 86400000) {
            int chatday = Integer.parseInt(date.substring(0, 2));
            if (today_day == chatday)
                return "Today";
            else if ((today_day - chatday) == 1)
                return "Yesterday";
        } else if (difference < 172800000) {
            int chatday = Integer.parseInt(date.substring(0, 2));
            if ((today_day - chatday) == 1)
                return "Yesterday";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");

        if (d != null)
            return sdf.format(d);
        else
            return "";
    }

    public String showMessageTime(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        Date d = null;
        try {
            d = Variables.df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (d != null)
            return sdf.format(d);
        else
            return "null";
    }

    public String changeDateToTime(String date) {


        Date d = null;
        try {
            d = Variables.df2.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        if (d != null)
            return sdf.format(d);
        else
            return "";
    }

    public String getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            long second = (file_duration / 1000) % 60;
            long minute = (file_duration / (1000 * 60)) % 60;

            return String.format("%02d:%02d", minute, second);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface OnItemClickListener {
        void onItemClick(ChatModel item, View view);
    }


    public interface OnLongClickListener {
        void onLongclick(ChatModel item, View view);
    }

}
