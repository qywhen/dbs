package com.wisd.dbs.session;

import com.wisd.dbs.bean.Device;
import com.wisd.dbs.bean.ReturnCode;
import com.wisd.dbs.bean.StaticConsts;
import com.wisd.dbs.exception.MyException;
import com.wisd.dbs.properties.LiveProperties;
import lombok.Getter;
import lombok.Synchronized;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 存储会话的容器
 *
 * @author scarlet
 * @date 2019-01-24
 * @time 15:09
 */
@Service
@Getter
public class SessionHolder {
    private final LiveProperties liveProperties;
    private ConcurrentHashMap<String, Device> dsMap = new ConcurrentHashMap<>(256);

    public SessionHolder(
            LiveProperties liveProperties) {
        this.liveProperties = liveProperties;
    }


    public Device get(String sessionId) {
        return dsMap.get(sessionId);
    }


    public Optional<Device> getByIdNotExpire(int id) {
        return dsMap.values()
                .stream()
                .filter(device -> device.getId().equals(id) &&
                                  !isExpired(device.getSessionId()))
                .findFirst();
    }

    public Optional<Device> getById(int id) {
        return dsMap.values()
                .stream()
                .filter(device -> device.getId().equals(id))
                .findFirst();
    }

    /**
     * 保活
     *
     * @param key
     */
    @Synchronized
    public void refreshTimeOut(String key, int expire) {
        if (isExpired(key)) {
            throw new MyException(ReturnCode.sessionExpired);
        }
        val device = dsMap.get(key);
        device.setExpireTime(LocalDateTime.now().plusSeconds(expire));
    }

    /**
     * 是否已经过期
     *
     * @param key
     * @return 已经过期则返回true 否则返回false
     */
    public boolean isExpired(String key) {
        return !dsMap.containsKey(key) ||
               dsMap.get(key).getExpireTime().isBefore(LocalDateTime.now());
    }


    /**
     * 查找最早将过期的用户
     *
     * @return
     */
    Device firstToExpired() {
        return dsMap.values()
                .stream()
                .filter(dev -> dev.getExpireTime().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Device::getExpireTime))
                .orElseGet(() -> {
                    val device = new Device();
                    device.setExpireTime(
                            LocalDateTime.now().plusSeconds(liveProperties.getPeriod()));
                    return device;
                });
    }


    public void saveSession(String sid, Device device) {
        dsMap.put(sid, device);
    }

    @Synchronized
    public Device remove(String key) {
        return dsMap.remove(key);
    }

    boolean isEmpty() {
        return dsMap.isEmpty();
    }

    public Device offline(int id) {
        return dsMap.keySet()
                .stream()
                .filter(key -> dsMap.get(key).getId().equals(id))
                .findFirst()
                .map(key -> dsMap.remove(key))
                .orElse(null);
    }

    /**
     * 所有已经登陆的设备
     *
     * @return
     */
    public List<Device> getLoginDevices() {
        return dsMap.values()
                .stream()
                .filter(device -> !isExpired(device.getSessionId()))
                .collect(Collectors.toList());
    }

    Map<String, Device> expiredDevices() {
        return dsMap.keySet()
                .stream()
                .filter(this::isExpired)
                .collect(Collectors.toMap(o -> o, s -> dsMap.get(s)));
    }

    int size() {
        return dsMap.size();
    }

    public List<Device> allEndPoint() {
        return dsMap.values()
                .stream()
                .filter(device -> device.getType() == StaticConsts.ENDPOINT)
                .collect(Collectors.toList());
    }
}
