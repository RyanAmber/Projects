package AOA;
public class Queue {
    public Node head;
    public Queue(Node head) {
        this.head=head;
    }
    public boolean isEmpty(){
        return head==null;
    }
    public void push(Node n){
        n.next=head;
        head=n;
    }
    public Object pop(){
        Node curr=new Node(head.data,head.next);
        if(curr.next!=null){
            curr=curr.next;
        }
        Object value=curr.data;
        curr=null;
        return value;
    }
    public Object peek(){
        Node temp=new Node(head.data,head.next);
        while(temp.next!=null){
            temp=temp.next;
        }
        return temp.data;
    }
}
