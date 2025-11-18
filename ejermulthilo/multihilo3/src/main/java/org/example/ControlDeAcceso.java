package org.example;

class SalaDeConferencias {
    private final int capacidadMaxima;
    private int personasDentro = 0;

    public SalaDeConferencias(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public synchronized boolean intentarEntrar(String empleado) {
        if (personasDentro < capacidadMaxima) {
            personasDentro++;
            System.out.println(empleado + " ha entrado. Personas dentro: " + personasDentro);
            return true;
        } else {
            System.out.println(empleado + " no puede entrar, sala llena. Personas dentro: " + personasDentro);
            return false;
        }
    }

    public synchronized void salir(String empleado) {
        personasDentro--;
        System.out.println(empleado + " ha salido. Personas dentro: " + personasDentro);
        notifyAll(); // Avisar a los que esperan
    }
}

class Empleado extends Thread {
    private final String nombre;
    private final SalaDeConferencias sala;

    public Empleado(String nombre, SalaDeConferencias sala) {
        this.nombre = nombre;
        this.sala = sala;
    }

    @Override
    public void run() {
        boolean entro = false;

        for (int intento = 1; intento <= 3 && !entro; intento++) {
            synchronized (sala) {
                entro = sala.intentarEntrar(nombre);
            }

            if (!entro) {
                if (intento < 3) {
                    try {
                        System.out.println(nombre + " espera 12 segundos antes del intento " + (intento + 1));
                        sala.wait(12000); // Espera 12 segundos o hasta que se libere un espacio
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    System.out.println(nombre + " no logró entrar después de 3 intentos.");
                }
            }
        }

        if (entro) {
            try {
                // Simular tiempo dentro de la sala (2–5 segundos)
                Thread.sleep((long) (Math.random() * 3000 + 2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronized (sala) {
                    sala.salir(nombre);
                }
            }
        }
    }
}

public class ControlDeAcceso {
    public static void main(String[] args) {
        final int CAPACIDAD = 5;
        final int EMPLEADOS = 30;

        SalaDeConferencias sala = new SalaDeConferencias(CAPACIDAD);

        for (int i = 1; i <= EMPLEADOS; i++) {
            new Empleado("Empleado " + i, sala).start();
        }
    }
}
