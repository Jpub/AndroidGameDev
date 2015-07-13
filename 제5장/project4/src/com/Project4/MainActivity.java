package com.Project4;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;

public class MainActivity extends Activity {

	// MyGameView�� ������ �����
	MyGameView mGameView; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	mGameView = (MyGameView) findViewById(R.id.mGameView); 
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    	mGameView.PauseGame();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    	mGameView.ResumeGame();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
    	mGameView.PauseGame();
    }
    
	//-------------------------------------
	//  Option Menu
	//-------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "��������");
		menu.add(0, 2, 0, "�Ͻ�����");
		menu.add(0, 3, 0, "�������");
		menu.add(0, 4, 0, "�����ʱ�ȭ");
		return true;
	}

	//-------------------------------------
	//  onOptions ItemSelected
	//-------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case 1:    // ����
        	mGameView.StopGame();
        	finish();
             break;
        case 2:    // �Ͻ� ����
        	mGameView.PauseGame();
             break;
        case 3:    // ��� ����
        	mGameView.ResumeGame();
            break;
        case 4:    // ���� �����
        	mGameView.RestartGame();
        }
        return true;
	}

}

