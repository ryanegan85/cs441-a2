package edu.binghamton.cs.cs441a2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PongView extends SurfaceView implements Runnable {
    Thread mGameThread = null;

    SurfaceHolder mOurHolder;

    volatile boolean mPlaying;

    boolean mPaused = true;

    Canvas mCanvas;
    Paint mPaint;

    long mFPS;

    int mScreenX;
    int mScreenY;

    Paddle mPaddle;

    Ball mBall;

    /*
    SoundPool sp;
    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    */

    int mScore = 0;

    int mLives = 3;

    public PongView(Context context, int x, int y) {
        super(context);

        mScreenX = x;
        mScreenY = y;

        mOurHolder = getHolder();
        mPaint = new Paint();

        mPaddle = new Paddle(mScreenX, mScreenY);

        mBall = new Ball(mScreenX, mScreenY);

        setupAndRestart();
    }

    public void setupAndRestart() {
        mBall.reset(mScreenX, mScreenY);

        if(mLives == 0) {
            mScore = 0;
            mLives = 3;
        }
    }

    @Override
    public void run() {
        while(mPlaying) {
            long startFrameTime = System.currentTimeMillis();

            if(!mPaused) {
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }

    public void update() {
        mPaddle.update(mFPS);
        mBall.update(mFPS);

        if(RectF.intersects(mPaddle.getmRect(), mBall.getmRect())) {
            mBall.setRandomXVelocity();
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mPaddle.getmRect().top - 2);

            mScore++;
            mBall.increaseVelocity();
        }
        if (mBall.getmRect().bottom > mScreenY) {
            mBall.reverseYVelocity();
            mBall.clearObstacleY(mScreenY - 2);

            mLives--;

            if(mLives == 0) {
                mPaused = true;
                setupAndRestart();
            }
        }
        if(mBall.getmRect().top < 0) {
            mBall.reverseYVelocity();
            mBall.clearObstacleY(12);
        }

        if(mBall.getmRect().left < 0) {
            mBall.reverseXVelocity();
            mBall.clearObstacleX(2);
        }

        if(mBall.getmRect().right > mScreenX) {
            mBall.reverseXVelocity();
            mBall.clearObstacleX(mScreenX - 22);
        }
    }

    public void draw() {
        if(mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas();

            mCanvas.drawColor(Color.argb(255, 120, 197, 87));

            mPaint.setColor(Color.argb(255, 255, 255, 255));

            mCanvas.drawRect(mPaddle.getmRect(), mPaint);

            mCanvas.drawRect(mBall.getmRect(), mPaint);


            mPaint.setColor(Color.argb(255, 255, 255, 255));

            mPaint.setTextSize(40);
            mCanvas.drawText("Score: " + mScore + "   Lives: " + mLives, 10, 50, mPaint);

            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void pause() {
        mPlaying = false;
        try {
            mGameThread.join();
        } catch(InterruptedException e) {
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
        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mPaused = false;

                if(motionEvent.getX() > mScreenX / 2) {
                    mPaddle.setMovementState(mPaddle.RIGHT);
                } else {
                    mPaddle.setMovementState(mPaddle.LEFT);
                }
                break;

            case MotionEvent.ACTION_UP:

                mPaddle.setMovementState(mPaddle.STOPPED);
                break;
        }
        return true;
    }
}
