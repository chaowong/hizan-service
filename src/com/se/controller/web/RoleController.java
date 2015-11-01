package com.se.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;

import com.se.service.RoleService;
@Controller
public class RoleController {
         @Resource
         private RoleService roleService;

}
