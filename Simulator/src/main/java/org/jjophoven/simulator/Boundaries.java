package org.jjophoven.simulator;

import org.psilynx.psikit.core.Logger;

public class Boundaries {
    public static final Point[] FIELD = {
            new Point(0, 0),
            new Point(0, 68.8),
            new Point(7.6, 68.8),
            new Point(23.4, 141.3),
            new Point(117.9, 141.3),
            new Point(133.8, 118.8),
            new Point(133.8, 68.8),
            new Point(141.3, 68.8),
            new Point(141.3, 0)
    };

    public static Point closestInBoundsPosition(
            double centerX,
            double centerY,
            double width,
            double length,
            double heading
    ) {
        double x = centerX;
        double y = centerY;

        final double EPS = 1e-6;

        for (int iter = 0; iter < 20; iter++) {

            Point[] robot = robotCorners(x + 4.1338583, y, width, length, heading);

            boolean changed = false;

            for (Point corner : robot) {

                if (pointInsidePolygon(corner, FIELD))
                    continue;

                double bestDist = Double.POSITIVE_INFINITY;
                double pushX = 0;
                double pushY = 0;

                for (int i = 0; i < FIELD.length; i++) {

                    Point a = FIELD[i];
                    Point b = FIELD[(i + 1) % FIELD.length];

                    Point closest = closestPointOnSegment(corner, a, b);

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

            if (!changed && !isOutOfBounds(x, y, width, length, heading))
                break;
        }

        return new Point(x, y);
    }

    private static Point closestPointOnSegment(Point p, Point a, Point b) {

        double dx = b.x - a.x;
        double dy = b.y - a.y;

        double lenSq = dx * dx + dy * dy;

        if (lenSq == 0)
            return a;

        double t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / lenSq;
        t = Math.max(0, Math.min(1, t));

        return new Point(
                a.x + t * dx,
                a.y + t * dy
        );
    }

    /**
     * @param centerX Robot center X
     * @param centerY Robot center Y
     * @param width Robot width (left-right)
     * @param length Robot length (front-back)
     * @param heading Heading in radians.
     *                0 = +X
     *                CCW positive.
     * @return true if any part of the robot is outside the field.
     */
    public static boolean isOutOfBounds(
            double centerX,
            double centerY,
            double width,
            double length,
            double heading
    ) {
        // TODO configure distance between center of drivetrain and center of robot
        Point[] robot = robotCorners(centerX + 4.1338583, centerY, width, length, heading);

        // Every corner must be inside.
        for (Point p : robot) {
            if (!pointInsidePolygon(p, FIELD)) {
                return true;
            }
        }

        // No robot edge may cross a wall.
        for (int i = 0; i < 4; i++) {
            Point a1 = robot[i];
            Point a2 = robot[(i + 1) % 4];

            for (int j = 0; j < FIELD.length; j++) {
                Point b1 = FIELD[j];
                Point b2 = FIELD[(j + 1) % FIELD.length];

                if (segmentsIntersect(a1, a2, b1, b2)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static Point[] robotCorners(
            double cx,
            double cy,
            double width,
            double length,
            double heading
    ) {
        double halfL = length / 2.0;
        double halfW = width / 2.0;

        double cos = Math.cos(heading);
        double sin = Math.sin(heading);

        // Forward vector
        double fx = cos;
        double fy = sin;

        // Left vector
        double lx = -sin;
        double ly = cos;

        return new Point[]{
                // Front Left
                new Point(
                        cx + fx * halfL + lx * halfW,
                        cy + fy * halfL + ly * halfW
                ),

                // Front Right
                new Point(
                        cx + fx * halfL - lx * halfW,
                        cy + fy * halfL - ly * halfW
                ),

                // Back Right
                new Point(
                        cx - fx * halfL - lx * halfW,
                        cy - fy * halfL - ly * halfW
                ),

                // Back Left
                new Point(
                        cx - fx * halfL + lx * halfW,
                        cy - fy * halfL + ly * halfW
                )
        };
    }

    private static boolean pointInsidePolygon(Point p, Point[] polygon) {
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

    private static boolean segmentsIntersect(Point p1, Point p2,
                                             Point q1, Point q2) {

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

    private static double orientation(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y)
                - (b.y - a.y) * (c.x - a.x);
    }

    private static boolean onSegment(Point a, Point b, Point c) {
        final double EPS = 1e-9;

        return b.x >= Math.min(a.x, c.x) - EPS
                && b.x <= Math.max(a.x, c.x) + EPS
                && b.y >= Math.min(a.y, c.y) - EPS
                && b.y <= Math.max(a.y, c.y) + EPS;
    }

    public static class Point {
        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}