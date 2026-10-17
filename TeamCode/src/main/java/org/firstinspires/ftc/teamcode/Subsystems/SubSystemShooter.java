package org.firstinspires.ftc.teamcode.Subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;

public class SubSystemShooter {
    private DcMotorEx flywheel;
    private DcMotorEx intakeMotor;
    private Servo gate1;
    private Servo gate2;
    private Servo angle;
    public boolean isReady = false;

    public SubSystemShooter(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intakeMotor = hwmap.get(DcMotorEx.class, "intake");

        PIDFCoefficients pidStore = intakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        pidStore.f = 200;

        intakeMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidStore);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gate1 = hwmap.get(Servo.class, "gate1");
        gate2 = hwmap.get(Servo.class, "gate2");
        angle = hwmap.get(Servo.class, "angle");
    }

    public void setVelocityRPM(double velocity) {
        flywheel.setVelocity(velocity / 60 * 28);
    }
    public void setIntakeVelocityRPM(double velocity) {intakeMotor.setVelocity(velocity / 60 * 28);}
    public void setIntakePower(double power) {intakeMotor.setPower(power);}

    public void setBangBangRPM(double velocity) {
        if (getVelocityRPM() < velocity) {
            flywheel.setPower(1);
        } else {
            flywheel.setPower(0);
        }
    }

    public void setIntakeBangBangRPM(double velocity) {
        if (getIntakeVelocityRPM() < velocity) {
            intakeMotor.setPower(1);
        } else {
            intakeMotor.setPower(0);
        }
    }

    public void setPower(double power) {flywheel.setPower(power);}

    public double getVelocityRPM() {
        return flywheel.getVelocity() / 28 * 60;
    }
    public double getIntakeVelocityRPM() {return intakeMotor.getVelocity() / 28 * 60 / 5.2;}

    public void updatePID(double p, double i, double d, double f) {
        PIDFCoefficients pidstore;
        pidstore = flywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        pidstore.p = p;
        pidstore.i = i;
        pidstore.d = d;
        pidstore.f = f;

        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidstore);
    }

    public PIDFCoefficients getPID() {
        return flywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void gateControl(int gate, boolean up) {
        Servo gateUsed = (gate == 1) ? gate1 : gate2;

        gateUsed.setPosition((up) ? RobotConstants.ShooterConstants.gateMax : RobotConstants.ShooterConstants.gateMin);
    }

    public void updateFlywheel(Pose robotPose, Pose goal) {
        double dtg = GeneralUtils.getPointsDistance(goal.x(), goal.y(), robotPose.x(), robotPose.y());
        double speed = RobotConstants.ShooterConstants.velocityLERP.interpolated(dtg);

        setBangBangRPM(speed);

        isReady = (Math.abs(getVelocityRPM() - speed) <= 50);
    }
}
