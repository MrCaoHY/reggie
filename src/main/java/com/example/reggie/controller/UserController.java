package com.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import com.example.reggie.utils.ValidateCodeUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: reggie
 * @description:
 * @author: CaoHaiyang
 * @create: 2022-07-26 00:55
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.hasText(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("获取到验证码:{}", code);
            //调用阿里云提供的短信服务API完成发送短信,这里我们省略这一步
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
//            session.setAttribute(phone, code);
            redisTemplate.opsForValue().set(phone,code,5,TimeUnit.MINUTES);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("前台登录:{}", map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        String codeInSession = (String) session.getAttribute(phone);
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);
        if (StringUtils.hasText(codeInSession) && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出登录成功");
    }

}
