package client.view;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Animation extends Texture {
	ArrayList<Texture> frames = new ArrayList<Texture>();
	String name;

	/**
	 * Initializes a texture, if the texture has never been loaded, it is loaded.
	 * It checks whether a texture is animated, and loads all frames if applicable.
	 *
	 * @param name The name (path) of the texture
	 * @author Steven Bronsveld
	 * @author Jelmer Firet
	 */

	Animation(String name) {
		super(name,false);
		this.name = name;
	}

	public void addFrame(int frameIdx, Texture frame){
		frames.add(frameIdx,frame);
	}

	public Bitmap getBitmap(int offset){
		long time = System.currentTimeMillis()/200;
		return textures.get("sprites/"+this.name+"_"+ (time+offset) % frames.size()).getBitmap();
	}

	public Bitmap getBitmap(){
		return getBitmap(0);
	}
}
