package com.bittech.everything.core.common;

import lombok.Data;

import java.util.Set;

/**
 * @PACKAGE_NAME: com.bittech.everything.core.common
 * @NAME: HandlePath
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/1/11
 **/
@Data
public class HandlePath {
    private  Set<String> includePath;
    private  Set<String> excludePath;
}
