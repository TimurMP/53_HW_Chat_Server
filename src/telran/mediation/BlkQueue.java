package telran.mediation;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlkQueue<T> implements IBlkQueue<T> {
	int maxSize;
	LinkedList<T> queue = new LinkedList<>();
	Lock mutex = new ReentrantLock();
	Condition senderWaitingCondition = mutex.newCondition();
	Condition receiverWaitingCondition = mutex.newCondition();

	public BlkQueue(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public void push(T message) {
		mutex.lock();
		try {
			while (queue.size() >= maxSize) {
				try {
					senderWaitingCondition.await();
				} catch (InterruptedException e) {
					System.out.println("thread was interrupted");
				}
			}
			queue.add(message);
			receiverWaitingCondition.signal();
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public T pop() {
		mutex.lock();
		try {
			while (queue.isEmpty()) {
				try {
					receiverWaitingCondition.await();
				} catch (InterruptedException e) {
					System.out.println("thread was interrupted");
				}
			}
			T msg = queue.poll();
			senderWaitingCondition.signal();
			return msg;
		} finally {
			mutex.unlock();
		}
	}
}