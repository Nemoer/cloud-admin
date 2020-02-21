package com.hb0730.cloud.admin.server.permission.menu.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hb0730.cloud.admin.common.web.controller.AbstractBaseController;
import com.hb0730.cloud.admin.common.web.response.ResultJson;
import com.hb0730.cloud.admin.common.web.utils.ResponseResult;
import com.hb0730.cloud.admin.commons.model.security.UserDetail;
import com.hb0730.cloud.admin.server.permission.menu.system.model.entity.SystemPermissionMenuEntity;
import com.hb0730.cloud.admin.server.permission.menu.system.service.ISystemPermissionMenuService;
import com.hb0730.cloud.admin.server.permission.menu.utils.SecurityContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.hb0730.cloud.admin.common.util.RequestMappingConstants.PERMISSION_MENU_SERVER_REQUEST;

/**
 * <p>
 * 菜单权限  前端控制器
 * </p>
 *
 * @author bing_huang
 * @since 2020-02-19
 */
@RestController
@RequestMapping(PERMISSION_MENU_SERVER_REQUEST)
public class SystemPermissionMenuController extends AbstractBaseController<SystemPermissionMenuEntity> {
    @Autowired
    private ISystemPermissionMenuService systemPermissionMenuService;

    @PostMapping("/save")
    @Override
    public ResultJson save(@RequestBody SystemPermissionMenuEntity target) {
        UserDetail currentUser = null;
        try {
            currentUser = SecurityContextUtils.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.resultFall("获取当前用户失败,请重新登录");
        }
        if (Objects.isNull(currentUser)) {
            return ResponseResult.resultFall("获取当前用户失败,请重新登录");
        }
        target.setCreateUserId(currentUser.getUserId());
        target.setCreateTime(new Date());
        systemPermissionMenuService.save(target);
        return ResponseResult.resultSuccess("保存成功");
    }

    @Override
    public ResultJson delete(Object id) {
        return null;
    }

    @Override
    public ResultJson submit(SystemPermissionMenuEntity target) {
        return null;
    }

    @GetMapping("/permission/menu/{id}")
    @Override
    public ResultJson gitObject(@PathVariable Object id) {
        SystemPermissionMenuEntity result = systemPermissionMenuService.getById(id.toString());
        return ResponseResult.resultSuccess(result);
    }


    /**
     * <p>
     * 获取全部
     * </p>
     *
     * @param ids iDS
     * @return 结果集
     */
    @PostMapping("/permission/menu")
    public ResultJson getPermissionMenuByIds(@RequestBody List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseResult.resultSuccess(null);
        }
        List<SystemPermissionMenuEntity> results = systemPermissionMenuService.listByIds(ids);
        return ResponseResult.resultSuccess(results);
    }

    /**
     * <p>
     * 根据权限ID获取
     * </p>
     *
     * @param permissionId 权限id
     * @return 结果集
     */
    @GetMapping("/permission/menu/permission/{permissionId}")
    public ResultJson getPermissionMenuByPermissionId(@PathVariable Long permissionId) {
        SystemPermissionMenuEntity entity = new SystemPermissionMenuEntity();
        entity.setPermissionId(permissionId);
        QueryWrapper<SystemPermissionMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(entity);
        List<SystemPermissionMenuEntity> results = systemPermissionMenuService.list(queryWrapper);
        return ResponseResult.resultSuccess(results);
    }

    /**
     * <p>
     * 根据菜单id获取
     * </p>
     *
     * @param menuId 菜单id
     * @return 结果集
     */
    @GetMapping("/permission/menu/menu/{menuId}")
    public ResultJson getPermissionMenuByMenuId(@PathVariable Long menuId) {
        SystemPermissionMenuEntity entity = new SystemPermissionMenuEntity();
        entity.setMenuId(menuId);
        QueryWrapper<SystemPermissionMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(entity);
        List<SystemPermissionMenuEntity> results = systemPermissionMenuService.list(queryWrapper);
        return ResponseResult.resultSuccess(results);
    }
}
