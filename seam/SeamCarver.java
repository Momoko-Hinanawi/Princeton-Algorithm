/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver
{
    //************CORNER CASES*************
    /*
    DONE By convention, the indices x and y are integers between 0 and width − 1 and between 0 and height − 1 respectively, where width is the width of the current image and height is the height. Throw an IllegalArgumentException if either x or y is outside its prescribed range.

    DONE Throw an IllegalArgumentException if the constructor, removeVerticalSeam(), or removeHorizontalSeam() is called with a null argument.

    DONE Throw an IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with an array of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its prescribed range or two adjacent entries differ by more than 1).

    Throw an IllegalArgumentException if removeVerticalSeam() is called when the width of the picture is less than or equal to 1 or if removeHorizontalSeam() is called when the height of the picture is less than or equal to 1.*/
    private int[][] RGBArray;
    private int[][] EdgeTo;
    private double[][] DistTo;
    private double[][] Energy;
    private boolean[][] energyCalculated;
    private int width, height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if (picture == null) throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();
        RGBArray = new int[picture.width()][picture.height()];
        EdgeTo = new int[width()][height()];
        DistTo = new double[width()][height()];
        Energy = new double[width()][height()];
        energyCalculated = new boolean[width()][height()];
        for (int i = 0; i != picture.width(); ++i)
        {
            for (int j = 0; j != picture.height(); ++j)
            {
                RGBArray[i][j] = picture.getRGB(i, j);
            }
        }
    }

    private void Initialize()
    {
        for (int i = 0; i != width(); ++i)
        {
            for (int j = 0; j != height(); ++j)
            {
                DistTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
    }

    // current picture
    public Picture picture()
    {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i != picture.width(); ++i)
            for (int j = 0; j != picture.height(); ++j)
            {
                picture.setRGB(i, j, RGBArray[i][j]);
            }
        return picture;
    }

    private int R(int color)
    {
        return (color & 0xff0000) >> 16;
    }

    private int G(int color)
    {
        return (color & 0x00ff00) >> 8;
    }

    private int B(int color)
    {
        return (color & 0x0000ff);
    }

    private int R(int x, int y)
    {
        return R(RGBArray[x][y]);
    }

    private int G(int x, int y)
    {
        return G(RGBArray[x][y]);
    }

    private int B(int x, int y)
    {
        return B(RGBArray[x][y]);
    }

    // width of current picture
    public int width()
    {
        return width;
    }

    // height of current picture
    public int height()
    {
        return height;
    }


    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (x >= width() || x < 0 || y >= height() || y < 0) throw new IllegalArgumentException();
        if (energyCalculated[x][y]) return Energy[x][y];
        double result;
        if (x == width() - 1 || x == 0 || y == height() - 1 || y == 0)
            result = 1000;
        else
        {
            int rx = R(x + 1, y) - R(x - 1, y);
            int gx = G(x + 1, y) - G(x - 1, y);
            int bx = B(x + 1, y) - B(x - 1, y);
            int squaredDeltaX = rx * rx + gx * gx + bx * bx;
            int ry = R(x, y + 1) - R(x, y - 1);
            int gy = G(x, y + 1) - G(x, y - 1);
            int by = B(x, y + 1) - B(x, y - 1);
            int squaredDeltaY = ry * ry + gy * gy + by * by;
            result = Math.sqrt(squaredDeltaX + squaredDeltaY);
        }
        Energy[x][y] = result;
        energyCalculated[x][y] = true;
        return result;
    }

    private void relax(int fromx, int fromy, int x, int y, boolean isHorizontal)
    {
        double newDist = DistTo[fromx][fromy] + energy(x, y);
        if (newDist < DistTo[x][y])
        {
            DistTo[x][y] = newDist;
            if (isHorizontal)
            {
                EdgeTo[x][y] = fromy;
            } else
            {
                EdgeTo[x][y] = fromx;
            }
        }
        ;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        Initialize();
        if (height == 1)
        {
            int[] result = new int[width()];
            return result;
        }
        for (int j = 0; j != height(); ++j)
        {
            DistTo[0][j] = 1000;
        }
        for (int i = 0; i != width() - 1; ++i)
        {
            for (int j = 1; j != height() - 1; ++j)
            {
                relax(i, j, i + 1, j - 1, true);
                relax(i, j, i + 1, j, true);
                relax(i, j, i + 1, j + 1, true);
            }
        }
        int minDistPosition = 0;
        for (int j = 0; j != height(); ++j)
        {
            if (DistTo[width() - 1][j] < DistTo[width() - 1][minDistPosition])
            {
                minDistPosition = j;
            }
        }
        int[] result = new int[width()];

        for (int i = width() - 1; i >= 0; --i)
        {
            result[i] = minDistPosition;
            minDistPosition = EdgeTo[i][minDistPosition];
        }
        return result;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        if (width == 1)
        {
            int[] result = new int[height()];
            return result;
        }
        Initialize();
        for (int i = 0; i != width(); ++i)
        {
            DistTo[i][0] = 1000;
        }
        for (int j = 0; j != height() - 1; ++j)
        {
            for (int i = 1; i != width() - 1; ++i)
            {
                relax(i, j, i - 1, j + 1, false);
                relax(i, j, i, j + 1, false);
                relax(i, j, i + 1, j + 1, false);
            }
        }
        int minDistPosition = 0;
        for (int i = 0; i != width(); ++i)
        {
            if (DistTo[i][height() - 1] < DistTo[minDistPosition][height() - 1])
            {
                minDistPosition = i;
            }
        }
        int[] result = new int[height()];

        for (int i = height() - 1; i >= 0; --i)
        {
            result[i] = minDistPosition;
            minDistPosition = EdgeTo[minDistPosition][i];
        }

        return result;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        if (height() <= 1) throw new IllegalArgumentException();

        for (int i = 0; i != width(); ++i)
        {
            int kebab = seam[i]; //REMOVE KEBAB SERBIA STRONK!
            if (kebab == height() - 1) continue;
            if (kebab < 0 || kebab >= height()) throw new IllegalArgumentException();

            for (int j = kebab; j != height() - 1; ++j)
            {
                RGBArray[i][j] = RGBArray[i][j + 1];
                Energy[i][j] = Energy[i][j + 1];
            }
        }
        --height;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        if (width() <= 1) throw new IllegalArgumentException();

        for (int j = 0; j != height(); ++j)
        {
            int kebab = seam[j]; //REMOVE KEBAB SERBIA STRONK!
            if (kebab == width() - 1) continue;
            if (kebab < 0 || kebab >= width()) throw new IllegalArgumentException();
            for (int i = kebab; i != width() - 1; ++i)
            {
                RGBArray[i][j] = RGBArray[i + 1][j];
                Energy[i][j] = Energy[i + 1][j];
            }
        }
        --width;
    }

    //  unit testing (optional)
    public static void main(String[] args)
    {
        SeamCarver seamCarver = new SeamCarver(new Picture("1x8.png"));
        seamCarver.findVerticalSeam();
        StdOut.println("--------------");
        seamCarver.removeHorizontalSeam(seamCarver.findVerticalSeam());
    }

}
