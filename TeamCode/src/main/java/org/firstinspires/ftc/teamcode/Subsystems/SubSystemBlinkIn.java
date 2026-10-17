package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SubSystemBlinkIn {
    RevBlinkinLedDriver blinkIn;

    public SubSystemBlinkIn(HardwareMap hwmap) {
        blinkIn = hwmap.get(RevBlinkinLedDriver.class, "led");
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        blinkIn.setPattern(pattern);
    }
}
