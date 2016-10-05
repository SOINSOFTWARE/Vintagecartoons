package co.com.soinsoftware.vintagecartoons.entity;

import java.io.Serializable;

public class ChapterItem implements Serializable {

	private static final long serialVersionUID = -2213446361334355702L;

	final private String imageAddress;

	final private String title;

	final private String description;

	final private String videoAddress;

	public ChapterItem(final String imageAddress, final String title,
			final String description, final String videoAddress) {
		super();
		this.imageAddress = imageAddress;
		this.title = title;
		this.description = description;
		this.videoAddress = videoAddress;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getVideoAddress() {
		return videoAddress;
	}
}