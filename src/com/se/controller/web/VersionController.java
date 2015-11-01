package com.se.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;

import com.se.service.VersionService;
@Controller
public class VersionController {
         @Resource
         private VersionService versionService;

         public String getLatestVersion(String apptype) {
        	String latestVersion="";
			if (apptype.endsWith("ios")) {
				//-ios
				
			}else{
				//-android
				
			}
        	 return latestVersion;
		}
}
