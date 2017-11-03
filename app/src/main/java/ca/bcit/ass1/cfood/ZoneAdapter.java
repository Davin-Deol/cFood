package ca.bcit.ass1.cfood;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by E on 2017-11-02.
 */

public class ZoneAdapter extends CursorAdapter {

    public ZoneAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.activity_list, viewGroup, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
//        TextView zoneName = (TextView) view.findViewById(R.id.zoneType);
//        zoneName.setText(cursor.getString(1));
//
//        TextView zoneCoord = (TextView) view.findViewById(R.id.zoneCoord);
//        zoneCoord.setText(cursor.getString(2));

        TextView type = (TextView)view.findViewById(R.id.type);
         TextView coords = (TextView)view.findViewById(R.id.coords);
       //  TextView resultsRate = (TextView)view.findViewById(R.id.resultsRating);

        type.setText(cursor.getString(1));
        coords.setText(cursor.getString(2));
//         resultsRate.setText(cursor.getString(3));

    }
}
