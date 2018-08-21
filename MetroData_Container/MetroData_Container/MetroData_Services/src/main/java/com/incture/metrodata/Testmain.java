package com.incture.metrodata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.incture.metrodata.service.SequenceNumberGen;
import com.incture.metrodata.util.ServicesUtil;

class MergeSort {

	public void mergesort(int a[], int l, int r) {
		if (l < r) {
			int m = l + r / 2;
			mergesort(a, l, m);
			mergesort(a, m + 1, r);
			merge(a, l, m, r);
		}
	}

	private void merge(int[] a, int l, int m, int r) {
		int L[] = new int[l + m + 1];
		int R[] = new int[r - m];
		int lc = l, mc = m, rc = r;
		for (int i = 0; i < L.length; i++)
			L[i] = a[lc++];
		for (int i = 0; i < R.length; i++)
			R[i] = a[++mc];

		int k = l, i = 0, j = 0;
		while (i < L.length && j < R.length) {
			if (L[i] > R[j]) {
				a[k] = R[j++];

			} else {
				a[k] = L[i++];
				i++;
			}
			k++;
		}

		if (i < L.length) {
			for (; i <= m; i++)
				a[k++] = L[i];
		} else if (j < R.length) {
			for (i = j; i <= r; i++)
				a[k++] = R[i];
		}

	}

	public void swap(int a[], int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
}

public class Testmain {

	public static void main(String[] args) {
		Long str = 1534813490083L;
		Date today;
		String dateOut;
		DateFormat dateFormatter;

		new Date(Long.valueOf(str));
		System.out.println(ServicesUtil.convertTime(str));
		System.out.println(new Date());
	}

	public static int longestConsecutive(int[] num) {
		// if array is empty, return 0
		if (num.length == 0) {
			return 0;
		}

		Set<Integer> set = new HashSet<Integer>();
		int max = 1;

		for (int e : num)
			set.add(e);

		for (int e : num) {
			int left = e - 1;
			int right = e + 1;
			int count = 1;

			while (set.contains(left)) {
				count++;
				set.remove(left);
				left--;
			}

			while (set.contains(right)) {
				count++;
				set.remove(right);
				right++;
			}

			max = Math.max(count, max);
		}

		return max;
	}
}
