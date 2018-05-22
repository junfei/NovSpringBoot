package me.dragon.web.restcontroller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.dragon.domain.User;
import me.dragon.service.UserService;
import me.dragon.utils.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by dragon on 11/4/2017.
 */
@Slf4j
@Controller
@RequestMapping(value = "/test", produces = {"application/json;charset=UTF-8"})
@Api(value = "测试ControllerValue", tags = "测试ControllerTags", description = "测试ControllerDescription", produces = "application/json;charset=utf-8")
public class TestController extends BaseController {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserService userService;

    @RequestMapping(value = "/testController", method = RequestMethod.POST)
    @ResponseBody
    public void testController() {
        logger.info("testController接口...");
    }

    @RequestMapping(value = "/testRedis", method = RequestMethod.POST)
    @ResponseBody
    public void testRedis() {
        log.info("testRedis接口..." + redisUtils.getNowCode("TEST", "T", 3));
    }

    @RequestMapping(value = "/testMapper", method = RequestMethod.POST)
    @ResponseBody
    public void testMapper() {
        log.info("testMapper接口...");
        List<User> list = userService.getUserList();
        log.info("res = {}", list.toString());
    }
}
