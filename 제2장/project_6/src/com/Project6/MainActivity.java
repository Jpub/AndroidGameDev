package com.Project6;

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

    	//----------------------------------
        //  ������(Constructor)     
        //----------------------------------
		public MyView(Context context) {
			super(context);
			
		}
		
        //----------------------------------
        //  onDraw     
        //----------------------------------
		public void onDraw(Canvas canvas) {
			int cx = getWidth() / 2;       // View �� �߽���
			int cy = getHeight() / 2;
			int w = 0;                 
			int h = 0;
			int DIRECTION = 4; 	    // �̹��� ���� (��� 1~4)

			Bitmap rose[] = new Bitmap[4];
			Resources res = getResources();

			rose[0] = BitmapFactory.decodeResource(res, R.drawable.rose_1);
			rose[1] = BitmapFactory.decodeResource(res, R.drawable.rose_2);
			rose[2] = BitmapFactory.decodeResource(res, R.drawable.rose_3);
			rose[3] = BitmapFactory.decodeResource(res, R.drawable.rose_4);
			
			Paint paint = new Paint();
			paint.setColor(Color.RED);                
			paint.setStyle(Paint.Style.STROKE);	// �ܰ����� �׸���

			canvas.drawColor(Color.WHITE);		// View�� ������� ä���
			switch (DIRECTION) {
			case 1 :  // �� 
				w = rose[0].getWidth() / 2;		// ȸ���� ���
				h =rose[0].getHeight();
				break;
			case 2 :  // �� 
				w = 0;
				h = rose[1].getHeight() / 2;
				break;
			case 3 :  // �� 
				w = rose[2].getWidth() / 2;
				h = 0;
				break;
			case 4 :  // �� 
				w = rose[3].getWidth();
				h = rose[3].getHeight() / 2;
			} // switch
			
			// ���α׷� Ÿ��Ʋ ����
			getWindow().setTitle("��̲� ȸ��  " + DIRECTION);  
	   
			// 10, 10 ��ġ�� ������ �̹��� ���
			canvas.drawBitmap(rose[DIRECTION - 1], 10, 10, null);
			// ȸ������ ���� ������ ǥ��
			canvas.drawCircle(w + 10, h + 10, 10, paint);	
	   
			// 20�� �������� ȸ���ϸ� 18�� �ݺ�
			for (int i = 1; i <= 18; i++) {
				// Canvas�� �߽����� ȸ�������� 20�� ȸ��
				canvas.rotate(20, cx, cy);
				// �߽����� �̹��� ���� ������ ���
	            canvas.drawBitmap(rose[DIRECTION - 1], cx - w, cy - h, null);    
	       } // for
		} // onDraw

    } // MyView

} // Activiry

