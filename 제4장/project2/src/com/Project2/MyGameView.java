package com.Project2;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {
	GameThread mThread;
	SurfaceHolder mHolder;

	//-------------------------------------
	//  ������
	//-------------------------------------
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mHolder = holder;
		mThread = new GameThread(holder, context); 

		setFocusable(true);		// View�� ��Ŀ���� ���� �� �ֵ��� ����
	}

	//-------------------------------------
    //  SurfaceView�� ������ �� ����Ǵ� �κ�
    //-------------------------------------
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread.start();
	}

	//-------------------------------------
    //  SurfaceView�� �ٲ� �� ����Ǵ� �κ�
    //-------------------------------------
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {

	}

	//-------------------------------------
    //  SurfaceView�� ������ �� ����Ǵ� �κ�
    //-------------------------------------
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean done = true;
	    while (done) {
	    	try {
	    		mThread.join();        // �����尡 ���� step�� ���� �� ���� ���
	    		done = false;
	 		} catch (InterruptedException e) {  // ���ͷ�Ʈ ��ȣ�� ����?   
	 			// �� ��ȣ ���� - �ƹ��͵� ����
	 		} 
	    } // while
	}
	
//-------------------------------------------------------------------
	
	//-------------------------------------
	//  GameThread Class
	//-------------------------------------
	class GameThread extends Thread {
		SurfaceHolder mHolder;		// SurfaceHolder ����

		int width, height;
		int x, y, dw, dh;			// ���� ��ǥ, ũ��
		int sx, sy;					// ���� ����� �ӵ�
		int num;					// �̹��� ��ȣ
		int x1, y1;					// ������ Touch �� ��ġ
		Bitmap imgBack;				// ����̹���
		Bitmap Dragon[] = new Bitmap[2];   // �� - �¿� �̹���
		
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;	// SurfaceHolder ����
			Display display = ((WindowManager) context.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight() - 50;   // View�� ���� ����

			// ��� �а� �ø���
			imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back_1);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);

			Dragon[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dragon);
			dw = Dragon[0].getWidth() / 2;		// ���� ��
			dh = Dragon[0].getHeight() / 2;		// ����
			
			Matrix matrix = new Matrix();		// �̹����� ������ ���� Matrix ����
			matrix.postScale(-1, 1);			// �������� ������
			Dragon[1] = Bitmap.createBitmap(Dragon[0], 0, 0, dw * 2, dh * 2, matrix, false);
			
			//���� �ʱⰪ ����
			num = 0;			// �̹��� ��ȣ
			sx = -2;
			sy = 3;
			x = 100;
			y = 100;
		}
		
		//-------------------------------------
		//  �� �����̱� 
		//-------------------------------------
		public void MoveDragon() {
			x += sx;
			y += sy;
			if (x <= dw || x >= width - dw) {
				sx = -sx;
				num = 1 - num;		// 0, 1, 0, 1, ...
			}
			if (y <= dh || y >= height - dh) {
				sy = -sy;
			}	
		}
		
		//-------------------------------------
		//  Canvas�� �׸��� 
		//-------------------------------------
		public void run() {
			Canvas canvas = null; 					// canvas�� �����
			while (true) {
				canvas = mHolder.lockCanvas();		// canvas�� ��װ� ���� �Ҵ�
				try {
					synchronized (mHolder) {		// ����ȭ ����
						MoveDragon();							// �� �����̱�
						canvas.drawBitmap(imgBack, 0, 0, null);	// ���
						canvas.drawBitmap(Dragon[num], x - dw, y - dh, null); // ��
					}
				} finally {							// ���� �۾��� ������ 
					if (canvas != null)				// ������ ������ View�� ����
						mHolder.unlockCanvasAndPost(canvas);     
				}
			} // while
		} // run
	} // GameThread ��

//----------------------------------------------------------
	
	//-------------------------------------
	//  onTouch Event 
	//-------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mThread.x1 = (int) event.getX();
			mThread.y1 = (int) event.getY();
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int x2 = (int) event.getX();
			int y2 = (int) event.getY();
			
			mThread.sx = (x2 - mThread.x1) / 10;
			mThread.sy = (y2 - mThread.y1) / 10;
			if (mThread.x1 < x2) 
				mThread.num = 1;
			else
				mThread.num = 0;
		}
		return true;
	} // TouchEvent 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.exit(0);
		return true;
	}

} // SurfaceView 
