//package Fun;
import java.util.*;
public class AI {
    public static void main(String[] args) {
        int ans=(int)(Math.random()*6+1);
        System.out.println(ans);
        int[] amt=new int[7];
        List<Integer> data=new ArrayList<Integer>();
        for (int i=0;i<18;i++){
            data.add(i%6+1);
            amt[i%6+1]++;
        }
        for (int i=0;i<200;i++){
            int guess=data.get((int)(Math.random()*data.size()));
            if (guess==ans&&amt[guess]<15){
                data.add(guess);
                amt[guess]++;
            }else if (amt[guess]>1){
                data.remove(data.indexOf(guess));
                amt[guess]--;
            }
            System.out.println(guess);
            if (Math.random()<0.01){
                ans=(int)(Math.random()*6+1);
                System.out.println("CHANGE");
                //System.out.println(data);
            }
        }
    }
}
