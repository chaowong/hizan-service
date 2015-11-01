package com.se.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;

import com.se.service.PermissionService;
@Controller
public class PermissionController {
         @Resource
         private PermissionService permissionService;

}
