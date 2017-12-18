package javaproject;

import java.util.ArrayList;

public class JobKindInfo {
	private ArrayList<String> list=new ArrayList<String>();
	private int[] cnt=new int[1000];		
	public ArrayList<String> getList()
	{
		return this.list;
	}
	public int[] getCnt()
	{
		return cnt;
	}
}
