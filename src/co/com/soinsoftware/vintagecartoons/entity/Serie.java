package co.com.soinsoftware.vintagecartoons.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;

public abstract class Serie {

	protected static final String THUMB = "_thumb.jpg";

	protected static final String MP4 = ".mp4";

	protected static final String JSON_PARENT_TAG = "chapters";

	protected static final String JSON_TITLE_TAG = "title";

	protected static final String JSON_DESCRIPTION_TAG = "description";

	protected static final String JSON_IMAGE_TAG = "image";

	protected static final String JSON_VIDEO_TAG = "video";

	protected List<ChapterItem> chapterItemList;

	public String loadJSONFromAsset(final Activity activity,
			final String jsonFile) {
		String json = null;
		try {
			InputStream is = activity.getAssets().open(jsonFile);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}

	public List<ChapterItem> getChapterItemList() {
		return chapterItemList;
	}

	protected ChapterItem buildChapterItem(final String image, final String title,
			final String description, final String video) {
		return new ChapterItem(image, title, description, video);
	}
}