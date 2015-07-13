package com.Project_1;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {

	//-------------------------------------
	//  ������
	//-------------------------------------
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//-------------------------------------
    //  SurfaceView�� ������ �� ����Ǵ� �κ�
    //-------------------------------------
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

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

	}
	
//------------------------------------------------------
	
	//-------------------------------------
	//  GameThread Class
	//-------------------------------------
	class GameThread extends Thread {
		SurfaceHolder mHolder;		// SurfaceHolder�� ������ ���� 
		  
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;	// SurfaceHolder ����
		}
		
		//-------------------------------------
		//  run Callback �Լ� 
		//-------------------------------------
		public void run() {
			Canvas canvas = null; 					// canvas�� �����
			while (true) {
				canvas = mHolder.lockCanvas();		// canvas�� ��װ� ���� �Ҵ�
				try {
					synchronized (mHolder) {		// ����ȭ ����
						// ���� �׷��� ó�� �κ�
					}
				} finally {							// ���� �۾��� ������ 
					if (canvas != null)				// ������ ������ View�� ����
						mHolder.unlockCanvasAndPost(canvas);     
				}
			} // while
		} // run
	} // GameThread ��
	
} // SurfaceView 
