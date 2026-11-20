package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;

import java.util.ArrayList;
import java.util.List;

public class RobotConstants {
    public static class PositionConstants {
//        public static Pose audienceGoal = new Pose(58, 50, 90);
//        public static Pose farGoal = new Pose(58, 94, 270);
        public static Pose audienceGoal = new Pose(59, 51.3, 90);
        public static Pose farGoal = new Pose(59, 86.3, 270);

        public static Pose audienceStart = new Pose(8.5,9.5, 0);
        public static Pose farStart = new Pose(8.5, 134.5, 0);
        public static Pose audienceStart2 = new Pose(56.7, 10, 270);
        public static Pose farStart2 = new Pose(60.5, 132.1, 90);

        public static Pose parkPoint1 = new Pose(14.1, 94, 270);
    }

    public static class ShooterConstants {
        public static double servoOneUp = 0.97;
        public static double servoTwoUp = 0.01;
        public static double servoOneDown = 0.26;
        public static double servoTwoDown = 0.72;
        public static double gateOneUp = 31.5;
        public static double gateTwoUp = 35;
        public static double gateOneDown = 52.5;
        public static double gateTwoDown = 57;
        public static double cornerPosition = 100;
        public static double closePosition = 50;
        public static double closeVel = 3067;
        public static double cornerVel = 3333;
        public static double farDist = 62.71;

        public static double intakeWait = 400; // In milliseconds

        private static List<Pose> constructRedAudiencePentagonPoints() {
            List<Pose> redAudiencePentagonPoints = new ArrayList<>();

            redAudiencePentagonPoints.add(new Pose(0,0));
            redAudiencePentagonPoints.add(new Pose(22.6, 35.4));
            redAudiencePentagonPoints.add(new Pose(69.8, 35.4));
            redAudiencePentagonPoints.add(new Pose(90,0));

            return redAudiencePentagonPoints;
        }

        private static List<Pose> constructRedFarPentagonPoints() {
            List<Pose> redFarPentagonPoints = new ArrayList<>();

            redFarPentagonPoints.add(new Pose(0, 144.0));
            redFarPentagonPoints.add(new Pose(22.6, 108.6));
            redFarPentagonPoints.add(new Pose(69.8, 108.6));
            redFarPentagonPoints.add(new Pose(90.0, 144.0));

            return redFarPentagonPoints;
        }

        public static LERP velocityLERP = new LERP(0, closeVel, farDist, cornerVel, true);
        public static LERP positionLERP = new LERP(0, closePosition, farDist, cornerPosition, true);
        public static LERP servoOneLERP = new LERP(0, servoTwoUp, 100, servoTwoDown, true);
        public static LERP servoTwoLERP = new LERP(0, servoOneUp, 100, servoOneDown, true);
        public static LERP leftGateLERP = new LERP(0, gateTwoDown, 100, gateTwoUp, true);
        public static LERP rightGateLERP = new LERP(0, gateOneDown, 100, gateOneUp, true);

        public static ZoneDetector audiencePentagon = new ZoneDetector(constructRedAudiencePentagonPoints());
        public static ZoneDetector farPentagon = new ZoneDetector(constructRedFarPentagonPoints());
    }
}
