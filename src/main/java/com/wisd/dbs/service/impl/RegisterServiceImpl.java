package com.wisd.dbs.service.impl;

import com.wisd.dbs.bean.*;
import com.wisd.dbs.exception.MyException;
import com.wisd.dbs.live.MediaServerHolder;
import com.wisd.dbs.live.RecordorHolder;
import com.wisd.dbs.mapper.EquipmentMapper;
import com.wisd.dbs.mapper.LiveRoomMapper;
import com.wisd.dbs.mapper.ManagerMapper;
import com.wisd.dbs.record.RecordorServer;
import com.wisd.dbs.service.LivePublishService;
import com.wisd.dbs.service.LiveViewService;
import com.wisd.dbs.service.RegisterService;
import com.wisd.dbs.session.SessionHolder;
import com.wisd.dbs.utils.IdUtil;
import com.wisd.dbs.wbp.WbpContextHolder;
import com.wisd.dbs.wbp.WbpRequestHolder;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;

import static com.wisd.dbs.bean.StaticConsts.*;

/**
 * 登陆逻辑处理
 *
 * @author scarlet
 */
@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final SessionHolder sessionHolder;
    private final LiveRoomMapper liveRoomMapper;
    private final EquipmentMapper equipmentMapper;
    private final LiveViewService liveViewService;
    private final LivePublishService livePublishService;
    private final ManagerMapper managerMapper;

    /**
     * 登陆直播间
     *
     * @param liveRoom
     * @return
     */
    @Override
    public Response signinLiveRoom(LiveRoom liveRoom) {
        Function<Device, Response> verifyLr = device -> {
            val id = liveRoom.getId();
            LiveRoom lr = liveRoomMapper.findByRoomId(id);
            if (lr == null) {
                return Response.build(ReturnCode.liveRoomNotExist);
            }
            if (verify(liveRoom)) {
                return Response.ok(lr);
            } else {
                return Response.build(ReturnCode.verifyFailed);
            }
        };
        return baseSignin(liveRoom, verifyLr);
    }

    /**
     * 登陆终端
     *
     * @param endpoint
     * @return
     */

    @Override
    public Response signinEndpoint(Endpoint endpoint) {
        Function<Device, Response> verifyEp = device -> {
            final val epType = endpoint.getEpType();
            if (epType != null && epType != 0) {
                if (epType != StaticConsts.EP_PREVIEW) {
                    endpoint.setName("终端" + endpoint.getId());
                    return Response.ok(endpoint);
                }
            }
            val id = endpoint.getId();
            Endpoint ep = equipmentMapper.findById(id);
            if (ep == null) {
                return Response.build(ReturnCode.epNotExist);
            }
            if (verify(endpoint)) {
                ep.setEpType(epType);
                return Response.ok(ep);
            } else {
                return Response.build(ReturnCode.verifyFailed);
            }
        };
        return baseSignin(endpoint, verifyEp);
    }

    /**
     * 登陆媒体服务
     *
     * @param device
     * @return
     */
    @Override
    public Response signinMediaServer(Device device) {
        final val mediaServer = device.getMediaServer();
        final boolean b = mediaServer == null || mediaServer.getIp() == null ||
                          mediaServer.getPort() == null;
        return b ? Response.build(ReturnCode.invalidParam) : baseSignin(device);
    }

    /**
     * 登陆录制服务
     *
     * @param recordorServer
     * @return
     */
    @Override
    public Response signinRecordor(RecordorServer recordorServer) {
        return baseSignin(recordorServer);
    }

    /**
     * 复制必要数据并登陆
     *
     * @param param  参数
     * @param verify 身份认证方法
     * @return
     */
    private Response baseSignin(Device param, Function<Device, Response> verify) {
        final val response = verify.apply(param);
        if (response.getError() != ReturnCode.ok.getError()) {
            return response;
        }
        final val device = ((Device) response.getData());
        device.setExpire(param.getExpire());
        device.setType(param.getType());
        device.setMediaServer(param.getMediaServer());
        return baseSignin(device);
    }

    /**
     * 抽取的基础登陆逻辑
     *
     * @param device
     * @return
     */
    private Response baseSignin(Device device) {
        final val request = WbpContextHolder.get();
        final val id = device.getId();
        //获取会话有效时长
        val expire = device.getExpire();
        //过期的时间
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(expire);
        val d = new Session();
        StaticConsts.rwLock.writeLock().lock();
        try {
            //查看是否已经登陆
            val op = sessionHolder.getById(device.getId());
            if (op.isPresent()) {
                //如果已经登陆
                val dev = op.get();
                //如果已经过期
                if (sessionHolder.isExpired(dev.getSessionId())) {
                    //先退出
                    signout(dev, false);
                } else {
                    final val wbpRequest = WbpRequestHolder.get(id);
                    final val ipInHolder = wbpRequest.getFrom().getAddress();

                    final val ipThisTime = request.getFrom().getAddress();
                    if (!ipThisTime.equals(ipInHolder)) {
                        //不在同一ip登陆
                        return Response.build(ReturnCode.hasSigninWithOtherAddr);
                    }
                    //设置过期时间
                    dev.setExpireTime(expireTime);
                    if (device.getType() == MEDIA_SERVER) {
                        final val mediaServer = device.getMediaServer();
                        mediaServer.setId(device.getId());
                        dev.setMediaServer(mediaServer);
                        MediaServerHolder.add(mediaServer);
                    } else if (device.getType() == RECORD_SERVER) {
                        final val recordor = (RecordorServer) device;
                        RecordorHolder.add(recordor);
                    }
                    WbpRequestHolder.save(id, request);
                    //响应
                    return Response.ok(d.setSessionId(dev.getSessionId()));
                }
            }
            device.setExpireTime(expireTime);
            //设置sessionid
            String uuid = IdUtil.uuid();
            device.setSessionId(uuid);
            if (device.getType() == MEDIA_SERVER) {
                val mediaServer = device.getMediaServer();
                mediaServer.setId(device.getId());
                //向媒体服务容器存储此媒体服务
                MediaServerHolder.add(mediaServer);
            } else if (device.getType() == RECORD_SERVER) {
                final val recordor = (RecordorServer) device;
                RecordorHolder.add(recordor);
            }
            sessionHolder.saveSession(uuid, device);

            WbpRequestHolder.save(id, request);
            return Response.ok(d.setSessionId(uuid));
        } finally {
            StaticConsts.rwLock.writeLock().unlock();
        }
    }

    /**
     * 注销
     *
     * @param device
     * @param b      是否退出直播
     * @return
     */
    @Override
    public Response signout(Device device, boolean b) {
        String sessionId = device.getSessionId();
        StaticConsts.rwLock.readLock().lock();
        try {
            val dev = sessionHolder.get(sessionId);
            if (dev == null) {
                //如果并未登陆
                return Response.build(ReturnCode.sessionNotExist);
            }
            //此设备类型
            val type = dev.getType();
            switch (type) {
                //如果是终端
                case StaticConsts.ENDPOINT:
                    Endpoint ep = (Endpoint) dev;
                    //先停止观看 直播
                    liveViewService.stop(ep);
                    break;
                //如果是媒体服务
                case MEDIA_SERVER:
                    //从媒体服务容器中移除
                    MediaServerHolder.remove(dev.getMediaServer());
                    break;
                //如果是直播室
                case GUIDE_EP:
                    //先停止 直播
                    if (b) {
                        livePublishService.stop(dev);
                    }
                    break;
                case RECORD_SERVER:
                    //从录制服务容器中移除
                    RecordorHolder.remove((RecordorServer) dev);
                    break;
                default:
                    break;
            }
            //会话容器中移除
            sessionHolder.remove(sessionId);
            //传输协议客户端容器中移除
            WbpRequestHolder.remove(dev.getId());
        } finally {
            StaticConsts.rwLock.readLock().unlock();
        }
        return Response.ok();
    }

    /**
     * 保活
     *
     * @param device
     * @return
     */
    @Override
    public Response keepAlive(Device device) {
        val sessionId = device.getSessionId();
        val dev = sessionHolder.get(sessionId);
        if (dev == null) {
            //
            return Response.build((ReturnCode.sessionNotExist));
        }
        val expire = device.getExpire();
        if (expire <= 0) {
            return Response.build(ReturnCode.invalidParam);
        } else {
            rwLock.writeLock().lock();
            try {
                //保活，刷新过期时间
                sessionHolder.refreshTimeOut(sessionId, expire);
            } catch (MyException e) {
                //此会话已经超时的情况
                return Response.build(e);
            } finally {
                rwLock.writeLock().unlock();
            }
            return Response.ok();
        }
    }

    private boolean verify(Device device) {
        final val user = device.getUser();
        final val token = device.getToken();
        final val manager = managerMapper.findByUserName(user);
        if (manager == null) {
            return false;
        }
        final val pwd = manager.getPwd();
        return pwd.equalsIgnoreCase(token);
    }

    @Override
    public LiveRoom lrquery(Integer id) {
        return liveRoomMapper.findByRoomId(id);
    }

    final private EquipmentMapper epmapper;

    @Override
    public Endpoint epquery(Integer id) {
        return epmapper.findById(id);
    }
}