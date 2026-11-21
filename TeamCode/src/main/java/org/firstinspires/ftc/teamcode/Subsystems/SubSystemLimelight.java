package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DriveTestPedro;
import org.firstinspires.ftc.teamcode.RobotConstants;
import org.firstinspires.ftc.teamcode.Utilities.ZoneDetector;

import java.util.List;

public class SubSystemLimelight {
    private Limelight3A limelight;

    public SubSystemLimelight(HardwareMap hwmap) {
        limelight = hwmap.get(Limelight3A.class, "lime");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public boolean canShoot(ZoneDetector pentagon, DriveTestPedro.allianceColors alliance) {
        LLResult result = limelight.getLatestResult();
        if (pentagon == null) {return false;}
        if (result == null || !result.isValid()) {return true;} //I would rather the robot shoot if the limelight drops out than die
        List<LLResultTypes.FiducialResult> results = result.getFiducialResults();
        List<Integer> acceptedIDS = null;
        boolean detected = false;

        if (pentagon.equals(RobotConstants.ShooterConstants.noFlowerPentagon) && alliance == DriveTestPedro.allianceColors.RED) {acceptedIDS = List.of(34, 35, 36, 37);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.flowerPentagon) && alliance == DriveTestPedro.allianceColors.RED) {acceptedIDS = List.of(30, 31, 32, 33);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.noFlowerPentagon) && alliance == DriveTestPedro.allianceColors.BLUE) {acceptedIDS = List.of(38, 39, 40, 41);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.flowerPentagon) && alliance == DriveTestPedro.allianceColors.BLUE) {acceptedIDS = List.of(42, 43, 44, 45);}

        if (acceptedIDS == null) {return false;}

        for(LLResultTypes.FiducialResult fid : results) {
            if (acceptedIDS.contains(fid.getFiducialId())) {
                detected = true;
                break;
            }
        }

        return detected;
    }
}
