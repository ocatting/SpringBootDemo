package com.yu.zk.use.agent;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class OsBean implements Serializable {

    private String ip;
    private Double cpu;
    private long usedMemorySize;
    private long usableMemorySize;
    private String pid;
    private long lastUpdateTime;

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
