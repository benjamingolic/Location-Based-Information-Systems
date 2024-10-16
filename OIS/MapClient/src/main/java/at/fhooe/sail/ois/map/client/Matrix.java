package at.fhooe.sail.ois.map.client;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * This class represents a 3x3 transformation matrix for performing various geometric transformations
 * such as translation, scaling, rotation, and mirroring.
 */
public class Matrix {

    private double[][] mMatrix;

    /**
     * Default constructor. Initializes the matrix to a 3x3 zero matrix.
     */
    public Matrix() {
        mMatrix = new double[3][3];
    }

    /**
     * Constructor that initializes the matrix with specific values.
     *
     * @param _m11 The value at row 1, column 1.
     * @param _m12 The value at row 1, column 2.
     * @param _m13 The value at row 1, column 3.
     * @param _m21 The value at row 2, column 1.
     * @param _m22 The value at row 2, column 2.
     * @param _m23 The value at row 2, column 3.
     * @param _m31 The value at row 3, column 1.
     * @param _m32 The value at row 3, column 2.
     * @param _m33 The value at row 3, column 3.
     */
    public Matrix(double _m11, double _m12, double _m13, double _m21, double _m22, double _m23, double _m31, double _m32, double _m33) {
        this();
        mMatrix[0][0] = _m11;
        mMatrix[0][1] = _m12;
        mMatrix[0][2] = _m13;
        mMatrix[1][0] = _m21;
        mMatrix[1][1] = _m22;
        mMatrix[1][2] = _m23;
        mMatrix[2][0] = _m31;
        mMatrix[2][1] = _m32;
        mMatrix[2][2] = _m33;
    }

    /**
     * Returns a string representation of the matrix.
     *
     * @return A string containing the values of the matrix.
     * @see java.lang.String
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (double[] doubles : mMatrix) {
            for (double aDouble : doubles) {
                ret.append(aDouble).append(" ");
            }
            ret.append("\n");
        }
        return ret.toString();
    }

    /**
     * Returns the inverse of the transformation matrix.
     *
     * @return The inverse matrix.
     */
    public Matrix invers() {
        double a = mMatrix[0][0];
        double b = mMatrix[0][1];
        double c = mMatrix[0][2];
        double d = mMatrix[1][0];
        double e = mMatrix[1][1];
        double f = mMatrix[1][2];
        double g = mMatrix[2][0];
        double h = mMatrix[2][1];
        double i = mMatrix[2][2];

        double det = a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g);

        if (det == 0) {
            throw new IllegalStateException("Matrix is singular and cannot be inverted.");
        }

        double invDet = 1.0 / det;

        double m11 = (e * i - f * h) * invDet;
        double m12 = (c * h - b * i) * invDet;
        double m13 = (b * f - c * e) * invDet;
        double m21 = (f * g - d * i) * invDet;
        double m22 = (a * i - c * g) * invDet;
        double m23 = (c * d - a * f) * invDet;
        double m31 = (d * h - e * g) * invDet;
        double m32 = (b * g - a * h) * invDet;
        double m33 = (a * e - b * d) * invDet;

        return new Matrix(m11, m12, m13, m21, m22, m23, m31, m32, m33);
    }

    /**
     * Multiplies this matrix with another matrix and returns the result.
     *
     * @param _other The matrix to multiply with.
     * @return The resulting matrix from the multiplication.
     */
    public Matrix multiply(Matrix _other) {
        double[][] resultMatrix = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double sum = 0;
                for (int k = 0; k < 3; k++) {
                    sum += mMatrix[i][k] * _other.mMatrix[k][j];
                }
                resultMatrix[i][j] = sum;
            }
        }

        return new Matrix(resultMatrix[0][0], resultMatrix[0][1], resultMatrix[0][2],
                resultMatrix[1][0], resultMatrix[1][1], resultMatrix[1][2],
                resultMatrix[2][0], resultMatrix[2][1], resultMatrix[2][2]);
    }

    /**
     * Multiplies a point with the matrix and returns the result.
     *
     * @param _pt The point to be multiplied.
     * @return A new point representing the result of the multiplication.
     * @see Point
     */
    public Point multiply(Point _pt) {
        double newX = mMatrix[0][0] * _pt.getX() + mMatrix[0][1] * _pt.getY() + mMatrix[0][2] * 1;
        double newY = mMatrix[1][0] * _pt.getX() + mMatrix[1][1] * _pt.getY() + mMatrix[1][2] * 1;

        return new Point((int) newX, (int) newY);
    }

    /**
     * Multiplies a rectangle with the matrix and returns the result.
     *
     * @param _rect The rectangle to be multiplied.
     * @return A new rectangle representing the result of the multiplication.
     * @see Rectangle
     */
    public Rectangle multiply(Rectangle _rect) {
        Rectangle ret = new Rectangle();

        Point pt = new Point(_rect.x, _rect.y);
        Point pt2 = new Point(_rect.x + _rect.width, _rect.y + _rect.height);

        Point newPt = multiply(pt);
        Point newPt2 = multiply(pt2);

        ret.x = newPt.x;
        ret.y = newPt.y;
        ret.width = newPt2.x - newPt.x;
        ret.height = newPt2.y - newPt.y;

        return ret;
    }

    /**
     * Multiplies a polygon with the matrix and returns the result.
     *
     * @param _poly The polygon to be multiplied.
     * @return A new polygon representing the result of the multiplication.
     * @see Polygon
     */
    public Polygon multiply(Polygon _poly) {
        Polygon ret = new Polygon();
        for (int i = 0; i < _poly.npoints; i++) {
            Point pt = new Point(_poly.xpoints[i], _poly.ypoints[i]);
            Point newPt = multiply(pt);
            ret.addPoint(newPt.x, newPt.y);
        }
        return ret;
    }

    /**
     * Returns a translation matrix.
     *
     * @param _x The translation value in the X direction.
     * @param _y The translation value in the Y direction.
     * @return The translation matrix.
     */
    public static Matrix translate(double _x, double _y) {
        return new Matrix(1, 0, _x, 0, 1, _y, 0, 0, 1);
    }

    /**
     * Returns a translation matrix.
     *
     * @param _pt A point containing the translation values.
     * @return The translation matrix.
     * @see java.awt.Point
     */
    public static Matrix translate(Point _pt) {
        return translate(_pt.x, _pt.y);
    }

    /**
     * Returns a scaling matrix.
     *
     * @param _scaleVal The scaling value.
     * @return The scaling matrix.
     */
    public static Matrix scale(double _scaleVal) {
        return new Matrix(_scaleVal, 0, 0, 0, _scaleVal, 0, 0, 0, 1);
    }

    /**
     * Returns a mirroring matrix for the X-axis.
     *
     * @return The mirroring matrix.
     */
    public static Matrix mirrorX() {
        return new Matrix(1, 0, 0, 0, -1, 0, 0, 0, 1);
    }

    /**
     * Returns a mirroring matrix for the Y-axis.
     *
     * @return The mirroring matrix.
     */
    public static Matrix mirrorY() {
        return new Matrix(-1, 0, 0, 0, 1, 0, 0, 0, 1);
    }

    /**
     * Returns a rotation matrix.
     *
     * @param _alpha The angle (in radians) to rotate by.
     * @return The rotation matrix.
     */
    public static Matrix rotate(double _alpha) {
        return new Matrix(Math.cos(_alpha), -Math.sin(_alpha), 0, Math.sin(_alpha), Math.cos(_alpha), 0, 0, 0, 1);
    }

    /**
     * Returns the scaling factor needed to fit the _world rectangle into the _win rectangle based on the X-axis.
     *
     * @param _world The rectangle in world coordinates.
     * @param _win The rectangle in screen coordinates.
     * @return The scaling factor.
     * @see java.awt.Rectangle
     */
    public static double getZoomFactorX(Rectangle _world, Rectangle _win) {
        return (double) _win.width / _world.width;
    }

    /**
     * Returns the scaling factor needed to fit the _world rectangle into the _win rectangle based on the Y-axis.
     *
     * @param _world The rectangle in world coordinates.
     * @param _win The rectangle in screen coordinates.
     * @return The scaling factor.
     * @see java.awt.Rectangle
     */
    public static double getZoomFactorY(Rectangle _world, Rectangle _win) {
        return (double) _win.height / _world.height;
    }

    /**
     * Returns a matrix that contains all the necessary transformations
     * (translation, scaling, mirroring, and translation) to map a _world rectangle into a _win rectangle.
     *
     * @param _world The rectangle in world coordinates.
     * @param _win The rectangle in screen coordinates.
     * @return The transformation matrix.
     * @see java.awt.Rectangle
     */
    public static Matrix zoomToFit(Rectangle _world, Rectangle _win) {
        Matrix translateToOrigin = translate(-_world.x, -_world.y);

        double zoomX = getZoomFactorX(_world, _win);
        double zoomY = getZoomFactorY(_world, _win);
        double zoomFactor = Math.min(zoomX, zoomY);
        Matrix scale = scale(zoomFactor);

        Matrix mirrorX = mirrorX();
        Matrix transform = mirrorX.multiply(scale).multiply(translateToOrigin);
        Rectangle transformedBounds = transform.multiply(_world);

        double transX = (double) (_win.width - transformedBounds.width) / 2 - transformedBounds.x;
        double transY = (double) (_win.height - transformedBounds.height) / 2 - transformedBounds.y;
        Matrix translateToCenter = translate(transX, transY);

        return translateToCenter.multiply(transform);
    }


    /**
     * Returns a matrix that extends an existing transformation
     * matrix to zoom in or out at a specific point.
     *
     * @param _old The existing transformation matrix.
     * @param _zoomPt The point to zoom at.
     * @param _zoomScale The zoom factor.
     * @return The new transformation matrix.
     * @see java.awt.Point
     */
    public static Matrix zoomPoint(Matrix _old, Point _zoomPt, double _zoomScale) {
        Matrix trans1 = translate(-_zoomPt.x, -_zoomPt.y);
        Matrix scale = scale(_zoomScale);
        Matrix trans2 = translate(_zoomPt.x, _zoomPt.y);

        return trans2.multiply(scale).multiply(trans1).multiply(_old);
    }

    /**
     * Multiplies a Point2D.Double with the transformation matrix.
     *
     * @param _pt The point to be multiplied.
     * @return The transformed point.
     */
    public Point2D.Double multiply(Point2D.Double _pt) {
        double destX = mMatrix[0][0] * _pt.x + mMatrix[0][1] * _pt.y + mMatrix[0][2];
        double destY = mMatrix[1][0] * _pt.x + mMatrix[1][1] * _pt.y + mMatrix[1][2];
        return new Point2D.Double(destX, destY);
    }

    /**
     * Returns a matrix with the translation components removed.
     *
     * @return The matrix with translation components removed.
     */
    public Matrix cleanTranslation() {
        return new Matrix(mMatrix[0][0], mMatrix[0][1], 0,
                mMatrix[1][0], mMatrix[1][1], 0,
                mMatrix[2][0], mMatrix[2][1], mMatrix[2][2]);
    }

}
