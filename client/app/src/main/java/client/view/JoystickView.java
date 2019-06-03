package client.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;


public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float Radius;
    private float HatRadius;
    private JoystickListener joystickCallback;

    public interface JoystickListener{
        void onJoystickMoved(float xPercent, float yPercent, int source);
    }

    public JoystickView (Context context, AttributeSet attributes){
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    private void setupDminensions(){
        centerX = getWidth()/2;
        centerY = getHeight()/2;
        Radius  = Math.min(getWidth(), getHeight())/3;
        HatRadius = Math.min(getWidth(), getHeight())/6;
    }

    private void drawJoystick(float newX, float newY){
        if(getHolder().getSurface().isValid()){
            Canvas JoyStick = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            JoyStick.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255,50,50,50);
            JoyStick.drawCircle(centerX, centerY, Radius, colors);
            colors.setARGB(255,  0 ,0, 255);
            JoyStick.drawCircle(newX, newY, HatRadius, colors);
            getHolder().unlockCanvasAndPost(JoyStick);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        setupDminensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public boolean onTouch(View v, MotionEvent me){
        float displacement = (float) Math.sqrt(Math.pow(me.getX() - centerX, 2) + Math.pow(me.getY() - centerY, 2));
        float ratio;
        float constrainedX;
        float constrainedY;
        if (v.equals(this)){
            if (me.getAction() != me.ACTION_UP) {
                if (displacement < Radius) {
                    drawJoystick(me.getX(), me.getY());
                    joystickCallback.onJoystickMoved((me.getX() - centerX) / Radius, (me.getY() - centerY) / Radius, getId());
                }
                else {
                    ratio = Radius / displacement;
                    constrainedX = centerX + (me.getX() - centerX) * ratio;
                    constrainedY = centerY + (me.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / Radius, (constrainedY - centerY) / Radius, getId());
                }
            }
            else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
        }
        return true;
    }
}
