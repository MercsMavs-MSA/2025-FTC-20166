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
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Utilities.DrawRobot;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
//@Disabled
public class DriveTestPedro extends LinearOpMode {
    private ElapsedTime flywheelTimer = new ElapsedTime();
    private double finalTime;
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    private DrawRobot drawUtil = new DrawRobot();
    private SubSystemShooter subShooter;
    private SubSystemBlinkIn blinkIn;
    private Pose robotPose;
    private String robotString;
    private Pose goalPose1;
    private ZoneDetector pentagon1;
    private Pose goalPose2;
    private ZoneDetector pentagon2;
    private Pose currentGoal;
    private Pose heldPose;
    private Follower follower = null;
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
        follower = Constants.create(hardwareMap);
    }

    public void sequenceShooting() {
        double heading = GeneralUtils.getPointsHeading(currentGoal.x(), currentGoal.y(), heldPose.x(), heldPose.y());
        follower.hold(new Pose(heldPose.x(), heldPose.y(), heading));

        if (subShooter.isReady) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIME);
            subShooter.gateControl(1, true);
            subShooter.gateControl(2, true);
        }
    }

    private Pose whichPentagon() {
        if (pentagon1.isRobotInside(robotPose)) {return goalPose1;}
        if (pentagon2.isRobotInside(robotPose)) {return goalPose2;}
        return null;
    }

    public void runOpMode() throws InterruptedException {
        initializeHardware();
        flywheelTimer.reset();
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
                subShooter.setIntakePower(1);
            } else if (gamepad1.dpad_down) {
                subShooter.setIntakePower(-1);
            } else {
                subShooter.setIntakePower(0);
            }

//            if (pentagon1.isRobotInside(robotPose)) {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN); if (gamepad1.y) {sequenceShooting();}} else {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);}

            currentGoal = whichPentagon();

            subShooter.updateFlywheel(robotPose, currentGoal);

            if (currentGoal != null) {
                blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                if (gamepad1.y) {
                    sequenceShooting();
                }
            } else {
                blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
            }

//            subShooter.setBangBangRPM(Configurables.targetVelocity);

//            if (subShooter.getVelocityRPM() <= 2300) {
//                finalTime = flywheelTimer.seconds();
//            }

            updatePose();

            panelsTelemetry.addData("flywheel velocity (rpm)", subShooter.getVelocityRPM());
            panelsTelemetry.addData("flywheel target (rpm)", Configurables.targetVelocity);
            panelsTelemetry.addData("DTG", subShooter.dtg);
            panelsTelemetry.addLine("");
            panelsTelemetry.addData("x", robotPose.x());
            panelsTelemetry.addData("y", robotPose.y());
            panelsTelemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            drawUtil.drawRobot(robotPose.x(), robotPose.y(), robotPose.heading());

            panelsTelemetry.update();

            telemetry.addData("x", robotPose.x());
            telemetry.addData("y", robotPose.y());
            telemetry.addData("heading", Math.toDegrees(robotPose.heading()));
            telemetry.addLine();
            telemetry.addData("flywheel Velocity (rpm)", subShooter.getVelocityRPM());
            telemetry.addData("flywheel timer (seconds)", finalTime);
            telemetry.addData("target flywheel velocity", Configurables.targetVelocity);

            updateTelemetry(telemetry);
        }
    }
}




