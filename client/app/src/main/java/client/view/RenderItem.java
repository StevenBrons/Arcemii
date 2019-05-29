package client.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class RenderItem implements Comparable<RenderItem>{
    private Texture texture;
    private double x,y;
    double refX,refY;
    float rotation;
    boolean flip;

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

    public void setRotation(float rotation){
    	this.rotation = rotation;
	}

	  public void setFlip(boolean flip){
    	this.flip = flip;
	}

    /**
     * Sort method to ensure correct drawing order
     * @param other
     * @return
     */
    @Override
    public int compareTo(RenderItem other) {
        return (int) ((this.y * 1000) - (other.y * 1000));
    }

    public void renderTo(Canvas c, int offsetX, int offsetY){
		Bitmap bitmap = texture.getBitmap();
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
