public class VertexSpherical {

    double r, zeta, phi;

    VertexSpherical(double r, double zeta, double phi){
        this.r = r;
        this.phi = phi;
        this.zeta = zeta;
    }

    public static VertexSpherical convert(Vertex v) {
        double r = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
        double zeta = Math.acos(v.z / r);
        double rxy = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
        double phi = 0;
        if (rxy != 0) {
            phi = Math.acos(v.x / rxy);
        }

        return new VertexSpherical(r, zeta, phi);
    }

}
