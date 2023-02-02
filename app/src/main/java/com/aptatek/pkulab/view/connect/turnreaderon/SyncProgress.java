package com.aptatek.pkulab.view.connect.turnreaderon;

public class SyncProgress {

    final int current;
    final int failed;
    final int total;

    public SyncProgress(int current, int failed, int total) {
        this.current = current;
        this.failed = failed;
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public int getFailed() {
        return failed;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "SyncProgress{" +
                "current=" + current +
                ", failed=" + failed +
                ", total=" + total +
                '}';
    }
}
