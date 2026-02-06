package AOA.InterfaceLab;

public class Fish extends Animal implements Pet {
    private String name;
    public Fish(String name) {
        super(0);
        this.name = name;
    }
    public Fish() {
        this("Fish");
    }
    @Override
    protected void walk() {
        System.out.println("Fish can't walk.");
    }
    @Override
    protected void eat() {
        System.out.println("The fish eats.");
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void play() {
        System.out.println("The fish plays.");
    }
    
}
