package main;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.lang.Math.*;

public class Main {

    private static final int N = 8;

    //kolejne punkty Ek
    private static int[][] EK = new int[][]{{4, 5, 2, 1}, {5, 6, 3, 2}, {7, 8, 6, 5}};

    // Srodkowe punkty kolejnych krawedzi (od lewej do prawej)
    private static final double[][][] CP = new double[][][]{{{-1, 0.5}, {-0.5, 1}}, {{0.5, 1}, {1, 0.5}}, {{1, -0.5}, {0.5, -1}}};

    //Pochodne po x kolejnych fi
    private static final double[] DX = new double[]{-0.5,0.5,0.5,-0.5};

    //Pochodne po y kolejnych fi
    private static final double[] DY = new double[]{-0.5,-0.5,0.5,0.5};

    //Punkty b1 b2 kolejnych ek
    private static final double[][] B1B2 = new double[][]{{-1,0},{0,0},{0,-1}};

    private static double[] solved;

    public static void main(String[] args) {

        //B(1:8,1:8)=0 (zerowanie macierzy)
        double[][] B = new double[N][];
        for(int i=0; i<N; i++) {
            B[i] = new double[N];
        }

        //Zmniejszenie o 1 numerow wezlow
        for(int i =0;i<EK.length;i++)
        {
            for(int j = 0;j<EK[0].length;j++)
            {
                EK[i][j]-=1;
            }
        }

        //L(1:8)=0(zerowanie wektora prawej strony)
        double[] L = new double[N];

        int i1,j1;
        double derX,derY;

        //Petla po elementach Ek
        for(int k=0; k<EK.length; k++)
        {
            //petla po funkcjach fi elementu Ek
            for(int i =0;i<EK[k].length;i++)
            {
                i1=EK[k][i];
                for(double[] point : CP[k])
                {
                    L[i1]+=functionG(point[0],point[1])*functionFi(point[0],point[1],i,k);
                }
                for(int j =0;j<EK[k].length;j++)
                {
                    j1=EK[k][j];
                    derX=derByX(i)*derByX(j);
                    derY=derByY(i)*derByY(j);
                    B[i1][j1]+=derX+derY;
                }
            }
        }

        //zerowanie wierszy 4,5 i 7 macierzy
        for(int i=0;i<N;i++)
        {
            B[3][i] = B[4][i] = B[6][i] = 0;
        }

        //zerowanie wiersza 4,5 i 7 wektora prawej strony
        L[3]=L[4]=L[6]=0;

        //1 na przekątnej w wierszu 4,5,7
        B[3][3]=B[4][4]=B[6][6]=1;

        //printArray(B);
        //System.out.println(Arrays.toString(L));
        solved = GaussianElimination.lsolve(B,L);
        //System.out.println(Arrays.toString(solved));
        double[][] result = calculate(100);
        printArray(result);
        try {
            Plik.zapisz(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double derByX(int i) {
        if(i>=0 && i<=4)
            return DX[i];
        throw new IllegalArgumentException("Fi function "+i+1+ " not found");
    }
    private static double derByY(int i) {
        if(i>=0 && i<=4)
            return DY[i];
        throw new IllegalArgumentException("Fi function "+i+1+ " not found");
    }

    private static double functionFi(double x, double y, int i, int k) {
        double b1,b2;
        double returned;
        b1=B1B2[k][0];
        b2=B1B2[k][1];
        switch (i+1)
        {
            case 1:
                returned = (1-(x-b1))*(1-(y-b2));
                break;
            case 2:
                returned = (x-b1)*(1-(y-b2));
                break;
            case 3:
                returned = (x-b1)*(y-b2);
                break;
            case 4:
                returned = (1-(x-b1))*(y-b2);
                break;
                default:
                    throw new IllegalArgumentException("Fi function "+i+1+ " not found");
        }
        return returned;
    }

    private static double functionG(double x, double y)
    {
        double angle = atan(y/x);
        double r = sqrt(x*x+y*y);
        return pow(r,2./3.)*pow(Math.sin(angle+PI/2),2./3.);
    }

    private static double functionU(double x, double y)
    {
        double value=0.0;

        value+=solved[0]*functionFi(x,y,3,0);
        value+=solved[1]*(functionFi(x,y,2,0)+functionFi(x,y,3,1));
        value+=solved[2]*functionFi(x,y,2,1);
        value+=solved[5]*(functionFi(x,y,1,1)+functionFi(x,y,2,2));
        value+=solved[7]*functionFi(x,y,1,2);

        return value;
    }

    private static void printArray(double[][] array)
    {
        for (double[] row : array) {
            for (Double column : row) {
                System.out.print(fixedLengthString(column.toString()) + " ");
            }
            System.out.println();
        }
    }
    private static double[][] calculate(int width)
    {
        double[][] result = new double[width+1][width+1];
        double x;
        double y;
        double diffeerence = 2.0/width;
        for(int i =0;i<=width;i++)
        {
            for(int j =0;j<=width;j++)
            {
                x = -1 + diffeerence * j;
                y=(-1+diffeerence*i)*-1;
                result[i][j] = functionU(x,y);
                //System.out.print("("+fixedLengthString(Double.toString(x))+", "+fixedLengthString(Double.toString(y))+")");
            }
            //System.out.println();
        }
        //zerowanie lewej dolnej cwiartki
        for(int i = width/2;i<=width;i++)
        {
            for(int j =0;j<=width/2;j++)
            {
                result[i][j]=0;
            }
        }
        return result;
    }
    private static String fixedLengthString(String string) {
        int length=6;
        if(string.length()> length)
        {
            return string.substring(0, length);
        }
        else
        return String.format("%1$"+ length + "s", string);
    }
}
