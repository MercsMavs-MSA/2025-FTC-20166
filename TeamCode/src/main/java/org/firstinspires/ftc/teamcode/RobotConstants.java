package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;

import java.util.ArrayList;
import java.util.List;

public class RobotConstants {
    public static class positionConstants {
        public Pose backRedStart = new Pose(8.5, 132, 0);
        public Pose backRedGoal = new Pose(58, 93.5, 270);
    }

    public static class shooterConstants {
        public double closeVel = 2300;
        public double cornerVel = 2500;
        public double closeAng = -13;
        public double cornerAng = 10;
        public double farDist = 62.71;
        private List<Pose> constructPentagonPoints() {
            List<Pose> pentagonPoints = new ArrayList<>();

//            pentagonPoints.add(new Pose());

            return pentagonPoints;
        }

        public LERP velocityLERP = new LERP(0, closeVel, farDist, cornerVel, true);
        public LERP angleLERP = new LERP(0, closeAng, farDist, cornerAng, true);

        public ZoneDetector pentagon = new ZoneDetector(constructPentagonPoints());
    }
}
