package com.yu.algorithm;

/**
 * @Description: 数组
 * @Author Yan XinYu
 **/
public class Array {

    /**
     * 给定一个整数类型的数组 nums，请编写一个能够返回数组“中心索引”的方法。
     * 我们是这样定义数组中心索引的：数组中心索引的左侧所有元素相加的和等于右侧所有元素相加的和。
     * 如果数组不存在中心索引，那么我们应该返回 -1。如果数组有多个中心索引，那么我们应该返回最靠近左边的那一个。
     *
     * 输入:
     * nums = [1, 7, 3, 6, 5, 6]
     * 输出: 3
     * 题解：
     *  RightSum = Sum - leftSum;
     */
    public static int zhongjian(int[] nums){
        int sum = 0,leftSum = 0;
        for(int i:nums){sum+=i;}
        System.out.println("sum:"+sum);
        for(int i=0 ;i<nums.length;i++){
            int rightSum = sum - nums[i] - leftSum;
            if( rightSum == leftSum){
                return i;
            }
            leftSum += nums[i];
        }
        return -1;
    }

    public static int dominantIndex(int[] nums) {
        int indexMax = 0;int max = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[indexMax] < nums[i]){
                indexMax = i;
            }
        }
        for(int i=0;i<nums.length;i++){
            if(indexMax!=i && nums[indexMax] < (nums[i] * 2)){
                return -1;
            }
        }
        return indexMax;
    }

    public static void main(String[] args) {
//        int[] nums = {1, 7, 3, 6, 5, 6};
        int[] nums = {1};
//        System.out.println(Array.zhongjian(nums));
        System.out.println(Array.dominantIndex(nums));
    }
}
