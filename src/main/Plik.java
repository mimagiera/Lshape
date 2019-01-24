package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Plik {
    static void zapisz(double[][] tab)throws FileNotFoundException
    {
        PrintWriter zapis = new PrintWriter("tablica.txt");
        for (double[] aTab : tab) {
            for (int j = 0; j < tab[0].length; j++)
                zapis.print(aTab[j] + "\t");
            zapis.println();
        }
        zapis.close();
    }
}
