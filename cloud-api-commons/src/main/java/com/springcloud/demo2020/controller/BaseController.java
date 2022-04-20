package com.springcloud.demo2020.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springcloud.demo2020.entity.Result;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Djh
 */
public class BaseController<E, S extends IService<E>> {

    @Setter(onMethod_ = {@Autowired, @Lazy})
    private S service;

    @GetMapping("/list")
    @ResponseBody
    public Result<List<E>> list(E condition, OrderItem orderItem) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>(condition);

        if (orderItem != null && StringUtils.isNotBlank(orderItem.getColumn())) {
            queryWrapper.orderBy(true, orderItem.isAsc(), orderItem.getColumn());
        }

        List<E> list;
        try {
            queryWrapper = queryWrapper.orderByDesc("createTime");
            list = service.list(queryWrapper);
            return Result.success(list);
        } catch (Exception e){
            list = service.list(queryWrapper);
            return Result.success(list);
        }
    }

    @GetMapping("/page")
    @ResponseBody
    public Result<Page<E>> page (Page<E> page, E condition, OrderItem orderItem) {
        /*
         * 此处必须清除已存在的orderItem,
         * 因为Page类中存在setAsc方法，
         * 所以如果按照OrderItem属性传参数，就会触发setAsc方法，生成一个OrderItem
         */
        page.getOrders().clear();
        page.getOrders().add(orderItem);
        Page<E> pageData = service.page(page, new QueryWrapper<>(condition));
        return Result.success(pageData);
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable String id) {
        service.removeById(id);
        return Result.success("删除成功", null);
    }

    @DeleteMapping("/del")
    @ResponseBody
    public Result<String> delete(E condition) {
        service.removeById(new QueryWrapper<>(condition));
        return Result.success("删除成功", null);
    }

    @PostMapping("/save")
    @ResponseBody
    public Result<String> save(@RequestBody @Validated E entity) {
        service.saveOrUpdate(entity);
        return Result.success("保存成功", null);
    }
}
