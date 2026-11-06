package com.macro.mall.sdc.e3;

import com.macro.mall.model.OmsOrderItem;
import com.macro.mall.model.SmsCoupon;
import com.macro.mall.portal.service.impl.OmsPortalOrderServiceImpl;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class E3Test {

    @Test
    public void testCouponAmountPrecision() throws Exception {
        // 1. 不用 Spring 注入，直接 new
        OmsPortalOrderServiceImpl orderService = new OmsPortalOrderServiceImpl();

        // 2. 构造测试数据：3个商品，每个 10.00 元，总价 30.00 元
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OmsOrderItem item = new OmsOrderItem();
            item.setProductPrice(new BigDecimal("10.00"));
            item.setProductQuantity(1);
            orderItemList.add(item);
        }

        // 3. 构造优惠券：面额 10.00 元
        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("10.00"));

        // 4. 反射调用私有方法
        Method method = OmsPortalOrderServiceImpl.class.getDeclaredMethod("calcPerCouponAmount", List.class, SmsCoupon.class);
        method.setAccessible(true);
        method.invoke(orderService, orderItemList, coupon);

        // 5. 业务断言
        BigDecimal sum = BigDecimal.ZERO;
        for (OmsOrderItem item : orderItemList) {
            sum = sum.add(item.getCouponAmount());
        }

        // 这里一定会报 AssertionFailedError
        assertEquals(0, sum.compareTo(new BigDecimal("10.00")), "优惠券分摊金额之和必须严格等于优惠券面额");
    }

    @Test
    public void testHundredItemsCoupon() throws Exception {
        OmsPortalOrderServiceImpl orderService = new OmsPortalOrderServiceImpl();
        List<OmsOrderItem> orderItemList = new ArrayList<>();

        // 构造 100 件商品，每件 0.33 元，总价 33.00 元
        for (int i = 0; i < 100; i++) {
            OmsOrderItem item = new OmsOrderItem();
            item.setProductPrice(new BigDecimal("0.33"));
            item.setProductQuantity(1);
            orderItemList.add(item);
        }

        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("10.00")); // 10元券

        Method method = OmsPortalOrderServiceImpl.class.getDeclaredMethod("calcPerCouponAmount", List.class, SmsCoupon.class);
        method.setAccessible(true);
        method.invoke(orderService, orderItemList, coupon);

        // 计算总和
        BigDecimal sum = BigDecimal.ZERO;
        for (OmsOrderItem item : orderItemList) {
            sum = sum.add(item.getCouponAmount());
        }

        // 严格断言，一分钱都不能差
        assertEquals(0, sum.compareTo(new BigDecimal("10.00")), "100件商品大批量分摊，总额也必须严格等于10.00");
    }

    @Test
    public void testZeroCouponBoundary() throws Exception {
        OmsPortalOrderServiceImpl orderService = new OmsPortalOrderServiceImpl();
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        OmsOrderItem item = new OmsOrderItem();
        item.setProductPrice(new BigDecimal("100.00"));
        item.setProductQuantity(1);
        orderItemList.add(item);

        SmsCoupon coupon = new SmsCoupon();
        coupon.setAmount(new BigDecimal("0.00")); // 0 元券

        Method method = OmsPortalOrderServiceImpl.class.getDeclaredMethod("calcPerCouponAmount", List.class, SmsCoupon.class);
        method.setAccessible(true);
        method.invoke(orderService, orderItemList, coupon);

        // 断言：0 元券分摊后，每个商品分到的金额必须是 0
        assertEquals(0, orderItemList.get(0).getCouponAmount().compareTo(BigDecimal.ZERO), "0元券分摊金额应为0");
    }
}