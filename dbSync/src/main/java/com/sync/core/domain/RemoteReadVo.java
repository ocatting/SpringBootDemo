package com.sync.core.domain;

import com.sync.core.element.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 远程读
 * @Author: Yan XinYu
 * @Date: 2021-04-17 20:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoteReadVo {

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 线程存活
     */
    private boolean alive;

    /**
     * 记录
     */
    private List<Record> records;

}
