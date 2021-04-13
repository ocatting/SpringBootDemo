package com.yu.algorithm;

import java.util.*;

/**
 * @Description: 283 移动零
 * @Author: Yan XinYu
 * @Date: 2021-03-31 21:13
 */

public class moveZero {

    public void moveZeroes(int[] nums) {
        int j = 0;
        for(int i=0;i<nums.length;i++){
            if (nums[i] != 0) {
                nums[j] = nums[i];
                if(j != i){
                    nums[i] = 0;
                }
                j++;
            }
        }
    }

    /**
     * 1.枚举法: O(n^2) 盛水最多 思路：嵌套循环
     * @param nums
     */
    public int maxArea(int [] nums){

        int max = 0;
        // 这个一定要记住，对同一个数组遍历，且不会重复遍历
        for(int i =0;i<nums.length -1;i++){
            for(int j =i+1;j<nums.length;j++){
                int area = (j-i)*Math.min(nums[j],nums[i]);
                max = Math.max(area,max);
            }
        }
        return max;
    }

    /**
     * 1.左右边界向中间移动 盛水最多 O(n) 思路:左右夹逼
     * @param nums
     * @return
     */
    public int maxArea2(int [] nums) {

        int max = 0;
        for(int i = 0, j = nums.length -1;i < j;){
            int area = nums[i] < nums[j] ? ((j - i) * nums[i++]): ((j - i) * nums[j--]);
            max = Math.max(max, area);
        }
        return max;
    }

    public int climbStairs(int n){
        if(n<3){return n;}
        int f1=1,f2=2,f3=3;
        for(int i = 3; i < n+1 ; i++){
            f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }
        return f3;
    }

    public int[] twoSum(int[] nums, int target) {
        for(int i=0;i<nums.length;i++){
            for (int j =i+1;j<nums.length;j++){
                if(nums[i] + nums[j] == target ){
                    return new int[]{i,j};
                }
            }
        }
        return new int[0];
    }

    public int[] twoSum2(int[] nums, int target) {
        Map<Integer,Integer> hash = new HashMap<>();
        for (int i=0;i<nums.length;i++){
            int as = target - nums[i];
            if (hash.containsKey(as)) {
                return new int[]{hash.get(as),i};
            }
            hash.put(nums[i],i);
        }
        return new int[0];
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> result = new LinkedHashSet<>();
        // 因为最少需要三个数所以 -2 ；
        for(int i=0;i<nums.length-2;i++){
            // a + b =-c 变通：-a - b = c
            int target = - nums[i];
            Map<Integer,Integer> hash = new HashMap<>();
            for(int j=i+1;j<nums.length;j++){
                int v = target - nums[j];
                // 这里判断是否存在相等的数，如歌存在则返回三个数；
                Integer exist = hash.get(v);
                if(exist == null){
                    hash.put(nums[j],nums[j]);
                } else {
                    List<Integer> list = Arrays.asList(nums[i],exist,nums[j]);
                    list.sort(Comparator.naturalOrder());
                    result.add(list);
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 时间复杂度 O(n^2)
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if(nums == null || nums.length < 3) {return result;}
        // 自然排序
        Arrays.sort(nums);
        for(int i = 0; i< nums.length;i++){
            // 大于 0 则没有
            if(nums[i] > 0){
                break;
            }
            // 重复
            if(i>0 && nums[i] == nums[i-1]){
                continue;
            }
            int l = i+1;
            int r = nums.length -1;
            while (l<r){
                int sum = nums[i] + nums[l] + nums[r];
                if(sum == 0){
                    result.add(Arrays.asList(nums[i],nums[l],nums[r]));
                    while (l<r && nums[l] == nums[++l]){};
                    while (l<r && nums[r] == nums[--r]){};
                } else if(sum < 0){
                    l++;
                } else {
                    r--;
                }
            }
        }
        return result;
    }

    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next; // 3,4,5
            curr.next = prev; // 1 -> null  这一句代表改变指针方向，"指向" prev
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(4);
        ListNode node4 = new ListNode(5);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

//        1->2->3->4->5->null;
//    prev:    null<-1<-2<-3
//        curr 1,null,3,4,5,null
//        prev = 1;
//        curr 2,3,4,5,null;
//
//        3,4,5
//        curr.next = 1
//        prev = 2
//        prev = 2
//
//
//        1,2,3,4,5,null;

//        while (head != null){
//            System.out.println("for:"+ head.val);
//            head = head.next;
//        }

        ListNode listNode = moveZero.reverseList(head);
        while (listNode != null) {
            System.out.println("for->:"+ listNode.val);
            listNode = listNode.next;
        }
    }

}
