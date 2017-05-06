import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
    private final static double EPSILON = 0.000001;
    public static double quardiliteralArea;
    public static double topLeftSide;
    public static double bottomLeftSide;
    public static double topRightSide;
    public static double bottomRightSide;
    public static int total;
    public static double maxValue = -1000000001;

    public static Point yMin,xMin,yMax,xMax;
    public static Point topLeft,bottomLeft,topRight,bottomRight;
    public static List<Point>topLeftPoints = new ArrayList<>();
    public static List<Point>bottomLeftPoints = new ArrayList<>();
    public static List<Point>topRightPoints = new ArrayList<>();
    public static List<Point>bottomRightPoints = new ArrayList<>();

    private static Set<Point>pointSet = new HashSet<>();

    public static double dist(Point p,Point q){
        return Math.sqrt((p.getX() - q.getX()) * (p.getX() - q.getX()) + (p.getY() - q.getY()) * (p.getY() - q.getY()));
    }

    public static int diff(int a,int b){
        if(a > b) return a - b;
        else return b - a;
    }

    public  static int check(Point A, Point B, Point C){
        double res = A.getX() * B.getY() - A.getY()*B.getX() + B.getX() * C.getY() - B.getY() * C.getX() + C.getX() * A.getY() - C.getY() * A.getX();
        if(res < 0) {
            return -1;
        }
        if(res > 0){
            return 1;
        }
        return 0;
    }

    public static Stack<Point> applyGrahamScan(List<Point>points){
        Collections.sort(points, (A, B) -> {
            if(check(yMin, A, B) == 0){
                if (dist(yMin,A) < dist(yMin,B)){
                    return -1;
                }
                return 1;
            }
            double dx1 = A.getX() - yMin.getX();
            double dx2 = B.getX() - yMin.getX();
            double dy1 = A.getY() - yMin.getY();
            double dy2 = B.getY() - yMin.getY();

            if(dy1*dx2 < dy2 * dx1){
                return -1;
            }
            return  1;
        });
        Stack<Point> pointStack = new Stack<>();
        pointStack.push(points.get(0));
        pointStack.push(points.get(1));
        Point first,mid;
        for(int i=2; i < points.size(); i++){
            while(pointStack.size() >= 2){
                mid = pointStack.peek();
                pointStack.pop();
                first = pointStack.peek();
                if(check(first, mid, points.get(i)) >= 0){
                    pointStack.push(mid);
                    pointStack.push(points.get(i));
                    break;
                }
            }
        }

        return pointStack;
    }

    public static Line createStraightLine(Point p,Point q){
        double a = p.getY() - q.getY();
        double b = q.getX() - p.getX();
        double c = (p.getY() - q.getY() * p.getX() + (p.getX() - q.getX()) * p.getY());
        double sqRootOfASquarePlusBSquare = Math.sqrt(a * a + b * b);
        Line line = new Line(a, b, c, sqRootOfASquarePlusBSquare);
        return line;
    }

    public static double getTriangleArea(double a,double b,double c){
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    public static boolean isInsideQuadriliteral(Point p){
        double up =dist(yMax, p);
        double down =dist(yMin, p);
        double left =dist(xMin, p);
        double right =dist(xMax, p);
        double area1 = getTriangleArea(up, left, topLeftSide);
        double area2 = getTriangleArea(up, right, topRightSide);
        double area3 = getTriangleArea(down, right, bottomRightSide);
        double area4 = getTriangleArea(down, left, bottomLeftSide);

        if( (area1 + area2 + area3 + area4) > quardiliteralArea+EPSILON){
            return false;
        }
        return true;
    }

    public static double getQuardiliteralAreaByFourExtremePoints(){

        topLeftSide = dist(xMin, yMax);
        topRightSide = dist(xMax, yMax);
        bottomRightSide = dist(xMax, yMin);
        bottomLeftSide = dist(xMin, yMin);
        double corner = dist(yMin, yMax);

        double area1 = getTriangleArea(topLeftSide, bottomLeftSide, corner);
        double area2 = getTriangleArea(topRightSide, bottomRightSide, corner);

        return area1 + area2;

    }

    public static List<Point> getPointAfterEliminateFromQuardiliteral(){
        List<Point>points = new ArrayList<>();

        for(Point point : pointSet){
            if(!isInsideQuadriliteral(point)){
                points.add(point);
            }
        }
        return points;
    }
    public static void separatePoints(List<Point>points){

        for(Point point:points){
            if(point.getX() < yMax.getX() && point.getY() > xMin.getY()){
                topLeftPoints.add(point);
            }else if(point.getX() > yMax.getX() && point.getY() > xMax.getY()){
                topRightPoints.add(point);
            }else if(point.getX() > yMin.getX() && point.getY() < xMax.getY()){
                bottomRightPoints.add(point);
            }else if(point.getX() < yMin.getX() && point.getY() < xMin.getY()){
                bottomLeftPoints.add(point);
            }

        }

    }
    public static void  yMinOnly(Set<Point>pointSet){

        yMin = new Point(0, 1000000001);


        for(Point point:pointSet){

            if(point.getY() < yMin.getY()){
                yMin = point;
            }
        }
    }

    public static void  findFourExtremePoints(Set<Point>pointSet){

        xMin = new Point(1000000001, 0);
        xMax = new Point(-1000000001, 0);
        yMin = new Point(0, 1000000001);
        yMax = new Point(0, -1000000001);

        for(Point point:pointSet){
             if(point.getX() < xMin.getX()){
                 xMin = point;
             }
             if(point.getX() > xMax.getX()){
                xMax = point;
             }
             if(point.getY() < yMin.getY()){
                yMin = point;
             }
             if(point.getY() > yMax.getY()){
                yMax = point;
             }
        }
    }

    public static double distanceFromLineToPoint(Point point, Line line){
        return (line.getA() * point.getX() + line.getB() * point.getY() + line.getC()) / line.getSqRootOfASquarePlusBSquare();
    }
    public static Point getPeakPoint(List<Point>points, Point p, Point q){
        Line line = createStraightLine(p, q);
        Point top = null;
        double mx = maxValue;
        for(Point point : points){
            double distance = distanceFromLineToPoint(point, line);
            if(distance > mx + EPSILON){
                top = point;
                mx = distance;
            }
        }

        return top;
    }

    public static boolean insideTriangle(Point point, Point xExtreme, Point yExtreme, Point peakPoint, double side1, double side2, double side3, double triangleArea){

        double a =dist(xExtreme, point);
        double b =dist(yExtreme, point);
        double c =dist(peakPoint, point);
        double area1 = getTriangleArea(a, c, side1);
        double area2 = getTriangleArea(c, b, side2);
        double area3 = getTriangleArea(a, b, side3);
        if((area1 + area2 + area3) > triangleArea + EPSILON){
            return false;
        }
        return true;
    }

    public static List<Point> getPointsNotInsideTriangle(List<Point>points, Point xExtreme,Point yExtreme, Point peakPoint, double side){
        List<Point> pointList = new ArrayList<>();
        double side1 = dist(xExtreme, peakPoint);
        double side2 = dist(yExtreme, peakPoint);
        double side3 = side;
        for (Point point : points){
            if(!insideTriangle(point, xExtreme, yExtreme, peakPoint, side1, side2, side3, getTriangleArea(side1, side2, side3))){
                pointList.add(point);
            }
        }

        return  pointList;
    }
    public static void debugList(List<Point>points){
        System.out.println("Previous Point :: "+points.size());
        int cnt = 0;
        for(Point point : points){
            if(point == null){
                System.out.println("NZLOG:: point is null inside:: "+point);
            } else {
                cnt++;
            }
        }
        System.out.println("Count after loop:: "+cnt);
        System.out.println("-----------------------------------------------------------------------");
    }

//
    public static List<Point> pointsAfterElimination(){

        List<Point>finalPoints = new ArrayList<>();
        findFourExtremePoints(pointSet);
        finalPoints.add(xMax);
        finalPoints.add(yMax);
        finalPoints.add(xMin);
        finalPoints.add(yMin);
        quardiliteralArea = getQuardiliteralAreaByFourExtremePoints();
        List<Point>existPoints = getPointAfterEliminateFromQuardiliteral();
        System.out.println("NZLOG:: points Eliminated after Quadriliteral points:: " + (total - existPoints.size()));
        separatePoints(existPoints);
        if(topLeftPoints!=null && topLeftPoints.size()>0) {
            topLeft = getPeakPoint(topLeftPoints, xMin, yMax);
            finalPoints.add(topLeft);
        }
        if(topRightPoints != null && topRightPoints.size()>0) {
            topRight = getPeakPoint(topRightPoints, yMax, xMax);
            finalPoints.add(topRight);
        }
        if(bottomRightPoints!=null && bottomRightPoints.size()>0) {
            bottomRight = getPeakPoint(bottomRightPoints, xMax, xMin);
            finalPoints.add(bottomRight);
        }
        if(bottomLeftPoints != null && bottomLeftPoints.size()>0) {
            bottomLeft = getPeakPoint(bottomLeftPoints, yMin, xMin);
            finalPoints.add(bottomLeft);
        }

//        debugList(finalPoints);
        if(topLeft != null){
            finalPoints.addAll(getPointsNotInsideTriangle(topLeftPoints, xMin, yMax, topLeft, topLeftSide)); //topLeft
        }
        if(topRight != null){
            finalPoints.addAll(getPointsNotInsideTriangle(topRightPoints, xMax, yMax, topRight, topRightSide)); //topRight
        }
        if(bottomRight != null) {
            finalPoints.addAll(getPointsNotInsideTriangle(bottomRightPoints, xMax, yMin, bottomRight, bottomRightSide)); //bottomRightPoints
        }
        if(bottomLeft != null) {
            finalPoints.addAll(getPointsNotInsideTriangle(bottomLeftPoints, xMin, yMin, bottomLeft, bottomLeftSide)); //bottomLeftPoints
        }
        return finalPoints;

    }


    public static void main(String [] args) {

        Map<Point,Boolean> map =new HashMap<Point,Boolean>();
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        Random rand = new Random();
        for(int i= 0; i < N; i++){
            int x = rand.nextInt()%N;
            int y = rand.nextInt()%N;
            Point p = new Point(x, y);
            pointSet.add(p);
        }
        total = pointSet.size();
//
//////////////////////////////////RANDOM INPUT ENDS////////////////////////////////////////////////////////////////

        long startTime = System.currentTimeMillis(); // Time Starts

// With Elimination Technique
        List<Point>finalPoints = pointsAfterElimination();
        System.out.println("NZLOG:: Total points eleminated:: "+(N-finalPoints.size()));
        System.out.println("NZLOG:: points exists:: "+finalPoints.size());
        Stack<Point>convexHull = applyGrahamScan(finalPoints);
        long endTime = System.currentTimeMillis(); // Time Ends
        System.out.println("NZLOG:: Time:: "+(endTime - startTime)+" Miliseconds");
        System.out.println("NZLOG:: HULL points:: "+convexHull.size());
        System.out.println("------------------------------------------------------------");

// Without Elimination Technique
        List<Point> points = new ArrayList<>();
        points.addAll(pointSet);
        long stTime = System.currentTimeMillis();
        yMinOnly(pointSet);
        Stack<Point>convexHullGraham = applyGrahamScan(points);
        long edTime = System.currentTimeMillis();
        System.out.println("NZLOG:: Time:: "+(edTime - stTime)+" Miliseconds");
        System.out.println("NZLOG:: HULL points:: "+convexHullGraham.size());
        System.out.println("------------------------------------------------------------");

    }

}
