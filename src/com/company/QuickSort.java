package com.company;

import java.util.List;

/**
 * Created by Юрий_2 on 20.12.2015.
 */
public class QuickSort extends Quest {
    public List<Double> data;
    private int left;
    private int right;

    public QuickSort(List<Double> data){
        this.data=data;
        this.left = 0;
        this.right = data.size() - 1;
    }

    public QuickSort(List<Double> data, int left, int right){
        this.data = data;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        if (left < right) {
            //Находим центральный элемент
            Double mid = data.get((left + right) / 2);
            int i = left;
            int j = right;
            //Упорядочиваем элементы
            while (i <= j) {
                while (data.get(i).compareTo(mid) < 0) ++i;
                while (data.get(j).compareTo(mid) > 0) --j;
                if (i > j) continue;
                if (i != j) {
                    Double temp = data.get(i);
                    data.set(i, data.get(j));
                    data.set(j, temp);
                }
                ++i;
                --j;
            }
            //Создаем новые задания
            QuickSort x1 = new QuickSort(data, i, right);
            QuickSort x2 = new QuickSort(data, left, j);
            //Добавляем задания в очередь
            x1.fork();x2.fork();
            //Дожидаемся выполнения заданий
            x1.join();x2.join();
        }
    }
}
