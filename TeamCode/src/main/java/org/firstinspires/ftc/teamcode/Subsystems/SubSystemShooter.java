package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Bitmap;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Configurables;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.GeneralUtils;

public class SubSystemShooter {
    private DcMotorEx flywheel;
    private DcMotorEx intakeMotor;
    private Servo gateLeft;
    private Servo gateRight;
    private Servo angleLeft;
    private Servo angleRight;
    public boolean isReady = false;
    public double dtg = 0;

    public SubSystemShooter(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intakeMotor = hwmap.get(DcMotorEx.class, "intake");

        PIDFCoefficients pidStore = intakeMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        pidStore.f = 200;

        intakeMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidStore);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gateLeft = hwmap.get(Servo.class, "gateL");
        gateRight = hwmap.get(Servo.class, "gateR");
        angleLeft = hwmap.get(Servo.class, "angleL");
        angleRight = hwmap.get(Servo.class, "angleR");
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
        Servo gateUsed = (gate == 1) ? gateLeft : gateRight;

        gateUsed.setPosition((up) ? RobotConstants.ShooterConstants.gateMax : RobotConstants.ShooterConstants.gateMin);
    }

    public void updateFlywheel(Pose robotPose, Pose goal) {
        if (goal == null) {return;}

        double dtg = GeneralUtils.getPointsDistance(goal.x(), goal.y(), robotPose.x(), robotPose.y());
        double speed = Configurables.targetVelocity; //RobotConstants.ShooterConstants.velocityLERP.interpolated(dtg);

        this.dtg = dtg;

        setBangBangRPM(speed);

//        angleLeft.setPosition(RobotConstants.ShooterConstants.servoOneLERP.interpolated(RobotConstants.ShooterConstants.positionLERP.interpolated(dtg)));
//        angleRight.setPosition(RobotConstants.ShooterConstants.servoTwoLERP.interpolated(RobotConstants.ShooterConstants.positionLERP.interpolated(dtg)));

        angleLeft.setPosition(RobotConstants.ShooterConstants.servoOneLERP.interpolated(Configurables.position));
        angleRight.setPosition(RobotConstants.ShooterConstants.servoTwoLERP.interpolated(Configurables.position));

        isReady = (Math.abs(getVelocityRPM() - speed) <= 50);
    }
}
