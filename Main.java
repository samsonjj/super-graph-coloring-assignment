import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        takeStandardInput();

        /*ColorableGraph g = new ColorableGraph(9, new int[] {0, 2, 0, 0, 0, 0, 0, 0, 0, 0});

        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(1, 7);
        g.addEdge(2, 3);
        g.addEdge(2, 5);
        g.addEdge(2, 8);
        g.addEdge(3, 6);
        g.addEdge(3, 9);
        g.addEdge(4, 5);
        g.addEdge(4, 6);
        g.addEdge(4, 7);
        g.addEdge(5, 6);
        g.addEdge(5, 8);
        g.addEdge(6, 9);
        g.addEdge(7, 8);
        g.addEdge(7, 9);
        g.addEdge(8, 9);
        */

        /*ColorableGraph g = generateSudoku();

        int[] colors = g.initialColorGreedyByDegree();
        colors = g.fixBySwap();


        for (int i = 0; i < 81; i+=9) {
            for (int j = 0; j < 9; j++) {
                System.out.printf("%2d "+ (j%3==2 && j!=8 ? "|" : ""), colors[i + j + 1]);
            }
            System.out.println();
            if(i/9%3==2 && i/9!=8) {
                System.out.println("-----------------------------");
            }
        }

        System.out.println();

        return;
        */
    }

    public static ColorableGraph generateSudoku() {
        Scanner in = null;
        try {
            in = new Scanner(new File("sudoku.txt"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        int[] sudokuColors = new int[82];

        // set colors
        for(int i = 1; i < 82; i++) {
            sudokuColors[i] = in.nextInt();
        }

        ColorableGraph cg = new ColorableGraph(81, sudokuColors);

        for(int row = 0; row < 9; row++) {
            for(int element1 = 1; element1 <= 8; element1++) {
                for(int element2 = element1+1; element2 <= 9; element2++) {
                    cg.addEdge(element1 + row*9, element2 + row*9);
                }
            }
        }

        for(int col = 0; col < 9; col++) {
            for(int element1 = 0; element1 < 8; element1++) {
                for(int element2 = element1+1; element2 < 9; element2++) {
                    cg.addEdge(element1*9 + col + 1, element2*9 + col + 1);
                }
            }
        }

        for(int square = 0; square < 9; square++) {
            for(int element1 = 0; element1 < 8; element1++) {
                for(int element2 = element1+1; element2 < 9; element2++) {
                    int row = (square / 3) * 3;
                    int col = (square % 3) * 3;
                    System.out.println(((element1/3)*9 + (element1%3) + row * 9 + col + 1) + " " + ((element2/3)*9 + element2%3 + row*9 + col + 1));
                    if(((element1/3)*9 + (element1%3) + row * 9 + col) / 9 != ((element2/3)*9 + element2%3 + row*9 + col) / 9
                            && (((element1/3)*9 + (element1%3) + row * 9 + col)%9 != ((element2/3)*9 + element2%3 + row*9 + col)%9)) {
                        cg.addEdge((element1/3)*9 + (element1%3) + row * 9 + col + 1, (element2/3)*9 + element2%3 + row*9 + col + 1);
                    }
                }
            }
        }

        return cg;
    }

    public static void takeStandardInput() {
        int n = 0;
        int m = 0;

        Scanner in = new Scanner(System.in);

        n = in.nextInt();
        m = in.nextInt();

        ColorableGraph cg = new ColorableGraph(n);

        for(int i = 0; i < m; i++) {
            cg.addEdge(in.nextInt(), in.nextInt());
        }

        int[] colors = new int[n+1];
        for(int i = 1; i < colors.length; i++) {
            colors[i] = in.nextInt();
        }
        cg.setColors(colors);

        int[] newColors = cg.initialColorGreedyByDegree();
        newColors = cg.fixBySwap();

        for(int color : colors) {
            System.out.print(color + " ");
        }
        System.out.println();
        for(int color : newColors) {
            System.out.print(color + " ");
        }
        System.out.println();

        for(int i = 1; i < colors.length; i++) {
            if(colors[i] != newColors[i] && colors[i] != 0) {
                System.out.println("Initial color change detected, index " + i);
            }
        }

        for (int i = 0; i < 81; i+=9) {
            for (int j = 0; j < 9; j++) {
                System.out.printf("%2d "+ (j%3==2 && j!=8 ? "|" : ""), newColors[i + j + 1]);
            }
            System.out.println();
            if(i/9%3==2 && i/9!=8) {
                System.out.println("-----------------------------");
            }
        }
    }
}
