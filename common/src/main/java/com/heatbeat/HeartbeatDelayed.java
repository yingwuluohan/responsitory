package com.heatbeat;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by fn on 2017/2/27.
 */
public abstract class HeartbeatDelayed implements Delayed {
    private long time;
    private final long sequenceNumber;
    private long nano_origin = System.nanoTime();
    private static final AtomicLong sequencer = new AtomicLong(0L);
    private long nextFireTime;

    public boolean isCycle() {
        return false;
    }

    public HeartbeatDelayed(long nsTime, TimeUnit timeUnit) {
        this.nextFireTime = this.nano_origin;
        this.time = TimeUnit.NANOSECONDS.convert(nsTime, timeUnit);
        this.sequenceNumber = sequencer.getAndIncrement();
        this.nextFireTime = this.time + this.nano_origin;
    }

    public abstract String getName();

    public void setDelayedTime(long time, TimeUnit timeUnit) {
        this.nano_origin = System.nanoTime();
        this.time = TimeUnit.NANOSECONDS.convert(time, timeUnit);
        this.nextFireTime = time + this.nano_origin;
    }

    public void cancel() {
    }

    public void reset() {
        this.nano_origin = System.nanoTime();
        this.nextFireTime = this.time + this.nano_origin;
    }

    public long getDelay(TimeUnit unit) {
        long d = unit.convert(this.time - this.now(), TimeUnit.NANOSECONDS);
        return d;
    }

    public abstract Status doCheck();

    public int compareTo(Delayed other) {
        if(other == this) {
            return 0;
        } else {
            HeartbeatDelayed x = (HeartbeatDelayed)other;
            long diff = this.nextFireTime - x.nextFireTime;
            return diff < 0L?-1:(diff > 0L?1:(this.sequenceNumber < x.sequenceNumber?-1:1));
        }
    }

    final long now() {
        return System.nanoTime() - this.nano_origin;
    }
}
