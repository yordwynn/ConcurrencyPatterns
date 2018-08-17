package com.company;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Юрий_2 on 20.12.2015.
 */
public class Sort {
    public void sort(ArrayList<Double> data) {
        sort(data, 0, data.size() - 1);
    }

    private void sort(ArrayList<Double> data, int left, int right) {
        if (left < right) {
            Double mid = data.get((left + right) / 2);
            int i = left;
            int j = right;
            while (i <= j) {
                while (data.get(i).compareTo(mid) < 0) ++i;
                while (data.get(j).compareTo(mid) > 0) --j;
                if (i > j) continue;
                Double temp = data.get(i);
                data.set(i, data.get(j));
                data.set(j, temp);
                ++i;
                --j;
            }
            sort(data, i, right);
            sort(data, left, j);
        }
    }
}
