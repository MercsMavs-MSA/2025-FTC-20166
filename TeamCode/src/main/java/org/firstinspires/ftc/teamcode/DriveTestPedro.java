package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
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
    private PIDFCoefficients pidstore;
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    private DrawRobot drawUtil = new DrawRobot();
    private SubSystemShooter subShooter;
    private SubSystemBlinkIn blinkIn;
    private Pose robotPose;
    private Pose goalPose;
    private Pose heldPose;
    private Follower follower = null;
    private ZoneDetector pentagon1;

    private void updatePose() {
        follower.update();
        robotPose = follower.pose();
    }
    public void initializeHardware() {
        subShooter = new SubSystemShooter(hardwareMap);
        blinkIn = new SubSystemBlinkIn(hardwareMap);
        follower = Constants.create(hardwareMap);
    }

    public void sequenceShooting() {
        follower.hold(new Pose(heldPose.x(), heldPose.y(), GeneralUtils.getPointsHeading(goalPose.x(), goalPose.y(), heldPose.x(), heldPose.y())));

        if (subShooter.isReady) {
            blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.LIME);
            subShooter.gateControl(1, true);
            subShooter.gateControl(2, true);
        }
    }

    public void runOpMode() throws InterruptedException {
        initializeHardware();
        flywheelTimer.reset();

        while(opModeInInit()) {
            goalPose = RobotConstants.PositionConstants.audienceRedGoal;
            robotPose = RobotConstants.PositionConstants.audienceRedStart;

            follower.setPose(robotPose);

            pentagon1 = RobotConstants.ShooterConstants.redAudiencePentagon;
        }

        while (opModeIsActive())
        {
            if (gamepad1.right_bumper) {
                follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            } else {
                follower.manual(
                        ManualDrive.fieldCentric(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x,
                                robotPose.heading()
                        )
                );
            }

            if (gamepad1.left_bumper) {
                follower.hold(new Pose(70.75, 70.75, Math.toRadians(90)));
            }

            subShooter.updatePID(Configurables.shooterP, Configurables.shooterI, Configurables.shooterD, Configurables.shooterF);

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

            subShooter.updateFlywheel(robotPose, goalPose);

            if (!gamepad1.y) {heldPose = robotPose;}

            if (pentagon1.isRobotInside(robotPose)) {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN); if (gamepad1.y) {sequenceShooting();}} else {blinkIn.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);}

//            subShooter.setBangBangRPM(Configurables.targetVelocity);

            if (subShooter.getVelocityRPM() <= 2300) {
                finalTime = flywheelTimer.seconds();
            }

            updatePose();

            panelsTelemetry.addData("flywheel velocity (rpm)", subShooter.getVelocityRPM());
            panelsTelemetry.addData("flywheel target (rpm)", Configurables.targetVelocity);
            panelsTelemetry.addLine("");
            panelsTelemetry.addData("x", robotPose.x());
            panelsTelemetry.addData("y", robotPose.y());
            panelsTelemetry.addData("heading", Math.toDegrees(robotPose.heading()));

            drawUtil.drawRobot(robotPose.x(), robotPose.y(), robotPose.heading());

            panelsTelemetry.update();

            pidstore = subShooter.getPID();

            telemetry.addData("x", robotPose.x());
            telemetry.addData("y", robotPose.y());
            telemetry.addData("heading", Math.toDegrees(robotPose.heading()));
            telemetry.addLine();
            telemetry.addData("flywheel Velocity (rpm)", subShooter.getVelocityRPM());
            telemetry.addData("flywheel timer (seconds)", finalTime);
            telemetry.addData("target flywheel velocity", Configurables.targetVelocity);
            telemetry.addData("P", pidstore.p);
            telemetry.addData("F", pidstore.f);
            telemetry.addData("Target P", Configurables.shooterP);

            updateTelemetry(telemetry);
        }
    }
}




