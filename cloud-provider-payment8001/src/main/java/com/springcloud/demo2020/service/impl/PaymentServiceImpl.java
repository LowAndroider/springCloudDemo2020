package com.springcloud.demo2020.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springcloud.demo2020.dao.PaymentDao;
import com.springcloud.demo2020.entity.Payment;
import com.springcloud.demo2020.service.PaymentService;
import org.springframework.stereotype.Service;

/**
 * @author Djh
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentDao, Payment> implements PaymentService {
}
