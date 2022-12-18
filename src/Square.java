public class Square {
    Vertex v1, v2, v3, v4;

    Square(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    boolean isVisibleAlg1(Vertex camera) {

        double A = this.v1.y * (this.v2.z - this.v3.z) + this.v2.y * (this.v3.z - this.v1.z) + this.v3.y * (this.v1.z - this.v2.z);
        double B = this.v1.z * (this.v2.x - this.v3.x) + this.v2.z * (this.v3.x - this.v1.x) + this.v3.z * (this.v1.x - this.v2.x);
        double C = this.v1.x * (this.v2.y - this.v3.y) + this.v2.x * (this.v3.y - this.v1.y) + this.v3.x * (this.v1.y - this.v2.y);
        double D = - (this.v1.x * (this.v2.y * this.v3.z - this.v3.y * this.v2.z) + this.v2.x * (this.v3.y * this.v1.z - this.v1.y * this.v3.z) + this.v3.x * (this.v1.y * this.v2.z - this.v2.y * this.v1.z));

        double sol = A * camera.x + B * camera.y + C * camera.z + D;

        return sol > 0;

    }

    boolean isVisibleAlg2(Vertex camera) {

        Vertex norm = new Vertex ((this.v2.y - this.v1.y) * (this.v4.z - this.v1.z) - (this.v2.z - this.v1.z) * (this.v4.y - this.v1.y),
                (this.v2.z - this.v1.z) * (this.v4.x - this.v1.x) - (this.v2.x - this.v1.x) * (this.v4.z - this.v1.z),
                (this.v2.x - this.v1.x) * (this.v4.y - this.v1.y) - (this.v2.y - this.v1.y) * (this.v4.x - this.v1.x));

        double normLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);

        Vertex center = new Vertex((v1.x + v3.x) / 2, (v1.y + v3.y) / 2, (v1.z + v3.z) / 2);

        norm.x /= normLength;
        norm.y /= normLength;
        norm.z /= normLength;

        Vertex d = new Vertex(camera.x - center.x, camera.y - center.y, camera.z - center.z);

        double dLen = Math.sqrt(d.x * d.x + d.y * d.y + d.z * d.z);

        double x = d.x / dLen;
        double y = d.y / dLen;
        double z = d.z / dLen;

        double phi = Math.acos((x * norm.x + y * norm.y + z * norm.z));

        return phi < Math.PI/2;

    }

}
