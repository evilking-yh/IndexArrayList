package com.evilking.test;

import java.util.LinkedList;
import java.util.List;

import com.evilking.indexlist.StringIndexArrayList;

public class IndexArrayListTest {
	private static int count = 3000000;

	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		StringIndexArrayList ial = new StringIndexArrayList();
		
		for(int i = 0;i < count;i++){
			ial.put(i, i + 100 + "");
		}
		
		for(int i = 0; i < count;i++){
			ial.get(i);
//			System.out.println(ial.get(i));
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("IndexArrayList 耗时: " + (endTime - startTime) + "ms");
		
		List<String> nib = new LinkedList<String>();
		for(int i = 0; i < count; i++){
			nib.add(i + 100 + "");
		}
		
		for(int i = 0; i < count;i++){
			nib.get(i);
		}
		
		System.out.println("LinkedList 耗时: " + (System.currentTimeMillis() - endTime) + "ms");
	}

}
