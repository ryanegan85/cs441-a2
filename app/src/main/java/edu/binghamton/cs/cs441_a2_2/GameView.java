package edu.binghamton.cs.cs441_a2_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class GameView extends SurfaceView implements Runnable {

    Thread mGameThread = null;

    SurfaceHolder mOurHolder;

    volatile boolean mPlaying;

    boolean mPaused = true;

    Canvas mCanvas;
    Paint mPaint;

    long mFPS;

    int mScreenX;
    int mScreenY;

    Block mBlock;

    int mScore = 0;

    public GameView(Context context, int x, int y) {
        super(context);

        mScreenX = x;
        mScreenY = y;

        mOurHolder = getHolder();
        mPaint = new Paint();

        mBlock = new Block(mScreenX, mScreenY);

        setupAndRestart();
    }

    public void setupAndRestart() {
        mBlock.reset(mScreenX, mScreenY);
    }

    @Override
    public void run() {
        mScore = 0;
        while(mPlaying) {
            long startFrameTime = System.currentTimeMillis();

            if(!mPaused){
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        mBlock.update(mFPS);

        //Adds score if the block hits a corner before doing any bounces
        if((mBlock.getRect().bottom > mScreenY) && (mBlock.getRect().left < 0)) mScore++;
        if((mBlock.getRect().bottom > mScreenY) && (mBlock.getRect().right > mScreenX)) mScore++;
        if((mBlock.getRect().top < 0) && (mBlock.getRect().left < 0)) mScore++;
        if((mBlock.getRect().top < 0) && (mBlock.getRect().right > mScreenX)) mScore++;

        //Bounce block when it hits screen bottom
        if(mBlock.getRect().bottom > mScreenY) {
            int temp = (int)mBlock.getmBlockWidth();
            mBlock.reverseYVelocity();
            //mBlock.clearObstacleY(mScreenY - 3);
            mBlock.setRectBottom(mScreenY - 1);
            mBlock.setRectTop(mScreenY - 1 - temp);
        }

        //Bounce block when it hits screen top
        if(mBlock.getRect().top < 0) {
            int temp = (int)mBlock.getmBlockWidth();
            mBlock.reverseYVelocity();
            //mBlock.clearObstacleY(mBlock.getmBlockWidth() + 3);
            mBlock.setRectTop(1);
            mBlock.setRectBottom(1 + temp);
        }

        //Bounce block when it hits screen left
        if(mBlock.getRect().left < 0){
            mBlock.reverseXVelocity();
            mBlock.clearObstacleX(2);
        }

        //Bounce block when it hits screen right
        if(mBlock.getRect().right > mScreenX){
            mBlock.reverseXVelocity();
            mBlock.clearObstacleX(mScreenX - mBlock.getmBlockWidth() - 2);
        }
    }

    public void draw() {
        if(mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas();

            mCanvas.drawColor(Color.argb(255, 120, 197, 87));

            mPaint.setColor(Color.argb(255, 255, 255, 255));

            mCanvas.drawRect(mBlock.getRect(), mPaint);

            mPaint.setColor(Color.argb(255, 255, 255, 255));

            mPaint.setTextSize(40);
            mCanvas.drawText("Score: " + mScore, 10, 50, mPaint);

            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        mPlaying = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                mPaused = false;
                break;
        }
        return true;
    }
}
