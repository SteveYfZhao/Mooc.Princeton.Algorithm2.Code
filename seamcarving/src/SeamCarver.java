import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.awt.image.ColorModel;
import java.lang.*;
import java.util.Arrays;

/**
 * Created by Zhao on 2015-11-11.
 */
public class SeamCarver {
    private int width;
    private int height;
    private int caseNum;
    private int[][] picArray;
    private boolean isRotated;

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        width = picture.width();
        height = picture.height();
        picArray = new int[width][height];

        if (width == 1 && height == 1) {
            caseNum = 0;

        }
        //case 1 linear graph, vertical line
        if (width == 1 && height > 1) {

            caseNum = 1;

        }
        //case 2 linear graph, horizontal line
        if (width > 1 && height == 1) {

            caseNum = 2;

        }

        //case 3 normal gragh;
        if (width > 1 && height > 1) {
            caseNum = 3;
        }


        isRotated = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picArray[i][j] = picture.get(i, j).getRGB();

            }
        }

    }

    public Picture picture()                          // current picture
    {
        if (isRotated) {
            picArray = rotateArray(picArray);
            isRotated= false;
        }
        int wt = picArray.length;
        int ht = picArray[0].length;

        Picture newPic = new Picture(wt, ht);
        for (int i = 0; i < wt; i++) {
            for (int j = 0; j < ht; j++) {
                newPic.set(i, j, new Color(picArray[i][j]));
            }
        }
        return newPic;
    }

    public int width()                            // width of current picture
    {
        return width;
    }

    public int height()                           // height of current picture
    {
        return height;
    }

    public double energy(int i, int j)               // energy of pixel at column x and row y
    {

        if (i<0 || i> width-1 || j <0 || j > height-1) {
            throw new IndexOutOfBoundsException("Energy index out of bound.");
        }

        if (caseNum==0 || caseNum==1 ||caseNum==2) {
            return 1000.00;
        }
        if (!isRotated) {
            return energyHelper(i, j);
        } else {
            //StdOut.println("Rotated!" );
            return energyHelper(j, i);
        }
    }

    private int getColorSum(Color color1, Color color2) {
        int r1 = color1.getRed() - color2.getRed();
        r1 = r1 * r1;
        int g1 = color1.getGreen() - color2.getGreen();
        g1 = g1 * g1;
        int b1 = color1.getBlue() - color2.getBlue();
        b1 = b1 * b1;
        return r1 + g1 + b1;
    }


    private double energyHelper(int x, int y) {
        int wt = picArray.length;
        int ht = picArray[0].length;
        switch (caseNum) {
            case 0:
                return (1000.00);

            case 1:
                if (y == 0 || y == ht - 1) {
                    return (1000.00);
                } else if (y > 0 && y < ht - 1) {
                    Color up = new Color(picArray[x][y - 1]);
                    Color down = new Color(picArray[x][y + 1]);
                    return Math.sqrt((double) getColorSum(up, down));
                } else {
                    throw new NullPointerException("The energy helper gets a wrong index.");
                }

            case 2:
                if (x == 0 || x == wt - 1) {
                    return (1000.00);
                } else if (x > 0 && x < wt - 1) {
                    Color left = new Color(picArray[x - 1][y]);
                    Color right = new Color(picArray[x + 1][y]);
                    return Math.sqrt((double) getColorSum(left, right));
                } else {
                    throw new NullPointerException("The energy helper gets a wrong index.");
                }

            case 3:
                if (x == 0 || x == wt - 1 || y == 0 || y == ht - 1) {
                    //StdOut.println("Edge wt: " + wt + " ht: " + ht + " x " + x + " y " + y );
                    return (1000.00);
                } else if (x > 0 && x < wt - 1 && y > 0 && y < ht - 1) {
                    //StdOut.println("Okay, wt: " + wt + " ht: " + ht + " x " + x + " y " + y );
                    Color up = new Color(picArray[x][y - 1]);
                    Color down = new Color(picArray[x][y + 1]);
                    Color left = new Color(picArray[x - 1][y]);
                    Color right = new Color(picArray[x + 1][y]);
                    return Math.sqrt((double) getColorSum(left, right) + (double) getColorSum(up, down));
                } else {
                    //StdOut.println("Wrong wt: " + wt + " ht: " + ht + " x " + x + " y " + y );
                    throw new NullPointerException("The energy helper gets a wrong index." + x +" "+ y);
                }
        }
        return -1.0;

    }


    private int[][] rotateArray(int[][] originalArray) {
        int w = originalArray.length;
        int h = originalArray[0].length;
        int[][] rotatedArray = new int[h][w];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                rotatedArray[j][i] = originalArray[i][j];
            }
        }
        return rotatedArray;
    }


    private int[] findSeamHelper() {

        int wt = picArray.length;
        int ht = picArray[0].length;

        //array of int queue to save the seam

        //two double 1d arrays for distance


        // handle some special cases first


        //case 0 single point graph
        if (wt == 1 && ht == 1) {
            int result[] = {0};
            caseNum = 0;
            return result;
        }
        //case 1 linear graph, vertical line
        if (wt == 1 && ht > 1) {
            int result[] = {0};
            caseNum = 1;
            return result;
        }
        //case 2 linear graph, horizontal line
        if (wt > 1 && ht == 1) {
            int result[] = new int[wt];
            Arrays.fill(result, 0);
            caseNum = 2;
            return result;
        }

        //case 3 normal gragh;
        if (wt > 1 && ht > 1) {
            caseNum = 3;
            double[] parentDistArr = new double[ht];
            double minDist = Double.MAX_VALUE;
            int resultEnd = -1;

            int[][] pixelArr = new int[wt][ht];
            Arrays.fill(parentDistArr, 1000.00);
            Arrays.fill(pixelArr[0], -1);

            //relax loop
            for (int i = 0; i < wt; i++) {
                double[] childDistArr = new double[ht];
                // set children ready to be relaxed;
                Arrays.fill(childDistArr, Double.MAX_VALUE);
                for (int j = 0; j < ht; j++) {
                    double parentDist = parentDistArr[j];

                    if (i < wt - 1) {
                        //loop through 2-3 children
                        for (int k = j - 1; k < j + 2; k++) {
                            if (k > -1 && k < ht) { //keep only children in range;
                                //relax
                                double childEn = energyHelper(i + 1, k);
                                double childDist = childDistArr[k];

                                if (childDist > parentDist + childEn) {
                                    childDistArr[k] = parentDist + childEn;
                                    pixelArr[i + 1][k] = j;
                                }
                            }
                        }

                    } else {
                        // last line
                        double currentDist = parentDistArr[j];
                        if (minDist > currentDist) {
                            minDist = currentDist;
                            resultEnd = j;
                        }
                    }
                }
                // make children parents
                parentDistArr = childDistArr;
            }


            int[] result = new int[wt];
            for (int ii = wt - 1; ii > -1; ii--) {
                result[ii] = resultEnd;
                int next = pixelArr[ii][resultEnd];
                resultEnd = next;
            }


            return result;
        }
        return null;
    }


    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        if (isRotated) {
            picArray = rotateArray(picArray);
            isRotated = false;
        }


        return findSeamHelper();
    }

    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        if (!isRotated) {
            picArray = rotateArray(picArray);
            isRotated = true;

        }

        return findSeamHelper();
    }

    private void removeSeam(int[] seam) {


        int w = picArray.length;
        int h = picArray[0].length;

        if (seam == null) {
            throw new NullPointerException("This is a null seam array");
        }
        if (seam.length != w) {
            throw new IllegalArgumentException("Wrong array length:" + seam.length + ". Length should be" +
                    w);
        }

        int prevS = -1;
        for (int s : seam) {
            if (prevS == -1) {
                prevS = s;
            } else {
                if (Math.abs(s - prevS) > 1) {
                    throw new IllegalArgumentException("Wrong seam value, disconnect");
                }
                prevS = s;
            }
            if (s < 0 || s >= h) {
                throw new IllegalArgumentException("Wrong seam value, out of bound");
            }
        }

        int[][] tempPicArr = new int[w][h - 1];

        for (int i = 0; i < w; i++) {
            int point = seam[i];
            for (int j = 0; j < h - 1; j++) {
                if (j < point) {
                    tempPicArr[i][j] = picArray[i][j];
                } else {
                    tempPicArr[i][j] = picArray[i][j + 1];
                }

            }

        }
        picArray = tempPicArr;

    }

    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if (isRotated) {
            picArray = rotateArray(picArray);
            isRotated = false;

        }
        removeSeam(seam);

        height = height - 1;

    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (!isRotated) {
            picArray = rotateArray(picArray);
            isRotated = true;
        }
        removeSeam(seam);

        width = width - 1;

    }

    public static void main(String[] args) {
        StdOut.printf(args[0]);
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d columns by %d rows\n", picture.width(), picture.height());
        picture.show();
        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Displaying horizontal seam calculated.\n");
        sc.energy(1, 0);
        sc.picture();

        int[] seam = sc.findVerticalSeam();

        double en = 0.0;
        for (int i = 0; i < seam.length; i++) {
            StdOut.println(Integer.toString(seam[i]));


            en += sc.energy(i, seam[i]);
        }
        StdOut.println(" ");
        StdOut.println(en);
        for(int jj=0; jj<sc.height(); jj++){
            for (int ii=0; ii< sc.width(); ii++) {

                StdOut.print(sc.energy(ii, jj));
                StdOut.print(" ");

            }
            StdOut.println(" ");
        }

        sc.removeVerticalSeam(seam);
        StdOut.printf("image is %d columns by %d rows\n", sc.width(), sc.height());
        sc.picture().show();
        for(int jj=0; jj<sc.height(); jj++){
            for (int ii=0; ii< sc.width(); ii++) {

                StdOut.print(sc.energy(ii, jj));
                StdOut.print(" ");

            }
            StdOut.println(" ");
        }



        seam = sc.findVerticalSeam();
        en = 0.0;
        for (int i = 0; i < seam.length; i++) {
            StdOut.printf(Integer.toString(seam[i]));
            en += sc.energy(i, seam[i]);
        }
        StdOut.println(" ");
        StdOut.println(en);

    }
}