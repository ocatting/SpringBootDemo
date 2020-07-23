package com.yu.executor;

import com.yu.domain.UrlStore;
import com.yu.handler.ResultHandler;

import java.sql.ResultSet;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface SQLExecutor {

    ResultSet execute(UrlStore urlStore,Map parse);

}
