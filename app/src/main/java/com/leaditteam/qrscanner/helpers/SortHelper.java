package com.leaditteam.qrscanner.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leaditteam on 21.04.17.
 */

public class SortHelper {
    public static <T extends Comparable<T>> void concurrentSort(
            final List<T> key, List<?>... lists){
        // Create a List of indices
        // Lists are size 0 or 1, nothing to sort
        if(key.size() < 2)
            return;

        // Create a List of indices
        final List<Integer> indices = new ArrayList<Integer>();
        for(int i = 0; i < key.size(); i++) {
            indices.add(i);
        }
        // Sort the indices list based on the key
        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                DateHelper dt = new DateHelper();
                TimeParser oc = dt.parseTime(key.get(i).toString());
                TimeParser ov = dt.parseTime(key.get(j).toString());
                if (oc.getMIN() > ov.getMIN()){
                    return -1;
                } else return 0;


            }
        });
        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                DateHelper dt = new DateHelper();
                TimeParser oc = dt.parseTime(key.get(i).toString());
                TimeParser ov = dt.parseTime(key.get(j).toString());
                if (oc.getHOUR() > ov.getHOUR()){
                    return -1;
                } else return 0;


            }
        });

        Collections.sort(indices, new Comparator<Integer>(){
            @Override public int compare(Integer i, Integer j) {
                DateHelper dt = new DateHelper();
                TimeParser oc = dt.parseTime(key.get(i).toString());
                TimeParser ov = dt.parseTime(key.get(j).toString());
                if (oc.getDAY() > ov.getDAY()){
                    return -1;
                } else return 0;


            }
        });



        // Create a mapping that allows sorting of the List by N swaps.
        // Only swaps can be used since we do not know the type of the lists
        Map<Integer,Integer> swapMap = new HashMap<Integer, Integer>(indices.size());
        List<Integer> swapFrom = new ArrayList<Integer>(indices.size()),
                swapTo   = new ArrayList<Integer>(indices.size());
        for(int i = 0; i < key.size(); i++){
            int k = indices.get(i);
            while(i != k && swapMap.containsKey(k))
                k = swapMap.get(k);

            swapFrom.add(i);
            swapTo.add(k);
            swapMap.put(i, k);
        }

        // use the swap order to sort each list by swapping elements
        for(List<?> list : lists)
            for(int i = 0; i < list.size(); i++)
                try {

                    Collections.swap(list, swapFrom.get(i),swapTo.get(i));
                }catch (Exception e){e.printStackTrace();}
    }

}
