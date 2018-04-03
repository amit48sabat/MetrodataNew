package com.incture.metrodata.util;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class LeaderBoardComparator implements Comparator<Map<String, Long>>{

	private String sortBy="a";
	
	public LeaderBoardComparator(String sortBy) {
		super();
		this.sortBy = sortBy;
	}


	@Override
	public int compare(Map<String, Long> o1, Map<String, Long> o2) {
		 
		 int res = o2.get(sortBy).compareTo(o1.get(sortBy));
         return res != 0 ? res : 1;
	}

	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e2.getValue().compareTo(e1.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
	}

}
