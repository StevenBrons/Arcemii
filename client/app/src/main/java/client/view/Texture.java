package client.view;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * The Texture object should always be used if an asset is used multiple times to make
 * sure it is not initialized more than necessary.
 * @author Steven Bronsveld
 */
public class Texture {

    static HashMap<String, Bitmap> textures = new HashMap<>();
    private static AssetManager manager;

    private String name;
    private boolean animated = false;
    private int frames = 0;

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
        String[] namepart = name.split("/");
        StringBuilder path = new StringBuilder("sprites");
        for (int i = 0;i<namepart.length-1;i++){
            path.append('/');
            path.append(namepart[i]);
        }
        Log.i("path",path.toString());
        name = namepart[namepart.length-1];
        try {
            for (String spriteFile: manager.list(path.toString())){
                String spriteName = FilenameUtils.removeExtension(spriteFile);
                if (spriteName.matches(name)){
                    Log.i("Texture",path.toString()+"/"+spriteName);
                    if (!textures.containsKey(spriteName)) {
                        Bitmap b = BitmapFactory.decodeStream(manager.open(path.toString()+ "/" + spriteFile));
                        textures.put(path.toString()+"/"+spriteName, b);
                    }
                }
                if (spriteName.matches(name+"_\\d+")){
                    Log.i("Texture",spriteName);
                    this.animated = true;
                    if (!textures.containsKey(spriteName)) {
                        Bitmap b = BitmapFactory.decodeStream(manager.open(path.toString() + "/" + spriteFile));
                        textures.put(path.toString()+"/"+spriteName, b);
                    }
                    frames++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        if (animated){
            long time = System.currentTimeMillis()/200;
            return textures.get("sprites/"+this.name+"_"+ time % frames);
        }
        return textures.get("sprites/"+this.name);
    }
}