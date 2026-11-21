package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.Utilities.LERP;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;

import java.util.ArrayList;
import java.util.List;

public class RobotConstants {

    public static class PositionConstants {
        private static final PoseFactory factory = PoseFactory.degrees();

//        public static Pose audienceGoal = new Pose(58, 50, 90);
//        public static Pose farGoal = new Pose(58, 94, 270);
//        public static Pose audienceGoal = factory.of(57.8, 54, 90);
        public static Pose flowerGoal = factory.of(58.45, 92.2, 90);
        public static Pose noFlowerGoal = factory.of(58.45, 53.3, 90);
//        public static Pose farGoal = factory.of(52.8, 91.8, 270);
        public static Pose audienceStart = factory.of(8.5,9.5, 0);
        public static Pose farStart = factory.of(8.5, 134.5, 0);
        public static Pose noFlowerStart = factory.of(58.5, 10, 270);
        public static Pose flowerStart = factory.of(60.5, 135.1, 90);
        public static Pose noFlowerPark = factory.of(12.8, 92.8, 270);
        public static Pose flowerPark = factory.of(13.3, 121.2, 90);
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

        public static double flowerArmRightUp = 0.66;
        public static double flowerArmRightDown = 0.16;
        public static double flowerArmLeftUp = 0.4;
        public static double flowerArmLeftDown = 0.9;

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

        public static ZoneDetector noFlowerPentagon = new ZoneDetector(constructRedAudiencePentagonPoints());
        public static ZoneDetector flowerPentagon = new ZoneDetector(constructRedFarPentagonPoints());
    }
}
