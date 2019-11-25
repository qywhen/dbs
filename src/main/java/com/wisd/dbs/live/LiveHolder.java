package com.wisd.dbs.live;

import com.wisd.dbs.bean.LiveCoursePlan;
import com.wisd.dbs.bean.LiveRoom;
import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.utils.TimeUtil;
import lombok.val;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/26 10:29
 */
public class LiveHolder {
    private static ConcurrentHashMap<Integer, LiveRoom> lrMap = new ConcurrentHashMap<>(128);

    public static ConcurrentHashMap<Integer, LiveRoom> getLrMap() {
        return lrMap;
    }

    /**
     * 课程是否正在直播
     *
     * @param courseId
     * @return
     */
    public static boolean isLiving(String courseId) {
        return lrMap.values()
                .stream()
                .anyMatch(lr -> courseId.equals(lr.getCourse().getId()));
    }

    public static LiveRoom get(int liveId) {
        return lrMap.get(liveId);
    }

    public static LiveRoom remove(int liveId) {
        return lrMap.remove(liveId);
    }

    /**
     * 所有正在进行的直播
     *
     * @return
     */
    public static List<LiveCoursePlan> allLivingCourse() {
        return LiveHolder.lrMap.values()
                .stream()
                .map(LiveRoom::getCourse)
                .sorted(Comparator.comparing(LiveCoursePlan::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * 通过课程获取直播间
     *
     * @param courseId
     * @return
     */
    public static Optional<LiveRoom> getLiveRoomByCourseId(String courseId) {
        return lrMap.values().stream().filter(device -> {
            val course = device.getCourse();
            return device.getType() == StaticConsts.GUIDE_EP && course != null &&
                   courseId.equals(course.getId());
        }).findAny();
    }

    public static Optional<LiveRoom> getByRoomId(Integer roomId) {
        return lrMap.values().stream().filter(
                liveRoom -> roomId.equals(liveRoom.getId())).findFirst();
    }

    public static void put(int liveId, LiveRoom device) {
        lrMap.put(liveId, device);
    }

    public static int count() {
        return lrMap.size();
    }

    public static Collection<LiveRoom> all() {
        return lrMap.values();
    }

    public static boolean isEmpty() {
        return lrMap.isEmpty();
    }

    static Collection<LiveRoom> allFinished() {
        return lrMap.keySet()
                .stream()
                .filter(i -> lrMap.get(i).isOffline())
                .filter(i -> {
                    final val liveRoom = lrMap.get(i);
                    final val endTime = liveRoom.getCourse().getEndTime();
                    final val eTime = TimeUtil.parse(endTime);
                    return eTime.isBefore(LocalDateTime.now());
                })
                .map(i -> lrMap.get(i))
                .collect(Collectors.toList());
    }
}
