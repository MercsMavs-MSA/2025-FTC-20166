package org.firstinspires.ftc.teamcode.Utilities;
import com.bylazar.field.Canvas;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.pedropathing.math.Pose;

import java.util.UUID;

public class DrawRobot {
    PanelsField panelsField = PanelsField.INSTANCE;
    FieldManager field = panelsField.getField();


    public void drawRobot(double x, double y, double heading, Pose goal1, Pose goal2) {
        field.setOffsets(panelsField.getPresets().getPEDRO_PATHING());

        field.setStyle("none", "red", 2);

        field.moveCursor(x, y);
        field.circle(4);
        field.line(
                1 * Math.cos(heading),
                1 * Math.sin(heading)
        );

        field.setStyle("none", "blue", 1);
        field.moveCursor(goal1.x(), goal1.y());
        field.circle(2);

        field.setStyle("none", "white", 1);
        field.moveCursor(goal2.x(), goal2.y());
        field.circle(2);

        field.update();
    }
}
