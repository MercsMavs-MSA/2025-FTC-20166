package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.pedro.PedroCommands.hold;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Configurables;
import org.firstinspires.ftc.teamcode.DriveTestPedro;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class NoFlowerAuto extends LinearOpMode {

    private Follower follower;
    private SubSystemShooter subShooter;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = RobotConstants.PositionConstants.audienceStart2;
    private final Pose start2 = poseFactory.of(56.7, 19, 270);
    private final Pose park = RobotConstants.PositionConstants.parkPoint1;
    private final Pose parkControl1 = poseFactory.of(16.2741, 30.1497, 0);
    private DriveTestPedro.allianceColors alliance;

    private void updateInitialSettings() {
        if (gamepad1.xWasPressed()) {alliance = (alliance == DriveTestPedro.allianceColors.RED ? DriveTestPedro.allianceColors.BLUE : DriveTestPedro.allianceColors.RED);}
    }

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, park2()),
                Commands.waitUntil(() -> subShooter.isReady),
                Commands.instant(() -> subShooter.gateControl(0, false)),
                Commands.instant(() -> subShooter.gateControl(1, false)),
                Commands.waitMs(RobotConstants.ShooterConstants.intakeWait),
                Commands.instant(() -> subShooter.setIntakePower(Configurables.shootingIntakePower)),
                Commands.waitMs(2500),
                follow(follower, park()),
                hold(follower, park)
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        subShooter = new SubSystemShooter(hardwareMap);

        while (opModeInInit()) {
            updateInitialSettings();
        }

        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            subShooter.updateFlywheel(follower.pose(), RobotConstants.PositionConstants.audienceGoal);
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
        blackboard.put("Pose", follower.pose());
        blackboard.put("Alliance", alliance);
    }

    public Path park() {
        return Paths.curve(start2, parkControl1, park).constant(park);
    }
    public Path park2() {
        return Paths.line(start, start2).constant(start2);
    }
}
