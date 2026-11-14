package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SubSystemLimelight {
    private Limelight3A ll;

    public SubSystemLimelight(HardwareMap hwmap) {
        ll = hwmap.get(Limelight3A.class, "lime");
    }
}
