package donnoe.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author joshuadonnoe
 */
enum ValueStatus {

    /**
     * DO NOT CHANGE THE ORDER.
     */
    KNOWN {

                @Override
                <R> R get(CalculatingThread<R> thread) throws ExecutionException, InterruptedException, TimeoutException {
                    return thread.getValue();
                }
            },
    /**
     *
     */
    PENDING {

                @Override
                <R> R get(CalculatingThread<R> thread) throws ExecutionException, InterruptedException, TimeoutException {
                    throw new TimeoutException();
                }

            },
    /**
     *
     */
    DOES_NOT_EXIST {

                @Override
                <R> R get(CalculatingThread<R> thread) throws ExecutionException, InterruptedException, TimeoutException {
                    return thread.getException();
                }

            };

    /**
     *
     * @param future
     * @return
     */
    public static ValueStatus getValueStatus(Future<?> future) {
        return FutureStatus.getFutureStatus(future).valueStatus;
    }

    abstract <R> R get(CalculatingThread<R> thread) throws ExecutionException, InterruptedException, TimeoutException;

}
