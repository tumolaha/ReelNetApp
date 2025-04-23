package com.learning.reelnet.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reelnet.common.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to provide API information
 */
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Info", description = "API information endpoints")
public class APIInfoController {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${server.servlet.context-path:/api/v1}")
    private String contextPath;

    /**
     * Get API information
     * @return API information
     */
    @GetMapping
    @Operation(summary = "Get API information", description = "Returns basic API information")
    public ApiResponse<APIInfo> getAPIInfo() {
        log.debug("REST request to get API information");
        APIInfo info = new APIInfo();
        info.setVersion(appVersion);
        info.setBasePath(contextPath);
        info.setName("ReelNet API");
        info.setDescription("RESTful API for the ReelNet application");
        return ApiResponse.success(info, "API information retrieved successfully");
    }

    @Data
    public static class APIInfo {
        private String name;
        private String version;
        private String basePath;
        private String description;
    }
} 