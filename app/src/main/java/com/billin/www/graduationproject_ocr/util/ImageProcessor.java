package com.billin.www.graduationproject_ocr.util;

import android.graphics.Bitmap;
import android.graphics.PointF;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ImageProcessor {

    private static final String TAG = "ImageProcessor";
    private boolean colorMode = false;
    private boolean filterMode = true;
    private double colorGain = 1.5;       // contrast
    private double colorBias = 0;         // bright
    private int colorThresh = 110;        // threshold
    private Size mPreviewSize;
    private Point[] mPreviewPoints;

    private static ImageProcessor mSingleton = new ImageProcessor();

    private ImageProcessor() {

    }

    public static ImageProcessor getInstance() {
        return mSingleton;
    }

    public PointF[] getQuadrilateral(String imagePath) {
        Mat image = Imgcodecs.imread(imagePath);
//        return adapterPointToAndroidPoint(getQuadrilateral(findContours(image), image.size()));

        Mat edges = edgeDetection(image);
        MatOfPoint2f largest = findLargestContour(edges);
        PointF[] result = new PointF[4];
        if (largest != null) {
            Point[] points = sortPoints(largest.toArray());

            result[0] = (new PointF(Double.valueOf(points[0].x).floatValue(), Double.valueOf(points[0].y).floatValue()));
            result[1] = (new PointF(Double.valueOf(points[1].x).floatValue(), Double.valueOf(points[1].y).floatValue()));
            result[2] = (new PointF(Double.valueOf(points[2].x).floatValue(), Double.valueOf(points[2].y).floatValue()));
            result[3] = (new PointF(Double.valueOf(points[3].x).floatValue(), Double.valueOf(points[3].y).floatValue()));
            largest.release();

            return result;
        } else {

            result[0] = new PointF(0, 0);
            result[1] = new PointF(image.width(), 0);
            result[2] = new PointF(image.width(), image.height());
            result[3] = new PointF(0, image.height());

            return result;
        }
    }

    public Bitmap perspectiveTransform(Bitmap bitmap, PointF[] points) {
        Mat src = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, src);

        Mat dst = fourPointTransform(src, sortPoints(new Point[]{
                new Point(points[0].x, points[0].y),
                new Point(points[1].x, points[1].y),
                new Point(points[2].x, points[2].y),
                new Point(points[3].x, points[3].y)
        }));
        Bitmap res = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, res);

        return res;
    }

    /**
     * Transform the coordinates on the given Mat to correct the perspective.
     *
     * @param src    A valid Mat
     * @param points A list of coordinates from the given Mat to adjust the perspective
     * @return A perspective transformed Mat
     */
    public Mat perspectiveTransform(Mat src, List<PointF> points) {
        Point point1 = new Point(points.get(0).x, points.get(0).y);
        Point point2 = new Point(points.get(1).x, points.get(1).y);
        Point point3 = new Point(points.get(2).x, points.get(2).y);
        Point point4 = new Point(points.get(3).x, points.get(3).y);
        Point[] pts = {point1, point2, point3, point4};
        return fourPointTransform(src, sortPoints(pts));
    }

    /**
     * Find the largest 4 point contour in the given Mat.
     *
     * @param src A valid Mat
     * @return The largest contour as a Mat
     */
    private MatOfPoint2f findLargestContour(Mat src) {
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(src, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Get the 5 largest contours
        Collections.sort(contours, new Comparator<MatOfPoint>() {
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                double area1 = Imgproc.contourArea(o1);
                double area2 = Imgproc.contourArea(o2);
                return (int) (area2 - area1);
            }
        });
        if (contours.size() > 5) contours.subList(4, contours.size() - 1).clear();

        MatOfPoint2f largest = null;
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approx = new MatOfPoint2f();
            MatOfPoint2f c = new MatOfPoint2f();
            contour.convertTo(c, CvType.CV_32FC2);
            Imgproc.approxPolyDP(c, approx, Imgproc.arcLength(c, true) * 0.02, true);

            if (approx.total() == 4 && Imgproc.contourArea(contour) > 150) {
                // the contour has 4 points, it's valid
                largest = approx;
                break;
            }
        }

        return largest;
    }

    /**
     * Detect the edges in the given Mat
     *
     * @param src A valid Mat object
     * @return A Mat processed to find edges
     */
    private Mat edgeDetection(Mat src) {
        Mat edges = new Mat();
        Imgproc.cvtColor(src, edges, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(edges, edges, new Size(5, 5), 0);
        Imgproc.Canny(edges, edges, 75, 200);
        return edges;
    }


    private PointF[] adapterPointToAndroidPoint(Point[] point) {
        if (point == null) {
            return new PointF[]{new PointF(), new PointF(), new PointF(), new PointF()};
        }

        PointF[] pointFS = new PointF[point.length];

        int i = 0;
        for (Point p : point) {
            pointFS[i++] = new PointF((float) p.x, (float) p.y);
        }

        return pointFS;
    }

    private Point[] getQuadrilateral(ArrayList<MatOfPoint> contours, Size srcSize) {

        double ratio = srcSize.height / 500;
        int height = Double.valueOf(srcSize.height).intValue();
        int width = Double.valueOf(srcSize.width).intValue();
        Size size = new Size(width, height);

        Collections.sort(contours, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                return Double.compare(Imgproc.contourArea(o1), Imgproc.contourArea(o2));
            }
        });

        for (MatOfPoint c : contours) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length == 4) {
                Point[] foundPoints = sortPoints(points);

//                if (insideArea(foundPoints, size)) {
                return foundPoints;
//                }
            }
        }

        return null;
    }


    private Point[] sortPoints(Point[] src) {

        ArrayList<Point> srcPoints = new ArrayList<>(Arrays.asList(src));

        Point[] result = {null, null, null, null};

        Comparator<Point> sumComparator = new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.compare(lhs.y + lhs.x, rhs.y + rhs.x);
            }
        };

        Comparator<Point> diffComparator = new Comparator<Point>() {

            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.compare(lhs.y - lhs.x, rhs.y - rhs.x);
            }
        };

        // top-left corner = minimal sum
        result[0] = Collections.min(srcPoints, sumComparator);

        // bottom-right corner = maximal sum
        result[2] = Collections.max(srcPoints, sumComparator);

        // top-right corner = minimal diference
        result[1] = Collections.min(srcPoints, diffComparator);

        // bottom-left corner = maximal diference
        result[3] = Collections.max(srcPoints, diffComparator);

        return result;
    }

    private boolean insideArea(Point[] rp, Size size) {

        int width = Double.valueOf(size.width).intValue();
        int height = Double.valueOf(size.height).intValue();
        int baseMeasure = height / 4;

        int bottomPos = height - baseMeasure;
        int topPos = baseMeasure;
        int leftPos = width / 2 - baseMeasure;
        int rightPos = width / 2 + baseMeasure;

        return (
                rp[0].x <= leftPos && rp[0].y <= topPos
                        && rp[1].x >= rightPos && rp[1].y <= topPos
                        && rp[2].x >= rightPos && rp[2].y >= bottomPos
                        && rp[3].x <= leftPos && rp[3].y >= bottomPos

        );
    }

    private void enhanceDocument(Mat src) {
        if (colorMode && filterMode) {
            src.convertTo(src, -1, colorGain, colorBias);
            Mat mask = new Mat(src.size(), CvType.CV_8UC1);
            Imgproc.cvtColor(src, mask, Imgproc.COLOR_RGBA2GRAY);

            Mat copy = new Mat(src.size(), CvType.CV_8UC3);
            src.copyTo(copy);

            Imgproc.adaptiveThreshold(mask, mask, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                    Imgproc.THRESH_BINARY_INV, 15, 15);

            src.setTo(new Scalar(255, 255, 255));
            copy.copyTo(src, mask);

            copy.release();
            mask.release();

            // special color threshold algorithm
            colorThresh(src, colorThresh);
        } else if (!colorMode) {
            Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2GRAY);
            if (filterMode) {
                Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                        Imgproc.THRESH_BINARY, 15, 15);
            }
        }
    }

    private void colorThresh(Mat src, int threshold) {
        Size srcSize = src.size();
        int size = (int) (srcSize.height * srcSize.width) * 3;
        byte[] d = new byte[size];
        src.get(0, 0, d);

        for (int i = 0; i < size; i += 3) {

            // the "& 0xff" operations are needed to convert the signed byte to double

            // avoid unneeded work
            if ((double) (d[i] & 0xff) == 255) {
                continue;
            }

            double max = Math.max(Math.max((double) (d[i] & 0xff), (double) (d[i + 1] & 0xff)),
                    (double) (d[i + 2] & 0xff));
            double mean = ((double) (d[i] & 0xff) + (double) (d[i + 1] & 0xff)
                    + (double) (d[i + 2] & 0xff)) / 3;

            if (max > threshold && mean < max * 0.8) {
                d[i] = (byte) ((double) (d[i] & 0xff) * 255 / max);
                d[i + 1] = (byte) ((double) (d[i + 1] & 0xff) * 255 / max);
                d[i + 2] = (byte) ((double) (d[i + 2] & 0xff) * 255 / max);
            } else {
                d[i] = d[i + 1] = d[i + 2] = 0;
            }
        }
        src.put(0, 0, d);
    }

    private Mat fourPointTransform(Mat src, Point[] pts) {

        double ratio = 1;

        Point tl = pts[0];
        Point tr = pts[1];
        Point br = pts[2];
        Point bl = pts[3];

        double widthA = Math.sqrt(Math.pow(br.x - bl.x, 2) + Math.pow(br.y - bl.y, 2));
        double widthB = Math.sqrt(Math.pow(tr.x - tl.x, 2) + Math.pow(tr.y - tl.y, 2));

        double dw = Math.max(widthA, widthB) * ratio;
        int maxWidth = Double.valueOf(dw).intValue();


        double heightA = Math.sqrt(Math.pow(tr.x - br.x, 2) + Math.pow(tr.y - br.y, 2));
        double heightB = Math.sqrt(Math.pow(tl.x - bl.x, 2) + Math.pow(tl.y - bl.y, 2));

        double dh = Math.max(heightA, heightB) * ratio;
        int maxHeight = Double.valueOf(dh).intValue();

        Mat doc = new Mat(maxHeight, maxWidth, CvType.CV_8UC4);

        Mat src_mat = new Mat(4, 1, CvType.CV_32FC2);
        Mat dst_mat = new Mat(4, 1, CvType.CV_32FC2);

        src_mat.put(0, 0, tl.x * ratio, tl.y * ratio, tr.x * ratio, tr.y * ratio, br.x * ratio, br.y * ratio, bl.x * ratio, bl.y * ratio);
        dst_mat.put(0, 0, 0.0, 0.0, dw, 0.0, dw, dh, 0.0, dh);

        Mat m = Imgproc.getPerspectiveTransform(src_mat, dst_mat);

        Imgproc.warpPerspective(src, doc, m, doc.size());

        return doc;
    }

    private ArrayList<MatOfPoint> findContours(Mat src) {

        Mat grayImage = null;
        Mat cannedImage = null;
        Mat resizedImage = null;

        double ratio = src.size().height / 100;
        int height = Double.valueOf(src.size().height).intValue();
        int width = Double.valueOf(src.size().width).intValue();
        Size size = new Size(width, height);

        resizedImage = new Mat(size, CvType.CV_8UC4);
        grayImage = new Mat(size, CvType.CV_8UC4);
        cannedImage = new Mat(size, CvType.CV_8UC1);

        Imgproc.resize(src, resizedImage, size);
        Imgproc.cvtColor(resizedImage, grayImage, Imgproc.COLOR_RGBA2GRAY, 4);
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(5, 5), 0);
        Imgproc.Canny(grayImage, cannedImage, 75, 200);

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(cannedImage, contours, hierarchy, Imgproc.RETR_LIST,
                Imgproc.CHAIN_APPROX_SIMPLE);

        hierarchy.release();

        Collections.sort(contours, new Comparator<MatOfPoint>() {

            @Override
            public int compare(MatOfPoint lhs, MatOfPoint rhs) {
                return Double.valueOf(Imgproc.contourArea(rhs)).compareTo(Imgproc.contourArea(lhs));
            }
        });

        resizedImage.release();
        grayImage.release();
        cannedImage.release();

        return contours;
    }
}
