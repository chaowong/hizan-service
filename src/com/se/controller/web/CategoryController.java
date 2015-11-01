package com.se.controller.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.se.service.CategoryService;
@Controller
public class CategoryController {
         @Resource
         private CategoryService categoryService;

}
