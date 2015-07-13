package com.Project3;

import java.util.*;

import android.content.*;
import android.graphics.*;

//-------------------------------------
//  �񴰹��
//-------------------------------------
public class Bubble {
	public int x, y, rad;			// ��ǥ, ������
	public  Bitmap imgBall;			// ��Ʈ�� �̹���
	public  boolean dead = false; 	// �Ͷ߸� ����
	
	private int _rad;				// ������ ������
	private int sx, sy;				// �̵� ���� �� �ӵ�
	private int width, height;		// View�� ũ��
	private Bitmap Bubbles[] = new Bitmap[6];	// ǳ�� �ִϸ��̼� �� �̹���	
	private int imgNum = 0;			// �̹��� ��ȣ
	private int loop = 0;			// ���� ī����
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public Bubble(Context context, int _x, int _y, int _width, int _height) {
		width = _width;
		height = _height;
		x = _x;
		y = _y;
		
	    imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble_1);
	    
	    Random rnd = new Random();
	    _rad = rnd.nextInt(11) + 20;		// ������ : 20 ~ 30;
	    rad = _rad;
	    
	    // �������� 2 �������� Ŀ���� �۾����� �� 6�� �����
	    for (int i = 0; i <= 3; i++)
	    	Bubbles[i] = Bitmap.createScaledBitmap(imgBall, _rad * 2 + i * 2, _rad * 2 + i * 2, false);
	    Bubbles[4] = Bubbles[2];  
	    Bubbles[5] = Bubbles[1];
	    imgBall = Bubbles[0];
	    
	    sx = 2;
	    sy = rnd.nextInt(2) == 0 ? -2 : 2;
	    MoveBubble();
	}
	
	//-------------------------------------
	//  Move
	//-------------------------------------
	public void MoveBubble() {
		loop++;
		if (loop % 3 == 0) {
			imgNum++;			// �񴰹�� ��ȣ
			if (imgNum > 5) imgNum = 0;
			imgBall = Bubbles[imgNum];
			
			// �񴰹���� ������ ����
			rad = _rad + (imgNum <= 3 ? imgNum : 6 - imgNum) * 2;   
		}
		
		x += sx;
		y += sy;
		if (x >= width + rad) 	// ������ ���̸� �� �������� 
			x = -rad;
		
		if (y <= rad || y >= 200) { 	// ���Ϸ� �浹 
			sy = -sy;
			y += sy;
		}
	}
} // Bubble

//------------------------------------------------------------

//-------------------------------------
//  ���� ���
//-------------------------------------
class SmallBall {
	public int x, y, rad;			// ��ǥ, ������
	public  boolean dead = false; 	// �Ͷ߸� ����
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
	public SmallBall(Context context, int _x, int _y, int ang, int _width, int _height) {
		cx = _x;		// �߽���
		cy = _y;
		width = _width;
		height = _height;
		r = ang * Math.PI / 180;		// ���� radian
		
		Random rnd = new Random();
		speed = rnd.nextInt(5) + 2;		// �ӵ�     : 2~6
		rad = rnd.nextInt(6) + 2;		// ������   : 2~7
		num = rnd.nextInt(6);			// �̹���  : 0~5
		life = rnd.nextInt(31) + 20;	// 20~50
		
		imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0 + num);
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

//------------------------------------------------------------

//-------------------------------------
//  ����� - �Ź��� �Ѿ�
//-------------------------------------
class WaterBall {
	public int x, y, rad;			// ��ǥ, ������
	public  boolean dead = false; 	// ���� ����
	public  Bitmap imgBall;			// ��Ʈ�� �̹���
	
	private int width, height;		// View�� ũ��
	private int speed;				// �̵� �ӵ�
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public WaterBall(Context context, int _x, int _y, int _width, int _height) {
		x = _x;	
		y = _y;
		width = _width;
		height = _height;
	    imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.w0);
	    rad = imgBall.getWidth() / 2;
	    speed = 8;
	    MoveBall();
	}

	//-------------------------------------
	//  Move Water
	//-------------------------------------
	public void MoveBall() {
		y -= speed;
		if (y < 0) dead = true;
	}
}

//------------------------------------------------------------

//-------------------------------------
//  ���� ǥ�� 
//-------------------------------------
class Score {
	public int x, y, sw, sh;			// ��ǥ, ��ü �̹��� ũ��
	public Bitmap imgScore;				// ���� ��¿� �̹���
	
	private Bitmap fonts[] = new Bitmap[10];
	private int loop = 0;				// loop counter
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public Score(Context context, int _x, int _y, int _score) {
		x = _x;	
		y = _y;
		for (int i = 0; i <= 9; i++)
			fonts[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.f0 + i);
		MakeScore(_score);
		Move();
	}

	//-------------------------------------
	//  ���� �̹���
	//-------------------------------------
	public void MakeScore(int _score) {
		String score = "" + _score;			// ���ڷ� ��ȯ
		int L = score.length();				// ���ڿ��� ����

		// �� ��Ʈ���� �����
		imgScore = Bitmap.createBitmap(fonts[0].getWidth() * L, fonts[0].getHeight(), fonts[0].getConfig());

		// ��Ʈ�ʿ� �̹����� �׷����� Canvas ����
		Canvas canvas = new Canvas();
		// Canvas�� ��Ʈ�ʿ� ����ϵ��� ���� - ������ ��� ����� imgScore�� �׷����� 
		canvas.setBitmap(imgScore);
		
		int w = fonts[0].getWidth();	 
		for (int i = 0; i < L; i++) {
			int n = (int) score.charAt(i) - 48;
			canvas.drawBitmap(fonts[n], w * i, 0, null); 
		}

		sw = imgScore.getWidth() / 2;
		sh = imgScore.getHeight() / 2;
	}

	//-------------------------------------
	//  Move
	//-------------------------------------
	public boolean Move() {
		y -= 4;
		if (y < -20)
			return false;
		else
			return true;
	}
}
