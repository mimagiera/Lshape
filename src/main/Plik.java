package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Plik {
    static void zapisz(double[][] tab)throws FileNotFoundException
    {
        PrintWriter zapis = new PrintWriter("tablica.txt");
        for (int i =0;i<tab.length;i++) {
            for (int j = 0; j < tab[0].length; j++) {
                zapis.print(tab[i][j] + "\t");
            }
            zapis.println();
        }
        zapis.close();
    }
}
