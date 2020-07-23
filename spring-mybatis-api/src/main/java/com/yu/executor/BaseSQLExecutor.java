package com.yu.executor;

import com.yu.builder.BoundSql;
import com.yu.builder.SqlSource;
import com.yu.config.MappingConfiguration;
import com.yu.domain.UrlSqlSource;
import com.yu.domain.UrlStore;
import com.yu.handler.BaseResultHandler;
import com.yu.handler.ResultHandler;
import com.yu.parsing.ParameterMapping;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 执行数据库操作
 * @Author Yan XinYu
 **/
public class BaseSQLExecutor implements SQLExecutor {

    private DataSource dataSource;
    private MappingConfiguration mappingConfiguration;

    public BaseSQLExecutor(MappingConfiguration mappingConfiguration, DataSource dataSource){
        this.dataSource = dataSource;
        this.mappingConfiguration = mappingConfiguration;
    }

    /**
     * 查询数据库
     * @param sql
     * @param parameterMappings
     * @param params
     * @return
     */
    public ResultSet executeFromDataSource(String sql,List<ParameterMapping> parameterMappings,Map params){
        Assert.hasLength(sql,"查询的SQL语句不为空");
        try {
            // 打开连接
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if(parameterMappings != null && !parameterMappings.isEmpty()){
                for( int i = 0;i < parameterMappings.size(); i++ ){
                    ParameterMapping mapping = parameterMappings.get(i);
                    switch (mapping.getJavaTypeName()){
                        case "string":
                            preparedStatement.setString(i,params.get(mapping.getProperty()).toString());
                            break;
                        case "integer":
                            preparedStatement.setInt(i,Integer.parseInt(params.get(mapping.getProperty()).toString()));
                            break;
                        case "double":
                            preparedStatement.setBigDecimal(i,new BigDecimal(params.get(mapping.getProperty()).toString()));
                            break;
                        default:
                            throw new IllegalArgumentException("请求的参数格式不支持:"+mapping.getProperty());
                    }
                }
            }
            // 执行
            preparedStatement.execute();
            // 处理结果集
            return preparedStatement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException("SQL执行器执行结果异常!");
        }
    }

    @Override
    public ResultSet execute(UrlStore urlStore,Map params) {
        UrlSqlSource sqlMapper = mappingConfiguration.getSqlMapper(urlStore.getUrlSqlSourceId());
        SqlSource sql = sqlMapper.getSqlSource();
        BoundSql boundSql = sql.getBoundSql(params);
        return executeFromDataSource(boundSql.getSql(),boundSql.getParameterMappings(),params);
    }
}
