package co.com.soinsoftware.vintagecartoons.task;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	final ImageView bmImage;

	public DownloadImageTask(final ImageView bmImage) {
		this.bmImage = bmImage;
	}

	protected Bitmap doInBackground(final String... urls) {
		final String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		InputStream stream = null;
		try {
			stream = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(stream);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					Log.e("Error", e.getMessage());
				}
			}
		}
		return mIcon11;
	}

	protected void onPostExecute(final Bitmap result) {
		bmImage.setImageBitmap(result);
	}
}