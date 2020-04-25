package lab4;

public class Utils {
    public static double computeDistance(Node node1,Node node2)
    {
        double distance=Math.sqrt(Math.pow(node1.getX()-node2.getX(),2)+Math.pow(node1.getY()-node2.getY(),2));
        return distance;
    }
}
