package com.dekhan.swagata.util.algo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SuffixArray {
    
    private static final Logger log = LogManager.getLogger(com.dekhan.swagata.util.algo.SuffixArray.class); 
    
    private static final int CUTOFF =  5;   // cutoff to insertion sort (any value between 0 and 12)

    private final char[] text;
    private final int[] index;   
    private final int n;
    
    public int length() {
        return n;
    }
    
    private boolean isNullOrEmpty(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public SuffixArray(String text) {
	log.debug("Start");
        n = text.length();
        text = text + '\0';
        this.text = text.toCharArray();
        this.index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        sort(0, n-1, 0);
        log.debug("End");
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) { 
	log.debug("Start");
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            log.debug("End");
            return;
        }

        int lt = lo, gt = hi;
        char v = text[index[lo] + d];
        int i = lo + 1;
        while (i <= gt) {
            char t = text[index[i] + d];
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(lo, lt-1, d);
        if (v > 0) sort(lt, gt, d+1);
        sort(gt+1, hi, d);
        log.debug("End");
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
	log.debug("Start");
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
        log.debug("End");
    }

    // is text[i+d..n) < text[j+d..n) ?
    private boolean less(int i, int j, int d) {
	log.debug("Start");
        if (i == j) {
            log.debug("End");
            return false;
        }
        i = i + d;
        j = j + d;
        while (i < n && j < n) {
            if (text[i] < text[j]) {
        	log.debug("End");
        	return true;
            }
            if (text[i] > text[j]) {
        	log.debug("End");
        	return false;
            }
            i++;
            j++;
        }
        log.debug("End");
        return i > j;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
	log.debug("Start");
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
        log.debug("End");
    }    

    public int index(int i) {
	log.debug("Start");
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        log.debug("End");
        return index[i];
    }

    public int lcp(int i) {
	log.debug("Start");
        if (i < 1 || i >= n) throw new IllegalArgumentException();
        log.debug("End");
        return lcp(index[i], index[i-1]);
    }

    // longest common prefix of text[i..n) and text[j..n)
    private int lcp(int i, int j) {
	log.debug("Start");
        int length = 0;
        while (i < n && j < n) {
            if (text[i] != text[j]) {
        	log.debug("End");
        	return length;
            }
            i++;
            j++;
            length++;
        }
        log.debug("End");
        return length;
    }

    public String select(int i) {
	log.debug("Start");
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        log.debug("End");
        return new String(text, index[i], n - index[i]);
    }

    public int rank(String query) {
	log.debug("Start");
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(query, index[mid]);
            if      (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else {
        	log.debug("End");
        	return mid;
            }
        }
        log.debug("End");
        return lo;
    } 

    private int compare(String query, int i) {
	log.debug("Start");
        int m = query.length();
        int j = 0;        
        while (i < n && j < m) {
            if (query.charAt(j) != text[i]) {
        	log.debug("End");
        	return query.charAt(j) - text[i];
            }
            i++;
            j++;
        }
        if (i < n) {
            log.debug("End");
            return -1;
        }
        if (j < m) {
            log.debug("End");
            return +1;        
        }
        log.debug("End");
        return 0;
    }     
}

