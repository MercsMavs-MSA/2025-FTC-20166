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
    }

    public boolean canShoot(ZoneDetector pentagon, DriveTestPedro.allianceColors alliance) {
        LLResult result = limelight.getLatestResult();
        if (pentagon == null) {return false;}
        if (result == null) {return true;} //I would rather the robot shoot if the limelight drops out than die
        List<LLResultTypes.FiducialResult> results = result.getFiducialResults();
        List<Integer> acceptedIDS = null;
        boolean detected = false;

        if (pentagon.equals(RobotConstants.ShooterConstants.audiencePentagon) && alliance == DriveTestPedro.allianceColors.RED) {acceptedIDS = List.of(34, 35, 36, 37);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.farPentagon) && alliance == DriveTestPedro.allianceColors.RED) {acceptedIDS = List.of(30, 31, 32, 33);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.audiencePentagon) && alliance == DriveTestPedro.allianceColors.BLUE) {acceptedIDS = List.of(38, 39, 40, 41);}
        else if (pentagon.equals(RobotConstants.ShooterConstants.farPentagon) && alliance == DriveTestPedro.allianceColors.BLUE) {acceptedIDS = List.of(42, 43, 44, 45);}

        if (acceptedIDS == null) {return false;}

        for(LLResultTypes.FiducialResult fid : results) {
            if (acceptedIDS.contains(fid.getFiducialId())) {
                detected = true;
                break;
            }
        }

        return detected;
    }

    public List<LLResultTypes.FiducialResult> listThem() {
        LLResult result = limelight.getLatestResult();
        if (result == null) {return List.of();} //I would rather the robot shoot if the limelight drops out than die
        List<LLResultTypes.FiducialResult> results = result.getFiducialResults();
        return results;

//        String ids = "";
//
//        for(LLResultTypes.FiducialResult fid : results) {
//            ids = ids.concat(String.valueOf(fid.getFiducialId()).concat(", "));
//        }
//
//        return ids;
    }
}
