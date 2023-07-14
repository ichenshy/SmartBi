package com.chen.Bi.manager;

import com.chen.Bi.common.ErrorCode;
import com.chen.Bi.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author CSY
 * @version v1.0
 * @date 2023/5/22 22:03
 */
@Service
@Slf4j
public class AiManager {
    @Resource
    private YuCongMingClient yuCongMingClient;

    public String doChat(long modelId, String message) {
        // 开始时间
        long startTime = System.currentTimeMillis();
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> res = yuCongMingClient.doChat(devChatRequest);
        if (res == null || res.getData() == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        String content = res.getData().getContent();
        if (StringUtils.isEmpty(content)) {
            content = "";
        }
        // 结束时间
        long endTime = System.currentTimeMillis();
        // 计算执行时间
        log.info("doChat方法 执行时长:" + (endTime - startTime) + "ms");
        return content;
    }
}
