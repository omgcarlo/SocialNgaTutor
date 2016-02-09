package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;

import org.w3c.dom.Text;

public class EventRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private String MONTH[] = {"January","February","March",
            "April","May","June","July","August",
            "September","October","November","December"};
    private Context context;
    //  FIELDS
    private TextView eventId;
    private TextView title;
    private TextView description;
    private TextView eventDate; // full date ni - needs to be converted to date only
    private TextView monthyear; //

    public EventRecyclerItemViewHolder(final View parent, TextView eventId, TextView title,
                                       TextView description,TextView eventDate,TextView monthyear) {
        super(parent);

        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.monthyear = monthyear;

        context =  parent.getContext();
    }

    public static EventRecyclerItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.e_card_title);
        TextView details = (TextView) parent.findViewById(R.id.e_card_details);
        TextView eventId = (TextView) parent.findViewById(R.id.e_card_eventId);
        TextView eventDate = (TextView) parent.findViewById(R.id.e_card_date);
        TextView monthyear = (TextView) parent.findViewById(R.id.e_card_monthyear);
        return new EventRecyclerItemViewHolder(parent,eventId,title,details,eventDate,monthyear);
    }


    @Override
    public void onClick(View v) {

    }


    public void setEventId(String eventId) {
        this.eventId.setText(eventId);
    }

    public void setEventTitle(String eventTitle) {
        title.setText(eventTitle);
    }

    public void setEventDescription(String eventDescription) {
        description.setText(eventDescription);
    }

    public void setEventDate(String eventDate) {
        String split[] = eventDate.split("-");
        this.eventDate.setText(split[2]);
        int month = Integer.parseInt(split[1]);
        this.monthyear.setText(MONTH[month]+"•"+split[0]);
    }
}
