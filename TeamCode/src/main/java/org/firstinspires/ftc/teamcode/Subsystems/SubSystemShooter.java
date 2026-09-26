package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class SubSystemShooter {
    private DcMotorEx flywheel;
    private PIDFCoefficients pidstore;

    public SubSystemShooter(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
        pidstore = flywheel.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);

        pidstore.p = 10;

        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidstore);
    }

    public void setVelocity(double velocity) {
        flywheel.setVelocity(velocity);
    }
    public double getVelocity() {
        return flywheel.getVelocity() / 28;
    }
}
