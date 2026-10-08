package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class SubSystemShooter {
    private DcMotorEx flywheel;
    private DcMotorEx intakeMotor;

    public SubSystemShooter(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intakeMotor = hwmap.get(DcMotorEx.class, "intake");
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setVelocityRPM(double velocity) {
        flywheel.setVelocity(velocity / 60 * 28);
    }

    public void setBangBangRPM(double velocity) {
        if (getVelocityRPM() < velocity) {
            flywheel.setPower(1);
        } else {
            flywheel.setPower(0);
        }
    }

    public void setPower(double power) {flywheel.setPower(power);}

    public double getVelocityRPM() {
        return flywheel.getVelocity() / 28 * 60;
    }

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

    public void intake(boolean enable) {
        intakeMotor.setPower(enable ? 0.5 : 0);
    }
}
