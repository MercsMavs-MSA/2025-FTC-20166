package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class TestAuto2 extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(9.5, 15, 270);
    private final Pose path1 = poseFactory.of(9.5, 8.5, 270);
    private final Pose shootHere = poseFactory.of(60, 12, 270);
    private final Pose point3 = poseFactory.of(60, 20, 270);
    private final Pose point4 = poseFactory.of(25.7347, 46.5534, 270);
    private final Pose point5 = poseFactory.of(9.388, 91.1616, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, shootHere()),
                follow(follower, path3()),
                follow(follower, path4()),
                follow(follower, path5())
        );
    }

    @Override
    public void runOpMode() {
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
        return Paths.line(start, path1).constant(path1);
    }

    public Path shootHere() {
        return Paths.line(path1, shootHere).constant(shootHere);
    }

    public Path path3() {
        return Paths.line(shootHere, point3).reverseTangent();
    }

    public Path path4() {
        return Paths.line(point3, point4).constant(point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).constant(point5)
                .with(Constants.foresightConfig.maxVelocityConstraint.at(10.0));
    }
}
