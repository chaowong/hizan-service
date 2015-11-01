package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.VersionDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Version;
import com.se.service.VersionService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class VersionServiceImpl extends BaseServiceImpl<Version> implements VersionService{
	@Resource
	private VersionDao versionDao;	
	public BaseDao<Version> getBaseDao() {
		return this.versionDao;
	}
}
