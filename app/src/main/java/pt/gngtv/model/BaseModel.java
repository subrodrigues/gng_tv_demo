package pt.gngtv.model;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public class BaseModel<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
