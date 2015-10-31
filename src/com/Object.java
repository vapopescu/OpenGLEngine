package com;

import java.util.*;

public class Object {
	protected String name;
	protected boolean ready = false;
	protected boolean loaded = false;
	protected ArrayList<Object> child = new ArrayList<Object>();
	
	public static ArrayList<Object> list = new ArrayList<Object>();
	
	private class Thread extends java.lang.Thread {
		private Object obj;
		
		public Thread(Object x) {
			obj = x;
		}
		
		public void run() {
			synchronized (obj) {
				obj.thread();
				obj.notifyAll();
				list.add(obj);
				obj.ready = true;
				
			}
		}
	}

	protected void thread() {
		
	}
	
	public static float readyPercent() {
		int size = list.size(), s = 0;
		for (int i = 0; i < size; i++)
			if (list.get(i).ready())
				s++;
		return (float) s / size;
	}
	
	public static float loadPercent() {
		int size = list.size(), s = 0;
		for (int i = 0; i < size; i++)
			if (list.get(i).loaded())
				s++;
		return (float) s / size;
	}
	
	public void load() {
		if(ready && !loaded)
			loaded = true;
		loadChildren();
	}
	
	protected void loadChildren() {
		for (int i = 0; i < child.size(); i++)
			child.get(i).load();
	}

	protected void startThread() {
		Thread loader = new Thread(this);
		loader.start();
	}
	
	public boolean ready() {
		return ready;
	}
	
	public boolean readyAll() {
		boolean ok = true;
		for (int i = 0; i < child.size(); i++)
				ok = ok && child.get(i).readyAll();
		return ready && ok;
	}
	
	public boolean loaded() {
		return loaded;
	}
	
	public boolean loadedAll() {
		boolean ok = true;
		for (int i = 0; i < child.size(); i++)
				ok = ok && child.get(i).loadedAll();
		return loaded && ok;
	}
	
	public void destroy() {
		
	}
	
	public void destroyAll() {
		for (int i = 0; i < child.size(); i++)
			child.get(i).destroy();
		destroy();
	}
}