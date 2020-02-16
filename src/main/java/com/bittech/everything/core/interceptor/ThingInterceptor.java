package com.bittech.everything.core.interceptor;


import com.bittech.everything.core.model.Thing;

/**
 * @PACKAGE_NAME: com.bittech.everything.core.interceptor
 * @NAME: ThingInterceptor
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/10
 **/
@FunctionalInterface
public interface ThingInterceptor {
    void apply(Thing thing);
}
