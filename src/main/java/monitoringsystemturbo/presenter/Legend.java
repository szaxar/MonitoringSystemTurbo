package monitoringsystemturbo.presenter;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Legend extends HBox {

    public Legend() {
        super();
    }

    public Legend(double spacing) {
        super(spacing);
    }

    public Legend(Node... children) {
        super(children);
    }

    public Legend(double spacing, Node... children) {
        super(spacing, children);
    }

    public void addElement(Color color, String name) {
        Rectangle square = new Rectangle(15, 15);
        square.setArcHeight(5);
        square.setArcWidth(5);
        square.setFill(color);
        Label label = new Label(name);
        label.setPadding(new Insets(0, 5, 0, 0));
        getChildren().addAll(square, label);
    }
}
