
//-------------------------------------
// Game�� ���� ���� - Option Menu���� �ۼ�
//-------------------------------------
package com.StarWars;

import android.app.*;

//-----------------------------------
// ���� ���� ������
//-----------------------------------
public class GlobalVars extends Application {
	private int diffcult;		// ���̵�
	private boolean isMusic;	// Music
	private boolean isSound;	// Sound
	private boolean isVibe;		// Vibrator

	//-----------------------------
	// Difficult
	//-----------------------------
	public int getDifficult() {
		return diffcult;
	}
	
	public void setDifficult(int value) {
		diffcult = value;
	}

	//-----------------------------
	// isMusic
	//-----------------------------
	public boolean getIsMusic() {
		return isMusic;
	}
	
	public void setIsMusic(boolean value) {
		isMusic = value;
	}

	//-----------------------------
	// isSound
	//-----------------------------
	public boolean getIsSound() {
		return isSound;
	}
	
	public void setIsSound(boolean value) {
		isSound = value;
	}

	//-----------------------------
	// isVibe
	//-----------------------------
	public boolean getIsVibe() {
		return isVibe;
	}
	
	public void setIsVibe(boolean value) {
		isVibe = value;
	}

}

