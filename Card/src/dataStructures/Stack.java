package dataStructures;


public class Stack<T> implements StackADT<T> {
	protected Node<T> head;
	protected int size = 0;

	public Stack(){} 
	public void push(T value){
		if(head == null){ 
			head = new Node<T>(value, null);
		} else { 
			Node<T> latest = new Node<T>(value, head);
			head = latest;
		}
		size++;
	}

	public T pop(){
		if(isEmpty()){ 
			return null;
		}
		size--;
		T temp = head.getValue();
		head = head.getNext();
		return temp;
	}

	public T peek(){
		if(isEmpty()){
			return null;
		}
		return head.getValue();
	}

	public boolean isEmpty(){
		return head == null;
	}
	public void clear(){
		head = null;
		size = 0;
	}
	public int size(){
		return size;
	}
	public void reverse(){
		reverse(head, null);
	}
	private void reverse(Node<T> node, Node<T> prev){
		if(node != null){ 
			reverse(node.getNext(), node); 
			if(node.getNext() == null){ 
				head = node;
			}
			node.setNext(prev);
		}
	}

	public Stack<T> reverseCopy(){
		Stack<T> temp = new Stack<T>(); 
		for(Node<T> node = head; node != null; node = node.getNext()){
			temp.push(node.getValue());
		}
		return temp;
	}

	/**返回一个反转了的*/
	public Stack<T> copy(){
		Stack<T> temp = reverseCopy();
		temp.reverse(); 
		return temp;
	}

	public void appendStack(Stack<T> stack){
		if(stack == null || stack.isEmpty()){
			return;
		}
		Stack<T> temp = stack.reverseCopy();
		while(!temp.isEmpty()){
			push(temp.pop()); 
		}
	}

	@SuppressWarnings("hiding")
	protected class Node<T> {
		private T value;
		private Node<T> next;
		
		public Node(T value, Node<T> next){
			this.value = value;
			this.next = next;
		}
		public void setValue(T value){this.value = value;}
		public T getValue(){return value;}
		public void setNext(Node<T> next){this.next = next;}
		public Node<T> getNext(){return next;}
	}
}