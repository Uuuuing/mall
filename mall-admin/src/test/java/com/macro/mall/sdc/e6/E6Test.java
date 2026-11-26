package com.macro.mall.sdc.e6;

import com.macro.mall.common.exception.Asserts;
import com.macro.mall.mapper.PmsProductCategoryMapper;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.service.impl.PmsProductCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class E6Test {

    @Test
    public void testDeleteCategoryWithProducts_ShouldThrowException() {
        // 1. 准备 Mock 替身
        PmsProductCategoryMapper categoryMapper = Mockito.mock(PmsProductCategoryMapper.class);
        PmsProductMapper productMapper = Mockito.mock(PmsProductMapper.class); // 需要新增这个

        PmsProductCategoryServiceImpl service = new PmsProductCategoryServiceImpl();
        ReflectionTestUtils.setField(service, "productCategoryMapper", categoryMapper);
        ReflectionTestUtils.setField(service, "productMapper", productMapper); // 把这个也塞进去

        // 2. 设定场景：分类下有商品（假设查到 1 个商品）
        // 注意：这里需要根据实际代码查商品表的写法来 Mock，假设是用 selectByExample 或 countByExample 查
        Mockito.when(productMapper.countByExample(Mockito.any())).thenReturn(1L);
        // 假设查子分类也是 0 个
        Mockito.when(categoryMapper.countByExample(Mockito.any())).thenReturn(0L);

        // 3. 断言：预期抛出业务异常
        Exception exception = assertThrows(Exception.class, () -> {
            service.delete(1L);
        });
        assertEquals("该分类下存在商品，不能直接删除", exception.getMessage());
    }
}