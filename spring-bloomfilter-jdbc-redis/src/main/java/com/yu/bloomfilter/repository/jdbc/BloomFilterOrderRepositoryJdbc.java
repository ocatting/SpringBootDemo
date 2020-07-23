package com.yu.bloomfilter.repository.jdbc;

import com.yu.bloomfilter.domian.TBloomFilterOrder;
import com.yu.bloomfilter.repository.BloomFilterOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: jdbc实现
 * @Author Yan XinYu
 **/
@Repository
public class BloomFilterOrderRepositoryJdbc implements BloomFilterOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insert(TBloomFilterOrder order) {
        String sql = "insert into t_bloomfilter_order (id,order_num) values(?,?)";
        jdbcTemplate.update(sql,order.getId(),order.getOrder_num());
    }

    @Override
    public List<TBloomFilterOrder> findAll() {
        String sql = "select * from t_bloomfilter_order";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(TBloomFilterOrder.class));
    }
}
