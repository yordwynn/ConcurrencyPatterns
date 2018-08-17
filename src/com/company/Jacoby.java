package com.company;

import java.util.DoubleSummaryStatistics;
import java.util.Vector;

/**
 * Created by yongv on 20.12.2015.
 */
public class Jacoby {
    Vector<Vector<Double>> a;
    Vector<Double> b;
    Vector<Double> x;
    int n;

    public Vector<Double> getSolution() {
        return x;
    }

    public double formJacobi(Vector<Double> x1) {
        double max = 0;

        for (int i = 0; i < n; ++i) {
            double s = 0;

            for (int j = 0; j < n; ++j) {
                if (i != j) {
                    s -= a.get(i).get(j) * x.get(j);
                }
            }

            s += b.get(i);
            s /= a.get(i).get(i);
            if (Math.abs(x.get(i) - s) > max) {
                max = Math.abs(x.get(i) - s);
            }
            x1.set(i, s);

        }
        return max;
    }

    public int Solve(double eps) {
        int kvo = 0;
        Double max = 5 * eps;
        Vector<Double> x1 = new Vector<>();
        for (int i = 0; i < n; ++i) {
            x1.add(0.0);
        }

        //итерации до достижения точности
        while (max > eps) {
            x = (Vector<Double>)x1.clone();
            max = formJacobi(x1);
            kvo++;// число итераций
        }

        return kvo;
    }

    public Jacoby(int n) {
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
}
