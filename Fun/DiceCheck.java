//package Fun;
public class DiceCheck{
    public static int roll(int sides){
        return (int) (Math.random()*sides+1);
    }
    public static void main(String[] args){
        int[] dist = new int[11];
        for (int i=1;i<=100;i++){
            int sum1=0;
            for (int j=0;j<10;j++){
                sum1+=roll(2)==2?1:0;
            }
            dist[sum1]++;
        }
        for (int i=0;i<dist.length;i++){
            System.out.println(i+": "+dist[i]);
        }
    }
}