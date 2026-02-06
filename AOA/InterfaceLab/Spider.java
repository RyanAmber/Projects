package AOA.InterfaceLab;
public class Spider extends Animal{
    public Spider() {
        super(8);
    }
    @Override
    protected void eat() {
        System.out.println("The spider eats.");
    }
}
