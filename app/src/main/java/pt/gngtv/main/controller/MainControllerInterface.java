package pt.gngtv.main.controller;

import java.util.List;

import pt.gngtv.model.Model;
import pt.gngtv.model.Wishlist;

/**
 * Created by filiperodrigues on 28/09/15.
 */
public interface MainControllerInterface {
    void setContent(List<Wishlist> data);

    void setModelsContent(List<Model> data);
}
