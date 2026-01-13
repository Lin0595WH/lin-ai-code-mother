package com.lin.linaicodemother.service.impl;

import com.lin.linaicodemother.mapper.AppMapper;
import com.lin.linaicodemother.model.entity.App;
import com.lin.linaicodemother.service.AppService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author Lin
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService {

}
