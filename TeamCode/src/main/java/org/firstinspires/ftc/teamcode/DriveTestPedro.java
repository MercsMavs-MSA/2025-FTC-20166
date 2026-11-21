package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.SubSystemBlinkIn;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemLimelight;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Utilities.DrawRobot;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
//@Disabled
public class DriveTestPedro extends LinearOpMode {
    private ElapsedTime flywheelStallAlarm = new ElapsedTime();
    private ElapsedTime intakeshootergate = new ElapsedTime();
    boolean stalled = false;
    double flywheelCurrent = 0;
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    private DrawRobot drawUtil = new DrawRobot();
    private SubSystemShooter subShooter;
    private SubSystemBlinkIn blinkIn;
    private SubSystemLimelight subLL;
    private Pose robotPose;
    private String robotString;
    private Pose goalPose1;
    private ZoneDetector pentagon1;
    private Pose goalPose2;
    private ZoneDetector pentagon2;
    private ZoneDetector currentPent;
    private Pose currentGoal;
    private double targetHeading; // in radians
    private double targetingError;
    private boolean tracking = false;
    private Follower follower = null;
    private boolean posGoodFromAuto = false;
    private boolean allGoodFromAuto = false;
    private String autoString;
//    private boolean canshoot = false;
    public enum allianceColors {
        RED,
        BLUE
    }

    private allianceColors alliance;

    private void updatePose() {
        follower.update();
        robotPose = follower.pose();
    }
    private void updateInitialSettings() {
        if (!(posGoodFromAuto && allGoodFromAuto)) {
            if (gamepad1.yWasPressed()) {
                goalPose1 = RobotConstants.PositionConstants.flowerGoal;
                pentagon1 = RobotConstants.ShooterConstants.flowerPentagon;
                goalPose2 = RobotConstants.PositionConstants.noFlowerGoal;
                pentagon2 = RobotConstants.ShooterConstants.noFlowerPentagon;
                robotPose = RobotConstants.PositionConstants.flowerStart;
                robotString = "Against flower, tiny gap, Far";
            } else if (gamepad1.bWasPressed()) {
                goalPose1 = RobotConstants.PositionConstants.flowerGoal;
                pentagon1 = RobotConstants.ShooterConstants.flowerPentagon;
                goalPose2 = RobotConstants.PositionConstants.noFlowerGoal;
                pentagon2 = RobotConstants.ShooterConstants.noFlowerPentagon;
                robotPose = RobotConstants.PositionConstants.noFlowerStart;
                robotString = "Audience, against tile teeth";
            } else if (gamepad1.xWasPressed()) {
                alliance = (alliance == allianceColors.RED ? allianceColors.BLUE : allianceColors.RED);
            }
        }
    }
    public void initializeHardware() {
        subShooter = new SubSystemShooter(hardwareMap);
        blinkIn = new SubSystemBlinkIn(hardwareMap);
        subLL = new SubSystemLimelight(hardwareMap);
        follower = Constants.create(hardwareMap);
    }

    public void sequenceShooting() {
        if (gamepad2.yWasPressed()) {
            intakeshootergate.reset();
        }

        if (subShooter.isReady && !stalled && targetingError <= 5) {
            subShooter.gateControl(0, false);
            subShooter.gateControl(1, false);

            if (intakeshootergate.milliseconds() >= RobotConstants.ShooterConstants.intakeWait) {
                subShooter.setIntakePower(Configurables.shootingIntakePower);
            }
        }
    }

    private void updateBlinkin()
    {
        if (stalled)
        {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.VIOLET);
        } else if (subShooter.isReady && (currentGoal != null) && Math.abs(Math.toDegrees(robotPose.heading()) - Math.toDegrees(targetHeading)) <= 5) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        } else if (currentGoal != null) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIME);
        } else {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        }
    }

    private Pose whichPentagon() {
        if (pentagon1.isRobotInside(robotPose)) {currentPent = pentagon1; return goalPose2;}
        if (pentagon2.isRobotInside(robotPose)) {currentPent = pentagon2; return goalPose1;}
        currentPent = null;
        return null;
    }

    private void monitorMotors() {
        if (flywheelCurrent < 6.5)
        {
            flywheelStallAlarm.reset();
        } else
        {
            if (flywheelStallAlarm.seconds() > 5) {stalled = true; subShooter.enabled = false;}
        }
    }

    public void runOpMode() throws InterruptedException {
        initializeHardware();
        alliance = allianceColors.RED;
        robotPose = RobotConstants.PositionConstants.flowerStart;
        robotString = "Against flower, tiny gap, Far";
        goalPose1 = RobotConstants.PositionConstants.flowerGoal;
        pentagon1 = RobotConstants.ShooterConstants.noFlowerPentagon;
        goalPose2 = RobotConstants.PositionConstants.noFlowerGoal   ;
        pentagon2 = RobotConstants.ShooterConstants.flowerPentagon;

        if (blackboard.get("Pose") != null) {
            robotPose = (Pose) blackboard.get("Pose");
            blackboard.remove("Pose");
            posGoodFromAuto = true;
        }
        if (blackboard.get("Alliance") != null) {
            alliance = (allianceColors) blackboard.get("Alliance");
            blackboard.remove("Alliance");
            allGoodFromAuto = true;
        }

        if (posGoodFromAuto && allGoodFromAuto) {robotString = "PULLED_FROM_BLACKBOARD";}

        while(opModeInInit()) {
            updateInitialSettings();
            follower.setPose(robotPose);
            updatePose();

            flywheelStallAlarm.reset();
            intakeshootergate.reset();

            subShooter.setFlowerArm(true);

            telemetry.addData("Alliance", alliance.toString());
            telemetry.addData("Starting position", robotString);

            telemetry.update();
        }

        while (opModeIsActive())
        {
            updatePose();
            tracking = false;

            if (!gamepad2.y) {
                if (gamepad2.dpad_up) {
                    subShooter.setIntakePower(Configurables.intakePower);
                } else if (gamepad2.dpad_down) {
                    subShooter.setIntakePower(-Configurables.intakePower);
                } else {
                    subShooter.setIntakePower(0);
                }
            } else if (gamepad2.y && (gamepad2.dpad_down || gamepad2.dpad_up)) {
                subShooter.setIntakePower(0);
            }

            currentGoal = whichPentagon();

            if (currentGoal != null) {
                targetHeading = GeneralUtils.getPointsHeading(currentGoal.x(), currentGoal.y(), robotPose.x(), robotPose.y()) - Math.PI;
            }

            targetingError = (Math.toDegrees(targetHeading) - Math.toDegrees(robotPose.heading()));
            targetingError = GeneralUtils.wrapRange(targetingError, 180);

            subShooter.updateFlywheel(robotPose, currentGoal);

//            canshoot = subLL.canShoot(currentPent, alliance);

            if (currentGoal != null && gamepad2.y) {
                tracking = true;
                sequenceShooting();
            } else {
                subShooter.gateControl(0, true);
                subShooter.gateControl(1, true);
            }

            if (gamepad2.b) {
                tracking = true;
            }

            subShooter.setFlowerArm(!gamepad2.a);

            panelsTelemetry.addData("flywheel velocity (rpm)", subShooter.getVelocityRPM());
            panelsTelemetry.addData("flywheel speed", subShooter.targetVelocity);
            panelsTelemetry.addData("DTG", subShooter.dtg);
            panelsTelemetry.addLine("");
            panelsTelemetry.addData("x", robotPose.x());
            panelsTelemetry.addData("y", robotPose.y());
            panelsTelemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            drawUtil.drawRobot(robotPose.x(), robotPose.y(), robotPose.heading(), goalPose1, goalPose2);

            panelsTelemetry.update();

            flywheelCurrent = subShooter.getShooterCurrent();

//            telemetry.addData("LL IDS", subLL.listThem());
            telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
            telemetry.addData("Target Heading", Math.toDegrees(targetHeading));
            telemetry.addData("Target error", targetingError);
            telemetry.addData("DTG", subShooter.dtg);
            telemetry.addData("Tracking", tracking);
            telemetry.addData("In Trapezoid", (currentPent != null ? "Yes" : "No"));
            telemetry.addData("Flywheel to speed?", (subShooter.isReady ? "Yes" : "No"));
            telemetry.addData("FW Current", flywheelCurrent);
            telemetry.addData("Flywheel target", subShooter.targetVelocity);
            telemetry.addData("Flywheel speed", subShooter.getVelocityRPM());
            telemetry.addData("x", robotPose.x());
            telemetry.addData("y", robotPose.y());
            telemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            updateTelemetry(telemetry);

            updateBlinkin();
            monitorMotors();

            if (!gamepad1.y) {
                if (gamepad1.right_bumper) {
                    follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
                } else {
                    follower.manual(
                            ManualDrive.fieldCentric(
                                    -gamepad1.left_stick_y,
                                    -gamepad1.left_stick_x,
                                    (tracking ? targetingError * Configurables.targetingP : -gamepad1.right_stick_x),
                                    robotPose.heading()
                            )
                    );
                }
            }
        }
    }
}




