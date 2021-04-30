package com.sync.core.utils;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-04-17 10:27
 */
@Data
public class ListTriple {

    /** Left object */
    private final List<String> left;
    /** Middle object */
    private final List<Integer> middle;
    /** Right object */
    private final List<String> right;

    ListTriple(){
        this.left = new ArrayList<>();
        this.middle = new ArrayList<>();
        this.right = new ArrayList<>();
    }

    public List<String> getLeft() {
        return left;
    }

    public List<Integer> getMiddle() {
        return middle;
    }

    public List<String> getRight() {
        return right;
    }
}
