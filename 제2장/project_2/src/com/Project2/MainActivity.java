package com.Project2;

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
        
        // ��ü ȭ�� ���
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // ����� View ���
        setContentView(new MyView(this));
    }
    
    //----------------------------------
    //  MyView     
    //----------------------------------
    class MyView extends View {
    	int width, height;		// View�� ���� ����
        int x, y;   		    // ĳ������ ���� ��ǥ
        int sx, sy;       		// ĳ���Ͱ� �̵��� ����� �Ÿ�
    	int rw, rh;             // ĳ������ �߽���
    	Bitmap rabbit;
    	
    	
        //----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();		// View�� ���� ��
			height = display.getHeight();   // View�� ���� ����
			
			rabbit = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_1);
			rw = rabbit.getWidth() / 2;		// ĳ������ �߽���
			rh = rabbit.getHeight() / 2;
			
			x = 100;	// ĳ������ �ʱ� ��ǥ
			y = 100;
			sx = 3;		// ĳ���Ͱ� 1ȸ�� �̵��� �Ÿ�
			sy = 3;
			
    		mHandler.sendEmptyMessageDelayed(0, 10);  // Handler ȣ��
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			x += sx;				// �������� �̵�
			y += sy;				// �������� �̵�

/*          ���� ����� ����� ó��			
			if (x < -rw) 		 x = width + rw;
			if (x > width + rw)  x = - rw;
			if (y < -rh) 		 y = height + rh;
			if (y > height + rh) y = - rh;
*/			
			// ���� �ݻ��� ����� ó��
			if (x < rw) { 			// ���� ���� �浿
				x = rw;
				sx =  -sx;
			}
			if (x > width - rw) {	// ������ ���� �浹 
				x = width - rw;
				sx =  -sx;
			}
			if (y < rh) { 			// õ���� �浹
				y = rh;
				sy =  -sy;
			}
			if (y > height - rh) { 	// �ٴڰ� �浹
				y = height - rh;
				sy =  -sy;
			}
			
			canvas.drawBitmap(rabbit, x - rw, y - rh, null);
		}
		
		 //------------------------------------
        //      Timer Handler
        //------------------------------------
        Handler mHandler = new Handler() {          
        	public void handleMessage(Message msg) {
        		invalidate();		// View�� �ٽ� �׸�                       
        		mHandler.sendEmptyMessageDelayed(0, 10);
        	}
        }; // Handler
		
    } // MyView
    
} // Activiry

