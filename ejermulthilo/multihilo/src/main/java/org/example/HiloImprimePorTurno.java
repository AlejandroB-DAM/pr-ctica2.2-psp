// En esta clase he creado un ejemplo de hilos que imprimen números de forma ordenada por turnos.

package org.example;

public class HiloImprimePorTurno extends Thread {
    private Turnos turno;   // Objeto compartido que controla de quién es el turno
    private int miTurno;    // Número que identifica el turno de este hilo (0, 1 o 2)
    private int inicio;     // Primer número que imprimirá este hilo
    private int paso;       // Incremento entre números (aunque en este caso siempre vale 3)

    // Constructor: recibe el objeto Turnos compartido y los parámetros de configuración del hilo
    public HiloImprimePorTurno(Turnos turno, int miTurno, int inicio, int paso) {
        this.turno = turno;
        this.miTurno = miTurno;
        this.inicio = inicio;
        this.paso = paso;
    }

    // Método run(): aquí va la lógica que se ejecuta al arrancar el hilo
    public void run() {
        // El hilo imprime números desde 'inicio' hasta 10, aumentando de 3 en 3
        for (int i = inicio; i <= 10; i += 3) {
            try {
                // Espera hasta que sea su turno de imprimir
                turno.esperarTurno(miTurno);

                // Cuando le toca, muestra su nombre y el número correspondiente
                System.out.println(Thread.currentThread().getName() + " imprime: " + i);

                // Una vez ha impreso, pasa el turno al siguiente hilo
                turno.siguiente();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // En el main creo tres hilos, uno para cada turno, y los arranco
    public static void main(String[] args) throws InterruptedException {
        Turnos turno = new Turnos(); // Objeto que coordina los turnos

        // Cada hilo tiene su propio número de turno y secuencia de inicio
        HiloImprimePorTurno a = new HiloImprimePorTurno(turno, 0, 1, 3);
        HiloImprimePorTurno b = new HiloImprimePorTurno(turno, 1, 2, 3);
        HiloImprimePorTurno c = new HiloImprimePorTurno(turno, 2, 3, 3);

        // Les pongo nombres para identificar quién imprime
        a.setName("A");
        b.setName("B");
        c.setName("C");

        // Los inicio en paralelo
        a.start();
        b.start();
        c.start();

        // Espero a que todos acaben antes de imprimir el mensaje final
        a.join();
        b.join();
        c.join();

        // Mensaje final cuando los tres hilos han terminado
        System.out.println("Fin impresión ordenada.");
    }
}
