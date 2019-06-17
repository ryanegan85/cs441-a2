package edu.binghamton.cs.cs441_a2_2;

import android.graphics.RectF;

import java.util.Random;

public class Block {

    private RectF mRect;
    private float mXVelocity;
    private float mYVelocity;
    private float mBlockWidth;
    private float mBlockHeight;

    public Block(int screenX, int screenY) {
        mBlockWidth = screenX / 25;
        mBlockHeight = screenX / 25;

        mYVelocity = screenY / 2;
        mXVelocity = screenX / 3;

        mRect = new RectF();
    }

    public RectF getRect() {
        return mRect;
    }

    public float getmBlockWidth() {
        return mBlockWidth;
    }

    public void update(long fps) {
        mRect.left = mRect.left + (mXVelocity / fps);
        mRect.top = mRect.top + (mYVelocity / fps);
        mRect.right = mRect.left + mBlockWidth;
        mRect.bottom = mRect.top - mBlockHeight;
    }

    public void reverseYVelocity() {
        mYVelocity = -mYVelocity;
    }

    public void reverseXVelocity() {
        mXVelocity = -mXVelocity;
    }

    public void setRandomXVelocity() {
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0) {
            reverseXVelocity();
        }
    }

    public void increaseVelocity() {
        mXVelocity = mXVelocity + mXVelocity / 10;
        mYVelocity = mYVelocity + mYVelocity / 10;
    }

    public void clearObstacleY(float y) {
        mRect.bottom = y;
        mRect.top = y - mBlockHeight;
    }

    public void clearObstacleX(float x) {
        mRect.left = x;
        mRect.right = x + mBlockWidth;
    }

    public void reset(int x, int y) {
        mRect.left = x / 2;
        mRect.top = y / 2;
        mRect.right = mRect.left + mBlockWidth;
        mRect.bottom = mRect.top - mBlockHeight;
    }

    public void setRectBottom(int x) {
        mRect.bottom = x;
    }

    public void setRectTop(int x) {
        mRect.top = x;
    }

    public void setRectLeft(int x) {
        mRect.left = x;
    }

    public void setRectRight(int x) {
        mRect.right = x;
    }
}
