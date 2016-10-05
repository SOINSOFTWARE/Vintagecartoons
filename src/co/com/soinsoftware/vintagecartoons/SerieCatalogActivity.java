package co.com.soinsoftware.vintagecartoons;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import co.com.soinsoftware.vintagecartoons.adapter.SerieArrayAdapter;
import co.com.soinsoftware.vintagecartoons.entity.SerieItem;

public class SerieCatalogActivity extends Activity {

	private static final String URL = "http://www.thevintagecartoon.com/images/";

	private static final String JSON_FILE = "series-eng.json";

	private static final String JSON_PARENT_TAG = "series";

	private static final String JSON_TITLE_TAG = "title";

	private static final String JSON_IMAGE_TAG = "image";

	private static final String JSON_SERIE_TAG = "serie";

	protected static final String SERIE = "serie";

	private static SerieCatalogActivity instance;

	private SerieItem[] serieItemVals;

	private List<SerieItem> serieItemList;

	public static synchronized SerieCatalogActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_serie_catalog);
		this.buildSerieArray();
		this.buildListViewAdapter();
		instance = this;
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void buildSerieArray() {
		this.buildSerieItemList();
		this.serieItemVals = new SerieItem[this.serieItemList.size()];
		this.serieItemVals = this.serieItemList.toArray(this.serieItemVals);
	}

	private String loadJSONFromAsset() {
		String json = null;
		try {
			InputStream is = this.getAssets().open(JSON_FILE);
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

	private void buildSerieItemList() {
		final String json = this.loadJSONFromAsset();
		this.serieItemList = new ArrayList<SerieItem>();
		this.buildSerieItemList(json);
	}

	private void buildSerieItemList(final String json) {
		try {
			final JSONObject obj = new JSONObject(json);
			final JSONArray m_jArry = obj.getJSONArray(JSON_PARENT_TAG);
			for (int i = 0; i < m_jArry.length(); i++) {
				final JSONObject jo_inside = m_jArry.getJSONObject(i);
				final String title = jo_inside.getString(JSON_TITLE_TAG);
				final String image = jo_inside.getString(JSON_IMAGE_TAG);
				final String serie = jo_inside.getString(JSON_SERIE_TAG);

				this.serieItemList
						.add(new SerieItem(title, URL + image, serie));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void buildListViewAdapter() {
		final ListView serieView = (ListView) this
				.findViewById(R.id.serieListView);
		final SerieArrayAdapter adapter = new SerieArrayAdapter(this,
				this.serieItemVals);
		serieView.setAdapter(adapter);
		serieView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				startVideoPlayerActivity(serieItemList.get(position));
			}
		});
	}

	private void startVideoPlayerActivity(final SerieItem serieItem) {
		boolean isConnected = ConnectivityReceiver.isConnected();
		if (isConnected) {
			final Intent intent = new Intent(this, VideoPlayerActivity.class);
			final Bundle b = new Bundle();
			b.putSerializable(SERIE, serieItem);
			intent.putExtras(b);
			this.startActivity(intent);
		} else {
			Toast.makeText(
					this,
					"Vintage Cartoons requires internet connection to show episodes",
					Toast.LENGTH_LONG).show();
		}
	}
}