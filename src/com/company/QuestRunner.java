package com.company;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Юрий_2 on 17.12.2015.
 * Класс-поток. Содержит очередь заданий
 */
public class QuestRunner extends Thread {
    //Пулл потоков
    private final QuestRunnerGroup _container;
    //Статус потоков
    protected volatile boolean _active;
    //Индекс потока в пулле
    final int _id;

    Random dot;

    //Очередь заданий
    private final ConcurrentLinkedDeque<Quest> _quests;

    public QuestRunner(QuestRunnerGroup container, int id) {
        _container = container;
        _quests = new ConcurrentLinkedDeque<>();
        _id = id;
        _active = true;
        dot = new Random();
    }

    protected final int getQuestId() {
        return _id;
    }

    //Поместить задание в конец очереди потока
    public void put(Quest item) {
        _quests.addFirst(item);
    }
    public Quest take() {
        return _quests.pollFirst();
    }
    public Quest takeLast() {
        return _quests.pollLast();
    }

    //Получить пулл потоков
    protected final QuestRunnerGroup getTaskRunnerGroup() {
        return _container;
    }

    //Если в очереди потока закончились задания, поток пытается взять на выполнение задчу из чухой очереди
    void steal(final Quest waitingFor) {
        Quest task = null;
        QuestRunner[] runners = _container.getRunners();
        int stealFrom = dot.nextInt(runners.length - 1);
        //Обходим очереди соседних потокв
        for (int i = 0; i < runners.length; ++i) {
            QuestRunner temp = runners[stealFrom];
            //Если задание, выполнение которого ожидал поток, завершилось, выходим из функции
            if (waitingFor != null && waitingFor.isDone()) {
                break;
            }
            else {
                if (temp != null && temp != this) {
                    task = temp.takeLast();
                }
                if (task != null) {
                    break;
                }
                yield();
                //Если не удалось получить задание из случайной очереди, просматриваем следующую очередь
                stealFrom = (stealFrom + 1) % runners.length;
            }
        }
        if( task != null && ! task.isDone()) {
            task.invoke();
        }
    }

    //Основной метод для потока
    public void run() {
        Quest task;
        try {
            //Выполняем задания пока не встретится терминирующее задание
            while (!isInterrupted()) {
                task = take();
                if (task != null && !task.isDone()) {
                        task.invoke();
                }
                else {
                    steal(null);
                }
            }
        }
        finally {
            _active = false;
        }
    }

    //Ожидаем завершение задания
    //Пока задание не выполнено, можем выполнять другие задания
    protected final void taskJoin(final Quest w) {
        while(!w.isDone()) {
            Quest task = take();
            if (task != null && !task.isDone()) {
                task.invoke();
            }
            else {
                steal(w);
            }
        }
    }
}
