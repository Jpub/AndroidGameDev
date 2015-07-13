package com.Project_1;

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
    //  �񴩹��     
    //----------------------------------
    class MyBubble  {
    	public int x, y, rad;			// ��ġ, ������
 	   	public Bitmap imgBbl;			// �񴩹�� ��Ʈ�� �̹���
 	   	public boolean dead = false;	// ���������� ����

 	   	private int count = 0;			// ������ �浹 ȸ��
 	   	private int sx, sy;				// �̵� ����� �ӵ�
 	   	private int width, height;		// View�� ũ��

 	   	//----------------------------------
 	   	//  ������(Constructor)     
 	   	//----------------------------------
 	   	public 	MyBubble(int _x, int _y, int _width, int _height) {
 	   		x = _x;				// �Ķ���� ����
 	   		y = _y;
 	   		width = _width;		// View�� ũ��
 	   		height = _height;
    		
 	   		Random rnd = new Random();  
 	   		rad = rnd.nextInt(31) + 10;					// 10~40 : ������
 	   		int k = rnd.nextInt(2) == 0 ? -1 : 1;		// -1, 1
 	   		sx = (rnd.nextInt(4) + 2) * k;				// �� 2~5 : �ӵ�
 	   		sy = (rnd.nextInt(4) + 2) * k;				// �� 2~5

 	   		// ��Ʈ�� �̹����� ������ ������ ������ 2�� ũ��� �����
 	   		imgBbl = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
 	   		imgBbl = Bitmap.createScaledBitmap(imgBbl, rad * 2, rad * 2, false);
 	   		MoveBubble();		// �񴩹�� �̵�
 	   	}

 	   	//----------------------------------
 	   	//  �񴩹�� �̵�     
 	   	//----------------------------------
 	   	private void MoveBubble() {
 	   		x += sx;	// �̵�
 	   		y += sy;
 	   		if (x <= rad || x >= width - rad) {		// �¿��� ��
 	   			sx = -sx;							// �ݴ� �������� �ݻ�		
 	   			count++;							// ���� �ε�ģ Ƚ��
 	   		}
 	   		if (y <= rad || y >= height - rad) {
 	   			sy = -sy;
 	   			count++;
 	   		}
 	   		if (count >= 3) dead = true;			// ���� 2�� �̻� �浹�̸� �Ͷ߸�
 	   	}
    } // MyBubble
    
    //----------------------------------
    //  MyView     
    //----------------------------------
    class MyView extends View {
    	int width, height;		// View�� ���� ����
 	   	Bitmap imgBack;
 	   	ArrayList<MyBubble> mBubble; 
    	
 	   	//----------------------------------
 	   	//  ������(Constructor)     
 	   	//----------------------------------
 	   	public MyView(Context context) {
 	   		super(context);
 	   		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
 	   		width = display.getWidth();			// View�� ���� ��
 	   		height = display.getHeight() - 50;  // View�� ���� ����
			
 	   		imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
 	   		imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);
			
 	   		mBubble = new ArrayList<MyBubble>();
 	   		mHandler.sendEmptyMessageDelayed(0, 10);  // Handler ȣ��
 	   	}
		
 	   	//----------------------------------
 	   	//  �񴩹�� �̵�     
 	   	//----------------------------------
 	   	private void MoveBubble() {
 	   		for (int i = mBubble.size() - 1; i >= 0; i--) {
 	   			mBubble.get(i).MoveBubble();
 	   			if (mBubble.get(i).dead == true)
 	   				mBubble.remove(i);
 	   		}
 	   	}

 	   	//------------------------------------
 	   	//   Touch�� �񴩹�� �������� ����
 	   	//------------------------------------
 	   	private void CheckBubble(int x, int y) {
 	   		boolean flag = false;
 	   		for (MyBubble tmp :  mBubble) {
 	   			if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2) <= Math.pow(tmp.rad, 2)){
 	   				tmp.dead = true;
 	   				flag = true;
 	   			}
 	   		}
 	   		if (flag == false)	     // �񴩹�� Touch�� �ƴϸ� �񴩹�� ���� 
 	   			mBubble.add(new MyBubble(x, y, width, height));
 	   	}
        
       //----------------------------------
       //  onDraw     
       //----------------------------------
 	   	public void onDraw(Canvas canvas) {
 	   		MoveBubble();											// �񴩹�� �̵�
 	   		canvas.drawBitmap(imgBack, 0, 0, null);		// ���
 	   		for (MyBubble tmp :  mBubble) {					// �񴩹��
 	   			canvas.drawBitmap(tmp.imgBbl, tmp.x - tmp.rad, 
													tmp.y - tmp.rad, null);
 	   		}
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
		
 	   	//------------------------------------
 	   	//      onTouch Event
        //------------------------------------
 	   	@Override
 	   	public boolean onTouchEvent(MotionEvent event) {
 	   		if (event.getAction() == MotionEvent.ACTION_DOWN){
 	   			int x = (int) event.getX();
 	   			int y = (int) event.getY();
 	   			CheckBubble(x, y);		// �񴩹�� ����
 	   		}
 	   		return true;
 	   	} // onTouchEvent
 	   	
 	} // MyView
    
} // Activity
