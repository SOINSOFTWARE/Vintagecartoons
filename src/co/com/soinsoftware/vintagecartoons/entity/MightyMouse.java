package co.com.soinsoftware.vintagecartoons.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class MightyMouse extends Serie {

	public static final String CHAPTER_URL = URL + "mighty-mouse/";

	private static final String JSON_FILE = "mighty-mouse-eng.json";

	public MightyMouse(final Activity activity) {
		super();
		final String json = this.loadJSONFromAsset(activity, JSON_FILE);
		this.chapterItemList = new ArrayList<ChapterItem>();
		this.buildChapterItemList(json);
	}

	private void buildChapterItemList(final String json) {
		try {
			final JSONObject obj = new JSONObject(json);
			final JSONArray m_jArry = obj.getJSONArray(JSON_PARENT_TAG);
			for (int i = 0; i < m_jArry.length(); i++) {
				final JSONObject jo_inside = m_jArry.getJSONObject(i);
				final String title = jo_inside.getString(JSON_TITLE_TAG);
				final String description = jo_inside
						.getString(JSON_DESCRIPTION_TAG);
				final String image = jo_inside.getString(JSON_IMAGE_TAG);
				final String video = jo_inside.getString(JSON_VIDEO_TAG);

				this.chapterItemList.add(this.buildChapterItem(CHAPTER_URL,
						image, title, description, video));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}