package com.ypitta.auctionsite.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GeneralUtil {
	
	
	  public static <T> List<T> 
	    getListFromIterator(Iterator<T> iterator) 
	    { 
	  
	        // Create an empty list 
	        List<T> list = new ArrayList<>(); 
	  
	        // Add each element of iterator to the List 
	        iterator.forEachRemaining(list::add); 
	  
	        // Return the List 
	        return list; 
	    } 
}
