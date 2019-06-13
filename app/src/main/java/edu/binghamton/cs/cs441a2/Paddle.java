package edu.binghamton.cs.cs441a2;

import android.graphics.RectF;

public class Paddle {

    private RectF mRect;

    private float mLength;
    private float mHeight;

    private float mXCoord;
    private float mYCoord;

    private float mPaddleSpeed;

    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    private int mPaddleMoving = STOPPED;

    private int mScreenX;
    private int mScreenY;

    public Paddle (int x, int y) {
        mScreenX = x;
        mScreenY = y;

        mLength = mScreenX / 8;

        mHeight = mScreenY / 25;

        mXCoord = mScreenX / 2;
        mYCoord = mScreenY - 20;

        mRect = new RectF(mXCoord, mYCoord, mXCoord + mLength, mYCoord + mHeight);

        mPaddleSpeed = mScreenX;
    }

    public RectF getmRect () {
        return mRect;
    }

    public void setMovementState(int state) {
        mPaddleMoving = state;
    }

    public void update(long fps) {
        if(mPaddleMoving == LEFT) {
            mXCoord = mXCoord - mPaddleSpeed / fps;
        }

        if(mPaddleMoving == RIGHT) {
            mXCoord = mXCoord + mPaddleSpeed / fps;
        }

        if(mRect.left < 0) {
            mXCoord = 0;
        }
        if(mRect.right > mScreenX) {
            mXCoord = mScreenX - (mRect.right - mRect.left);
        }

        mRect.left = mXCoord;
        mRect.right = mXCoord + mLength;
    }
}
