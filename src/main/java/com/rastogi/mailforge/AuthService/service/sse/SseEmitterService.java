package com.rastogi.mailforge.AuthService.service.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {

    private final Map<String, SseEmitter> deviceEmitters = new ConcurrentHashMap<>();

    private static final long SSE_TIMEOUT = 5 * 60 * 1000L;

    private final ObjectMapper mapper = new ObjectMapper();

    public SseEmitter subscribe(String deviceId){

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        emitter.onCompletion(() -> {
            log.info("SSE connection completed for device: {}", deviceId);
            deviceEmitters.remove(deviceId);
        });
        emitter.onTimeout(() -> {
            log.info("SSE connection timed out for device: {}", deviceId);
            emitter.complete();
            deviceEmitters.remove(deviceId);
        });
        emitter.onError((ex) -> {
            log.error("SSE connection error for device: {}", deviceId, ex);
            deviceEmitters.remove(deviceId);
        });

        deviceEmitters.put(deviceId,emitter);

        return emitter;

    }


    public boolean notifyPrimaryDevice(String deviceId,Map<String, Object> payload) {

        SseEmitter sseEmitter = deviceEmitters.get(deviceId);

        if (sseEmitter == null) {
            log.warn("No connection found for deviceId {}", deviceId);
            return false;
        }

        try {
            String jsonData = mapper.writeValueAsString(payload);

            sseEmitter.send(SseEmitter.event()
                    .name("LOGIN_OTP")
                    .data(jsonData));

            log.info("OTP sent to device: {}", deviceId);
            return true;

        } catch (Exception e) {
            log.error("Failed to send SSE to device: {}", deviceId, e);
            deviceEmitters.remove(deviceId);
            return false;
        }
    }

    public boolean isDeviceOnline(String deviceId) {
        return deviceEmitters.containsKey(deviceId);
    }
    public void removeEmitter(String deviceId) {
        SseEmitter emitter = deviceEmitters.remove(deviceId);
        if (emitter != null) {
            emitter.complete();
        }
    }
}

