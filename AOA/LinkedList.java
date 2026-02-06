package AOA;
public class LinkedList {
    private Node head;
    public boolean isEmpty(){
        return head==null;
    }
    public void insert(Object data){
        head=new Node(data,head);
    }
    public Object extract(){
        if(isEmpty()){
            return null;
        }else{
            Object data=head.data;
            head=head.next;
            return data;
        }
    }
    public void print(int n){
        Node node=head;
        for (int i=0;i<n&&node!=null;i++){
            node=node.next;
        }
        if(node!=null)
        System.out.println(node.data);
    }
    public void print(){
        Node node=head;
        while(node!=null){
            System.out.println(node.data);
            node=node.next;
        }
    }
}
