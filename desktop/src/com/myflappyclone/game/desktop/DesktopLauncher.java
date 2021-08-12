package com.myflappyclone.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.myflappyclone.game.MyFlappyClone;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MyFlappyClone.WIDTH;
		config.height = MyFlappyClone.HEIGHT;
		config.title = MyFlappyClone.TITLE;
		new LwjglApplication(new MyFlappyClone(), config);
	}
}
