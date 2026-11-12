package com.macro.mall.sdc.e4;

import com.macro.mall.mapper.UmsAdminLoginLogMapper;
import com.macro.mall.mapper.UmsAdminMapper;
import com.macro.mall.mapper.UmsAdminRoleRelationMapper;
import com.macro.mall.dao.UmsAdminRoleRelationDao;
import com.macro.mall.security.util.JwtTokenUtil;
import com.macro.mall.service.UmsAdminCacheService;
import com.macro.mall.service.impl.UmsAdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class E4Test {

    @Test
    public void testAdminServiceDirectConstruction() {
        // 1. 准备所有依赖的 Mock 替身
        JwtTokenUtil jwtTokenUtil = Mockito.mock(JwtTokenUtil.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UmsAdminMapper adminMapper = Mockito.mock(UmsAdminMapper.class);
        UmsAdminRoleRelationMapper roleRelationMapper = Mockito.mock(UmsAdminRoleRelationMapper.class);
        UmsAdminRoleRelationDao roleRelationDao = Mockito.mock(UmsAdminRoleRelationDao.class);
        UmsAdminLoginLogMapper loginLogMapper = Mockito.mock(UmsAdminLoginLogMapper.class);
        UmsAdminCacheService cacheService = Mockito.mock(UmsAdminCacheService.class);

        // 2. 直接通过构造函数 new 出实例，不依赖 Spring 容器
        UmsAdminServiceImpl adminService = new UmsAdminServiceImpl(
                jwtTokenUtil,
                passwordEncoder,
                adminMapper,
                roleRelationMapper,
                roleRelationDao,
                loginLogMapper,
                cacheService
        );

        // 3. 断言创建成功，证明可测试性大幅提升
        assertNotNull(adminService, "服务应当能通过构造器直接创建");
    }
}