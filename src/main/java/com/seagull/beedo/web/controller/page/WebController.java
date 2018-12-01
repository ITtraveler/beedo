/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.web.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author guosheng.huang
 * @version WebController.java, v 0.1 2018年12月01日 22:23 guosheng.huang Exp $
 */
@Controller
public class WebController {
    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }
}
