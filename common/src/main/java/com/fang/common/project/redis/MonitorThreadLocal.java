package com.fang.common.project.redis;



public class MonitorThreadLocal {

	private static final ThreadLocal<MonitorThreadLocal> locals = new ThreadLocal<MonitorThreadLocal>();

	public static MonitorThreadLocal get() {
		return locals.get();
	}

	public static MonitorThreadLocal set(String uuid, int level, int index) {
		MonitorThreadLocal local = new MonitorThreadLocal(uuid, level, index);
		locals.set(local);
		return local;
	}

	private MonitorThreadLocal(String uuid, int level, int index) {
		this.uuid = uuid;
		this.level = level;
		this.index = index;
	}

	public static void remove() {
		locals.remove();
	}

	private String uuid;

	private int level;

	private int index;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void nextIndex() {
		this.index++;
	}

}
