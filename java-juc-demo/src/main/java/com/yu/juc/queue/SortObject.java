package com.yu.juc.queue;

/**
 * @Description: 用于
 * @Author Yan XinYu
 **/
public class SortObject implements Comparable<SortObject> {

    private String name;
    private int sort;

    public SortObject(String name, int sort){
        this.name = name;
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(SortObject sortObject) {
        return Integer.compare(this.sort, sortObject.getSort());
    }

    @Override
    public String toString() {
        return "SortObject{" +
                "name='" + name + '\'' +
                ", sort=" + sort +
                '}';
    }
}
