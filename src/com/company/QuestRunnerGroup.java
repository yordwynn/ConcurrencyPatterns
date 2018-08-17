package com.company;

/**
 * Created by Юрий_2 on 17.12.2015.
 */
public class QuestRunnerGroup {
    protected final QuestRunner[] threads;
    protected final int groupSize;

    /**
     * Конструктор
     */
    public QuestRunnerGroup(int groupSize) {
        this.groupSize = groupSize;
        threads = new QuestRunner[groupSize];
        for (int i = 0; i < groupSize; ++i) {
            threads[i] = new QuestRunner(this, i);
        }
        for (int i = 0; i < groupSize; ++i) {
            threads[i].start();
        }
    }

    /**
     * Заупскаем первую задачу на выполнение и ждем ее завершения
     * @param q
     */
    public void executeAndWait(final Quest q) {
        //Объект блокировки -- сам контейнер
        final QuestRunnerGroup thisGroup = this;
        //Создаем первое задание
        threads[0].put(new Quest() {
            public void run() {
                //Помещаем задание для выполнения
                q.fork();
                q.join();
                setDone();
                //Оповещаем контейнер о том, что задание выполнено
                synchronized(thisGroup) {
                    thisGroup.notifyAll();
                }
            }
        });
        //Ожидаем до тех пор, пока задание не будет выполнено
        synchronized(thisGroup) {
            try {
                thisGroup.wait();
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }

    public void cancel() {
        for(int i = 0; i < groupSize; i++) {
            threads[i].interrupt();
        }
    }

    public QuestRunner[] getRunners() {
        return threads;
    }
}
