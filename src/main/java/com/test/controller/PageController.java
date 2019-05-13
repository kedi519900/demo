package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @version V1.0
 * @ProjectName:launceviya
 * @Description:
 * @Copyright: Copyright (c) 2019
 * @Company:鲸力智享（北京）科技有限公司
 * @author: Lan Yuan
 * @email: yuan.lan@jingli365.com
 * @date 2019-01-17 13:55
 */
@RequestMapping("/page")
@Controller
public class PageController {
    @GetMapping("/{page}")
    public String index(@PathVariable String page) {
        return "/" + page + ".html";
    }
}