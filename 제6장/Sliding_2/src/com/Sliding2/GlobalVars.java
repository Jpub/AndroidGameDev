
//-------------------------------------
// Game�� ���� ���� - Option Menu���� �ۼ�
//-------------------------------------
package com.Sliding2;

import android.app.*;

//-----------------------------------
// ���� ���� ������
//-----------------------------------
public class GlobalVars extends Application {
	private int xCount;			// Slide X ����
	private int yCount;			// Slide Y ����
	private int backGround;		// 1: Snow, 2: Bubble
	private int imageType;		// 1: default, 2: user image
	private int storageType;	// 1: intenal, 2: external(Sd Casrd)
	private int imageId;		// �̹��� id
	private boolean isLoad;		// true: load, false: default
	

	//-----------------------------
	// xCount
	//-----------------------------
	public int getXCount() {
		return xCount;
	}
	
	public void setXCount(int value) {
		xCount = value;
	}

	//-----------------------------
	// yCount
	//-----------------------------
	public int getYCount() {
		return yCount;
	}
	
	public void setYCount(int value) {
		yCount = value;
	}

	//-----------------------------
	// backGround
	//-----------------------------
	public int getBackground() {
		return backGround;
	}
	
	public void setBackground(int value) {
		backGround = value;
	}

	//-----------------------------
	// imageType
	//-----------------------------
	public int getImageType() {
		return imageType;
	}
	
	public void setImageType(int value) {
		imageType = value;
	}

	//-----------------------------
	// stroageType
	//-----------------------------
	public int getStorageType() {
		return storageType;
	}
	
	public void setStorageType(int value) {
		storageType = value;
	}
	
	//-----------------------------
	// imageId
	//-----------------------------
	public int getImageId() {
		return imageId;
	}
	
	public void setImageId(int value) {
		imageId = value;
	}
	
	//-----------------------------
	// isLoad
	//-----------------------------
	public boolean getLoad() {
		return isLoad;
	}

	public void setLoad(boolean value) {
		isLoad = value;
	}
}

