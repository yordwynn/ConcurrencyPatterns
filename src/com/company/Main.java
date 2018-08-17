package com.company;

public class Main {
    public static void main(String[] args) {
        /*Vector<Integer> ns = new Vector<>();
        ns.add(4000);
        ns.add(8000);
        ns.add(16000);
        ns.add(32000);
        ns.add(64000);
        ns.add(128000);
        ns.add(265000);
        ns.add(512000);
        ns.add(1024000);
        ns.add(2048000);
        ns.add(4096000);
        ns.add(8192000);
        ns.add(16384000);
        ns.add(32768000);
        for (Integer n: ns) {
            ArrayList<Double> data1 = new ArrayList<>();
            Random dot = new Random();
            for (int i = 0; i < n; ++i) {
                data1.add(dot.nextDouble() * n);
            }
            /*Sort sort = new Sort();
            int m = 0;
            for (int i = 0; i < 10; ++i) {
                double t1 = System.currentTimeMillis();
                sort.sort((ArrayList<Double>) data1.clone());
                t1 = System.currentTimeMillis() - t1;
                m += t1;
            }
            System.out.println(m / 10.0);*/
            /*int m = 0;
            for (int i = 0; i < 10; ++i) {
                QuestRunnerGroup g = new QuestRunnerGroup(8);
                QuickSort quickSort = new QuickSort((ArrayList<Double>) data1.clone());
                double t2 = System.currentTimeMillis();
                g.executeAndWait(quickSort);
                t2 = System.currentTimeMillis() - t2;
                g.cancel();
                m += t2;
            }
            System.out.println(m / 10.0);*/
        int m = 0;
        for (int i = 0; i < 6; ++i) {
            JacobyParallel jacobyParallel = new JacobyParallel(6400);
            QuestRunnerGroup g = new QuestRunnerGroup(4);
            double t1 = System.currentTimeMillis();
            g.executeAndWait(jacobyParallel);
            t1 = System.currentTimeMillis() - t1;
            g.cancel();
            if (i != 0) {
                m += t1;
            }
        }
        System.out.println(m / 5.0);
        /*int m = 0;
        for (int i = 0; i < 6; ++i) {
            Jacoby jacoby = new Jacoby(6400);
            double t2 = System.currentTimeMillis();
            jacoby.Solve(1e-6);
            t2 = System.currentTimeMillis() - t2;
            if (i != 0) {
                m += t2;
            }
        }
        System.out.println(m / 5.0);*/
        //}
    }
}
