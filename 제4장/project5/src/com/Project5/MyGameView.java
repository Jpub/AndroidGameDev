package com.Project5;

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
		Bitmap imgBack;
		ArrayList<Bubble> mBall = new ArrayList<Bubble>();			// ū���
		ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();	// �������
		
		boolean canRun = true;
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
		}
		
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
		
		//-------------------------------------
		//  �񴰹�� �����  - Touch Event���� ȣ��
		//-------------------------------------
		public void MakeBubble(int x, int y) {
			boolean flag = false;
			for (Bubble tmp :  mBall) {
				if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2) 
						<= Math.pow(tmp.rad, 2)){
					tmp.dead = true;	// �񴰹�� Touch�� ��� 
					flag = true;		
				}
			}
			if (flag == false)	 // �񴰹�� Touch�� �ƴϸ� �񴰹�� ���� 
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
		//  �񴰹�� �̵�  - run���� ȣ��
		//-------------------------------------
		public void MoveBubble() {
			// ū �񴰹�� �̵�
			for (int i = mBall.size() - 1; i >= 0; i--) {
				mBall.get(i).MoveBubble();
				if (mBall.get(i).dead == true) {
					// ���� �񴰹���� ����� ū ���� �Ͷ߸�
					MakeSmallBall(mBall.get(i).x, mBall.get(i).y);	// ���� ���
					mBall.remove(i);
				}
			}

			// ���� �񴰹�� �̵�
			for (int i = sBall.size() - 1; i >= 0; i--) {
				sBall.get(i).MoveBall();
				if (sBall.get(i).dead == true)
					sBall.remove(i);
			}
		}
		
		//-------------------------------------
		//  Canvas�� �׸���
		//-------------------------------------
		public void run() {
			Canvas canvas = null; 					// canvas�� �����
			while (canRun) {
				canvas = mHolder.lockCanvas();		// canvas�� ��װ� ���� �Ҵ�
				try {
					synchronized (mHolder) {		// ����ȭ ����
						MoveBubble();
						canvas.drawBitmap(imgBack, 0, 0, null);

						// ū �񴰹��
						for (Bubble tmp : mBall) {
							canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
						}	
						// �����񴰹��
						for (SmallBall tmp : sBall) {
							canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
						}	
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
	
	} // GameThread ��
	
	//-------------------------------------
	//  onTouch Event 
	//-------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mHolder) {		
				int x = (int) event.getX(); 
				int y = (int) event.getY();
				mThread.MakeBubble(x, y);
			}
		}
		return true;
	}
	
} // SurfaceView 
