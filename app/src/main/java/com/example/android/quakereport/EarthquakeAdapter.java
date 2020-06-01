package com.example.android.quakereport;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> list)
    {
        super(context,0,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }
        Earthquake currentPair = getItem(position);
        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle=(GradientDrawable) magnitudeTextView.getBackground();
        int magnitudeColor= getMagnitudeColor(currentPair.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);


        DecimalFormat formatter=new DecimalFormat("0.00");
        String output=formatter.format(currentPair.getMagnitude());
        magnitudeTextView.setText(output);

        String loc=currentPair.getLocation();
        String delimiter=" of ";
        String secLocation="";
        String primLocation="";
        if(loc.contains(delimiter)) {
            String str[] = loc.split(" of ", 0);
            secLocation = str[0];
            primLocation = str[1];
            TextView SeclocationTextView = (TextView) listItemView.findViewById(R.id.Slocation);
            SeclocationTextView.setText(secLocation);
            TextView PrimlocationTextView = (TextView) listItemView.findViewById(R.id.Plocation);
            PrimlocationTextView.setText(primLocation);
        }
        else
        {
            secLocation="Near the";
            primLocation=loc;
            TextView SeclocationTextView = (TextView) listItemView.findViewById(R.id.Slocation);
            SeclocationTextView.setText(secLocation);
            TextView PrimlocationTextView = (TextView) listItemView.findViewById(R.id.Plocation);
            PrimlocationTextView.setText(primLocation);
        }

        Date obj=new Date(currentPair.getDate());
        TextView dateTextView=(TextView) listItemView.findViewById((R.id.date));
        String formattedDate=formatDate(obj);
        dateTextView.setText(formattedDate);

        TextView timeTextView=(TextView) listItemView.findViewById((R.id.time));
        String formattedTime=formatTime(obj);
        timeTextView.setText(formattedTime);
        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private int getMagnitudeColor(Double magnitude)
    {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
