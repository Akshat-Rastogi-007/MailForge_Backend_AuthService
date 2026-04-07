package com.rastogi.mailforge.AuthService.controller.sse;

import com.rastogi.mailforge.AuthService.service.sse.SseEmitterService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/app/v1/sse/")
public class SseController {

    private final SseEmitterService sseEmitterService;

    public SseController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    @GetMapping(value = "subscribe/{deviceId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String deviceId, Authentication authentication) {

        // Subscribe to SSE events for this device
        return sseEmitterService.subscribe(deviceId);
    }
}
