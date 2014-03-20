package com.gall.remote.adapters;

import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gall.remote.R;

/**
 * List adapter for music Chooser
 * Displays RowItem in list (layout - R.layout.row_layout)
 * Used for any list displaying song options
 * 
 * @param c application context
 * @param resourceID layout to be displayed in list
 * @param items List of RowItems to be displayed
 * @author Matt Gall
 *
 */
public class MusicChooserAdapter extends ArrayAdapter<RowItem> {
	private Context c;
	
	//Constructor
	public MusicChooserAdapter(Context c, int resourceID, List<RowItem> items){
		super(c, resourceID, items);
		this.c = c;
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView titleTxt;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		ViewHolder holder = null;
		RowItem rowItem = getItem(position);
		
		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			
			convertView = inflater.inflate(R.layout.row_layout, null);
			
			holder = new ViewHolder();
			
			holder.titleTxt = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			
			}
		
        holder.titleTxt.setText(rowItem.getTitle());
        	holder.imageView.setImageResource(rowItem.getImageID());
        
			
		return convertView;
		
	}
	


}
