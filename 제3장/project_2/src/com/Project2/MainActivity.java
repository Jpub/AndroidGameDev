package com.Project2;

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
    	public int x, y;    		// �Ѿ˱��� ��ǥ 
    	public Bitmap hole; 		// �Ѿ˱��� �̹��� 
    	public int bw, bh;			// �Ѿ˱���  size
    	public long lastTime;		// ��� �ð�
    	private int alpha = 255;	// �̹����� Alpha (����)
    	
        //----------------------------------
        // ������(Constructor)    
        //----------------------------------
    	public Bullet(int _x, int _y) {
			x = _x;
			y = _y;
			
			hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
			bw = hole.getWidth() / 2;
			bh = hole.getHeight() / 2;
			
			lastTime = System.currentTimeMillis();		// ���� �ð�
    	}
    	
        //----------------------------------
        // Alpha�� ����    
        //----------------------------------
    	public boolean MeltHole() {
    		alpha -= 15;
    		if (alpha <= 0) 
    			return true;
    		else
    			return false;
    	}
    } // Bullet
    
    //----------------------------------
    //  MyView     
    //----------------------------------
    class MyView extends View {
    	int width, height;				// View�� ũ��
    	int cx, cy;						// View�� �߽�
    	int tw, th;						// ���� �߽�
    	ArrayList<Bullet> mBullet;  	// �Ѿ� ����
    	Bitmap imgBack, imgTarget;		// ���, ����
    	
    	int Score[] = {10, 8, 6, 0};  	// ������ ����
    	int n = 3;                    	// ���� �迭 ÷��  
    	int tot = 0;                  	// ���� �հ�

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
	    	
    		mHandler.sendEmptyMessageDelayed(0, 10);  // Handler ȣ��
		}
		
        //----------------------------------
		//    �Ѿ˱��� ���̱�
        //----------------------------------
		private void MeltHoles() {
			long thisTime = System.currentTimeMillis();
			for (int i = mBullet.size() - 1; i >= 0; i--) {
				if (thisTime - mBullet.get(i).lastTime >= 2000) {
					if (mBullet.get(i).MeltHole() == true)
						mBullet.remove(i);
				}
			}
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

			MeltHoles();
			for (Bullet tBullet : mBullet) {
				paint.setAlpha(tBullet.alpha);
				canvas.drawBitmap(tBullet.hole, tBullet.x - tBullet.bw, tBullet.y - tBullet.bh, paint);  
			}
			paint.setAlpha(255);
		} // onDraw

		//------------------------------------
        //      Timer Handler
        //------------------------------------
        Handler mHandler = new Handler() {          
        	public void handleMessage(Message msg) {
        		invalidate();		// View�� �ٽ� �׸�                       
        		mHandler.sendEmptyMessageDelayed(0, 10);
        	}
        }; // Handler
		
		
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

