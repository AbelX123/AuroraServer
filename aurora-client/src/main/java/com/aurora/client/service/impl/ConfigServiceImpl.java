package com.aurora.client.service.impl;

import com.aurora.client.common.entity.ConfigEntity;
import com.aurora.client.mapper.ConfigMapper;
import com.aurora.client.service.IConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigEntity> implements IConfigService {

}
