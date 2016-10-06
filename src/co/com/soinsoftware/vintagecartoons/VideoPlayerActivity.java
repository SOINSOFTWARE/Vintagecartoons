package co.com.soinsoftware.vintagecartoons;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import co.com.soinsoftware.vintagecartoons.adapter.ChapterArrayAdapter;
import co.com.soinsoftware.vintagecartoons.entity.Casper;
import co.com.soinsoftware.vintagecartoons.entity.ChapterItem;
import co.com.soinsoftware.vintagecartoons.entity.DonaldDuck;
import co.com.soinsoftware.vintagecartoons.entity.MightyMouse;
import co.com.soinsoftware.vintagecartoons.entity.Popeye;
import co.com.soinsoftware.vintagecartoons.entity.Serie;
import co.com.soinsoftware.vintagecartoons.entity.SerieItem;
import co.com.soinsoftware.vintagecartoons.entity.Superman;

public class VideoPlayerActivity extends Activity implements
		SurfaceHolder.Callback, MediaPlayerControl {

	private static final String CHAPTER_KEY = "chapter";

	private static final String CHAPTER_ARRAY_KEY = "chapterArray";

	private SerieItem serieItem;

	private ChapterItem[] chapterItemVals;

	private ChapterItem chapterToPlay;

	private MediaPlayer mediaPlayer;

	private MediaController mediaController;

	private SurfaceHolder holder;

	private ProgressDialog pDialog;

	private boolean pauseForced;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_video_player);
		final Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.serieItem = (SerieItem) bundle
					.getSerializable(SerieCatalogActivity.SERIE);
		}
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(CHAPTER_ARRAY_KEY)) {
				this.chapterItemVals = (ChapterItem[]) savedInstanceState
						.getSerializable(CHAPTER_ARRAY_KEY);
			} else {
				this.buildVideoArray();
			}
			if (savedInstanceState.containsKey(CHAPTER_KEY)) {
				this.chapterToPlay = (ChapterItem) savedInstanceState
						.getSerializable(CHAPTER_KEY);
			} else {
				this.chapterToPlay = this.getRandomChapter();
			}
		} else {
			this.buildVideoArray();
			this.chapterToPlay = this.getRandomChapter();
		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			this.buildListViewAdapter();
		}
		this.buildPlayer();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(CHAPTER_KEY, this.chapterToPlay);
		outState.putSerializable(CHAPTER_ARRAY_KEY, this.chapterItemVals);
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
		final LayoutParams params = surfaceView.getLayoutParams();
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.MATCH_PARENT;
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			params.width = LayoutParams.MATCH_PARENT;
			params.height = (int) getResources().getDimension(
					R.dimen.surfaceview_height);
			this.buildListViewAdapter();
		}
		surfaceView.setLayoutParams(params);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		Log.d("Vintage Cartoons", "surfaceCreated called");
		if (this.mediaPlayer == null) {
			this.loadVideo(this.chapterToPlay, false);
			this.mediaController.setMediaPlayer(this);
			this.mediaController.setAnchorView(findViewById(R.id.surface));
			this.mediaController.setEnabled(true);
		}
		this.mediaPlayer.setDisplay(this.holder);
		if (this.pauseForced) {
			this.pauseForced = false;
			this.mediaPlayer.start();
		}
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		Log.d("Vintage Cartoons", "surfaceChanged called");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("Vintage Cartoons", "surfaceDestroyed called");
		if (this.mediaPlayer.isPlaying()) {
			this.mediaPlayer.pause();
			this.pauseForced = true;
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		this.mediaController.show();
		return false;
	}

	@Override
	public void start() {
		this.mediaPlayer.start();
	}

	@Override
	public void pause() {
		this.mediaPlayer.pause();
	}

	@Override
	public int getDuration() {
		return this.mediaPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return this.mediaPlayer.getCurrentPosition();
	}

	@Override
	public void seekTo(int pos) {
		this.mediaPlayer.seekTo(pos);
	}

	@Override
	public boolean isPlaying() {
		return this.mediaPlayer.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return this.mediaPlayer.getAudioSessionId();
	}

	public void setConnectivityListener(
			ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;
	}

	private void buildVideoArray() {
		final Serie chapter = this.getSerieChapter();
		this.chapterItemVals = new ChapterItem[chapter.getChapterItemList()
				.size()];
		this.chapterItemVals = chapter.getChapterItemList().toArray(
				this.chapterItemVals);
	}

	private Serie getSerieChapter() {
		final String serieName = this.serieItem.getSerie();
		Serie chapter = null;
		if (serieName.equalsIgnoreCase(SerieItem.CASPER)) {
			chapter = new Casper(this);
		} else if (serieName.equalsIgnoreCase(SerieItem.DONALD_DUCK)) {
			chapter = new DonaldDuck(this);
		} else if (serieName.equalsIgnoreCase(SerieItem.MIGHTY_MOUSE)) {
			chapter = new MightyMouse(this);
		} else if (serieName.equalsIgnoreCase(SerieItem.POPEYE)) {
			chapter = new Popeye(this);
		} else if (serieName.equalsIgnoreCase(SerieItem.SUPERMAN)) {
			chapter = new Superman(this);
		} else {
			chapter = new Popeye(this);
		}
		return chapter;
	}

	private void buildListViewAdapter() {
		final ListView chapterView = (ListView) this
				.findViewById(R.id.chapterListView);
		final ChapterArrayAdapter adapter = new ChapterArrayAdapter(this,
				this.chapterItemVals);
		if (chapterView != null) {
			chapterView.setAdapter(adapter);
			chapterView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(final AdapterView<?> parent,
						final View view, final int position, final long id) {
					final ChapterItem chapter = chapterItemVals[position];
					releaseMediaPlayer();
					loadVideo(chapter, true);
					mediaPlayer.setDisplay(holder);
				}
			});
		}
	}

	private ChapterItem getRandomChapter() {
		int chapNumber = 0;
		if (this.chapterItemVals.length > 1) {
			final Random random = new Random();
			final int maxNumber = this.chapterItemVals.length - 1;
			chapNumber = random.nextInt(maxNumber);
		}
		return this.chapterItemVals[chapNumber];
	}

	@SuppressWarnings("deprecation")
	private void buildPlayer() {
		final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
		this.holder = surfaceView.getHolder();
		this.holder.addCallback(this);
		this.mediaController = new MediaController(this);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
	}

	private void loadVideo(final ChapterItem chapter, final boolean autoPlay) {
		this.showProgressBar();
		this.setChapterInformation(chapter);
		try {
			Uri vidUri = Uri.parse(chapter.getVideoAddress());
			this.mediaPlayer = new MediaPlayer();
			this.mediaPlayer.setDataSource(this, vidUri);
			this.mediaPlayer.prepareAsync();
			this.mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(final MediaPlayer mp) {
					try {
						Log.d("Vintage Cartoons",
								"MediaPlayer onPrepared called");
						mp.start();
					} catch (Exception e) {
						Log.e("Vintage Cartoons", "error: " + e.getMessage(), e);
					} finally {
						pDialog.dismiss();
					}
				}
			});
		} catch (Exception e) {
			Log.e("Vintage Cartoons", "error: " + e.getMessage(), e);
			pDialog.dismiss();
		}
	}

	private void setChapterInformation(final ChapterItem chapter) {
		final TextView textViewTitle = (TextView) findViewById(R.id.title);
		if (textViewTitle != null) {
			textViewTitle.setText(chapter.getTitle());
		}
		final TextView textViewCopyright = (TextView) findViewById(R.id.copyright);
		if (textViewCopyright != null) {
			textViewCopyright.setText(this.serieItem.getCopyright());
		}
	}

	private void showProgressBar() {
		this.pDialog = new ProgressDialog(this);
		this.pDialog.setMessage(this.getString(R.string.loading));
		this.pDialog.setIndeterminate(false);
		this.pDialog.setCancelable(true);
		this.pDialog.show();
	}

	private void releaseMediaPlayer() {
		mediaPlayer.reset();
		mediaPlayer.release();
	}
}