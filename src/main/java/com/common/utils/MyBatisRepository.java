package com.common.utils;

import java.lang.annotation.*;

/**
 * Created by fn on 2017/2/9.
 */


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MyBatisRepository {
}
