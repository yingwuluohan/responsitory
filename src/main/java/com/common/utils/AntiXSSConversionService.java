package com.common.utils;

/**
 * Created by fn on 2017/2/9.
 */

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * springmvc参数转换过滤xss
 * @author zhanghua
 *
 */
public class AntiXSSConversionService extends GenericConversionService {

    @Override
    public Object convert(Object source, TypeDescriptor sourceType,
                          TypeDescriptor targetType) {
        if (sourceType.getObjectType() == String.class
                && targetType.getAnnotation(AnitXSSOff.class) == null) {
            return AntiXSSUtils.antiXSS(((String) source));
        } else {
            return super.convert(source, sourceType, targetType);
        }
    }

}
