package me.dragon.restcontroller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dragon on 11/4/2017.
 */
@Controller
@RequestMapping(value = "/test", produces = {"application/json;charset=UTF-8"})
@Api(value = "测试ControllerValue", tags = "测试ControllerTags", description = "测试ControllerDescription", produces = "application/json;charset=utf-8")
public class TestController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public void updateContact() {
        logger.info("test接口...");
    }
}
