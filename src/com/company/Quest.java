package com.company;

/**
 * Created by Юрий_2 on 15.12.2015.
 * Класс-задание. Имеет два состояния -- выполнен или не выполнен.
 * Может поместить сам себя в очередь текущего потока
 * Может обеспечить выполнение ожидание завершения задания
 */
public abstract class Quest implements Runnable {
    //Статус задания
    private volatile boolean isDone;

    public final void setDone() {
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }

    //Получить текущий поток
    public static QuestRunner getTaskRunner() {
        return (QuestRunner)Thread.currentThread();
    }

    //Добавить задачу в очередь случайного потока
    public void fork() {
        getTaskRunner().put(this);
    }

    //Ожидаем, пока не выполнится текущее задание
    public void join() {
        getTaskRunner().taskJoin(this);
    }

    //Запустить текущее задание
    public void invoke() {
        if (!isDone()) {
            run();
            setDone();
        }
    }
}
