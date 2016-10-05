package co.com.soinsoftware.vintagecartoons.entity;

import java.io.Serializable;

public class SerieItem implements Serializable {

	private static final long serialVersionUID = 3213211383680463075L;

	public static final String CASPER = "Casper";

	public static final String DONALD_DUCK = "DonaldDuck";

	public static final String MIGHTY_MOUSE = "MightyMouse";

	public static final String POPEYE = "Popeye";

	public static final String SUPERMAN = "Superman";

	private final String title;

	private final String imageAddress;

	private final String serie;

	public SerieItem(final String title, final String imageAddress,
			final String serie) {
		super();
		this.imageAddress = imageAddress;
		this.title = title;
		this.serie = serie;
	}

	public String getTitle() {
		return title;
	}

	public String getImageAddress() {
		return imageAddress;
	}

	public String getSerie() {
		return serie;
	}
}