package com.macro.mall.sdc.e5;

import com.macro.mall.mapper.PmsProductCategoryMapper;
import com.macro.mall.service.impl.PmsProductCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class E5Test {

    @Test
    public void testDeleteCategoryWithoutLimit() {
        // 1. 使用 Mockito 创建 Mapper 的 Mock 替身
        PmsProductCategoryMapper mockMapper = Mockito.mock(PmsProductCategoryMapper.class);

        // 2. 直接 new 出 Service 实现类
        PmsProductCategoryServiceImpl service = new PmsProductCategoryServiceImpl();

        // 3. 用 ReflectionTestUtils 强行把 Mock 的 Mapper 塞进 Service 的私有字段中
        // (注："productCategoryMapper" 必须和 ServiceImpl 里的字段名完全一致，你可以点开 ServiceImpl 看一眼)
        ReflectionTestUtils.setField(service, "productCategoryMapper", mockMapper);

        // 4. 设定 Mock 行为：当删除 id=1 时，返回 1 (代表删除成功)
        Mockito.when(mockMapper.deleteByPrimaryKey(1L)).thenReturn(1);

        // 5. 断言：因为基线代码没有做任何限制，所以它返回 1
        int result = service.delete(1L);
        assertEquals(1, result, "基线没有限制，即使分类下有商品也会直接删除");
    }
}