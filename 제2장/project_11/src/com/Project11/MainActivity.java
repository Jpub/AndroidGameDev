package com.Project11;

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
    // �Ѿ� ����    
    //----------------------------------
    class Bullet {
    	public int x, y;    // �Ѿ˱��� ��ǥ 
    	public Bitmap hole; // �Ѿ˱��� �̹��� 
    	public int bw, bh;	// �Ѿ˱���  size
    	
    	public Bullet(int _x, int _y) {
			hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
			bw = hole.getWidth() / 2;
			bh = hole.getHeight() / 2;
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
    	ArrayList<Bullet> mBullet;  // �Ѿ� ����
    	Bitmap imgBack, imgTarget;	// ���, ����
    	
    	int Score[] = {10, 8, 6, 0};  // ������ ����
    	int n = 3;                    // ���� �迭 ÷��  
    	int tot = 0;                  // ���� �հ�

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
			imgTarget = BitmapFactory.decodeResource(context.getResources(), R.drawable.target_1);
			imgTarget = Bitmap.createScaledBitmap(imgTarget, 280, 280, true);
			
			tw = imgTarget.getWidth() / 2;	// ������ �߽���
			th = imgTarget.getHeight() / 2;
			
	    	mBullet = new ArrayList<Bullet>();  // ArrayList ����
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
			canvas.drawText("���� = " + Score[n], 10, 30, paint);
			canvas.drawText("�հ� = " + tot, 200, 30, paint);
			canvas.drawBitmap(imgTarget, cx - tw, cy - th, null);
			for (Bullet tBullet : mBullet) {
				canvas.drawBitmap(tBullet.hole, tBullet.x - tBullet.bw, tBullet.y - tBullet.bh, null);  
			}
		} // onDraw

		//------------------------------------
        //      onTouch Event
        //------------------------------------
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int r[] = {40, 90, 140};
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				
				n = 3;
				// ��� �� �������� ����
				for (int i = 0; i < 3; i++) {
					if (Math.pow(cx - x, 2) + Math.pow(cy - y, 2) <= Math.pow(r[i], 2)) {
						mBullet.add(new Bullet(x, y));
						n = i;
						tot += Score[n];	// ����
						break;
					}
				} // for
			}
			invalidate();	// onDraw() ȣ�� 
			return true;
		}
		
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

