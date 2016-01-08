package org.androidannotations.api.mvc;

public abstract class PriorityTask implements Runnable, Comparable<PriorityTask> {

	private int priority;
	@Override
	public int compareTo(PriorityTask other) {
		return this.priority-other.priority;
	}

	
	public PriorityTask(int priority) {
		super();
		this.priority = priority;
	}


	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
