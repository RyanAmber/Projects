package AOA;
import java.util.*;
public class QuickSort{
public static ArrayList<Integer> quickSort(ArrayList<Integer> list) {
    int pivot=list.get((int)(Math.random()*(list.size())));
    ArrayList<Integer> smaller=new ArrayList<Integer>();
    ArrayList<Integer> bigger=new ArrayList<Integer>();
    boolean done=true;
    for (int i=0;i<list.size();i++){
        if (list.get(i)!=pivot){
            done=false;
        }
        if (list.get(i)<=pivot){
            smaller.add(list.get(i));
        }else{
            bigger.add(list.get(i));
        }
    }
    System.out.println(pivot);
    System.out.println(smaller);
    System.out.println(bigger);
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    if (done){
        return smaller;
    }
    if (smaller.size()>1){
    	smaller=quickSort(smaller);
    }
    if (bigger.size()>1){
        bigger=quickSort(bigger);
    }
    ArrayList<Integer> total=new ArrayList<Integer>();
    total.addAll(smaller);
    total.addAll(bigger);
    return total;
}
    public static void main(String[] args) {
        ArrayList<Integer> list=new ArrayList<Integer>();
        list.add(4);
        list.add(6);
        list.add(3);
        list.add(5);
        list.add(3);
        System.out.println(list);
        System.out.println(quickSort(list));
        
    }
}