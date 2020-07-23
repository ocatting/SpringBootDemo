package com.yu.store;

/**
 * @Description: 协议体 bit
 * 固定协议头(0-15,2byte) 协议总长度 (4byte)头长度 (4byte) version (4byte)ID (4byte)
 * @Author Yan XinYu
 **/
public interface StoreProtocol {

    short head = 2;
    short delete = 1;
    short length = 4;
    short headLength = 4;
    short version = 4;
    short id = 4;

}
