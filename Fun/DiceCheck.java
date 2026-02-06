//package Fun;
public class DiceCheck{
    public static int roll(int sides){
        return (int) (Math.random()*sides+1);
    }
    public static void main(String[] args){
        int total=0;
        int avg1=0;
        int avg2=0;
        for (int i=1;i<=1000;i++){
            int sum1=roll(roll(roll(12)));
            int sum2=roll(roll(6));
            avg1+=sum1;
            avg2+=sum2;
            total+=sum1-sum2;
            if (i%100==0){
                System.out.println(total);
                System.out.println(avg1/i);
                System.out.println(avg2/i);
                System.out.println();
            }
        }

    }
}