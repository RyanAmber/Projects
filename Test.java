import java.util.*;
public class Test{
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        HashTable<Integer, Integer> table=new HashTable<Integer, Integer>();
        for (int i=0;i<10;i++){
            table.put(i,i+1);
            System.out.println(table.debugString());
        }
        table.remove(0);
        System.out.println(table.debugString());
        s.close();
    }
    public static void whatFlavors(List<Integer> cost, int money) {
    Map<Integer, Integer> seen = new HashMap<>();
    for (int i = 0; i < cost.size(); i++) {
        int complement = money - cost.get(i);
        if (seen.containsKey(complement)) {
            int j = seen.get(complement) + 1;
            i++;
            System.out.println(j + " " + i);
            break;
        }
        seen.put(cost.get(i), i);
    }

    }
}
