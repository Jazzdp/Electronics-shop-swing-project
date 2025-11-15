package shop.util;

import java.awt.Color;

// vibe coded ts for the colors IM SORRY I COULDNT FIGURE IT OUT
public final class Palette {
    private Palette() {}

    // Primary header color (deep blue)
    public static final Color PRIMARY = new Color(21, 59, 122); // #153B7A
    public static final Color ON_PRIMARY = Color.WHITE;

    // Backgrounds / surfaces
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(250, 251, 253);

    // Cards / borders
    public static final Color CARD_BORDER = new Color(230, 230, 235);
    public static final Color MUTED_TEXT = new Color(120, 130, 140);

    // Accent / price green
    public static final Color PRICE_GREEN = new Color(22, 163, 74); // approx green used for prices

    // Search / light gray background
    public static final Color SEARCH_BG = new Color(245, 247, 250);
    
	// Colors
	public static final Color DARK_NAVY = new Color(7, 21, 40); // main button color
	public static final Color DARK_NAVY_ACCENT = new Color(10, 28, 55); // slightly lighter for hover/selected
	public static final Color TEXT_ON_DARK = Color.WHITE;
	public static final Color PANEL_BG = new Color(245, 245, 245);

	// Sizes / typography
	public static final int CATEGORY_FONT_SIZE = 22;
	public static final int CATEGORY_BUTTON_WIDTH = 220;
	public static final int CATEGORY_BUTTON_HEIGHT = 56;
}
