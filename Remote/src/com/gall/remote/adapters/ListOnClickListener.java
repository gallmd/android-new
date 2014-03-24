package com.gall.remote.adapters;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Not used at the moment
 * @author Matt Gall
 *
 */
public class ListOnClickListener extends Activity implements OnItemClickListener{

	private Context c;
	private List<RowItem> rowItems;
	private String toastString;
	@SuppressLint("Registered")
	public ListOnClickListener(Context c, List<RowItem> rowItems){
		this.c = c;
		this.rowItems = rowItems;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long viewID) {

		toastString = rowItems.get(position).getTitle();

		Toast toast = Toast.makeText(c,
				"Item " + (position + 1) + ": " + toastString,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();		
	}

}
