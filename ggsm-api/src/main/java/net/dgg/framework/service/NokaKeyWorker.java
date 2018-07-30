package net.dgg.framework.service;

import org.springframework.beans.factory.annotation.Value;

/**
 * <p>@Title 数据表ID生成器</p>
 * <p>@Description </p>
 * <p>@Version 1.0.0</p>
 * <p>@author rebin</p>
 * <p>@date 2016年7月15日</p>
 * <p>xiefangjian@163.com</p>
 * <p>Copyright © Noka Laboratory.All Rights Reserved.</p>
 */
public class NokaKeyWorker {
	private final static long twepoch = 12888349746579L;
	// 机器标识位数
	private final static long workerIdBits = 5L;
	// 数据中心标识位数
	private final static long datacenterIdBits = 5L;
	// 机器ID最大值
	private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 数据中心ID最大值
	private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	// 毫秒内自增位
	private final static long sequenceBits = 12L;
	// 机器ID偏左移12位
	private final static long workerIdShift = sequenceBits;
	// 数据中心ID左移17位
	private final static long datacenterIdShift = sequenceBits + workerIdBits;
	// 时间毫秒左移22位
	private final static long timestampLeftShift = sequenceBits + workerIdBits+ datacenterIdBits;
	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
	private static long lastTimestamp = -1L;
	private long sequence = 0L;
	private  long workerId=1L;
	private  long datacenterId=1L;
	private static NokaKeyWorker nokaKeyWorker = null;

	public synchronized  NokaKeyWorker Init(){
		if(null==nokaKeyWorker){
			nokaKeyWorker= new NokaKeyWorker();
		}
		return nokaKeyWorker;
	}
	
	private NokaKeyWorker() {
		if (workerId > maxWorkerId || workerId < 0L) {
			throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0L) {
			throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
		}
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			try {
				throw new Exception("Clock moved backwards.  Refusing to generate id for "+ (lastTimestamp - timestamp) + " milliseconds");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (lastTimestamp == timestamp) {
			// 当前毫秒内，则+1
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				// 当前毫秒内计数满了，则等待下一秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		// ID偏移组合生成最终的ID，并返回ID
		long nextId = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}
}
