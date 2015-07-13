package com.Project14;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
    
    //----------------------------------
    //  MyView     
    //----------------------------------
    class MyView extends View {
    	int width, height;			// View�� ũ��
    	int left, top;				// ����, �� ����
    	int orgW, orgH;				// ������ �̹��� ũ��
    	int picW, picH;				// �߷��� ������ ũ��	
    	Bitmap imgBack, imgOrg;		// ���, ����
    	Bitmap imgPic[][] = new Bitmap[5][3];   // �߶��� ���� ����
    	
    	//----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight() - 50;   // View�� ���� ����
			
			// ��� �̹����� �а� View ũ�⿡ �°� �ø�
			imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);

			imgOrg = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_1);
			orgW = imgOrg.getWidth();	// ������ ũ��
			orgH = imgOrg.getHeight();
			
			picW = orgW / 3;			// ���� ������ ������
			picH = orgH / 5;			// ���� ������ ���� ����

			left = (width - orgW) / 2;	// ���� ����
			top = (height - orgH) / 2;	// ���� ����
			
			// ���� �ڸ���
			for (int i = 0; i < 5; i++)	{
				for (int j = 0; j < 3; j++)
					imgPic[i][j] = Bitmap.createBitmap(imgOrg, j * picW, i * picH, picW, picH);
			}
			// �� ������ ������ ����
			imgPic[4][2] = Bitmap.createBitmap(imgOrg, 0, 0, 1, 1);
			Shuffling();
		}
		
		//----------------------------------
        //  ���� ���� ����     
        //----------------------------------
        private void Shuffling() {
        	Bitmap tmp;
        	int x, y, n;
        	Random rnd = new Random();
        	for (int i = 0; i < 5; i++) {
        		for (int j = 0; j < 3; j++) {
        			if (i == 4 && j == 2) break; 	// �� ������ ����
        			n = rnd.nextInt(14);	// 0~13
        			y = n / 5;		// row
        			x = n % 3;		// col
        			tmp = imgPic[i][j];
        			imgPic[i][j] = imgPic[y][x];
        			imgPic[y][x] = tmp;
        		}
        	}
		}

		//----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			Paint paint = new Paint();
			EmbossMaskFilter emboss = new EmbossMaskFilter(new float[] {1, 1, 1}, 0.5f, 1, 1);
			paint.setMaskFilter(emboss);
			
			canvas.drawBitmap(imgBack, 0, 0, null);
			for (int i = 0; i < 5; i++)	{
				for (int j = 0; j < 3; j++)
					canvas.drawBitmap(imgPic[i][j], left + j * picW, top + i * picH, paint); 
			}
		} // onDraw

    } // MyView

	//------------------------------------
    //     onKeyDown
    //------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �ƹ� Ű�� ������ ���α׷� ����
		System.exit(0);
		return true;
	}

} // Activiry

