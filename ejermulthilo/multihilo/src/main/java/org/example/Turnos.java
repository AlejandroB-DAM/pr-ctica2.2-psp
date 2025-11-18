// Esta clase se encarga de coordinar los turnos entre los hilos.
// Es la que asegura que A, B y C impriman en el orden correcto sin pisarse.

package org.example;

public class Turnos {
    // Variable que indica de quién es el turno actual: 0 -> A, 1 -> B, 2 -> C
    private int turno = 0;

    // Método sincronizado para que cada hilo espere hasta que le toque su turno
    public synchronized void esperarTurno(int miTurno) throws InterruptedException {
        // Mientras no sea su turno, el hilo se queda esperando (libera el monitor con wait)
        while (turno != miTurno) {
            wait();
        }
        // Cuando el turno coincide, el hilo continúa su ejecución
    }

    // Método sincronizado que pasa el turno al siguiente hilo
    public synchronized void siguiente() {
        // Incrementa el turno y vuelve a 0 cuando llega a 3 (ciclo A -> B -> C -> A)
        turno = (turno + 1) % 3;

        // Despierta a todos los hilos que estaban esperando, para que el siguiente compruebe si le toca
        notifyAll();
    }
}
