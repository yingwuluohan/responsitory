package com.fang.common.project.redis;

import com.exception.InitialisationException;

/**
 * Created by  on 2017/2/27.
 */
public interface Initialisable {
    void init() throws InitialisationException;
}
