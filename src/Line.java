/**
 * Created by newaz on 2/13/17.
 */
public class Line {
    private double A;
    private double B;
    private double C;
    private double sqRootOfASquarePlusBSquare;

    public Line(double a, double b, double c, double sqRootOfASquarePlusBSquare) {
        A = a;
        B = b;
        C = c;
        this.sqRootOfASquarePlusBSquare = sqRootOfASquarePlusBSquare;
    }

    public double getA() {
        return A;
    }

    public void setA(double a) {
        A = a;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getC() {
        return C;
    }

    public void setC(double c) {
        C = c;
    }

    public double getSqRootOfASquarePlusBSquare() {
        return sqRootOfASquarePlusBSquare;
    }

    public void setSqRootOfASquarePlusBSquare(double sqRootOfASquarePlusBSquare) {
        this.sqRootOfASquarePlusBSquare = sqRootOfASquarePlusBSquare;
    }
}
