package me.dragon.web.restcontroller;

import me.dragon.config.RestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

/**
 * Created by dragon on 11/4/2017.
 */
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    RestConfig restConfig;

    @Value("${env}")
    private String dev;

    protected void printErrorLog(String logName, Exception e) {
        logger.error(logName + "={}", e.getMessage(), e);
    }

}
