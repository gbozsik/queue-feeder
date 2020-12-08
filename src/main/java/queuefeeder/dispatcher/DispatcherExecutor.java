package queuefeeder.dispatcher;

public class DispatcherExecutor<T> implements Runnable {

    private DispatcherService<T> dispatcherService;

    public DispatcherExecutor(DispatcherService<T> dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void run() {
        boolean dispatching = true;
        while(dispatching) {
            dispatching = dispatcherService.dispatch();
        }
    }
}
