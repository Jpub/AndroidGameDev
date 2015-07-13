package com.Sliding2;

import java.util.*;

import android.graphics.*;
import android.graphics.Bitmap.Config;

public class Slice {
	public int x, y;					// ���� ��ġ
	public Bitmap imgPic;				// Slice �̹���
	
	private int w, h;					// slice ũ��
	
	//-------------------------------------
	//  ������ - ������ȣ, �迭 ÷��
	//-------------------------------------
	public Slice(int sliceNum, int pos) {
		w = MyGameView.sWidth;		// ���� ũ��
		h = MyGameView.sHeight;
		
		// �迭 ��ȣ�� View�� ��ǥ�� ��ȯ
		x = pos % MyGameView.xCnt * w + MyGameView.mgnLeft;  
		y = pos / MyGameView.xCnt * h + MyGameView.mgnTop;
		
		// Slice ��ȣ�� ���� ��ǥ�� ��ȯ
		int px = sliceNum % MyGameView.xCnt * w; 	
		int py = sliceNum / MyGameView.xCnt * h; 
		
		// ���� �ڸ���
		imgPic = Bitmap.createBitmap(MyGameView.imgOrg, px, py, w, h);

		// �����̿� Paint
		Paint paint = new Paint();
		EmbossMaskFilter emboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.5f, 1, 1);
		paint.setMaskFilter(emboss);
		
		// �� ������ Slice�� �������� ����������� ä���
		Canvas canvas = new Canvas();
		canvas.setBitmap(imgPic);
		if (sliceNum == MyGameView.sMax - 1) {	// ������ Slice
			paint.setColor(Color.YELLOW);
			paint.setAlpha(160);
			canvas.drawRect(0, 0, w, h, paint);
		} else	{
			canvas.drawBitmap(imgPic, 0, 0, paint);
		}	
	}
}

//------------------------------------------------------------

//-------------------------------------
//�̵� & �ð� & Stage ����� 
//-------------------------------------
class Score {
	public Bitmap imgScore;				// �̵� Ƚ��
	public Bitmap imgTime;				// ��� �ð�
	public Bitmap imgStage;				// Stage
	
	private Bitmap fonts[] = new Bitmap[15];
	private Bitmap imgStg;				// 'STAGE' ����
	private int fw, fh, sw;				// ���� �� & ����
	private int icoNum = 11;
	private long lastTime;
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public Score() {
		for (int i = 0; i < 15; i++)
			fonts[i] = BitmapFactory.decodeResource(MyGameView.mContext.getResources(), R.drawable.f00 + i);
		imgStg = BitmapFactory.decodeResource(MyGameView.mContext.getResources(), R.drawable.f_stage);
		fw = fonts[0].getWidth();
		fh = fonts[0].getHeight();
		sw = imgStg.getWidth(); 
	}

	//-------------------------------------
	//  Count �̹���
	//-------------------------------------
	public void MakeCount(int moveNum) {
		String s = "" + moveNum;
		int L = s.length();				// ���ڿ��� ����

		// �� ��Ʈ���� �����
		imgScore = Bitmap.createBitmap(fw * (L + 2), fh, Config.ARGB_8888);

		// ��Ʈ�ʿ� �̹����� �׷����� Canvas ����
		Canvas canvas = new Canvas();
		canvas.setBitmap(imgScore);			// Canvas�� ��Ʈ�ʿ� ����ϵ��� ���� 

		canvas.drawBitmap(fonts[icoNum], 0, 0, null);		// ������
		for (int i = 0; i < L; i++) { 				// Score
			int n = (int) s.charAt(i) - 48;
			canvas.drawBitmap(fonts[n], fw * (i + 1) + 10, 0, null); 
		}
		icoNum++;
		if (icoNum > 14) icoNum = 11;				// ������ �ִϸ��̼�
	}
	
	//-------------------------------------
	//  �ð� �̹���
	//-------------------------------------
	public void MakeTime(long startTime) {
		long curTime = System.currentTimeMillis();
		if (startTime > curTime)
			MyGameView.startTime = startTime = curTime;
			
		if (curTime - lastTime < 250) return;
		lastTime = curTime;
		int time = (int) (curTime - startTime) / 1000;
		int sec = time % 60;
		int min = time / 60 % 60;
		int hour = time / 3600;

		String s = hour + ":" + min + ":" + sec;
		int L = s.length();					// ���ڿ��� ����

		// �� ��Ʈ���� �����
		imgTime = Bitmap.createBitmap(fw * L, fh, Config.ARGB_8888);

		// ��Ʈ�ʿ� �̹����� �׷����� Canvas ����
		Canvas canvas = new Canvas();
		canvas.setBitmap(imgTime);    
		for (int i = 0; i < L; i++) { 
			int n = (int) s.charAt(i) - 48;
			canvas.drawBitmap(fonts[n], fw * i, 0, null); 
		}
	}

	//-------------------------------------
	//  Stage �̹���
	//-------------------------------------
	public void MakeStageNum(int stageNum) {
		String s = "" + (stageNum + 1);
		int L = s.length();				// ���ڿ��� ����

		// �� ��Ʈ���� �����
		imgStage = Bitmap.createBitmap(sw + fw * L, fonts[0].getHeight(), Config.ARGB_8888);

		// ��Ʈ�ʿ� �̹����� �׷����� Canvas ����
		Canvas canvas = new Canvas();
		canvas.setBitmap(imgStage);

		canvas.drawBitmap(imgStg, 0, 0, null);		// 'STAGE'
		for (int i = 0; i < L; i++) { 				// stageNum
			int n = (int) s.charAt(i) - 48;
			canvas.drawBitmap(fonts[n], sw + fw * i, 0, null); 
		}
	}
}	

//--------------------------------------------------------------------------------

//-------------------------------------
// Snow
//-------------------------------------
class Snow {
	public int x, y, rad;			// ��ǥ
	public  Bitmap imgSnow;			// ��Ʈ�� �̹���
	
	private int width, height;
	private int sx, sy; 
	private int range, dx, speed;
	private Random rnd;
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public Snow(int kind) {
		width = MyGameView.Width;
		height = MyGameView.Height;
		
		rnd = new Random();
		rad = rnd.nextInt(6) + 2; 				// 2~7 ������
		x = rnd.nextInt(width);					// View ��ü
		y = rnd.nextInt(height);
		sy = rnd.nextInt(4) + 2; 				// 2~5 �ӵ�
		sx = rnd.nextInt(3) + 1;				// 1~3
		range = rnd.nextInt(31) + 20;			// 20~50 �¿� �̵� 
		
		int k;									// 0~3 ��  ����
		if (kind == 1)
			k = rnd.nextInt(2);					// ����
		else 
			k = rnd.nextInt(4);					// ���
		imgSnow = BitmapFactory.decodeResource(MyGameView.mContext.getResources(), 
						R.drawable.s0 + k);
		imgSnow = Bitmap.createScaledBitmap(imgSnow, rad * 2 , rad * 2, true);
	}

	//-------------------------------------
	//  MoveSnow
	//-------------------------------------
	public void MoveSnow(int dir) {
		if (dir < 5) dir = 1;
		else if (dir < 10) dir = 2; 
		else if (dir > 200) dir = 0;
		
		switch (dir) {
		case 0 :				// �ٶ�����
			speed = sx;
			break;
		case 1 :				// �ٶ� ��
			speed = Math.abs(sx) * 2;	
			break;
		case 2 :				// �ٶ� ��
			speed = -Math.abs(sx) * 2;
			break;
		default :				// ���� �ٶ� ����
			if (speed == 0) speed = sx;
		}

		x += speed;
		y += sy;
		if (x < -rad) x = width + rad; 		// ������ ����� �������� ����
		if (x > width + rad) x = -rad;		// ������ ����� �������� ����
		if (y > height + rad) y = -rad;		// �ٴ��� ����� õ������ ����

		
		if (speed == sx) {					// Dir == 0 || �ʱ� ����
			dx += sx;						// �¿�� �̵��� �Ÿ� ����
			if (Math.abs(dx) > range) {		// ������ ������ ����� ������ �ٲ�
				dx = 0;
				sx = -sx;
			}
		} // if
	} // move
}

//-----------------------------------------------------------

//-------------------------------------
// �񴰹��
//-------------------------------------
class Bubble {
	public int x, y, rad;			// ��ǥ, ������
	public  Bitmap imgBall;			// ��Ʈ�� �̹���
	public  boolean dead; 			// �Ͷ߸� ����
	
	private int sx, sy;				// �̵� ���� �� �ӵ�
	private int width, height;		// View�� ũ��
	private int imgNum;				// �̹��� ��ȣ
	private int loop;				// ���� ī����
	private int counter;			// ���� �浹 ȸ��
	private Random rnd;
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public Bubble() {
		width = MyGameView.Width;
		height = MyGameView.Height;
		rnd = new Random();

	    rad = rnd.nextInt(21) + 20;		// ������ : 20 ~ 40;
	    imgBall = BitmapFactory.decodeResource(MyGameView.mContext.getResources(), 
	    						R.drawable.bubble_1);
	    imgBall = Bitmap.createScaledBitmap(imgBall, rad * 2, rad * 2, false);
	    sx = rnd.nextInt(2) == 0 ? -2 : 2;
	    sy = rnd.nextInt(2) == 0 ? -4 : 4;
	    x = rnd.nextInt(width - 100) + 50;
	    y = rnd.nextInt(height - 100) + 50;
	    MoveBubble();
	}
	
	//-------------------------------------
	//  Move
	//-------------------------------------
	public void MoveBubble() {
		x += sx;
		y += sy;
		if (x <= rad || x >= width - rad) {		// �¿�� �浹
			sx = -sx;
			x += sx;
			counter++;
		}	
		if (y <= rad || y >= height - rad) {	// ���Ϸ� �浹 
			sy = -sy;
			y += sy;
			counter++;
		}
		if (counter >= 3) dead = true;		// ���� �浹 Ƚ��
	}

} // Bubble

//------------------------------------------------------------

//-------------------------------------
// ���� ���
//-------------------------------------
class SmallBall {
	public int x, y, rad;			// ��ǥ, ������
	public  boolean dead; 			// �Ͷ߸� ����
	public  Bitmap imgBall;			// ��Ʈ�� �̹���
	
	private int width, height;		// View�� ũ��
	private int cx, cy;				// ���� �߽���
	private int cr;					// ���� ������
	private double r;				// �̵� ���� (radian)
	private int speed;				// �̵� �ӵ�
	private int num;				// �̹��� ��ȣ
	private int life;				// ���� �ֱ�
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public SmallBall(int _x, int _y, int ang) {
		width = MyGameView.Width;
		height = MyGameView.Height;
		cx = _x;		// �߽���
		cy = _y;
		r = Math.toRadians(ang);		// �������� ��ȯ
		
		Random rnd = new Random();
		speed = rnd.nextInt(5) + 2;		// �ӵ�     : 2~6
		rad = rnd.nextInt(6) + 3;		// ������   : 3~8
		num = rnd.nextInt(6);			// �̹�����  : 0~5
		life = rnd.nextInt(21) + 20;	// 20~40
		
		imgBall = BitmapFactory.decodeResource(MyGameView.mContext.getResources(), R.drawable.b0 + num);
		imgBall = Bitmap.createScaledBitmap(imgBall, rad * 2, rad * 2, false);
		cr = 10;						// ���� �ʱ� ������
		MoveBall();
	}

	//-------------------------------------
	//  MoveBall
	//-------------------------------------
	public void MoveBall() {
		life--;
		cr += speed;
		x = (int) (cx + Math.cos(r) * cr); 
		y = (int) (cy - Math.sin(r) * cr); 
		if (x < -rad || x > width + rad ||
				y < -rad || y > height + rad || life <= 0)
			dead = true;
	}
}

