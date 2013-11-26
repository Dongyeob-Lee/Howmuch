package com.nero.howmuch.image;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ImageEditView extends View {
	private ArrayList<Vertex> drawList;
	private Paint curPaint;
	// test
	boolean isBack =false;
	private Bitmap test_bitmap;
	private Rect test_bitmap_rect;
	public ImageEditView(Context context) {
		super(context);
		drawList = new ArrayList<ImageEditView.Vertex>();
		curPaint = new Paint();
		curPaint.setColor(Color.BLACK);
		curPaint.setStrokeWidth(2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			drawList.add(new Vertex(event.getX(),event.getY(),false, curPaint));
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			drawList.add(new Vertex(event.getX(),event.getY(),true,curPaint));
			invalidate();
			return true;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
			canvas.drawBitmap(test_bitmap, null, test_bitmap_rect, null);
		
	
		
		for(int i=1; i<drawList.size(); i++){
			if(drawList.get(i).draw){
				canvas.drawLine(drawList.get(i-1).x, drawList.get(i-1).y, drawList.get(i).x, drawList.get(i).y, drawList.get(i).paint);	
			}
		}
		Log.d("image edit", "onDraw");		
	}

	public Bitmap getTest_bitmap() {
		return test_bitmap;
	}

	public void setTest_bitmap(Bitmap test_bitmap) {
		this.test_bitmap = test_bitmap;
	}

	public Rect getTest_bitmap_rect() {
		return test_bitmap_rect;
	}

	public void setTest_bitmap_rect(Rect test_bitmap_rect) {
		this.test_bitmap_rect = test_bitmap_rect;
	}
	public class Vertex {
		float x, y;
		boolean draw;
		Paint paint;
		public Vertex(float x, float y, boolean draw, Paint paint){
			this.x = x;
			this.y = y;
			this.draw = draw;
			this.paint = paint;
		}
	}
	public Paint getCurPaint() {
		return curPaint;
	}

	public void setCurPaint(Paint curPaint) {
		this.curPaint = curPaint;
	}
	
}
