package com.Project1;

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
        int cx, cy;   		    // ĳ������ ���� ��ǥ
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
			
			cx = width / 2;					// ȭ���� �߽���
			cy = height / 2;
			
			rabbit = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_1);
			rw = rabbit.getWidth() / 2;		// ĳ������ �߽���
			rh = rabbit.getHeight() / 2;
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			canvas.drawBitmap(rabbit, cx - rw, cy - rh, null);
		}
    } // MyView
    
} // Activiry

