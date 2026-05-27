package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单支付超时
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时处理支付超时订单...{}", LocalDateTime.now());
        // 未支付15min超时
        List<Orders> timeoutOrders = orderMapper.getByStatusAndOrderTimeLT(
                Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15)
        );
        if (timeoutOrders != null && !timeoutOrders.isEmpty()) {
            timeoutOrders.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("支付超时");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }
    }

    /**
     * 处理派送中订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("定时处理派送中订单...{}", LocalDateTime.now());
        // 昨天24点00分00秒还未派送完成
        List<Orders> deliveryOrders = orderMapper.getByStatusAndOrderTimeLT(
                Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60)
        );
        if (deliveryOrders != null && !deliveryOrders.isEmpty()) {
            deliveryOrders.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        }
    }
}
