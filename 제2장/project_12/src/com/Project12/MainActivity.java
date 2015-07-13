package com.Project12;

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
    // ��Ʈ ȭ��    
    //----------------------------------
    class Arrow {
    	public int x, y;   		// ȭ�� ��ǥ 
    	public Bitmap dart; 	// ȭ�� �̹��� 
    	public int dh;			// ȭ��  size
    	
    	// ������ (Constructor)
    	public Arrow(int _x, int _y) {
    		dart = BitmapFactory.decodeResource(getResources(), 
    				R.drawable.dart);
			dh = dart.getHeight();
			x = _x;
			y = _y;
    	}
    }
    
    //----------------------------------
    //  MyView     
    //----------------------------------
    class MyView extends View {
    	int width, height;			// View�� ũ��
    	int cx, cy;					// View�� �߽�
    	int tw, th;					// ���� �߽�
   		ArrayList<Arrow> mArrow;  	// ȭ��
    	Bitmap imgBack, imgTarget;	// ���, ����
    	
		int arScore[] = {10, 6, 12, 4, 15, 8, 10, 6, 12, 4, 15, 8, 10};
		int score = 0;				 // ���
    	int tot = 0;                 // ���� �հ�
    	
    	class Point {
    		int x, y;
    	}
    	Point pt = new Point();

    	//----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight();   // View�� ���� ����
			
			cx = width / 2;
			cy = height / 2 - 30;			// View�� �߽ɺ��� ���� �̵� 
			
			// ��� �̹����� �а� View ũ�⿡ �°� �÷���
			imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);
			
			// ������ �̹����� 280x280���� ����
			imgTarget = BitmapFactory.decodeResource(context.getResources(), R.drawable.target_2);
			imgTarget = Bitmap.createScaledBitmap(imgTarget, 280, 280, true);
			
			tw = imgTarget.getWidth() / 2;	// ������ �߽���
			th = imgTarget.getHeight() / 2;
			
	    	mArrow = new ArrayList<Arrow>();  // ArrayList ����
	    	
	    	pt.x = 100;
	    	pt.y = 200;
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			paint.setTextSize(18);
			// ���
			canvas.drawBitmap(imgBack, 0, 0, null);
			// ����
			canvas.drawText("���� = " + score, 10, 30, paint);
			//canvas.drawText("����= " + deg, 200, 30, paint);
			canvas.drawText("�հ� = " + pt.x, 200, 30, paint);
			canvas.drawBitmap(imgTarget, cx - tw, cy - th, null);
			for (Arrow tDart : mArrow) {
				canvas.drawBitmap(tDart.dart, tDart.x, tDart.y - tDart.dh, null);  
			}
		} // onDraw

		//------------------------------------
        //      onTouch Event
        //------------------------------------
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				CalcScore(x, y);
				invalidate();	// onDraw() ȣ�� 
			}
			return true;
		} // TouchEvent

		//------------------------------------
        //    ���� ���
        //------------------------------------
		private void CalcScore(int x, int y) {
			int r[] = {40, 90, 140};	// ���� ������
			
			// ���� ���
			double deg = Math.atan2(x - cx,  y - cy) * 180 / Math.PI - 90;
			if (deg < 0) deg += 360;

			int n = 3;	// �� ���ʿ��� ������ 3��			
			score = 0;
			// ��� �� �������� ����
			for (int i = 0; i < 3; i++) {
				if (Math.pow(cx - x, 2) + Math.pow(cy - y, 2) <= Math.pow(r[i], 2)) {
					mArrow.add(new Arrow(x, y));	// ȭ�� �߰�
					for (int j = 0; j < 13; j++) {
						int k = j * 30 + 15;
						if (deg < k) {
							score = arScore[j] * n;		// ����
							tot += score;				// �հ�
							break;	
						}
					} // for j
					n--;
					if (score > 0) break;
				} // if
			} // for i
		} // CalcScore
		
    } // MyView

} // Activiry


