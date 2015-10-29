package com;

import java.util.*;

public class Loadable {
	protected boolean ready = false;
	protected boolean loaded = false;
	protected int runPriority = 5; //Default priority.
	protected ArrayList<Loadable> subcomp = new ArrayList<Loadable>();
	
	private class Thread extends java.lang.Thread {
		private Loadable obj;
		
		public Thread(Loadable x) {
			obj = x;
		}
		
		public void run() {
			synchronized (obj) {
				obj.thread();
				obj.notifyAll();
				obj.ready = true;
			}
		}
	}

	protected void thread() {
		
	}
	
	public void load() {
		if(ready && !loaded)
			loaded = true;
		loadSubcomp();
	}
	
	protected void loadSubcomp() {
		for (int i = 0; i < subcomp.size(); i++)
			subcomp.get(i).load();
	}

	protected void startThread() {
		Thread loader = new Thread(this);
		loader.setPriority(runPriority);
		loader.start();
	}
	
	public float selfReady() {
		if (ready)
			return 1.0f;
		else
			return 0.0f;
	}
	
	public float readyPercent() {
		float f = 0;
		for (int i = 0; i < subcomp.size(); i++)
			f += subcomp.get(i).readyPercent();
		return (selfReady() + f) / (subcomp.size() + 1);
	}
	
	public boolean isLoaded() {
		boolean ok = true;
		for (int i = 0; i < subcomp.size(); i++)
				ok = ok && subcomp.get(i).isLoaded();
		return loaded && ok;
	}
	
	public void destroySubcomp() {
		for (int i = 0; i < subcomp.size(); i++)
			subcomp.get(i).destroy();
	}
	
	public void destroy() {
		
	}
}