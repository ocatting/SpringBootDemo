package com.demo.io;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-01-21 15:08
 */

public class TailerListener extends TailerListenerAdapter {

    @Override
    public void init(Tailer tailer) {
        super.init(tailer);
    }

    @Override
    public void handle(String line) {
        super.handle(line);
    }
}
