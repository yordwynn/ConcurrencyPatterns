package com.company;

import sun.misc.Queue;

import java.util.Vector;

/**
 * Created by yongv on 20.12.2015.
 */
public class JacobyParallel extends Quest {
    Vector<Vector<Double>> a;
    Vector<Double> b;
    Vector<Double> x;
    Vector<Double> x1;
    Queue<JacobyThread> quests;
    volatile int n;

    public Vector<Double> getSolution() {
        if (!isDone()) {
            throw new Error("Not yet computed");
        }
        return x;
    }

    public JacobyParallel(int n) {
        quests = new Queue<>();
        this.n = n;
        a = new Vector<>();
        b = new Vector<>();
        for (int i = 0; i < n; ++i) {
            a.add(new Vector<>());
            for (int j = 0; j < n; ++j) {
                if (i != j) {
                    a.get(i).add(0.1 / (i + j));
                }
                else {
                    a.get(i).add(j, 1.0);
                }
            }
        }
        for (int i = 0; i < n; ++i) {
            b.add(Math.sin(i));
        }
    }

    @Override
    public void run() {
        Double eps = 1e-6;
        //Задаем вектор начальных приближений
        x1 = new Vector<>();
        for (int i = 0; i < n; ++i) {
            x1.add(0.0);
        }
        //Итерации до достижения точности
        double m;
        do {
            m = 0;
            x = (Vector<Double>)x1.clone();
            //Создаем побочные
            for (int i = 0; i < n; ++i) {
                JacobyThread temp = new JacobyThread(n, a, b, x, x1, i);
                //Добавляем все побочные задания в очередь основного задания
                quests.enqueue(temp);
                //Добавляем побочные задания в очередь задания потока
                temp.fork();
            }
            //Дожидаемся выполнения всех побочных заданий на текущей итерации и пересчитываем услове сходимости
            for (int i = 0; i < n; ++i) {
                JacobyThread temp;
                try {
                    temp = quests.dequeue();
                    temp.join();
                    if (m < Math.abs(x.get(i) - x1.get(i))) {
                        m = Math.abs(x.get(i) - x1.get(i));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } while (m > eps);
    }

    class JacobyThread extends Quest {
        Vector<Vector<Double>> a;
        Vector<Double> b;
        Vector<Double> x;
        Vector<Double> x1;
        volatile int n;
        volatile int i;

        public JacobyThread(int n, Vector<Vector<Double>> a, Vector<Double> b, Vector<Double> x, Vector<Double> x1, int i) {
            this.a = a;
            this.b = b;
            this.x = x;
            this.x1 = x1;
            this.n = n;
            this.i = i;
        }

        @Override
        public void run() {
            double s = 0;

            for (int j = 0; j < n; ++j) {
                if (i != j) {
                    s -= a.get(i).get(j) * x.get(j);
                }
            }

            s += b.get(i);
            s /= a.get(i).get(i);
            x1.set(i, s);
        }
    }
}
