package com.Project6;

import android.content.*;
import android.graphics.*;

//-----------------------------
// Block 
//-----------------------------
class Block {
	public int x1, y1, x2, y2, score;	// ��� ��ǥ, ����
	public Bitmap imgBlk;				// �̹���
	
	//-----------------------------
	// ������ (Constructor) 
	//-----------------------------
	public Block(Context context, float x, float y, float num) {
		x1 = (int) (MyGameView.M_left + x * MyGameView.B_width);
		y1 = (int) (MyGameView.M_top + y * MyGameView.B_height);

		x2 = x1 + MyGameView.B_width - 3;
		y2 = y1 + MyGameView.B_height - 3;
		score = (int) (num + 1) * 50;
		imgBlk = BitmapFactory.decodeResource(context.getResources(), R.drawable.k0 + (int) num);
		imgBlk = Bitmap.createScaledBitmap(imgBlk, MyGameView.B_width, MyGameView.B_height, true);
	}
}

//-----------------------------------------------------

//-----------------------------
// Ball 
//-----------------------------
class Ball {
	public int x, y, bw, bh;		// ���� ��ġ, ũ��
	public int sx, sy;				// ���� �ӵ�
	public boolean isMove = false;	// �̵����ΰ�
	public Bitmap imgBall;			// �̹���

	private int width, height;
	
	//-----------------------------
	// ������ (Constructor) 
	//-----------------------------
	public Ball(Context context, int _x, int _y, int _width, int _height) {
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		sx = 3;			// �ʱ� �ӵ�
		sy = -4;
		imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
		bw = imgBall.getWidth() / 2;
		bh = bw;
	}
	
	//-----------------------------
	// Move 
	//-----------------------------
	public boolean Move() {
		if (isMove == false) return true;
		x += sx;
		if (x < bw || x > width - bw) {		// �¿� ��
			sx = -sx;
			x += sx;
		}
		
		y += sy;
		if (y >= height + bh) return false;	// �ٴ�	
		if (y < bh) {						// õ��
			sy = -sy;				
			y += sy;
		}
		return true;
	}
}

//-----------------------------------------------------

//-----------------------------
// �е� 
//-----------------------------
class Paddle {
	public int x, y, pw, ph;	// ��ǥ
	public Bitmap imgPdl;		// �̹���
	public int sx; 

	private int width;
	
	//-----------------------------
	// ������ (Constructor) 
	//-----------------------------
	public Paddle(Context context, int _x, int _y, int _width) {
		x = _x;			// �е� ���� ��ǥ
		y = _y;
		width = _width;
		pw = MyGameView.B_width * 4 / 6;	// �е� �� - ����� 4/3ũ��
		ph = MyGameView.B_width / 6;		// �е� ���� - ��� ���� 1/3

		imgPdl = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle);
		imgPdl = Bitmap.createScaledBitmap(imgPdl, pw * 2, ph * 2, true);
	}
	
	//-----------------------------
	//  Move 
	//-----------------------------
	public void Move() {
		x += sx;
		if (x < pw || x > width - pw ) sx = 0;
	}
}

