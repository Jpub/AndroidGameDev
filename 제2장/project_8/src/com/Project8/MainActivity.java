package com.Project8;

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
    	int width, height;		// View�� ũ��
    	int cx, cy;				// ȸ����(�߽���)			
    	int tw, th;				// �������� ȸ����
    	int sw, sh;				// �׸����� ũ��
    	int ang, dir;			// ������ ����, ȸ�� ����
    	int an1, an2; 			// �¿��� �Ѱ���
    	
    	Bitmap imgBack, imgToy, imgShadow;

    	//----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight();   // View�� ���� ����
			
			cx = width / 2;
			cy = height / 2 + 100;		// View�� �߽ɺ��� �Ʒ��� �̵� 
			
			// ��� �̹����� �а� View ũ�⿡ �°� �÷���
			imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);
			
			// �����̿� �׸��� �̹��� �б�
			imgToy = BitmapFactory.decodeResource(context.getResources(), R.drawable.toy);
			imgShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
			
			tw = imgToy.getWidth() / 2;		// ĳ������ �߽���
			th = imgToy.getHeight();		// �������� �߽��� ������ �ϴܺ�
			sw = imgShadow.getWidth() / 2;	// �׸����� �߽���
			sh = imgShadow.getHeight() / 2;
			
			ang = 0;			// ȸ�� ����
			dir = 0;			// ȸ�� ����
    		mHandler.sendEmptyMessageDelayed(0, 10);
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			RotateToy();	// ȸ�� �� ���ϱ�
			
			// ���� �׸��� �׸���
			canvas.drawBitmap(imgBack, 0, 0, null);
			canvas.drawBitmap(imgShadow, cx - sw, cy - sh, null);
			
			canvas.rotate(ang, cx, cy);		// Canvas ȸ��
			canvas.drawBitmap(imgToy, cx - tw, cy - th, null);
			canvas.rotate(-ang, cx, cy);	// Canvas�� ������ ���·� ����
		} // onDraw

        //----------------------------------
        //  ���ѱ� ȸ���ϱ�     
        //----------------------------------
		private void RotateToy() {
			ang += dir;		   					// ���� ����/����
			if (ang <= an1 || ang >= an2) {		// �¿��� �Ѱ����� �ٴٸ���
				an1 ++;; 						// ���� ����
				an2 --;                        	// �������� ����
				dir = -dir;						// ȸ���� ���� ������Ŵ
				ang += dir;						// ����� ���� ���·�
			}
		}

		//------------------------------------
        //      Timer Handler
        //------------------------------------
        Handler mHandler = new Handler() {          
        	public void handleMessage(Message msg) {
        		invalidate();	                       
        		mHandler.sendEmptyMessageDelayed(0, 10);
        	}
        }; // Handler

		//------------------------------------
        //      onTouch Event
        //------------------------------------
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				an1 = -15;			// ���� �Ѱ�
				an2 = 15;			// ������ �Ѱ�
				if (dir == 0)		       
					dir = -1;		// �� ó���� �������� �����
			}
			return true;
		}
		
    } // MyView

} // Activiry

