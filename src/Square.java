public class Square {
    Vertex v1, v2, v3, v4;

    Square(Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }

    boolean isVisible(Vertex camera) {

        double A = this.v1.y * (this.v2.z - this.v3.z) + this.v2.y * (this.v3.z - this.v1.z) + this.v3.y * (this.v1.z - this.v2.z);
        double B = this.v1.z * (this.v2.x - this.v3.x) + this.v2.z * (this.v3.x - this.v1.x) + this.v3.z * (this.v1.x - this.v2.x);
        double C = this.v1.x * (this.v2.y - this.v3.y) + this.v2.x * (this.v3.y - this.v1.y) + this.v3.x * (this.v1.y - this.v2.y);
        double D = - (this.v1.x * (this.v2.y * this.v3.z - this.v3.y * this.v2.z) + this.v2.x * (this.v3.y * this.v1.z - this.v1.y * this.v3.z) + this.v3.x * (this.v1.y * this.v2.z - this.v2.y * this.v1.z));

        double sol = A * camera.x + B * camera.y + C * camera.z;

        return sol > 0;

    }

}
