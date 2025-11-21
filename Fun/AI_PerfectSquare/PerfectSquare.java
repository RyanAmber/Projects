package AI_PerfectSquare;

public class PerfectSquare {
    int[][] square=new int[3][3];
    int perfect=15;

    public PerfectSquare(int[][] square) {
        this.square = square;
    }
    public int cost(){
        int cost=0;
        int[] rowsum=new int[3];
        int[] colsum=new int[3];
        int diag1=0;
        int diag2=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                rowsum[i]+=square[i][j];
                colsum[j]+=square[i][j];
                if(i==j) diag1+=square[i][j];
                if(i+j==2) diag2+=square[i][j];
            }
        }
        for (int i=0;i<3;i++){
            cost+=Math.abs(perfect-rowsum[i]);
            cost+=Math.abs(perfect-colsum[i]);
        }
        cost+=Math.abs(perfect-diag1);
        cost+=Math.abs(perfect-diag2);
        return cost;
    }
    public int singleCost(int i, int j){
        int cost=0;
        int rowsum=0;
        int colsum=0;
        int diag1=0;
        int diag2=0;
        for(int k=0;k<3;k++){
            rowsum+=square[i][k];
            colsum+=square[k][j];
            if(i==k) diag1+=square[i][k];
            if(i+k==2) diag2+=square[i][k];
        }
        cost+=perfect-rowsum;
        cost+=perfect-colsum;
        if(i==j) cost+=perfect-diag1;
        if(i+j==2) cost+=perfect-diag2;
        return cost;
    }
    public void print(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                System.out.print(square[i][j]+" ");
            }
            System.out.println();
    }

    }
}
