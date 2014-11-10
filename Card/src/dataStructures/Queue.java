package dataStructures;

import java.util.Iterator;
import java.util.NoSuchElementException;
public class Queue<T> implements Iterable<T> {
	private Stack<T> elements;
	public Queue(){	elements = new Stack<T>();}
	public void enqueue(T value){
		elements.reverse();	 
		elements.push(value);
		elements.reverse();  
	}
	public T dequeue(){	return elements.pop();}
	public T peek(){return elements.peek();}
	public int size(){return elements.size();}
	public boolean isEmpty(){return elements.isEmpty();}
	@Override
	public Iterator<T> iterator(){
		return new QueueIterator();
	}
	
	private class QueueIterator implements Iterator<T> {
		private Stack<T> stack;
		public QueueIterator(){
			stack = elements.copy();
		}
		public boolean hasNext(){
			return !stack.isEmpty();
		}
		public T next(){
			if(hasNext()){
				return stack.pop();
			} else {
				throw new NoSuchElementException();
			}
		}
		public void remove(){
			throw new UnsupportedOperationException("Not Supported");
		}
	}
}