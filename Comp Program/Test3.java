import java.util.*;
public class Test3{
    public static void main(String[] args) {
        Scanner s=new Scanner(System.in);
        int offers=s.nextInt();
        int buys=s.nextInt();
        HashMap<Long,Long> milk=new HashMap<Long,Long>();
        for (int i=0;i<offers;i++){
            long index=i;
            long cost=s.nextLong();
            if(index>0){
                cost=Math.min(cost,milk.get(index-1)*2);
            }
            milk.put(index,cost);
        }
        System.err.println(milk);
        for (int i=0;i<buys;i++){
            long needed=s.nextLong();
            long cost=getCost(milk,needed);
            System.out.println(cost);
        }
        //System.err.println(milk);
        s.close();
    }
    public static long getCost(HashMap<Long,Long> milk,long needed){
        long cost=0;
        if(needed==0){
            return 0;
        }
        if(milk.containsKey(needed)){
            System.out.println((long)(Math.log(needed)/Math.log(2)));
            cost=milk.get((long)(Math.log(needed)/Math.log(2)));
        }else{
            Collection<Long> costs=milk.keySet();
            long max=0;
            for (long i:costs){
                if((long)(Math.log(i)/Math.log(2))<needed){
                    max=Math.max(max,i);
                }
            }
            //System.out.println(needed+" "+max);
            cost=getCost(milk,needed%max)+milk.get((long)(Math.log(max)/Math.log(2)))*(needed/max);
            for (long i:costs){

                cost=Math.min(cost,milk.get((long)(Math.log(i)/Math.log(2)))*((needed/(long)(Math.log(i)/Math.log(2)))+1));
            }
            //milk.put(needed,cost);
        }
        return cost;
    }
}