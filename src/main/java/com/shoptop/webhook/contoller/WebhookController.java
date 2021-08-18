package com.shoptop.webhook.contoller;

import com.shoptop.webhook.utils.HttpUtil;
import com.shoptop.webhook.utils.SignatureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Webhook SDK
 * @author: lqj
 * @date: 2021/7/30
 */
@RestController
@Slf4j
public class WebhookController {
    /**
     * 客户接收webhook消息
     * @return
     * @throws IOException
     */
    @RequestMapping("/webhooks")
    public String webhooks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求体信息
        String requestBody = HttpUtil.getRequestBody(request);
        //获取签名信息
        String sign = request.getHeader("X-Shoptop-Hmac-Sha256");
        //验证签名
        try {
            //key为插件的Secret
            String verify = SignatureUtil.HMACSHA256(requestBody, "29dbd25f-0cb8-476c-a2f7-2117ab0589b8");
            //签名校验错误
            if(!verify.equals(sign)) {
                log.info("\n############## 验签失败 sign= {} & verify= {}##############", sign, verify);
                return "failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("\n############## 验签成功 sign= {} ##############", sign);
        /**
         * 处理业务逻辑
         * X-Shoptop-Topic: orders/create
         * X-Shoptop-Shop-Domain: abc.ishoptop.com
         * X-Shoptop-Api-Version: v1
         */
        //获取topic
        String topic = request.getHeader("X-Shoptop-Topic");
        //获取域名
        String domain = request.getHeader("X-Shoptop-Shop-Domain");
        //获取版本信息
        String version = request.getHeader("X-Shoptop-Api-Version");


        return "success";
    }

}
