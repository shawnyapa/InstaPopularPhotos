package com.shawnyapa.instaphotos;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PopularPhotosAdapter extends ArrayAdapter<Photo> {

	public PopularPhotosAdapter(Context context, ArrayList<Photo> photos) {
		//super(context, 0, photos);
		super(context, android.R.layout.simple_list_item_1, photos);
		//super(context, R.layout.item_photo, photos);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get item at position, check if recycled, lookup subviews & populate, return view
		Photo photo = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		tvCaption.setText(photo.caption);
		imgPhoto.getLayoutParams().height = photo.photoheight;
		imgPhoto.setImageResource(0);
		// Async Download Image from URL and insert into ImageView
		Picasso.with(getContext()).load(photo.photoimage).into(imgPhoto);
		
		return convertView;
	}
}
