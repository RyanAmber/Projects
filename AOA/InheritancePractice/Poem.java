public abstract class Poem{
    public abstract int numLines();
    public abstract int getSyllables(int k);
    public void printRhythm(){
        for(int k = 1; k <= numLines(); k++){
            for(int j = 1; j <= getSyllables(k); j++){
                System.out.print("ta-");
            }
            System.out.println();
        }
    }
}