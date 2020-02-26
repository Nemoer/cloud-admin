package com.hb0730.cloud.admin.server.permission.system.service.impl;

import com.hb0730.cloud.admin.server.permission.system.model.entity.SystemPermissionEntity;
import com.hb0730.cloud.admin.server.permission.system.service.ISystemPermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SystemPermissionServiceImplTest {
    @Autowired
    private ISystemPermissionService systemPermissionService;

    @Test
    public void save() {
        SystemPermissionEntity entity = new SystemPermissionEntity();
        entity.setName("测试");
        systemPermissionService.save(entity);
        System.out.println(entity.getId());
    }
}