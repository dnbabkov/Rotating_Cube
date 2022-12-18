public class VertexSpherical {

    double r, zeta, phi;
    double zeta2;

    VertexSpherical(double r, double zeta, double phi){
        this.r = r;
        this.phi = phi;
        this.zeta = zeta;
    }

    public static void convertZ2toZ() {

    }

    public static VertexSpherical convert(Vertex v) {
        double r = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
        double zeta = Math.acos(v.z / r);
        double rxy = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
        double phi = 0;
        if (rxy != 0) {
            phi = Math.acos(v.x / rxy);
        }

        if (v.y < 0) {
            phi = -phi;
        }



        return new VertexSpherical(r, zeta, phi);
    }

}
