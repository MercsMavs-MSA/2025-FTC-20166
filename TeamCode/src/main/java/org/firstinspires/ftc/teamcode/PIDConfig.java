package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;

@Configurable
public class PIDConfig {

    public static double targetVelocity = 0;
    public static double shooterP = 10;
    public static double shooterI = 0;
    public static double shooterD = 0;
    public static double shooterF = 15;
}
