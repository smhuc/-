package com.wangtian.message.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.wangtian.message.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GestureLockView extends View {
	private Paint paintCenter;
	private Context context;
	private Paint paintNormal;
	private Paint paintOnTouch;
	private Paint paintInnerCycle;
	private Paint paintLines;
	private Paint paintKeyError;
	private MyCircle[] cycles;
	private Path linePath = new Path();
	private List<Integer> linedCycles = new ArrayList<Integer>();
	private OnGestureFinishListener onGestureFinishListener;
	private String key;
	private int eventX, eventY;
	private boolean canContinue = true;
	private boolean result;
	private Timer timer;
	private int flag = 1; //1为设置，2为解锁
	private boolean Initnum = false;

	private final int CENTER_CYCLE_NORMAL = Color.argb(0,237, 237, 237); // 正常中心圆圆颜色

	//private final int OUT_CYCLE_NORMAL = Color.rgb(108, 119, 138); // 正常外圆颜色
//	private final int OUT_CYCLE_NORMAL = Color.rgb(197, 197, 197); // 正常外圆颜色
	//private final int OUT_CYCLE_ONTOUCH = Color.rgb(025, 066, 103); // 选中外圆颜色
//	private final int OUT_CYCLE_ONTOUCH = Color.rgb(96, 93, 84); // 选中外圆颜色
	//private final int INNER_CYCLE_TOUCHED = Color.rgb(002, 210, 255); // 选中内圆颜色
//	private final int INNER_CYCLE_TOUCHED = Color.rgb(205, 10, 27); // 选中内圆颜色
	//private final int INNER_CYCLE_NOTOUCH = Color.rgb(100, 100, 100); // 未选中内圆颜色
//	private final int INNER_CYCLE_NOTOUCH = Color.argb(0,197, 197, 197); // 未选中内圆颜色
	//private final int LINE_COLOR = Color.argb(127, 002, 210, 255); // 连接线颜色
	private final int LINE_COLOR = Color.argb(125,0, 0, 255); // 连接线颜色
//	private final int OUT_ERROR_COLOR = Color.argb(197, 197, 197, 197); // 连接错误醒目提示颜色
//	private final int ERROR_COLOR = Color.argb(255, 205, 10, 27); // 连接错误醒目提示颜色
	private final int ERROR_COLOR = Color.argb(125,0, 0,255); // 连接错误醒目提示颜色
//	private final int INNER_CYCLE_ERROR_COLOR = Color.rgb(205, 10, 27); // 连接错误时内圆颜色

	private Bitmap outbitmap,bitmap; 
	
	public void setOnGestureFinishListener(
			OnGestureFinishListener onGestureFinishListener,int flags) {
		this.onGestureFinishListener = onGestureFinishListener;
		this.flag = flags;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public interface OnGestureFinishListener {
		public void OnGestureFinish(String success);
	}

	public GestureLockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public GestureLockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		Initnum = true;
		init();
	}

	public GestureLockView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int perSize = 0;
		if (cycles == null && (perSize = getWidth() / 6) > 0) {
			cycles = new MyCircle[9]; 
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					MyCircle cycle = new MyCircle();
					cycle.setNum(i * 3 + j);
					cycle.setOx(perSize * (j * 2 + 1));
					cycle.setOy(perSize * (i * 2 + 1));
					cycle.setR(perSize * 0.5f);
					cycles[i * 3 + j] = cycle;
				}
			}
		}
	}

	private void init() {
		outbitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.unlock_point_bottom);
		bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.unlock_point_top);
		paintNormal = new Paint();
		paintNormal.setAntiAlias(true);
		paintNormal.setStrokeWidth(5);
		paintNormal.setStyle(Paint.Style.STROKE);

		paintOnTouch = new Paint();
		paintOnTouch.setAntiAlias(true);
		paintOnTouch.setStrokeWidth(10);
		paintOnTouch.setStyle(Paint.Style.STROKE);
		

		paintCenter = new Paint();
		paintCenter.setAntiAlias(true);
		paintCenter.setStyle(Paint.Style.FILL);
		
		paintInnerCycle = new Paint();
		paintInnerCycle.setAntiAlias(true);
		paintInnerCycle.setStyle(Paint.Style.FILL);
		
		paintLines = new Paint();
		paintLines.setAntiAlias(true);
		paintLines.setStyle(Paint.Style.STROKE);
		paintLines.setStrokeWidth(25);

		paintKeyError = new Paint();
		paintKeyError.setAntiAlias(true);
		paintKeyError.setStyle(Paint.Style.STROKE);
		paintKeyError.setStrokeWidth(3);
	}

	private boolean onTouch = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(Initnum){
			eventX = 133;
			eventY = 125;
			for (int i = 0; i < cycles.length; i++) {
				if (cycles[i].isPointIn(eventX, eventY)) {
					cycles[i].setOnTouch(true);
					if (!linedCycles.contains(cycles[i].getNum())) {
						linedCycles.add(cycles[i].getNum());
					}
				}
			}
			
			
			cycles[0].setOnTouch(false);
			
			linedCycles.clear();
			linePath.reset();
			canContinue = true;
			postInvalidate();
			Initnum = false;
		}
		for (int i = 0; i < cycles.length; i++) {
			canvas.drawBitmap(outbitmap,cycles[i].getOx()-outbitmap.getWidth()/2, cycles[i].getOy()-outbitmap.getHeight()/2,
					 paintNormal);
			if (!canContinue && !result) {
				paintLines.setColor(ERROR_COLOR);
			} else if (cycles[i].isOnTouch()) {
				paintLines.setColor(LINE_COLOR);
			} else {
				paintLines.setColor(LINE_COLOR);
			}
			if (cycles[i].isOnTouch()) {
				if (canContinue || result) {
					canvas.drawBitmap(bitmap,cycles[i].getOx()-bitmap.getWidth()/2, cycles[i].getOy()-bitmap.getHeight()/2,paintInnerCycle);
				}else {
					canvas.drawBitmap(bitmap,cycles[i].getOx()-bitmap.getWidth()/2, cycles[i].getOy()-bitmap.getHeight()/2,paintInnerCycle);
				}
				canvas.drawBitmap(bitmap,cycles[i].getOx()-bitmap.getWidth()/2, cycles[i].getOy()-bitmap.getHeight()/2,paintCenter);
				
			} else {
				paintCenter.setColor(CENTER_CYCLE_NORMAL);
				canvas.drawCircle(cycles[i].getOx(), cycles[i].getOy(), cycles[i].getR() / 1f,
						paintCenter);
			}
			if(cycles[i].isOnTouch()){
				onTouch = true;
			}
		}
		if(onTouch){
			for (int i = 0; i < cycles.length; i++) {
				canvas.drawBitmap(bitmap,cycles[i].getOx()-bitmap.getWidth()/2, cycles[i].getOy()-bitmap.getHeight()/2,paintInnerCycle);
			}
			onTouch = false;
		}
		drawLine(canvas);

	}

	private void drawLine(Canvas canvas) {

		linePath.reset();
		if (linedCycles.size() > 0) {
			for (int i = 0; i < linedCycles.size(); i++) {
				int index = linedCycles.get(i);
				float x = cycles[index].getOx();
				float y = cycles[index].getOy();
				if (i == 0) {
					linePath.moveTo(x,y);
				} else {
					linePath.lineTo(x,y);
				}
			}
			if (canContinue) {
				linePath.lineTo(eventX, eventY);
			}else {
				linePath.lineTo(cycles[linedCycles.get(linedCycles.size()-1)].getOx(), cycles[linedCycles.get(linedCycles.size()-1)].getOy());
			}
			canvas.drawPath(linePath, paintLines);
			
		}

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canContinue) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE: {
				eventX = (int) event.getX();
				eventY = (int) event.getY();
				for (int i = 0; i < cycles.length; i++) {
					if (cycles[i].isPointIn(eventX, eventY)) {
						cycles[i].setOnTouch(true);
						if (!linedCycles.contains(cycles[i].getNum())) {
							linedCycles.add(cycles[i].getNum());
						}
					}
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				// 暂停触碰
				canContinue = false;
				// 检查结果
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < linedCycles.size(); i++) {
					sb.append(linedCycles.get(i));
				}
				if(flag == 1){
					onGestureFinishListener.OnGestureFinish(sb.toString());
				}else{
				//判断是否解锁成功
					//result = key.equals(sb.toString());
					if (onGestureFinishListener != null) {
						onGestureFinishListener.OnGestureFinish(sb.toString());
					}
					///**  自动 消除手势图案
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							// 还原
							eventX = eventY = 0;
							for (int i = 0; i < cycles.length; i++) {
								cycles[i].setOnTouch(false);
							}
							linedCycles.clear();
							linePath.reset();
							canContinue = true;
							postInvalidate();
						}
					}, 1000);
					//*/
				}
				break;
			}
			}
			invalidate();
		}
		return true;
	}
	

	public void cancel(){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// 还原
				eventX = eventY = 0;
				for (int i = 0; i < cycles.length; i++) {
					cycles[i].setOnTouch(false);
				}
				linedCycles.clear();
				linePath.reset();
				canContinue = true;
				postInvalidate();
			}
		}, 10);
	}
}
