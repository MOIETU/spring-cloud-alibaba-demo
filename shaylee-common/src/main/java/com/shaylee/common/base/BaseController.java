package com.shaylee.common.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shaylee.common.utils.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * web层通用数据处理
 * Project: shaylee-common
 *
 * @author Adrian
 * @date 2020-03-09
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected <E> Page<E> startPage(Integer pageNum, Integer pageSize) {
        return PageHelper.startPage(pageNum, pageSize);
    }

    protected <E> Page<E> startPage(Integer pageNum, Integer pageSize, String orderBy) {
        return PageHelper.startPage(pageNum, pageSize, SqlUtil.escapeOrderBySql(orderBy));
    }
}
