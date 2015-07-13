package com.Project7;

import java.util.*;

import android.app.*;
import android.content.*;
import android.content.res.*;
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
        int cx, cy, cw;   	    // View�� �߾�, �ð��� ��
    	int pw, ph;             // �ð� �ٴ��� ���� ����
		private Bitmap clock;
		private Bitmap pins[] = new Bitmap[3];

		int hour, min, sec;			// ��, ��, ��
		float rHour, rMin, rSec;	// ȸ������
		
        //----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			cx = display.getWidth() / 2;			
			cy = (display.getHeight() - 100) / 2;  

			Resources res = context.getResources();
			clock = BitmapFactory.decodeResource(res, R.drawable.dial);
			pins[0] = BitmapFactory.decodeResource(res, R.drawable.pin_1);
			pins[1] = BitmapFactory.decodeResource(res, R.drawable.pin_2);
			pins[2] = BitmapFactory.decodeResource(res, R.drawable.pin_3);
			
			cw = clock.getWidth() / 2;
			pw = pins[0].getWidth() / 2; 
	    	ph = pins[0].getHeight() - 10;  
			
    		mHandler.sendEmptyMessageDelayed(0, 500);  // Handler ȣ��
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			CalcTime();
			canvas.drawColor(Color.WHITE);
			//
			// ----- ���� -----
			//
			getWindow().setTitle("scale(0.7, 1), rotate(30)");
			canvas.scale(0.7f, 1, cx, cy);
			canvas.rotate(30, cx, cy);
//			canvas.translate(-100, -100);
/*			
			canvas.scale(-0.8f, 0.8f, cx, cy);
			canvas.skew(-0.1f, 1);
			canvas.translate(0, -100);
*/			
			
			canvas.drawBitmap(clock, cx - cw, cy - cw, null);
			canvas.rotate(rHour, cx, cy); 
			canvas.drawBitmap(pins[2], cx - pw, cy - ph, null);

			canvas.rotate(rMin - rHour, cx, cy);      
			canvas.drawBitmap(pins[1], cx - pw, cy - ph, null);

			canvas.rotate(rSec - rMin, cx, cy);   
			canvas.drawBitmap(pins[0], cx - pw, cy - ph, null);
			canvas.rotate(-rSec, cx, cy);                                      

			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextSize(24);
			canvas.drawText(String.format("%2d : %2d : %d", hour, min, sec), cx - 40, cy + cw + 50, paint);
		}

		//------------------------------------
        //      �ð� ���
        //------------------------------------
        public void CalcTime() {
    		GregorianCalendar calendar = new GregorianCalendar();
			hour = calendar.get(Calendar.HOUR);
			min = calendar.get(Calendar.MINUTE);
			sec = calendar.get(Calendar.SECOND);
			
			rSec = sec * 6;
			rMin = min * 6 + rSec / 60;
			rHour = hour * 30 + rMin / 12;
        }
		
		//------------------------------------
        //      Timer Handler
        //------------------------------------
        Handler mHandler = new Handler() {          
        	public void handleMessage(Message msg) {
        		invalidate();	                       
        		mHandler.sendEmptyMessageDelayed(0, 500);
        	}
        }; // Handler
		
    } // MyView

} // Activiry

