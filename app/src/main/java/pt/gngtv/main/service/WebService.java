package pt.gngtv.main.service;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class WebService<T> {

    private final T service;

    public WebService(T service) {
        this.service = service;
    }

    public T getService() {
        return service;
    }
}
