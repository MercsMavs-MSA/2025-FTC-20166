package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.math.Pose;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.Utilities.DrawRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
//@Disabled
public class DriveTestPedro extends LinearOpMode {
    private PIDFCoefficients pidstore;
    private TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
    private DrawRobot drawUtil = new DrawRobot();
    private SubSystemShooter subShooter;
    private Pose robotPose = new Pose(8, 8, 0);
    private Follower follower = null;

    private void updatePose() {
        follower.update();
        robotPose = follower.pose();
    }
    public void initializeHardware() {
        subShooter = new SubSystemShooter(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(robotPose);
    }

    public void runOpMode() throws InterruptedException {
        initializeHardware();

        waitForStart();
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

            subShooter.updatePID(PIDConfig.shooterP, PIDConfig.shooterI, PIDConfig.shooterD, PIDConfig.shooterF);

            if (gamepad1.dpad_right) {
                PIDConfig.targetVelocity = 2500;
            } else if (gamepad1.dpad_down) {
                PIDConfig.targetVelocity = 0;
            }

            subShooter.setVelocityRPM(PIDConfig.targetVelocity);

            updatePose();

            panelsTelemetry.addData("flywheel velocity (rpm)", subShooter.getVelocityRPM());
            panelsTelemetry.addData("flywheel target (rpm)", PIDConfig.targetVelocity);

            drawUtil.drawRobot(robotPose.x(), robotPose.y(), robotPose.heading());

            panelsTelemetry.update();

            pidstore = subShooter.getPID();

            telemetry.addData("x", robotPose.x());
            telemetry.addData("y", robotPose.y());
            telemetry.addData("heading", Math.toDegrees(robotPose.heading()));
            telemetry.addLine();
            telemetry.addData("flywheel Velocity (rpm)", subShooter.getVelocityRPM());
            telemetry.addData("target flywheel velocity", PIDConfig.targetVelocity);
            telemetry.addData("P", pidstore.p);
            telemetry.addData("F", pidstore.f);
            telemetry.addData("Target P", PIDConfig.shooterP);

            updateTelemetry(telemetry);
        }
    }
}




