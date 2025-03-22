package sypztep.hawsamoot.common.util;

public interface ItemEntityGroundTimeAccessor {
    long getGroundHitTime();
    void setGroundHitTime(long time);
    boolean getWasOnGroundLastTick();
    void setWasOnGroundLastTick(boolean state);
}