package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Util.Constants;

public class DesktopLauncher {

	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;
	public static  boolean drawCollisionBounds = false;

	public static void main (String[] arg) {
		if(rebuildAtlas){
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = true;
			settings.debug = drawDebugOutline;

			TexturePacker.process(settings, "images",
					"textureAtlas","LibgdxGame");
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MyLibgdxGame";
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		config.fullscreen = true;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
