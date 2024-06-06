package org.example;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
public class Main {
    private static final int TAMANO_MATRIS = 10;
    private static final int HILOS = 2;
    private static final int[][] matris = new int[TAMANO_MATRIS][TAMANO_MATRIS];
    private static final int TARGET = 2; // Número a buscar
    private static final AtomicBoolean resultado = new AtomicBoolean(false);

    public static void main(String[] args) {


        Random random = new Random();
        for (int i = 0; i < TAMANO_MATRIS; i++) {
            for (int j = 0; j < TAMANO_MATRIS; j++) {
                matris[i][j] = random.nextInt(10);
            }
        }

        long startTime = System.currentTimeMillis();
        boolean sequentialResult = sequentialSearch();
        long endTime = System.currentTimeMillis();
        System.out.println("Imprimir Matriz");
        printMatrix();

        System.out.println("Resultado búsqueda secuencial: " + sequentialResult + " objetivo: "+TARGET);
        System.out.println("Tiempo búsqueda secuencial: " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        parallelSearch();
        endTime = System.currentTimeMillis();
        System.out.println("Imprimir Matriz");
        printMatrix();

        System.out.println("Resultado búsqueda paralela: " + resultado.get() + " objetivo: "+TARGET);
        System.out.println("Tiempo búsqueda paralela: " + (endTime - startTime) + "ms");
    }


        private static boolean sequentialSearch() {
            for (int i = 0; i < TAMANO_MATRIS; i++) {
                for (int j = 0; j < TAMANO_MATRIS; j++) {
                    if (matris[i][j] == TARGET) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static void parallelSearch(){
            Thread[] threads = new Thread[HILOS];
            for (int i = 0; i < HILOS; i++) {
                final int threadIndex = i;
                threads[i] = new Thread(() -> {
                    int fromRow = threadIndex * TAMANO_MATRIS / HILOS;
                    int toRow = (threadIndex + 1) * TAMANO_MATRIS / HILOS;
                    for (int row = fromRow; row < toRow; row++) {
                        for (int col = 0; col < TAMANO_MATRIS; col++) {
                            if (matris[row][col] == TARGET) {
                                resultado.set(true);
                                return;
                            }
                        }
                    }
                });
                threads[i].start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void printMatrix(){
            for (int i = 0; i < TAMANO_MATRIS; i++) {
                for (int j = 0; j < TAMANO_MATRIS; j++) {
                    System.out.print(matris[i][j] + " ");
                }
                System.out.println();
            }
        }



}