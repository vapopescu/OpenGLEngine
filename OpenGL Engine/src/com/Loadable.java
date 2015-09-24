package com;

public class Loadable {
	protected boolean finished = false;
	protected boolean loaded = false;
	protected int runPriority = 5; //Default priority.
	
	private class Thread extends java.lang.Thread {
		private Loadable obj;
		
		public Thread(Loadable x) {
			obj = x;
		}
		
		public void run() {
			obj.thread();
			synchronized(this) {
				notifyAll();
			}
			obj.finished = true;
		}
	}

	protected void thread() {
		
	}
	
	public void load() {
		if(finished && !loaded)
			loaded = true;
	}

	protected void startThread() {
		Thread loader = new Thread(this);
		loader.setPriority(runPriority);
		loader.start();
	}
	
	public float loadPercent() {
		if (finished)
			return 1.0f;
		else
			return 0.0f;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
}