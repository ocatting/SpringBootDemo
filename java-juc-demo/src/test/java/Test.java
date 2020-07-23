/**
 * @Description:
 * @Author Yan XinYu
 **/
public class Test {

    /**
     * jstack 找出cpu占用最大的线程 (查看jstack信息)
     * jmap -histo 找出堆内存信息并统计
     * @param args
     */
    public static void main(String[] args) {
//        int i = 0;
//        while(i<100){
//            i = i * 100;
//        }

        int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int index = Test.binaryFind(array,7);
        System.out.println(index);
    }

    /**
     * 二分查找 返回下标
     * 首先是个有序队列
     */
    public static int binaryFind (int[] array, int key){

        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (key < array[mid]) {
                high = mid - 1;
            } else if (key > array[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;

    }

    /**
     * 冒泡排序
     */
    public void bubblingSort(){
        int[] numbers=new int[]{7,5,8,2,3,9,4};
        int i,j;
        for(i=0;i<numbers.length-1;i++)
        {
            for(j=0;j<numbers.length-1-i;j++)
            {
                if(numbers[j]>numbers[j+1])
                {
                    int temp=numbers[j];
                    numbers[j]=numbers[j+1];
                    numbers[j+1]=temp;
                }
            }
        }
        System.out.println("从小到大排序后的结果是:");
        for(i=0;i<numbers.length;i++)
            System.out.print(numbers[i]+" ");
    }


}
