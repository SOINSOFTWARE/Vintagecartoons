package co.com.soinsoftware.vintagecartoons.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.com.soinsoftware.vintagecartoons.R;
import co.com.soinsoftware.vintagecartoons.entity.SerieItem;
import co.com.soinsoftware.vintagecartoons.task.DownloadImageTask;

public class SerieArrayAdapter extends ArrayAdapter<SerieItem> {

	private final Context context;

	private final SerieItem[] serieItemValues;

	public SerieArrayAdapter(final Context context,
			final SerieItem[] serieItemValues) {
		super(context, -1, serieItemValues);
		this.context = context;
		this.serieItemValues = serieItemValues;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.activity_serie_item,
				parent, false);
		final SerieItem serieItem = this.serieItemValues[position];
		this.fillImage(rowView, serieItem);
		this.fillCopyright(rowView, serieItem);
		return rowView;
	}

	private void fillImage(final View rowView, final SerieItem chapterItem) {
		final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		final String imageAddress = chapterItem.getImageAddress();
		new DownloadImageTask((ImageView) imageView).execute(imageAddress);
	}

	private void fillCopyright(final View rowView, final SerieItem chapterItem) {
		final TextView textView = (TextView) rowView
				.findViewById(R.id.firstLine);
		final String copyright = chapterItem.getCopyright();
		textView.setText(copyright);
	}
}