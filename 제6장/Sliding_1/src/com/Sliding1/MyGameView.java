package com.Sliding1;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {

	GameThread 	  mThread;		// GameThread
	SurfaceHolder mHolder;		// SurfaceHolder 
	Context 	  mContext;		// Context 

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
	    		mThread.join();        			// �����尡 ���� step�� ���� �� ���� ���
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
		boolean canRun = true;			// ������ �����
		boolean isWait = false;
			
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			
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
						
					}
				} finally {							// ���� �۾��� ������ 
					if (canvas != null)				// ������ ������ View�� ����
						mHolder.unlockCanvasAndPost(canvas);
				} // try

				// ������ �Ͻ� ���� 
				synchronized (this) {
            		if (isWait)				// Pause ����̸�
            			try {
            				wait();			// ������ ���
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
} // SurfaceView 
