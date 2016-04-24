package dfs_api;

import java.util.LinkedList;
import java.util.List;

public class SynList<T> 
{
	private List<T> list;
	
	public SynList()
	{
		list = new LinkedList<T>();		
	}
	
	/* Synchronized method for accessing the list for modification */
	public void addLast(T t)
	{
		list.add(list.size(), t);		
	}
	
	/* Synchronized method for accessing the list for modification */
	public void set(int index,T t)
	{
		list.add(index, t);		
	}
	
	/* No need of Synchronized method for get*/
	public T get(int index)
	{
		return list.get(index);
	}
	
	/* Synchronized method for removing element from the list */
	public T remove(int index)
	{				
		return list.remove(index);
	}
	
	public int getSize()
	{
		return list.size();
	}
}
