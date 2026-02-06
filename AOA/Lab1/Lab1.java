package AOA.Lab1;
import java.util.Arrays;

public class Lab1 {
    /** Sorting algorithms **/

    // Insertion sort.

    public static void insertionSort(int[] array) {
        for (int i=1;i<array.length;i++){
            int value=array[i];
            for (int j=i-1;j>=0;j--){
                if (array[j]>value){
                    array[j+1]=array[j];
                    array[j]=value;
                }
            }
        }
    }

    // Quicksort.

    public static void quickSort(int[] array) {
        //System.out.println("                      Start");
        if (array.length<2){return;}
        int pivot=partition(array, 0, array.length-1);
        //System.out.println("Partition done");
        quickSort(array,0,pivot-1);
        //System.out.println("First half");
        quickSort(array,pivot,array.length-1);
        //System.out.println("Second half");
    }

    // Quicksort part of an array
    private static void quickSort(int[] array, int begin, int end) {
        //System.out.println(begin+" "+end);
        if (end<=begin){
            return;
        }
        int pivot=partition(array,begin,end);
        //System.out.println("Pivot:"+pivot);
        quickSort(array,begin,pivot-1);
        quickSort(array,pivot+1,end);
    }

    // Partition part of an array, and return the index where the pivot
    // ended up.
    private static int partition(int[] array, int begin, int end) {
        //int pivot=median(array, begin,end,(begin+end)/2);
        int pivot =begin;
        //System.out.println(begin+" "+end);
        //System.out.println("Pivot "+pivot+"  "+array[pivot]);
        while(begin<pivot||end>pivot){
            if (array[begin]<=array[pivot]&&begin!=pivot){
                begin++;
            }else{
                swap(array,begin,end);
            }
            if (array[end]>=array[pivot]&&end!=pivot){
                end--;
            }
        }
        return pivot;
    }
    /*private static int median(int[] array, int a, int b, int c){
        int[] arr=new int[3];
        arr[0]=array[a];
        arr[1]=array[b];
        arr[2]=array[c];
        Arrays.sort(arr);
        if (arr[1]==array[a]){
            return a;
        }else if(arr[1]==array[b]){
            return b;
        }
        return c;
    }*/

    // Swap two elements in an array
    private static void swap(int[] array, int i, int j) {
        int x = array[i];
        array[i] = array[j];
        array[j] = x;
    }

    // Mergesort.

    public static int[] mergeSort(int[] array) {
        if (array.length<2){
            return array;
        }
        return mergeSort(array,0,array.length-1);
    }

    // Mergesort part of an array
    private static int[] mergeSort(int[] array, int begin, int end) {
        if (end<=begin){
            return Arrays.copyOfRange(array,begin,end+1);
        }
        int[] left=mergeSort(array,begin,(begin+end)/2);
        int[] right=mergeSort(array,(begin+end)/2+1,end);
        /*for (int i=0;i<left.length;i++){
            System.out.println("Left"+left[i]);
        }
        for (int i=0;i<right.length;i++){
            System.out.println("Right"+right[i]);
        }
        System.out.println(begin+" "+end);*/
        return merge(left,right);
    }

    // Merge two sorted arrays into one
    private static int[] merge(int[] left, int[] right) {
        int left1=0;
        int right1=0;
        int[] combined=new int[left.length+right.length];
        for (int i=0;i<left.length+right.length;i++){
            if (left1>=left.length){
                combined[i]=right[right1];
                right1++;
            }else if (right1>=right.length||left[left1]<right[right1]){
                combined[i]=left[left1];
                left1++;
            }else{
                combined[i]=right[right1];
                right1++;
            }
        }
        return combined;
    }
}
