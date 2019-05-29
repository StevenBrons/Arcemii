package client.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * RenderItem is a class that represents a sprite to be rendered and handles all positioning
 * @author Jelmer Firet
 */
public class RenderItem implements Comparable<RenderItem>{
    private Texture texture;
    private int x,y;
    private int animationOffset = 0;
    private double refX,refY;
    private float rotation;
    private boolean flip;

    /**
     * Creates a new RenderItem
     * @param textureName
     * @param x The x coördinate where the reference point will end up on the canvas
     * @param y The y coördinate where the reference point will end up on the canvas
     * @param refX The x position of the reference point within the image: 0.0 = left, 1.0 = right
     * @param refY The y position of the reference point within the image: 0.0 = top, 1.0 = bottom
     * @author Jelmer Firet
     */
    public RenderItem(String textureName, double x, double y,double refX, double refY){
        this.texture = new Texture(textureName);
        this.x = x;
        this.y = y;
        this.refX = refX;
        this.refY = refY;
    }

	/**
	 * Same as RenderItem without animationOffset, except this sets an animation offset.
	 * @param animationOffset The number of frames this item is out of sync.
	 * @see RenderItem
	 * @autor Jelmer Firet
	 */
    public RenderItem(String textureName, int x, int y, double refX, double refY, int animationOffset){
		this.texture = new Texture(textureName);
		this.x = x;
		this.y = y;
		this.refX = refX;
		this.refY = refY;
    	this.animationOffset = animationOffset;
	}

	/**
	 * Sets the rotation of the sprite
	 * @param rotation degrees to rotate the object clockwise
	 * @author Jelmer Firet
	 */
	public void setRotation(float rotation){
    	this.rotation = rotation;
	}

	/**
	 * Sets whether the sprite is flipped horizontally; flipping occurs before any rotation
	 * @param flip sets the flip flag of this RenderItem
	 * @author Jelmer Firet
	 */
	public void setFlip(boolean flip){
    	this.flip = flip;
	}

    /**
     * Sort method to ensure correct drawing order
     * @param other object to compare to
     * @return a value that indicates which one should be drawn first
	 * @author Jelmer Firet
     */
    @Override
    public int compareTo(RenderItem other) {
        return (int) ((this.y * 1000) - (other.y * 1000));
    }

	/**
	 * Draws this RenderItem to a Canvas. It places the refX and refY of the image at the correct
	 * 	place and applies any flips, rotations and/or offsets (in that order)
	 * @param c Canvas to draw onto
	 * @param offsetX the x-offset to where the RenderItem would have been drawn
	 * @param offsetY the y-offset to where the RenderItem would have been drawn
	 */
    public void renderTo(Canvas c, int offsetX, int offsetY){
		Bitmap bitmap = texture.getBitmap(animationOffset);
		Matrix matrix = new Matrix();
		matrix.preTranslate((int)(-bitmap.getWidth()*refX),(int)(-bitmap.getHeight()*refY));
		if (flip){
			matrix.postScale(-1.0f,1.0f);
		}
		matrix.postRotate(rotation);
		matrix.postTranslate((float) x+offsetX,(float)y+offsetY);
		c.drawBitmap(bitmap,matrix,new Paint());
	}
}
