package org.firstinspires.ftc.teamcode.Utilities;
import com.bylazar.field.Canvas;
import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;

import java.util.UUID;

public class DrawRobot {
    PanelsField panelsField = PanelsField.INSTANCE;
    FieldManager field = panelsField.getField();


    public void drawRobot(double x, double y, double heading) {
        field.setOffsets(panelsField.getPresets().getPEDRO_PATHING());

        field.setStyle("none", "red", 2);

        field.moveCursor(x, y);
        field.circle(4);
        field.line(
                1 * Math.cos(heading),
                1 * Math.sin(heading)
        );

        field.update();
    }
}
