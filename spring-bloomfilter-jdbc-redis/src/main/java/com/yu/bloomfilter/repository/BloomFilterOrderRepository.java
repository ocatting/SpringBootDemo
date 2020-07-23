package com.yu.bloomfilter.repository;

import com.yu.bloomfilter.domian.TBloomFilterOrder;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface BloomFilterOrderRepository extends Repository {

    void insert(TBloomFilterOrder bloomFilterOrder);

    List<TBloomFilterOrder> findAll();
}
