import java.util.*;
import java.nio.file.*;
import java.io.IOException;
public class Test{
    public static void main(String[] args){
        Scanner s=new Scanner("shell.in");
        Path file = Paths.get("shell.out");
        int swaps=s.nextInt();
        int count1=0;
        int count2=0;
        int count3=0;
        int[] correct=new int[3];
        correct[0]=1;
        correct[1]=2;
        correct[2]=3;
        for (int swap=0;swap<swaps;swap++){
            int start=s.nextInt()-1;
            int end=s.nextInt()-1;
            int guess=s.nextInt()-1;
            int temp=correct[start];
            correct[start]=correct[end];
            correct[end]=temp;
            if(correct[guess]==1){
                count1++;
            }else if(correct[guess]==2){
                count2++;
            }else{
                count3++;
            }
        }
        try{
        Files.writeString(file, ""+Math.max(count1,Math.max(count2,count3)));
        }catch(IOException e){
            
        }
        //System.out.println(Math.max(count1,Math.max(count2,count3)));
        s.close();
    }
}
