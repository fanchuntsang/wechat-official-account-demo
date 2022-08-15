package com.fanchuntsang.wechatofficialaccount.controller;

import com.fanchuntsang.wechatofficialaccount.util.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/wechat/portal")
public class PortalController {
    @Value("${token}")
    private String token;

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\nReceived authentication message from WeChat server：[{}, {}, {}, {}]", signature,
                timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("The request parameter is illegal");
        }
        if (SHA1.gen(token, timestamp, nonce).equals(signature)) {
            return echostr;
        }
        log.warn("Illegal WeChat Request： " + signature);
        return "Illegal Request";
    }


    @PostMapping(produces = "application/json;charset=utf-8")
    public String post(@RequestBody String message) {
        log.info("received from wechat： \n" + message);
        if (StringUtils.isNoneBlank(message)) {
            return "success";
        }
        return "error";
    }
}
