package com.Project1;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {
	GameThread mThread;
	SurfaceHolder mHolder;
	Context mContext;

	final int LEFT = 1;		// �Ź� �̵� ����
	final int RIGHT = 2;
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mHolder = holder;		// holder�� Context ����
		mContext = context;
		mThread = new GameThread(holder, context);
		
		setFocusable(true);
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
	
	//-------------------------------------
	//  ������ ���� ����
	//-------------------------------------
	public void StopGame() {
		mThread.StopThread(); 
	}

	//-------------------------------------
	//  ������ �Ͻ� ����
	//-------------------------------------
	public void PauseGame() {
		mThread.PauseNResume(true); 
	}

	//-------------------------------------
	//  ������ ��⵿
	//-------------------------------------
	public void ResumeGame() {
		mThread.PauseNResume(false); 
	}

	//-------------------------------------
	//  ���� �ʱ�ȭ
	//-------------------------------------
	public void RestartGame() {
		mThread.StopThread();		// ������ ����

		// ������ �����带 ���� �ٽ� ����
	    mThread = null;	  
		mThread = new GameThread(mHolder, mContext); 
		mThread.start(); 
	}

//----------------------------------------------------------------
	
	//-------------------------------------
	//  GameThread Class
	//-------------------------------------
	class GameThread extends Thread {
		SurfaceHolder mHolder;		// SurfaceHolder�� ������ ����
		Context mContext;
		
		int width, height;
		Bitmap imgBack;			// ���
		Bitmap imgSpider;		// �Ź�
		int sw, sh;				// �Ź��� ũ��
		int mx, my;				// �Ź��� ��ǥ
		long lastTime;			// �ð� ����
		
		ArrayList<Bubble> mBall = new ArrayList<Bubble>();			// ū���
		ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();	// �������
		ArrayList<WaterBall> wBall = new ArrayList<WaterBall>();	// �Ź� �Ѿ�
		
		boolean canRun = true;			// ������ �����
		boolean isWait = false;
		  
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;	// SurfaceHolder ����
			mContext = context;
			
			Display display = ((WindowManager) context.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight() - 50;   // View�� ���� ����

			imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);

			imgSpider = BitmapFactory.decodeResource(getResources(), R.drawable.spider1);
			sw = imgSpider.getWidth() / 2;
			sh = imgSpider.getHeight() / 2;
			
			mx = width / 2;
			my = height - 45;
		}
		
		//-------------------------------------
		//  ū �񴰹�� ����� 
		//-------------------------------------
		public void MakeBubble() {
			Random rnd = new Random();
			if (mBall.size() > 9 || rnd.nextInt(40) < 38) return;
			int x = -50;  
			int y = rnd.nextInt(201) + 50;	// 50~250 
			mBall.add(new Bubble(mContext, x, y, width, height));
		}	
			
		//-------------------------------------
		//  ����  �񴰹�� �����
		//-------------------------------------
		private void MakeSmallBall(int x, int y) {
			Random rnd = new Random();
			int count = rnd.nextInt(9) + 7;   // 7~15��
			for (int i = 1; i <= count; i++) {
				int ang = rnd.nextInt(360);		 
				sBall.add(new SmallBall(mContext, x, y, ang, width, height));
			}
		}

		//-------------------------------------
		//  ��� ĳ���� �̵�  
		//-------------------------------------
		public void MoveCharacters() {
			// ū �񴰹��
			for (int i = mBall.size() - 1; i >= 0; i--) {
				mBall.get(i).MoveBubble();
				if (mBall.get(i).dead == true) mBall.remove(i);  
			}	

			// ���� �񴰹��
			for (int i = sBall.size() - 1; i >= 0; i--) {
				sBall.get(i).MoveBall();
				if (sBall.get(i).dead == true)
					sBall.remove(i);
			}	
			// �Ź� �Ѿ�
			for (int i = wBall.size() - 1; i >= 0; i--) {
				wBall.get(i).MoveBall();
				if (wBall.get(i).dead == true)
					wBall.remove(i);
			}	
		}

		//-------------------------------------
		//  �Ź� �̵� - KeyDown���� ȣ��
		//-------------------------------------
		private void MoveSpider(int n) {
			int sx = 4;
			if (n == LEFT) sx = -4; 
			mx += sx;
			if (mx < sw) mx = sw;					// ���� �� 
			if (mx > width - sw) mx = width - sw;	// ������ �� 
		}

		//-------------------------------------
		//  �Ź� �Ѿ� �߻� - KeyDown���� ȣ��
		//-------------------------------------
		private void MakeWaterBall() {
			long thisTime = System.currentTimeMillis();
			if (thisTime - lastTime >= 300)		// 1/3�ʿ� 1���� �߻�
				wBall.add(new WaterBall(mContext, mx, my - 20, width, height));
			lastTime = thisTime;
		}

		//-------------------------------------
		//  �浹 ����
		//-------------------------------------
		public void CheckCollision() {
			int x1, y1, x2, y2;

			// �Ѿ˰� �񴰹���� �浹
			for (WaterBall water : wBall) {	// �Ѿ�
				x1 = water.x;
				y1 = water.y;
				for (Bubble tmp : mBall) {	// �񴰹�� 
					x2 = tmp.x;
					y2 = tmp.y;
					if (Math.abs(x1 - x2) < tmp.rad && Math.abs(y1 - y2) < tmp.rad) {
						MakeSmallBall(tmp.x, tmp.y);	// ���� ǳ�� �����
						tmp.dead = true;				// ūǳ�� �Ͷ߸�
						water.dead = true;				// �Ѿ˵� ���ְ�
						break;
					}
				} // for
			} // for	
		}

		//-------------------------------------
		//  Canvas�� �׸���
		//-------------------------------------
		public void DrawCharacters(Canvas canvas) {
			canvas.drawBitmap(imgBack, 0, 0, null);		// ���
			// ū �񴰹��
			for (Bubble tmp : mBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			// �����񴰹��
			for (SmallBall tmp : sBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			// �Ź� �����
			for (WaterBall tmp : wBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			// �Ź�
			canvas.drawBitmap(imgSpider, mx - sw, my - sh, null);    
		}
		
		//-------------------------------------
		//  ������ ��ü
		//-------------------------------------
		public void run() {
			Canvas canvas = null; 					// canvas�� �����
			while (canRun) {
				canvas = mHolder.lockCanvas();		// canvas�� ��װ� ���� �Ҵ�
				try {
					synchronized (mHolder) {		// ����ȭ ����
						MakeBubble();
						MoveCharacters();
						CheckCollision();
						DrawCharacters(canvas);
					}
				} finally {							// ���� �۾��� ������ 
					if (canvas != null)				// ������ ������ View�� ����
						mHolder.unlockCanvasAndPost(canvas);
				} // try

				// ������ �Ͻ� ���� 
				synchronized (this) {
            		if (isWait)
            			try {
            				wait();
            			} catch (Exception e) {
							// nothing
						}
    			} // sync
				
			} // while
		} // run
	
		//-------------------------------------
		//  ������ ���� ����
		//-------------------------------------
		public void StopThread() {
			canRun = false;
        	synchronized (this) {
        		this.notify();
			}
		}
		
		//-------------------------------------
		//  ������ �Ͻ����� / ��⵿
		//-------------------------------------
		public void PauseNResume(boolean wait) { 
			isWait = wait;
        	synchronized (this) {
        		this.notify();
			}
		}
		
	} // GameThread ��

	//-------------------------------------
	//  onKeyDown
	//-------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		synchronized (mHolder) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT :
					mThread.MoveSpider(LEFT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT : 
					mThread.MoveSpider(RIGHT);
					break;
				case KeyEvent.KEYCODE_DPAD_UP : 
					mThread.MakeWaterBall();
				}
			}		
			return super.onKeyDown(keyCode, event);
		} // sync
	} // onKeyDown 
} // SurfaceView 
