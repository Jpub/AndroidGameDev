package com.Random;

import java.util.*;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
	// ���� ���� ����
    int Counter;					// ����ڰ� �з��� Ƚ��
    int n;							// ���� �߻���               	
    EditText edit;					// ����ڰ� �Է��ϴ� ��Ʈ��
    TextView tResult;				// ó�� ����� �Է��� ��Ʈ��
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ���� �ʱ�ȭ
        Counter = 0;
        n = new Random().nextInt(501) + 500;
        edit = (EditText) findViewById(R.id.EditText01);
        tResult = (TextView) findViewById(R.id.TextView02);
        findViewById(R.id.Button01).setOnClickListener(myButtonClick);
    } // onCreate ��
    
    //----------------------------------------
    //   Button OnClickListener
    //----------------------------------------
    Button.OnClickListener myButtonClick = new Button.OnClickListener() {
		public void onClick(View v) {
			String s;
			Counter++;
			int p = Integer.parseInt(edit.getText().toString());
			if (p < 500 || p > 1000) s = "�Է��� ���� 500~1000�� ������ϴ�";
			else if (p == n) s = Counter + "��°�� ���߼̽��ϴ�";
			else if (p > n) s = p + "���� ���� ���Դϴ�";
			else s = p + "���� ū ���Դϴ�";
			tResult.setText(s);
		}
    };

} // Activity�� ��


