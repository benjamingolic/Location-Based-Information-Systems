package at.fhooe.sail.ois.map;

import at.fhooe.sail.ois.map.client.Matrix;

import java.awt.*;

/**
 * A test class for the Matrix operations.
 *
 * This class contains methods to test matrix inversion and Zoom-to-Fit (ZTF) matrix
 * calculation. It includes a main method to run these tests.
 */
public class MatrixTest {
    /**
     * The main method to run the matrix tests.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        // Uncomment to test matrix inversion
        // testInverse();

        // Test the zoom-to-fit functionality
        testZTF();
    }

    /**
     * Tests the matrix inversion functionality.
     *
     * This method creates a matrix, prints it, and then attempts to print its inverse.
     * If the inversion fails, it catches and prints the exception stack trace.
     */
    public static void testInverse() {
        Matrix matrix = new Matrix(1, 2, 3, 4, 5, 6, 7, 8, 9);
        System.out.println(matrix);
        try {
            System.out.println(matrix.invers().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the Zoom-to-Fit (ZTF) matrix calculation and its application
     * to project and back-project a rectangle.
     *
     * This method calculates a ZTF matrix for a world rectangle and a window rectangle,
     * projects the world rectangle to the window rectangle, and then back-projects it
     * to verify the correctness of the projection. It prints the results and the expected results.
     */
    public static void testZTF() {
        // Welt-Rechteck (World Rectangle)
        Rectangle worldRect = new Rectangle(47944531, 608091485, 234500, 213463);

        // Fenster-Rechteck (Window Rectangle)
        Rectangle windowRect = new Rectangle(0, 0, 640, 480);

        // Berechnung der Zoom-to-Fit Matrix
        Matrix ztfMatrix = Matrix.zoomToFit(worldRect, windowRect);

        // Projektion des Welt-Rechtecks in das Fenster-Rechteck
        Rectangle projectedRect = ztfMatrix.multiply(worldRect);
        System.out.println("Projected Rectangle: " + projectedRect);

        // Berechnung der Invers-Matrix
        Matrix invMatrix = ztfMatrix.invers();

        // Rückprojektion des projizierten Rechtecks in das Welt-Rechteck
        Rectangle backProjectedRect = invMatrix.multiply(projectedRect);
        System.out.println("Back Projected Rectangle: " + backProjectedRect);

        // Überprüfung der Ergebnisse
        System.out.println("Expected Projected Rectangle: (56, 0, 527, 480)");
        System.out.println("Expected Back Projected Rectangle: " + worldRect);
    }
}
