package com.workinggroup.chenfamily.gpsracecar.dao;

import java.util.ArrayList;

public interface GenericDAO<E, K> {
	
	public ArrayList<E> list();
	
	public E find(K key);
	
	public int update(E entity);
	
	public void remove(E entity);
	
	public void add(E entity);
	
	public void open();
	
	public void close();

}
