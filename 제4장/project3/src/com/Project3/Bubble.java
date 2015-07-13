package com.Project3;

import java.util.*;

import android.content.*;
import android.graphics.*;

//-------------------------------------
//  �񴩹��
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
	private int counter = 0;		// ���� �浹 ȸ��
	
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
	    
	    sx = rnd.nextInt(2) == 0 ? -1 : 1;
	    sy = rnd.nextInt(2) == 0 ? -2 : 2;
	    MoveBubble();
	}
	
	//-------------------------------------
	//  Move
	//-------------------------------------
	public void MoveBubble() {
		loop++;
		if (loop % 3 == 0) {
			imgNum++;			// �񴩹�� ��ȣ
			if (imgNum > 5) imgNum = 0;
			imgBall = Bubbles[imgNum];
			
			// �񴩹���� ������ ����
			rad = _rad + (imgNum <= 3 ? imgNum : 6 - imgNum) * 2;   
		}
		
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

