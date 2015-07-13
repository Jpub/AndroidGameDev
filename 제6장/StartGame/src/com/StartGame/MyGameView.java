package com.StartGame;

import java.io.*;
import java.util.*;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.provider.MediaStore.*;
import android.util.*;
import android.view.*;
import android.view.SurfaceHolder.Callback;

public class MyGameView extends SurfaceView implements Callback {

	GameThread 	  mThread;				// GameThread
	SurfaceHolder mHolder;				// SurfaceHolder 
	static Context 	  mContext;			// Context 
	
	static int backGround;				// ��׶��� ����
	int imageType;						// Slice �̹��� ����
	int imageId;						// Slice �̹��� Id
	int storageType;					// �̹��� ���� ��ü

	static boolean isThumb = false;		// ����� ǥ�� ���� (�ɼǸ޴�)
	static boolean isSave;				// ���� Save - Thread ������
	static boolean isLoad;				// ���� Load - Thread ������
	
	static int Width, Height;			// View�� ũ��
	static int mgnLeft, mgnTop;			// ����, �� ����
	static int sMax;					// Slice ���� 
	static int xCnt, yCnt;				// Slice ����, ���� ����
	static int pWidth, pHeight;			// ���� ������
	static int sWidth, sHeight;			// Slice ������
	int stageNum;						// �������� ��ȣ
	int sliceNum[] = new int[36];		// Slice ��ȣ - �ִ� 6*6
	
	Slice mSlice[] = new Slice[36]; 	// Slice Class
	Score mScore;						// ���� ó����
	Rect mRect = new Rect();			// ���� ��ü ���� - Touch�� ����
	
	static Bitmap imgOrg;				// ���� �̹���
	Bitmap imgBack[] = new Bitmap[2];	// ��� �̹���
	Bitmap imgFrame[] = new Bitmap[2];	// ���� ������
	Bitmap imgThumb;					// �����
	
	static long startTime;				// �������� ���� �ð�
	int  moveCnt;						// Slice �̵� Ƚ��
	int msgNum;							// �޽��� ��ȣ
	Bitmap imgMsg[] = new Bitmap[6];	// �޽���

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

		// Options Menu�� ���� ���� �б�
		xCnt = ((GlobalVars) context.getApplicationContext()).getXCount();
		yCnt = ((GlobalVars) context.getApplicationContext()).getYCount();
		backGround = ((GlobalVars) context.getApplicationContext()).getBackground();
		isLoad = ((GlobalVars) context.getApplicationContext()).getLoad();

		// ImageType �б�
		imageType = ((GlobalVars) mContext.getApplicationContext()).getImageType();
		imageId = ((GlobalVars) mContext.getApplicationContext()).getImageId();
		storageType = ((GlobalVars) mContext.getApplicationContext()).getStorageType();
		
		if (xCnt < 3 || xCnt > 6) xCnt = 3;		// ��ȿ���� ����
		if (yCnt < 3 || yCnt > 6) yCnt = 3;
		if (backGround < 1 || backGround > 2) backGround = 1;
		
		stageNum = 0;			// �������� ��ȣ
		
		InitGame();				// ���� �ʱ�ȭ
		MakeStage();			// �������� �����

		setFocusable(true);
		Log.v("surface Created", "---------------------------");
	}

	//-------------------------------------
	//  ���� �ʱ�ȭ - �����ڿ��� ȣ��
	//-------------------------------------
	private void InitGame() {
		Display display = ((WindowManager) mContext.getSystemService (Context.WINDOW_SERVICE)).getDefaultDisplay();
		Width = display.getWidth();			
		Height = display.getHeight() - 50;  
		
		mgnLeft = 60;						// ����, �� ����
		mgnTop = (int)(Height / 7.5);		

		pWidth = Width - mgnLeft * 2;		// ���� ����
		pHeight = pWidth * 7 / 5;			// ���� ���� (5:7)

		// ���� ũ�⸦ Rect��  - Touch�� ����
		mRect.set(mgnLeft, mgnTop, mgnLeft + pWidth, mgnTop + pHeight);
		mScore = new Score();
		
		// �޽��� �̹��� �б�
		for (int i = 0; i < 5; i++)	{	
			// �޽��� ��ȣ�� 1���� ����
			imgMsg[i + 1] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.msg1 + i);
		}	
	}
	
	//-------------------------------------
	//  �������� ����� - �����ڿ��� ȣ��
	//-------------------------------------
	private void MakeStage() {
		sMax = xCnt * yCnt;				// Slice ����
		sWidth = pWidth / xCnt;			// Slice ũ��
		sHeight = pHeight / yCnt;
		
		// �迭�� Slice �Ϸù�ȣ ��ȣ �ֱ�
		for (int i = 0; i < sMax; i++) { 
			sliceNum[i] = i;
		}

		LoadImages();	// ���� ����  Trimming
		Shuffling();	// Slice ��ȣ ���� ���Ἲ ����
		
		// Slice()�� �迭�� �ֱ�
		for (int i = 0; i < sMax; i++) {
			mSlice[i] = null;	// �������� ���� ���� clear
			mSlice[i] = new Slice(sliceNum[i], i);
		}
		
		startTime = System.currentTimeMillis();
		moveCnt = 0;
		mScore.MakeCount(moveCnt);
		mScore.MakeStageNum(stageNum);
		mScore.MakeTime(startTime);
	}
	
	//-------------------------------------
    //  LoadImage & Rescale - MakeStage���� ȣ��
    //-------------------------------------
	public void LoadImages() {
		Resources res = mContext.getResources();
		int num = stageNum % 4;
		
		// �������� ��ȣ�� �´� ��� �̹��� �а� View�� ũ��� Rescale
		imgBack[0] = BitmapFactory.decodeResource(res, R.drawable.snow0 + num);
		imgBack[1] = BitmapFactory.decodeResource(res, R.drawable.sky0 + num);
		imgBack[0] = Bitmap.createScaledBitmap(imgBack[0], Width, Height, true);	
		imgBack[1] = Bitmap.createScaledBitmap(imgBack[1], Width, Height, true);	

		// ���� ������
		imgFrame[0] = BitmapFactory.decodeResource(res, R.drawable.frame1);	
		imgFrame[1] = BitmapFactory.decodeResource(res, R.drawable.frame2);	
		imgFrame[0] = Bitmap.createScaledBitmap(imgFrame[0], pWidth + 30, pHeight + 30, true);	
		imgFrame[1] = Bitmap.createScaledBitmap(imgFrame[1], pWidth + 30, pHeight + 30, true);	
		
		// ���� �̹��� �б�
		Bitmap imgtmp = BitmapFactory.decodeResource(res, R.drawable.pic_0 + num);
		
		// ����� ���� �̹��� �б�
		if (imageType == 2 && imageId > 0) {
			try {
    			Uri myUri;
    			if (storageType == 1)
    				myUri = ContentUris.withAppendedId(Images.Media.INTERNAL_CONTENT_URI, imageId); 
    			else
    				myUri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, imageId);
				imgtmp = Images.Media.getBitmap(mContext.getContentResolver(), myUri); 
			} catch (FileNotFoundException e) { 
				e.printStackTrace(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
		} // if
		
		int w = imgtmp.getWidth();		// �̹��� ũ�� 
		int h = imgtmp.getHeight();

		// ������ ������ 5:7�� �ǵ��� ���� �κ� �ڸ���
		if (w * 1.4 < h)				// ���η� �����̸� �Ʒ��� ����
			imgtmp = Bitmap.createBitmap(imgtmp, 0, 0, w, (int) (w * 1.4));   
		else if (w * 1.4 > h) {			// ���η� �����̸� ���� �¿� ����
			int p = (int) (w - h / 1.4) / 2;	// �¿� ���� �κ��� ����
			imgtmp = Bitmap.createBitmap(imgtmp, p, 0, (int) (h / 1.4), h);
		}
		imgOrg = Bitmap.createScaledBitmap(imgtmp, pWidth, pHeight, true);	
		imgThumb = Bitmap.createScaledBitmap(imgOrg, (int)(pWidth / 4), (int)(pHeight / 4), true);
	}

	
	//-------------------------------------
	//  Slice ���� - MakeStage()���� ȣ��
	//-------------------------------------
	public void Shuffling() {
		int r, t;
		Random rnd = new Random();
		for (int i = 0; i < sMax; i++) {
			r = rnd.nextInt(sMax);		// 0 ~ x*y-1
			t = sliceNum[i];			// ���� ��ġ�� ������ġ ��ȯ
			sliceNum[i] = sliceNum[r];
			sliceNum[r] = t;
		} // for

		CheckShuffle();	// ���Ἲ ����
	} // Shuffle

	//-------------------------------------
	//  ���Ἲ ���� - Shuffling()���� ȣ��
	//-------------------------------------
	public void CheckShuffle() {
		int k1, k2, cnt;
		// (6, 5)ó�� ���� ���ڰ� ū ġȯ�� ������ �� ������ ����
		do {
			k1 = k2 = cnt = 0;
			for (int i = 0; i < sMax - 1; i++) {
				// ������ ���� ��󿡼� ����
				if (sliceNum[i] == sMax - 1) continue;
				for (int j = i + 1; j < sMax; j++) {
					if (sliceNum[j] == sMax - 1) continue;
					if (sliceNum[i] > sliceNum[j]) {
						cnt++;		// ġȯ�� ����
						k1 = i;		// ġȯ ��ȣ �ӽ�����
						k2 = j;
					}
				} // for j
			} // for i
			if (cnt % 2 == 0) break;	// ¦�� ġȯ�̸� ����
		
			// ������ ġȯ�� �ٲ㼭 �ٽ� ����
			int t = sliceNum[k1];
			sliceNum[k1] = sliceNum[k2];
			sliceNum[k2] = t;
		} while (true);	
	} // Check

	//-------------------------------------
	//  Slice �̵� - Touch���� ȣ��
	//-------------------------------------
	public void MoveSlice(int x, int y) {
		synchronized (mHolder) {
			x = (x - mgnLeft) / sWidth;	// Slice�� ��� ��ǥ
			y = (y - mgnTop) / sHeight;
		
			int p  = y * xCnt + x;		// Slice�� �迭 ��ǥ 
			int pl = p - 1;				// Slice�� ���� 
			int pr = p + 1;				// Slice�� ������ 
			int pu = p - xCnt;			// Slice�� �� 
			int pd = p + xCnt;			// Slice�� �Ʒ�
			int last = sMax - 1;		// ������ Slice ��ȣ		

			// Slice�� �����¿쿡 ������ �ִ��� ���� - �����ǥ�� �Ǵ�
			if (x - 1 >= 0 && sliceNum[pl] == last) {	  
				CheckSlice(p, pl);	// ����
			} 
			else if (x + 1 < xCnt && sliceNum[pr] == last) {
				CheckSlice(p, pr);	// ������
			}	
			else if (y - 1 >= 0 && sliceNum[pu] == last) {
				CheckSlice(p, pu);	// ��
			}
			else if (y + 1 < yCnt && sliceNum[pd] == last) {
				CheckSlice(p, pd);	// �Ʒ�
			}
		} // sync
	}
	
	//-------------------------------------
	//  Slice���� - StartMove(), Thread���� ȣ��
	//-------------------------------------
	public void CheckSlice(int p1, int p2) {
		synchronized (mHolder) {
			moveCnt++;
			mScore.MakeCount(moveCnt);
			
			Bitmap tmp;						// �ӽ÷� ��Ʈ���� 1�� �����
			// Class�� ��Ʈ�ʸ� ��ȯ
			tmp = mSlice[p1].imgPic;		
			mSlice[p1].imgPic = mSlice[p2].imgPic;
			mSlice[p2].imgPic = tmp;

			// �迭 ���� ��ȯ
			int t = sliceNum[p1];
			sliceNum[p1] = sliceNum[p2]; 
			sliceNum[p2] = t;
			
			// �������� Clear ���� ����
			int n;
			for (n = 0; n < sMax; n++) {
				if (sliceNum[n] != n) break;
			}
			if (n >= sMax) msgNum = 5;			// �������� Clear 
		} // sync
	}
	
	//-------------------------------------
    //  ���� �������� - Thread���� ȣ��
    //-------------------------------------
	public void MakeNextStage() {
		PauseGame();		// ������ �Ͻ� ����
		stageNum++;
		if (xCnt == yCnt)	// Slice ���� ����
			yCnt++;
		else
			xCnt++;
		if (xCnt > 6) xCnt = 6;
		if (yCnt > 6) yCnt = 6; 

		MakeStage();
		ResumeGame();		// ������ �� �⵿
	}
	
	//-------------------------------------
    //  ���� ���� - Thread���� ȣ��
    //-------------------------------------
	public void SaveStage() {
		isSave = false;
		PauseGame();	// ���� �Ͻ� ����
		int time = (int)(System.currentTimeMillis() - startTime);	// ����ð�

		StringBuffer buffer = new StringBuffer();
		buffer.append(stageNum).append("|")			// �������� ��ȣ
			  .append(xCnt).append("|")				// Slice ���� ��
			  .append(yCnt).append("|")
			  .append(moveCnt).append("|")			// �̵� Ƚ��
			  .append(time).append("|")				// ��� �ð�
			  .append(backGround).append("|")		// Background ����
			  .append(imageType).append("|")		// �̹��� ����(�⺻/�����)
			  .append(imageId).append("|")			// �̹��� Id
			  .append(storageType).append("|");		// �����ü (1:����, 2:����)
		for (int i = 0; i < sMax; i++) {
			buffer.append(sliceNum[i]).append("|");	// �迭 ����
		}	  
		
		try {
			FileOutputStream fileOutput = mContext.openFileOutput("SlidingPuzzle", Context.MODE_PRIVATE);
			fileOutput.write(buffer.toString().getBytes()); 
			fileOutput.close();
			msgNum = 1;
		} catch (IOException e) {
			msgNum = 2;
		}
		ResumeGame();	// ���� ����
	}

	//-------------------------------------
    //  ���� �ҷ����� - ������/Thread���� ȣ��
    //-------------------------------------
	public boolean LoadStage() {
		isLoad = false;
		PauseGame();
		boolean result = false;
		try {
			FileInputStream fileInput = mContext.openFileInput("SlidingPuzzle");
			byte[] data = new byte[fileInput.available()];
			if (fileInput.read(data) != -1) {
				fileInput.close();
				
				// ���ڿ��� '|'���� �и��Ͽ� �迭�� �ֱ�
				String[] buffer = (new String(data)).split("\\|");

				stageNum = Integer.parseInt(buffer[0]);
				xCnt = Integer.parseInt(buffer[1]);
				yCnt = Integer.parseInt(buffer[2]);
				moveCnt = Integer.parseInt(buffer[3]);
				int time = Integer.parseInt(buffer[4]);
				backGround = Integer.parseInt(buffer[5]);
				imageType = Integer.parseInt(buffer[6]);
				imageId = Integer.parseInt(buffer[7]);
				storageType = Integer.parseInt(buffer[8]);
					
				LoadImages(); 				// �̹��� �б�				
				sMax = xCnt * yCnt;			
				sWidth = pWidth / xCnt;		// Slice ũ��
				sHeight = pHeight / yCnt;
				
				// �迭 �а� Slice �����
				for (int i = 0; i < sMax; i++) {
					sliceNum[i] = Integer.parseInt(buffer[9 + i]);
					mSlice[i] = null;
					mSlice[i] = new Slice(sliceNum[i], i);
				}	
				startTime = System.currentTimeMillis() - time;	// ����ð� ����
				mScore.MakeCount(moveCnt);  
				mScore.MakeStageNum(stageNum);  
				mScore.MakeTime(startTime);
				msgNum = 3;
				result = true;
			}
		} catch (IOException e) {
			msgNum = 4;
		}
		ResumeGame();
		return result;
	}
	
	//-------------------------------------
    //  SurfaceView�� ������ �� ����Ǵ� �κ�
    //-------------------------------------
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v("surface Started", "---------------------------");
		try {
			mThread.start();
		} catch (Exception e) {
			RestartGame(); 
		}
	}

	//-------------------------------------
    //  SurfaceView�� �ٲ� �� ����Ǵ� �κ�
    //-------------------------------------
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.v("surface Changed", "---------------------------");
	}

	//-------------------------------------
    public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v("surface Destroyed", "---------------------------");
		mThread.StopThread();
		Log.v("Save Stage", "---------------------------");
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
			
		ArrayList<Snow> mSnow1 = new ArrayList<Snow>();		// ��� 
		ArrayList<Snow> mSnow2 = new ArrayList<Snow>(); 	// ����	
		
		ArrayList<Bubble> mBall = new ArrayList<Bubble>();			// ū���
		ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();	// �������
		
		Random rnd = new Random();
		int msgLoop = 0;			// ���� ī����
		boolean isNext = false;		// ���� ���������� ����
		
		//-------------------------------------
		//  ������ 
		//-------------------------------------
		public GameThread(SurfaceHolder holder, Context context) {
			
		}
		
		//-------------------------------------
		//  �񴰹�� �����  - run()���� ȣ��
		//-------------------------------------
		public void MakeBubble() {
			if (mBall.size() > 7) return;
			synchronized (mHolder) {
				mBall.add(new Bubble());
			}
		}
		
		//-------------------------------------
		//  �񴰹�� Touch - Touch Event���� ȣ��
		//-------------------------------------
		public void TouchBubble(int x, int y) {
			boolean flag = false;
			for (Bubble tmp :  mBall) {
				if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2) 
						<= Math.pow(tmp.rad, 2)){
					tmp.dead = true;	// �񴰹�� Touch�� ��� 
					flag = true;		
				}
			}
		}
	
		//-------------------------------------
		//  ����  �񴰹�� ����� - MoveBubble()���� ȣ��
		//-------------------------------------
		public void MakeSmallBall(int x, int y) {
			int count = rnd.nextInt(7) + 7;   // 7~13��
			synchronized (mHolder) {
				for (int i = 1; i <= count; i++) {
					int ang = rnd.nextInt(360);		 
					sBall.add(new SmallBall(x, y, ang));
				}
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
		//  ��������  - run���� ȣ��
		//-------------------------------------
		public void MoveSnow() {
			synchronized (mHolder) {
				if (mSnow1.size() < 300)  mSnow1.add(new Snow(2));	// ���
				if (mSnow2.size() < 100)  mSnow2.add(new Snow(1));	// ����
			}
			
			int n = rnd.nextInt(300);
			for (Snow tmp : mSnow1) tmp.MoveSnow(n);
			for (Snow tmp : mSnow2) tmp.MoveSnow(n);
		}
		
		//-------------------------------------
		//  �ȳ� ���ڿ� ��� - run()���� ȣ��
		//-------------------------------------
		public void DrawMessage(Canvas canvas) {
			msgLoop++;
			if (msgLoop % 10 / 5 == 0) {
				int left = (Width - imgMsg[msgNum].getWidth()) / 2;
				canvas.drawBitmap(imgMsg[msgNum], left, Height - 90, null); 
			}	
			
			if (msgLoop >= 25) {				// �޽��� ǥ�ô� Blink 3ȸ������
				if (msgNum == 5) 				// Stage Clear�̸�
					synchronized (mHolder) {
						MakeNextStage();		// ���� Stage�� �����
					}
				msgLoop = 0;
				msgNum = 0;
			}	
		}
		
		//-------------------------------------
		//  ��� ��� - run()���� ȣ�� 
		//-------------------------------------
		public void DrawAll(Canvas canvas) {
			// ��� �̹���
			canvas.drawBitmap(imgBack[backGround - 1], 0, 0, null);
			
			if (backGround == 1) {	
				for (Snow tmp : mSnow1)
					canvas.drawBitmap(tmp.imgSnow, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			}  else { 
				// ū �񴰹��
				for (Bubble tmp : mBall)
					canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
				// �����񴰹��
				for (SmallBall tmp : sBall)
					canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			}
			
			// ���� Frame
			canvas.drawBitmap(imgFrame[backGround - 1], mgnLeft - 15, mgnTop - 15, null);	

			// �����
			if (isThumb == true)	
				canvas.drawBitmap(imgThumb, 5, Height - imgThumb.getHeight() - 5, null);
			
			// �̵�Ƚ�� & Stage & ����ð� ���
			mScore.MakeTime(startTime);
			canvas.drawBitmap(mScore.imgScore, 15, 15, null); 
			canvas.drawBitmap(mScore.imgStage, Width / 2 - mScore.imgStage.getWidth() / 2, 15, null);
			canvas.drawBitmap(mScore.imgTime, Width - mScore.imgTime.getWidth() - 15, 15, null);
			
			// Slice ǥ��
			for (int i = 0; i < sMax; i++)
				canvas.drawBitmap(mSlice[i].imgPic, mSlice[i].x, mSlice[i].y, null);

			if (backGround == 1) {
				for (Snow tmp : mSnow2)
					canvas.drawBitmap(tmp.imgSnow, tmp.x - tmp.rad,  tmp.y - tmp.rad, null);
			}
		}
		
		//-------------------------------------
		//  ������ ��ü
		//-------------------------------------
		public void run() {
			Canvas canvas = null; 					// canvas�� �����
			while (canRun) {
				canvas = mHolder.lockCanvas();		// canvas�� ��װ� ���� �Ҵ�
				try {
					synchronized (mHolder) {
						if (backGround == 1) {
							MoveSnow();					// ��������
						}
						else {
							MakeBubble();				// ǳ�� �����
							MoveBubble();				// ǳ�� �̵�
						}
						DrawAll(canvas);				// ��� ���
						if (isSave) SaveStage();		// Save
						if (isLoad) LoadStage();		// Load
						if (msgNum > 0) DrawMessage(canvas);	// �޽��� ���	
					}
				} finally {								// ���� �۾��� ������ 
					if (canvas != null)					// ������ ������ View�� ����
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
	//  onTouch Event 
	//-------------------------------------
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX(); 
			int y = (int) event.getY();
			synchronized(mHolder)  {
				if (mRect.contains(x, y)) 
					MoveSlice(x, y);
				else if (backGround == 2)
					mThread.TouchBubble(x, y);
			}
		}
		return true;
	} // Touch

} // SurfaceView 
