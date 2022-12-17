import java.awt.*;
import java.awt.geom.Point2D;


public class Vertex {

    double x, y, z;

    Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Point2D.Double screenTransition(double distance) {

        return new Point2D.Double(distance * this.x / this.z, distance * this.y / this.z);
    }

    Vertex viewCoordinateTrans(Vertex camera) {

        VertexSpherical s = new VertexSpherical(0, 0, 0);
        s = s.convert(camera);

        double xE = -this.x * Math.sin(s.phi) + this.y * Math.cos(s.phi);
        double yE = -this.x * Math.cos(s.zeta) * Math.cos(s.phi) - this.y * Math.cos(s.zeta) * Math.sin(s.phi) + this.z * Math.sin(s.zeta);
        double zE = -this.x * Math.sin(s.zeta) * Math.cos(s.phi) - this.y * Math.sin(s.zeta) * Math.sin(s.phi) - this.z * Math.cos(s.zeta) + s.r;

        return new Vertex(xE, yE, zE);
    }

}
