package com.a4basics;


import com.a4basics.ShipView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ShipDemo extends Application {
    @Override
    public void start(Stage stage) {
        ShipView view = new ShipView();
        ShipController controller = new ShipController();
        ShipModel model = new ShipModel();
        InteractionModel iModel = new InteractionModel();

        view.setModel(model);
        view.setController(controller);
        view.setInteractionModel(iModel);
        controller.setModel(model);
        controller.setInteractionModel(iModel);
        model.addSubscriber(view);
        iModel.addSubscriber(view);

        StackPane root = new StackPane(view);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(controller::handleKeyPressed);
    }

    public static void main(String[] args) {
        launch();
    }
}