package org.firstinspires.ftc.teamcode.Utilities;

import java.util.ArrayList;
import java.util.List;
import com.pedropathing.math.Pose;

public class ZoneDetector {
    private final List<Pose> vertices;

    public ZoneDetector(List<Pose> vertices) {
        this.vertices = new ArrayList<>(vertices);
    }

    public boolean isRobotInside(Pose robotPose) {
        double x = robotPose.x();
        double y = robotPose.y();
        boolean inside = false;
        int numVertices = vertices.size();

        // Loop through every edge of the polygon
        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            Pose xi = vertices.get(i);
            Pose xj = vertices.get(j);

            // Check if the ray intersects with the edge
            boolean intersect = ((xi.y() > y) != (xj.y() > y))
                    && (x < (xj.x() - xi.x()) * (y - xi.y()) / (xj.y() - xi.y()) + xi.x());

            if (intersect) {
                inside = !inside;
            }
        }

        return inside;
    }
}
