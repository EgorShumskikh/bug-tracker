package project.controller;

import project.model.factory.MainFactory;

public class SingleController {
    private static MainFactory controller = new MainFactory();
    private SingleController(){    };
    public static MainFactory getInstance() {
        return controller;
    }
}
