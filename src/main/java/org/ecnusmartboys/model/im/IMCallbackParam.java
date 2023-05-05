package org.ecnusmartboys.model.im;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IMCallbackParam {

    @JsonProperty("SdkAppid")
    private String sdkAppid;

    @JsonProperty("CallbackCommand")
    private String callbackCommand;

    @JsonProperty("ClientIP")
    private String clientIP;

    @JsonProperty("OptPlatform")
    private String optPlatform;

    @JsonProperty("Sign")
    private String sign;

    @JsonProperty("RequestTime")
    private Long requestTime;
}
