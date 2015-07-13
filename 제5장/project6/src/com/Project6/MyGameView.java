package com.Project6;

import java.util.*;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {
	GameThread 	  mThread;
	SurfaceHolder mHolder;
	Context 	  mContext;

	final int LEFT = 1;
	final int RIGHT = 2;
	final int STOP = 3;
	
	static int B_width;		// ����� ��
	static int B_height;	// ����� ����
	static int M_left;		// ���� ����
	static int M_top;		// ��� ����
	
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
		SurfaceHolder mHolder;			// SurfaceHolder�� ������ ����
		Context mContext;
		boolean canRun = true;			// ������ �����
		boolean isWait = false;
		int width, height;				// ȭ���� ũ��
		
		final int STAGE_COUNTER = 4;	// ��ü �������� ����
		int ballCnt = 4;				// ���� ����(0~5)
		int stgNum = 0;					// �������� ��ȣ
		int tot = 0;					// ���� �հ�
		int score = 0;					// ������ ��� ����
		int sx[] = {-3, -2, 2, 3};		// �е鿡 �浹 ���� ���� �ݻ� ������ �ٲ�
		Random rnd = new Random();		// ���� ó���� �ϱ� ���� ����
		Paint paint = new Paint();		// ���� ǥ�ÿ�
		Bitmap imgBack;					// ���
		Bitmap imgSball;				// ���� �� ���� ǥ�ÿ�
		
		ArrayList<Block> mBlock = new ArrayList<Block>();	// ���
		Ball   mBall;		// ��
		Paddle mPaddle;		// �е�							
		  
		float Stage[][][] = {		// x, y, ��Ϲ�ȣ
		{ 	
			{0, 0, 0}, {1, 0, 0}, {2, 0, 0}, {3, 0, 0}, {4, 0, 0},
			{0, 1, 0}, {1, 1, 1}, {2, 1, 1}, {3, 1, 1}, {4, 1, 0},
			{0, 2, 0}, {1, 2, 1}, {2, 2, 2}, {3, 2, 1}, {4, 2, 0},
			{0, 3, 0}, {1, 3, 1}, {2, 3, 2}, {3, 3, 1}, {4, 3, 0},
			{0, 4, 0}, {1, 4, 1}, {2, 4, 1}, {3, 4, 1}, {4, 4, 0},
			{0, 5, 0}, {1, 5, 0}, {2, 5, 0}, {3, 5, 0}, {4, 5, 0}
		},	
		
		{	{1.5f, 0, 0}, {2.5f, 0, 0}, 
			{1, 1, 0},    {2, 1, 1},    {3, 1, 0},
			{0.5f, 2, 0}, {1.5f, 2, 1}, {2.5f, 2, 1}, {3.5f, 2, 0},
			{0, 3, 0},    {1, 3, 1},    {2, 3, 2},    {3, 3, 1},    {4, 3, 0},
			{0.5f, 4, 0}, {1.5f, 4, 1}, {2.5f, 4, 1}, {3.5f, 4, 0},
			{1, 5, 0},    {2, 5, 1},    {3, 5, 0},
			{1.5f, 6, 0}, {2.5f, 6, 0}  
  		},	

  		{	{-0.5f, 0, 0}, {0.5f, 0, 0}, {1.5f, 0, 0}, {2.5f, 0, 0}, {3.5f, 0, 0}, {4.5f, 0, 0},
  			{-0.5f, 1, 0}, {4.5f, 1, 0},
  			{-0.5f, 2, 0}, {1, 2, 1}, {2, 2, 1}, {3, 2, 1}, {4.5f, 2, 0},
  			{-0.5f, 3, 0}, {1, 3, 1}, {2, 3, 2}, {3, 3, 1}, {4.5f, 3, 0},
  			{-0.5f, 4, 0}, {1, 4, 1}, {2, 4, 1}, {3, 4, 1}, {4.5f, 4, 0},
  			{-0.5f, 5, 0}, {4.5f, 5, 0},
 	  		{-0.5f, 6, 0}, {0.5f, 6, 0}, {1.5f, 6, 0}, {2.5f, 6, 0}, {3.5f, 6, 0}, {4.5f, 6, 0},
  		},
  		
  		{	{-0.5f, 0, 0}, {0.5f, 0, 0}, {1.5f, 0, 0}, {2.5f, 0, 0}, {3.5f, 0, 0}, {4.5f, 0, 0},
			{0, 1, 0},     {1, 1, 1},    {2, 1, 1},    {3, 1, 1},    {4, 1, 0},
			{0.5f, 2, 0}, {1.5f, 2, 1}, {2.5f, 2, 1},  {3.5f, 2, 0},
			{1, 3, 0},    {2, 3, 0},    {3, 3, 0},
			{1.5f, 4, 2}, {2.5f, 4, 2}, 
			{1, 5, 0},    {2, 5, 0},    {3, 5, 0},
			{0.5f, 6, 0}, {1.5f, 6, 1}, {2.5f, 6, 1},  {3.5f, 6, 0},
			{0, 7, 0},     {1, 7, 1},    {2, 7, 1},    {3, 7, 1},    {4, 7, 0},
			{-0.5f, 8, 0}, {0.5f, 8, 0}, {1.5f, 8, 0}, {2.5f, 8, 0}, {3.5f, 8, 0}, {4.5f, 8, 0},
  		}	
		};
			
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;					// SurfaceHolder ����
			mContext = context;
			
			Display display = ((WindowManager) context.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();			// View�� ���� ��
			height = display.getHeight() - 50;  // View�� ���� ����

			InitGame();
			MakeStage();
		}
		
		//-------------------------------------
		//  ���� �ʱ�ȭ 
		//-------------------------------------
		public void InitGame() {
			B_width = width / 6;					// ����� ��
			B_height = B_width / 2;					// ����� ��
			M_left = (width - B_width * 5) / 2;		// ���� ����
			M_top = B_width * 4 / 5;				// ��� ���� 

			mPaddle = new Paddle(mContext, width / 2, height - B_height, width);
			mBall = new Ball(mContext, width / 2, mPaddle.y - 17, width, height);
			
			paint.setAntiAlias(true);
			paint.setColor(Color.WHITE);
			paint.setTextSize(15);
			
			// ���� �� ���� ǥ�ÿ�
			imgSball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
			imgSball = Bitmap.createScaledBitmap(imgSball, 10, 14, false);
		}
		//-------------------------------------
		//  Stage ����� 
		//-------------------------------------
		public void MakeStage() {
			for (int i = 0; i < Stage[stgNum].length; i++)
				mBlock.add(new Block(mContext, Stage[stgNum][i][0], Stage[stgNum][i][1], Stage[stgNum][i][2]));
			ResetPosition();			// �е�� ���� ȭ�� �߽����� �̵�
			mBall.sy = -(4 + stgNum);	// ������������ 1�� �ӵ� ����		 
			
			// ���
			imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back0 + stgNum);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);
		}

		//-------------------------------------
		//  ���� �е��� �ʱ� ��ġ�� �̵� - ���� ���� �� 
		//-------------------------------------
		public void ResetPosition() {
			mPaddle.x = width / 2;
			mPaddle.y = height - B_height;
			mPaddle.sx = 0;

			mBall.x = width / 2;
			mBall.y = mPaddle.y - 17;
			mBall.sy = -Math.abs(mBall.sy); 
			mBall.isMove = false;
		}

		//-------------------------------------
		//  �е� �̵� - Key Event���� ȣ��
		//-------------------------------------
		public void MovePaddle(int direction) {
			switch (direction) {
			case LEFT :
				mPaddle.sx = -4;
				break;
			case RIGHT :
				mPaddle.sx = 4;
				break;
			case STOP :
				mPaddle.sx = 0;
			}
		}
		
		//-------------------------------------
		//  Ball �߻�  - Key Event���� ȣ��
		//-------------------------------------
		public void ShootBall() {
			mBall.isMove = true;
		}
		
		//-------------------------------------
		//  Ball�� �е� �̵� - run()���� ȣ��
		//-------------------------------------
		public void MoveBall() {
			mPaddle.Move();
			if (mBall.isMove == false)	// �ʱ� ����
				mBall.x = mPaddle.x;	// �е�� ���� ���� �̵�
			if (mBall.Move() == false) {
				ballCnt--;
				if (ballCnt < 0) {		// ���� ��� ������ ���� ����
					GameOver();
					return;
				}
				ResetPosition();		// �е�, �� �ʱ� ��ġ��
			}
		}
		//-------------------------------------
		//  �浹 ���� - run()���� ȣ�� 
		//-------------------------------------
		public void CheckCollision() {
			if (mBlock.size() == 0) {			// �� ���� Ŭ���� ������
				stgNum++;						// ���� ����������
				if (stgNum >= STAGE_COUNTER)	// �������� ���� ó�� �������� 
					stgNum = 0;
				MakeStage();					// ���ο� �������� �����
				return;
			}

			// �е�� �浹
			if (Math.abs(mBall.x - mPaddle.x) <= mPaddle.pw 
					&& mBall.y >= (mPaddle.y - 17) && mBall.y < mPaddle.y) {
				mBall.sx = sx[rnd.nextInt(4)];		// ������ ���� ����
				mBall.sy = -Math.abs(mBall.sy);		// ������ �ݻ� 
			}
			
			// ��ϰ� �浹
			for (Block tmp : mBlock) {
				// �浹 ����
				if (mBall.x + mBall.bw < tmp.x1 || mBall.x - mBall.bw > tmp.x2		 
						|| mBall.y + mBall.bw < tmp.y1 || mBall.y - mBall.bw > tmp.y2) {
					continue;
				}	
				// ���� ������ �浹���� ����
				if (tmp.x1 - mBall.x >= mBall.bw || mBall.x - tmp.x2 >= mBall.bw)
					mBall.sx = - mBall.sx;
				else											// ���� �浹
					mBall.sy = - mBall.sy;
				tot += tmp.score;								// ����
				score++;
				mBlock.remove(tmp);								// ��� ����
				break;
			}
		}

		//-------------------------------------
		//  Canvas�� �׸��� - run()���� ȣ��
		//-------------------------------------
		public void DrawCharacters(Canvas canvas) {
			// ���
			canvas.drawBitmap(imgBack, 0, 0, null);
			// ���� �� ��
			for (int i = 0; i <= ballCnt; i++) 
				canvas.drawBitmap(imgSball, i * 12 + 5, height - 20, null); 
			// ���
			for (Block tmp : mBlock) 
				canvas.drawBitmap(tmp.imgBlk, tmp.x1, tmp.y1, null);
			// Ball
			canvas.drawBitmap(mBall.imgBall, mBall.x - mBall.bw, mBall.y- mBall.bh, null);
			// Paddle
			canvas.drawBitmap(mPaddle.imgPdl, mPaddle.x - mPaddle.pw , mPaddle.y - mPaddle.ph, null);
			// ����
			canvas.drawText("Stage" + stgNum, 5, 18, paint);
			canvas.drawText("��� : " + score, width / 2 - 40, 18, paint);
			canvas.drawText("���� : " + tot, width - 80, 18, paint);
		}
		
		//-------------------------------------
		//  Game Over
		//-------------------------------------
		private void GameOver() {
			// ���� ������ ���⿡�� ó������
			// ���� ���� ���� ����
			ballCnt = 4;	// ���� ������ �ٽ� 4����...
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
						MoveBall();					// �� �̵��ϰ�
						CheckCollision();			// �浹 ���� ��
						DrawCharacters(canvas);		// Canvas�� �׸���
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

	//-------------------------------------
	//  onKeyDown
	//-------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		synchronized (mHolder) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					mThread.MovePaddle(LEFT);		// �е��� ��������
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					mThread.MovePaddle(RIGHT);		// �е��� ����������
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					mThread.MovePaddle(STOP);		// �е� ����
					break;
				case KeyEvent.KEYCODE_DPAD_UP:		// �� �߻�
					mThread.ShootBall();
				}
			}		
			return super.onKeyDown(keyCode, event);
		} // sync
	} // onKeyDown 
} // SurfaceView 
