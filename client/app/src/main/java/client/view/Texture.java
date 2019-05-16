package client.view;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * The Texture object should always be used if an asset is used multiple times to make
 * sure it is not initialized more than necessary.
 * @author Steven Bronsveld
 */
public class Texture {

    static HashMap<String, Bitmap> textures = new HashMap<>();
    public static final int WIDTH = 192;
    public static final int HEIGHT = 192;
    private static AssetManager manager;

    private String name;

    /**
     * Initializes the texture class, needs to be called before the game needs to display textures
     * @author Steven Bronsveld
     */
    public static void init(AssetManager manager) {
        Texture.manager = manager;
    }

    /**
     * Adds a texture to the global texture list
     * @param name The name (path) of the texture
     * @param bitmap The bitmap of the texture to add
     * @author Steven Bronsveld
     */
    public static void addTexture(String name, Bitmap bitmap){
        if (!hasTexture(name)){
            textures.put(name,bitmap);
        }
    }

    /**
     * Check if the texture is loaded before
     * @param name The name (path) of the texture
     * @return Whether the texture already exists
     * @author Steven Bronsveld
     */
    public static boolean hasTexture(String name){
        return textures.containsKey(name);
    }

    /**
     * Initializes a texture, if the texture has never been loaded, it is loaded.
     * @param name The name (path) of the texture
     * @author Steven Bronsveld
     */
    public Texture(String name) {
        this.name = name;
        if (!textures.containsKey(name)) {
            try {
                Bitmap b = BitmapFactory.decodeStream(manager.open("sprites/" + name + ".png"));
                textures.put(name,b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return The width of the texture
     * @author Steven Bronsveld
     */
    public int getWidth() {
        return getBitmap().getWidth();
    }

    /**
     * @return The height of the texture
     * @author Steven Bronsveld
     */
    public int getHeight() {
        return getBitmap().getHeight();
    }

    /**
     * @return The name (path) of the texture
     * @author Steven Bronsveld
     */
    public String getName() { return name; }

    /**
     * @return The bitmap of the texture. If the texture is used at more places the same bitmap will
     * be returned.
     */
    public Bitmap getBitmap() {
        return textures.get(this.name);
    }
}