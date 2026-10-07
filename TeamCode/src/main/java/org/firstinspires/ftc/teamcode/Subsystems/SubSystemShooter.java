package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class SubSystemShooter {
    private DcMotorEx flywheel;

    public SubSystemShooter(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
    }

    public void setVelocityRPM(double velocity) {
        flywheel.setVelocity(velocity / 60 * 28);
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
}
