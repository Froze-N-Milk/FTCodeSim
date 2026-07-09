package org.codeblooded.simulator;

import org.codeblooded.simhardware.drivetrain.MotionVector;

public class FieldBoundary {
    public static final MotionVector[] FIELD = {
            new MotionVector(0, 0, 0),
            new MotionVector(0, 68.8, 0),
            new MotionVector(7.6, 68.8, 0),
            new MotionVector(7.6, 118.8, 0),
            new MotionVector(23.4, 141.3, 0),
            new MotionVector(117.9, 141.3, 0),
            new MotionVector(133.8, 118.8, 0),
            new MotionVector(133.8, 68.8, 0),
            new MotionVector(141.3, 68.8, 0),
            new MotionVector(141.3, 0, 0)
    };

    public static boolean isOutOfBounds(
            MotionVector pose,
            RobotGeometry robot
    ) {
        MotionVector[] corners = robot.corners(pose);

        for (MotionVector p : corners) {
            if (!pointInsidePolygon(p, FIELD)) {
                return true;
            }
        }

        for (int i = 0; i < 4; i++) {
            MotionVector a1 = corners[i];
            MotionVector a2 = corners[(i + 1) % 4];

            for (int j = 0; j < FIELD.length; j++) {
                MotionVector b1 = FIELD[j];
                MotionVector b2 = FIELD[(j + 1) % FIELD.length];

                if (segmentsIntersect(a1, a2, b1, b2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static MotionVector closestInBoundsPosition(
            MotionVector pose,
            RobotGeometry robot
    ) {
        double x = pose.x;
        double y = pose.y;

        final double EPS = 1e-6;

        for (int iter = 0; iter < 20; iter++) {

            MotionVector[] corners =
                    robot.corners(new MotionVector(x, y, pose.theta));

            boolean changed = false;

            for (MotionVector corner : corners) {

                if (pointInsidePolygon(corner, FIELD))
                    continue;

                double bestDist = Double.POSITIVE_INFINITY;
                double pushX = 0;
                double pushY = 0;

                for (int i = 0; i < FIELD.length; i++) {

                    MotionVector a = FIELD[i];
                    MotionVector b = FIELD[(i + 1) % FIELD.length];

                    MotionVector closest =
                            closestPointOnSegment(corner, a, b);

                    double dx = closest.x - corner.x;
                    double dy = closest.y - corner.y;
                    double distSq = dx * dx + dy * dy;

                    if (distSq < bestDist) {
                        bestDist = distSq;
                        pushX = dx;
                        pushY = dy;
                    }
                }

                x += pushX + Math.signum(pushX) * EPS;
                y += pushY + Math.signum(pushY) * EPS;
                changed = true;
            }

            if (!changed &&
                    !isOutOfBounds(new MotionVector(x, y, pose.theta), robot)) {
                break;
            }
        }

        return new MotionVector(x, y, pose.theta);
    }

    private static boolean pointInsidePolygon(MotionVector p, MotionVector[] polygon) {
        boolean inside = false;

        for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {

            if ((polygon[i].y > p.y) != (polygon[j].y > p.y)
                    && p.x < (polygon[j].x - polygon[i].x)
                    * (p.y - polygon[i].y)
                    / (polygon[j].y - polygon[i].y)
                    + polygon[i].x) {

                inside = !inside;
            }
        }

        return inside;
    }

    private static MotionVector closestPointOnSegment(
            MotionVector p,
            MotionVector a,
            MotionVector b
    ) {
        double dx = b.x - a.x;
        double dy = b.y - a.y;

        double lenSq = dx * dx + dy * dy;

        if (lenSq == 0) {
            return new MotionVector(a.x, a.y, 0);
        }

        double t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / lenSq;
        t = Math.max(0, Math.min(1, t));

        return new MotionVector(
                a.x + t * dx,
                a.y + t * dy,
                0
        );
    }

    private static boolean segmentsIntersect(MotionVector p1, MotionVector p2,
                                             MotionVector q1, MotionVector q2) {

        double o1 = orientation(p1, p2, q1);
        double o2 = orientation(p1, p2, q2);
        double o3 = orientation(q1, q2, p1);
        double o4 = orientation(q1, q2, p2);

        if (o1 * o2 < 0 && o3 * o4 < 0) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, q1, p2)) return true;
        if (o2 == 0 && onSegment(p1, q2, p2)) return true;
        if (o3 == 0 && onSegment(q1, p1, q2)) return true;
        if (o4 == 0 && onSegment(q1, p2, q2)) return true;

        return false;
    }

    private static double orientation(MotionVector a, MotionVector b, MotionVector c) {
        return (b.x - a.x) * (c.y - a.y)
                - (b.y - a.y) * (c.x - a.x);
    }

    private static boolean onSegment(MotionVector a, MotionVector b, MotionVector c) {
        final double EPS = 1e-9;

        return b.x >= Math.min(a.x, c.x) - EPS
                && b.x <= Math.max(a.x, c.x) + EPS
                && b.y >= Math.min(a.y, c.y) - EPS
                && b.y <= Math.max(a.y, c.y) + EPS;
    }
}