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

    static HashMap<String, Texture> textures = new HashMap<>();
    private Bitmap bitmap;
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
     * Check if the texture is loaded before
     * @param name The name (path) of the texture
     * @return Whether the texture already exists
     * @author Steven Bronsveld
     */
    public static boolean hasTexture(String name){
        return textures.containsKey(name);
    }

    Texture(String path,boolean loadTexture){
        if (loadTexture){
            try {
                this.bitmap = BitmapFactory.decodeStream(manager.open(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets a texture from the database, if it isn't yet in the database it is constructed
     * @param name The name (path) of the texture
     * @author Steven Bronsveld
     * @author Jelmer Firet
     */
    public static Texture getTexture(String name) {
        if (hasTexture(name)){
            return textures.get(name);
        }
        String[] namepart = name.split("/");
        StringBuilder path = new StringBuilder("sprites");
        for (int i = 0;i<namepart.length-1;i++){
            path.append('/');
            path.append(namepart[i]);
        }
        String nameEnd = namepart[namepart.length-1];

        try {
            for (String spriteFile: manager.list(path.toString())){
                String spriteName = FilenameUtils.removeExtension(spriteFile);
                Animation animation = new Animation(name);
                if (spriteName.matches(nameEnd)){
                    textures.put(name, new Texture(path+"/"+spriteFile,true));
                }
                if (spriteName.matches(name+"_\\d+")){
                    int frameNum = Integer.parseInt(spriteName.split("_")[1]);
                    animation.addFrame(frameNum,new Texture(path+"/"+spriteFile,true));
                }
                if (!hasTexture(name)){
                    textures.put(name,animation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textures.get(name);
    }

    /**
     * @return The width of the texture
     * @author Steven Bronsveld
     */
    public int getWidth() {
        return bitmap.getWidth();
    }

    /**
     * @return The height of the texture
     * @author Steven Bronsveld
     */
    public int getHeight() {
        return bitmap.getHeight();
    }

    /**
     * @return The name (path) of the texture
     * @author Steven Bronsveld
     */
    public String getName() { return name; }

    /**
     * returns the bitmap of this texture. If the current texture is an animation it chooses the
     *  correct frame based on the system time.
     * @return The bitmap of the texture. If the texture is used at more places the same bitmap will
     * be returned.
     * @author Steven Bronsveld
     * @author Jelmer Firet
     */
    public Bitmap getBitmap() {
        return getBitmap(0);
    }

    /**
     * Returns the bitmap of this texture with any animation offset applied to it.
     * @param offset The number of frames the returned bitmap is.
     * @return The bitmap of the texture.
     * @see Bitmap getBitmap()
     */
    public Bitmap getBitmap(int offset) {
        return bitmap;
    }
}