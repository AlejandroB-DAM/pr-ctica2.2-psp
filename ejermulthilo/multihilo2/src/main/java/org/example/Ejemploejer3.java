package org.example;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

class Buffer {
    private int capacity = 10;     // Capacidad máxima del búfer
    private int count = 0;         // Elementos actuales en el búfer

    // Método para producir un elemento
    public synchronized void producir() throws InterruptedException {
        while (count == capacity) {
            System.out.println("El búfer está lleno, esperando...");
            // Espera hasta que haya espacio en el búfer
            wait();
        }

        count++;
        System.out.println("Producido 1 elemento. Elementos en el búfer: " + count);
        // Despierta a todos los hilos que están esperando, consumidores / productores
        // Esto podría incluir al consumidor que podría empezar a consumir
        notifyAll();
    }

    // Método para consumir un elemento
    public synchronized void consumir() throws InterruptedException {
        while (count == 0) {
            System.out.println("El búfer está vacío, esperando...");
            wait();   // Espera hasta que haya elementos para consumir
        }

        count--;
        System.out.println("Consumido 1 elemento. Elementos en el búfer: " + count);
        // Despierta a todos los hilos que están esperando, consumidores / productores
        // Esto podría incluir al productor que podría empezar a producir
        notifyAll();
    }
}

public class Ejemploejer3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        // Hilo productor
        Thread productor = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.producir();
                    Thread.sleep(500);   // Simula el tiempo de producción
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Hilo consumidor
        Thread consumidor = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    buffer.consumir();
                    Thread.sleep(1000);  // Simula el tiempo de consumo
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        productor.start();
        consumidor.start();
    }

}
