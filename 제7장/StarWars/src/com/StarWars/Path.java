package com.StarWars;

import java.util.ArrayList;

import android.util.Log;

//---------------------------------
// Path ArrayList
//---------------------------------
public class Path {
	private ArrayList<SinglePath> mPath = new ArrayList<SinglePath>();
	
	//---------------------------------
	// ������
	//---------------------------------
	public Path(String str) {
	  String tmp[] = str.split("\n");
	  for (int i = 0; i < tmp.length; i++) {
		  if (tmp[i].indexOf("//") >= 0 || tmp[i].trim().equals("")) continue;	
		  mPath.add(new SinglePath(tmp[i]));
	  }
		  
	  Log.v("Path", "Make path success");
	}

	//---------------------------------
	//  GetPath
	//---------------------------------
	public SinglePath GetPath(int index) {
		return mPath.get(index);
	}
	
}

//---------------------------------
// Single Path
//---------------------------------
class SinglePath {
	public int startX;			// ���� ��ǥ
	public int startY;
	public int dir[];			// ��� ����
	public int len[];			// ��� �Ÿ�

	//---------------------------------
	//  ������
	//---------------------------------
	public SinglePath(String str) {
		// str = " 0: 200,-30:  8-43, 9-3, 10-3, 11-3, 12-3, 13-3, 14-13";
		
		String tmp[] = str.split(":");   // tmp[1]: ���� ��ġ, tmp[2]: path

		int n = tmp[1].indexOf(',');
		startX = Integer.parseInt(tmp[1].substring(0, n).trim());
		startY = Integer.parseInt(tmp[1].substring(n + 1).trim());

		String stmp[] = tmp[2].split(",");
		n = stmp.length;

		dir = new int[n];
		len = new int[n];
		
		int p;
		for (int i = 0; i < n; i++) {
			p = stmp[i].indexOf('-');
			dir[i] = Integer.parseInt(stmp[i].substring(0, p).trim());
			len[i] = Integer.parseInt(stmp[i].substring(p + 1).trim());
		}
	}

}
