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
    boolean stalled = false;
    double flywheelCurrent = 0;
    private double finalTime;
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
    private Pose heldPose;
    private Follower follower = null;
    private boolean canshoot = false;
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
        if (gamepad1.yWasPressed()) {
            alliance = allianceColors.RED;
            goalPose1 = RobotConstants.PositionConstants.audienceRedGoal;
            pentagon1 = RobotConstants.ShooterConstants.redAudiencePentagon;
            goalPose2 = RobotConstants.PositionConstants.farRedGoal;
            pentagon2 = RobotConstants.ShooterConstants.redFarPentagon;
            robotPose = RobotConstants.PositionConstants.audienceRedStart;
            robotString = "Audience wall, facing inward (RED)";
        } else if (gamepad1.bWasPressed()) {
            alliance = allianceColors.RED;
            goalPose1 = RobotConstants.PositionConstants.audienceRedGoal;
            pentagon1 = RobotConstants.ShooterConstants.redAudiencePentagon;
            goalPose2 = RobotConstants.PositionConstants.farRedGoal;
            pentagon2 = RobotConstants.ShooterConstants.redFarPentagon;
            robotPose = RobotConstants.PositionConstants.farRedStart;
            robotString = "Far wall, facing inward (RED)";
        } else if (gamepad1.xWasPressed()) {
            alliance = allianceColors.BLUE;
            goalPose1 = RobotConstants.PositionConstants.audienceBlueGoal;
            pentagon1 = RobotConstants.ShooterConstants.blueAudiencePentagon;
            goalPose2 = RobotConstants.PositionConstants.farBlueGoal;
            pentagon2 = RobotConstants.ShooterConstants.blueFarPentagon;
            robotPose = RobotConstants.PositionConstants.audienceBlueStart;
            robotString = "Audience wall, facing inward (BLUE)";
        } else if (gamepad1.aWasPressed()) {
            alliance = allianceColors.BLUE;
            goalPose1 = RobotConstants.PositionConstants.audienceBlueGoal;
            pentagon1 = RobotConstants.ShooterConstants.blueAudiencePentagon;
            goalPose2 = RobotConstants.PositionConstants.farBlueGoal;
            pentagon2 = RobotConstants.ShooterConstants.blueFarPentagon;
            robotPose = RobotConstants.PositionConstants.farBlueStart;
            robotString = "Far wall, facing inward (BLUE)";
        }
    }
    public void initializeHardware() {
        subShooter = new SubSystemShooter(hardwareMap);
        blinkIn = new SubSystemBlinkIn(hardwareMap);
        subLL = new SubSystemLimelight(hardwareMap);
        follower = Constants.create(hardwareMap);
    }

    public void sequenceShooting() {
        double heading = GeneralUtils.getPointsHeading(currentGoal.x(), currentGoal.y(), heldPose.x(), heldPose.y());
        follower.hold(new Pose(heldPose.x(), heldPose.y(), heading-180));

        if (subShooter.isReady && canshoot && !stalled) {
        }
    }

    private void updateBlinkin()
    {
        if (stalled)
        {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED);
        } else if (subShooter.isReady && canshoot && (currentGoal != null)) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
        } else if (currentGoal != null) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIME);
        } else {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        }
    }

    private Pose whichPentagon() {
        if (pentagon1.isRobotInside(robotPose)) {currentPent = pentagon1; return goalPose1;}
        if (pentagon2.isRobotInside(robotPose)) {currentPent = pentagon2; return goalPose2;}
        currentPent = null;
        return null;
    }

    private void monitorMotors() {
//        if (flywheelCurrent >= 6.5 || stalled) {
//            if (flywheelStallAlarm.seconds() > 2) {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED);
//            subShooter.stop(); stalled = true;}
//        } else if (subShooter.getShooterCurrent() < 6.5) {
//            flywheelStallAlarm.reset();
//        }
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
        robotPose = RobotConstants.PositionConstants.audienceRedStart;
        robotString = "Audience wall, facing inward (RED)";
        goalPose1 = RobotConstants.PositionConstants.audienceRedGoal;
        pentagon1 = RobotConstants.ShooterConstants.redAudiencePentagon;
        goalPose2 = RobotConstants.PositionConstants.farRedGoal;
        pentagon2 = RobotConstants.ShooterConstants.redFarPentagon;

        while(opModeInInit()) {
            updateInitialSettings();
            follower.setPose(robotPose);
            updatePose();

            telemetry.addData("Alliance", alliance.toString());
            telemetry.addData("Starting position", robotString);

            telemetry.update();
        }

        while (opModeIsActive())
        {
            if (!gamepad1.y) {
                if (gamepad1.right_bumper) {
                    follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
                } else {
                    if (alliance == allianceColors.RED) {
                        follower.manual(
                                ManualDrive.fieldCentric(
                                        -gamepad1.left_stick_y,
                                        -gamepad1.left_stick_x,
                                        -gamepad1.right_stick_x,
                                        robotPose.heading()
                                )
                        );
                    } else if (alliance == allianceColors.BLUE) {
                        follower.manual(
                                ManualDrive.fieldCentric(
                                        gamepad1.left_stick_y,
                                        gamepad1.left_stick_x,
                                        -gamepad1.right_stick_x,
                                        robotPose.heading()
                                )
                        );
                    }
                }

                heldPose = robotPose;
            }

            if (gamepad1.left_bumper) {
                follower.hold(new Pose(70.75, 70.75, Math.toRadians(90)));
            }

//            if (gamepad1.dpad_right) {
//                Configurables.targetVelocity = 2400;
//                flywheelTimer.reset();
//            } else if (gamepad1.dpad_down) {
//                Configurables.targetVelocity = 0;
//            }

//            subShooter.setIntakeBangBangRPM(gamepad1.dpad_up ? Configurables.intakeVelocity : 0);

            if (gamepad1.dpad_up) {
                subShooter.setIntakePower(Configurables.intakePower);
            } else if (gamepad1.dpad_down) {
                subShooter.setIntakePower(-Configurables.intakePower);
            } else {
                subShooter.setIntakePower(0);
            }

            if (!gamepad1.dpad_right) {
                subShooter.gateControl(0, true);
                subShooter.gateControl(1, true);
            } else {
                subShooter.gateControl(0, false);
                subShooter.gateControl(1, false);
            }

//            if (pentagon1.isRobotInside(robotPose)) {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN); if (gamepad1.y) {sequenceShooting();}} else {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);}

            currentGoal = whichPentagon();

            subShooter.updateFlywheel(robotPose, currentGoal);

            canshoot = subLL.canShoot(currentPent);

            if (currentGoal != null) {
                if (gamepad1.y) {
                    sequenceShooting();
                }
            }

//            subShooter.setBangBangRPM(Configurables.targetVelocity);

//            if (subShooter.getVelocityRPM() <= 2300) {
//                finalTime = flywheelTimer.seconds();
//            }

            updatePose();

            panelsTelemetry.addData("flywheel velocity (rpm)", subShooter.getVelocityRPM());
            panelsTelemetry.addData("flywheel speed", subShooter.targetVelocity);
            panelsTelemetry.addData("bangbang", subShooter.bangbangfactor);
            panelsTelemetry.addData("DTG", subShooter.dtg);
            panelsTelemetry.addLine("");
            panelsTelemetry.addData("x", robotPose.x());
            panelsTelemetry.addData("y", robotPose.y());
            panelsTelemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            drawUtil.drawRobot(robotPose.x(), robotPose.y(), robotPose.heading());

            panelsTelemetry.update();

            flywheelCurrent = subShooter.getShooterCurrent();

            telemetry.addData("Can limelight see?", canshoot);
            telemetry.addData("FW Current", flywheelCurrent);
            telemetry.addData("Flywheel target", subShooter.targetVelocity);
            telemetry.addData("Flywheel speed", subShooter.getVelocityRPM());
            telemetry.addData("x", robotPose.x());
            telemetry.addData("y", robotPose.y());
            telemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            updateTelemetry(telemetry);

            updateBlinkin();
            monitorMotors();
        }
    }
}




