package org.firstinspires.ftc.teamcode.Autonomous;

import static com.pedropathing.api.Paths.*;

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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.teamcode.Subsystems.SubSystemShooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class TestAuto extends LinearOpMode {

    private Follower follower;

    private SubSystemShooter subShooter;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(8.5, 9.5, 0);
    private final Pose path1 = poseFactory.of(70.4239, 27.9665, 0);
    private final Pose point2 = poseFactory.of(70.4974, 112.4626, 90);
    private final Pose point3 = poseFactory.of(132, 118.9116, 90);
    private final Pose point4 = poseFactory.of(132, 133, 90);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4()),
                Commands.instant(() -> subShooter.setVelocityRPM(2000)),
                Commands.waitMs(5000),
                Commands.instant(() -> subShooter.setVelocityRPM(0))
        );
    }

    @Override
    public void runOpMode() {
        subShooter = new SubSystemShooter(hardwareMap);
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            telemetry.update();
        }
    }

    public Path path1() {
        return line(start, path1).constant(path1);
    }

    public Path path2() {
        return line(path1, point2).constant(point2);
    }

    public Path path3() {
        return line(point2, point3).constant(point3);
    }

    public Path path4() {
        return line(point3, point4).constant(point4)
                .with(Constants.foresightConfig.maxVelocityConstraint.at(12.0));
    }
}
