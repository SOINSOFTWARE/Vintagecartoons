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
import co.com.soinsoftware.vintagecartoons.entity.ChapterItem;
import co.com.soinsoftware.vintagecartoons.task.DownloadImageTask;

public class ChapterArrayAdapter extends ArrayAdapter<ChapterItem> {

	private final Context context;

	private final ChapterItem[] chapterItemValues;

	public ChapterArrayAdapter(final Context context,
			final ChapterItem[] chapterItemValues) {
		super(context, -1, chapterItemValues);
		this.context = context;
		this.chapterItemValues = chapterItemValues;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.activity_chapter_item,
				parent, false);
		final ChapterItem chapterItem = this.chapterItemValues[position];
		this.fillImage(rowView, chapterItem);
		this.fillTitle(rowView, chapterItem);
		this.fillDescription(rowView, chapterItem);
		return rowView;
	}

	private void fillImage(final View rowView, final ChapterItem chapterItem) {
		final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		final String imageAddress = chapterItem.getImageAddress();
		new DownloadImageTask((ImageView) imageView).execute(imageAddress);

	}

	private void fillTitle(final View rowView, final ChapterItem chapterItem) {
		final TextView textView = (TextView) rowView
				.findViewById(R.id.firstLine);
		final String title = chapterItem.getTitle();
		textView.setText(title);
	}

	private void fillDescription(final View rowView,
			final ChapterItem chapterItem) {
		final TextView textView = (TextView) rowView
				.findViewById(R.id.secondLine);
		final String description = chapterItem.getDescription();
		textView.setText(description);
	}
}