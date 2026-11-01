package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;

import java.util.ArrayList;
import java.util.List;

public class RobotConstants {
    public static class PositionConstants {
        public static Pose audienceRedStart = new Pose(8.5,9.5, 0);
        public static Pose audienceRedGoal = new Pose(58, 50, 90);
    }

    public static class ShooterConstants {
        public static double gateMin = 0;
        public static double gateMax = 1;
        public static double servoOneUp = 0.97;
        public static double servoTwoUp = 0.01;
        public static double servoOneDown = 0.26;
        public static double servoTwoDown = 0.72;
        public static double cornerPosition = 100;
        public static double closePosition = 35;
        public static double cornerSpeed = 2450;
        public static double closeSpeed = 2250;
        public static double closeVel = 2300;
        public static double cornerVel = 2500;
        public static double closeAng = -13;
        public static double cornerAng = 10;
        public static double farDist = 62.71;
        private static List<Pose> constructRedAudiencePentagonPoints() {
            List<Pose> redAudiencePentagonPoints = new ArrayList<>();

            redAudiencePentagonPoints.add(new Pose(0,0));
            redAudiencePentagonPoints.add(new Pose(22.6, 35.4));
            redAudiencePentagonPoints.add(new Pose(69.8, 35.4));
            redAudiencePentagonPoints.add(new Pose(90,0));

            return redAudiencePentagonPoints;
        }

        public static LERP velocityLERP = new LERP(0, closeVel, farDist, cornerVel, true);
        public static LERP positionLERP = new LERP(0, closePosition, farDist, cornerPosition, true);
        public static LERP servoOneLERP = new LERP(0, servoOneUp, 100, servoOneDown, true);
        public static LERP servoTwoLERP = new LERP(0, servoTwoUp, 100, servoTwoDown, true);

        public static ZoneDetector redAudiencePentagon = new ZoneDetector(constructRedAudiencePentagonPoints());
    }
}
