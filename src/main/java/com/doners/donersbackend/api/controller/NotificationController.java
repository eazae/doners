package com.doners.donersbackend.api.controller;

import com.doners.donersbackend.application.dto.request.donation.NotificationReadDTO;
import com.doners.donersbackend.application.dto.response.BaseResponseDTO;
import com.doners.donersbackend.application.dto.response.donation.NotificationResponseDTO;
import com.doners.donersbackend.application.service.NotificationService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(value = "Notification API", tags = {"Notification"})
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "기부글 알림")
    @ApiResponses({
            @ApiResponse(code = 200, message = "알림이 있습니다."),
            @ApiResponse(code = 204, message = "알림이 없습니다."),
            @ApiResponse(code = 404, message = "알림 확인에 필요한 정보를 찾을 수 없습니다."),
            @ApiResponse(code = 409, message = "알림 확인에 실패했습니다.")
    })
    @GetMapping
    public ResponseEntity<? extends BaseResponseDTO> get(
            @ApiIgnore @RequestHeader("Authorization") String accessToken
    ) {

        NotificationResponseDTO notificationResponseDTO = null;

        try {
            notificationResponseDTO = notificationService.getNotification(accessToken);

            if (notificationResponseDTO == null) {
                return ResponseEntity.status(200).body(NotificationResponseDTO.of("알림이 없습니다.", 200));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(BaseResponseDTO.of("알림 확인에 필요한 정보를 찾을 수 없습니다.", 404));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(BaseResponseDTO.of("알림 확인에 실패했습니다.", 409));
        }

        return ResponseEntity.ok(NotificationResponseDTO.of("알림이 있습니다.", 200, notificationResponseDTO));

    }

    @ApiOperation(value = "알림 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "알림이 있습니다."),
            @ApiResponse(code = 204, message = "알림이 없습니다."),
            @ApiResponse(code = 404, message = "알림 확인에 필요한 정보를 찾을 수 없습니다."),
            @ApiResponse(code = 409, message = "알림 확인에 실패했습니다.")
    })
    @PatchMapping
    public ResponseEntity<? extends BaseResponseDTO> read(
            @ApiIgnore @RequestHeader("Authorization") String accessToken,
            @ApiParam @Valid @RequestBody NotificationReadDTO notificationReadDTO
    ) {

        try {
            notificationService.readNotification(accessToken, notificationReadDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(BaseResponseDTO.of("알림 확인에 필요한 정보를 찾을 수 없습니다.", 404));
        } catch (Exception e) {
            return ResponseEntity.status(409).body(BaseResponseDTO.of("알림 확인에 실패했습니다.", 409));
        }

        return ResponseEntity.ok(BaseResponseDTO.of("알림을 확인했습니다.", 200));

    }

}