package org.example;

import java.util.Random;

class CuentaBancaria {
    private double saldo;
    private int clientesActivos = 0;

    public CuentaBancaria(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    // Control interno para permitir solo 2 clientes simultáneamente
    private synchronized void entrar(int idCliente) throws InterruptedException {
        while (clientesActivos >= 2) {
            wait(); // Espera mientras haya 2 clientes operando
        }
        clientesActivos++;
        System.out.println("Cliente " + idCliente + " entra a operar. Clientes activos: " + clientesActivos);
    }

    private synchronized void salir(int idCliente) {
        clientesActivos--;
        System.out.println("Cliente " + idCliente + " termina su operación. Clientes activos: " + clientesActivos);
        notifyAll(); // Libera espacio para otros clientes
    }

    public void operar(int idCliente) {
        try {
            entrar(idCliente);

            Random random = new Random();
            int operacion = random.nextInt(2); // 0 = ingresar, 1 = retirar
            double cantidad = 10 + random.nextInt(991); // 10 - 1000 €
            Thread.sleep((1 + random.nextInt(3)) * 1000); // Espera aleatoria 1-3 s

            synchronized (this) {
                if (operacion == 0) {
                    ingresar(cantidad, idCliente);
                } else {
                    retirar(cantidad, idCliente);
                }
                consultarSaldo(idCliente);
            }

            salir(idCliente);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ingresar(double cantidad, int idCliente) {
        saldo += cantidad;
        System.out.println("Cliente " + idCliente + " ingresó " + cantidad + " €. Saldo actual: " + saldo + " €");
    }

    private void retirar(double cantidad, int idCliente) {
        if (saldo >= cantidad) {
            saldo -= cantidad;
            System.out.println("Cliente " + idCliente + " retiró " + cantidad + " €. Saldo actual: " + saldo + " €");
        } else {
            System.out.println("Cliente " + idCliente + " intentó retirar " + cantidad + " € pero no hay saldo suficiente. Saldo actual: " + saldo + " €");
        }
    }

    private void consultarSaldo(int idCliente) {
        System.out.println("Cliente " + idCliente + " consultó el saldo: " + saldo + " €");
    }
}

class Cliente extends Thread {
    private static final int NUM_OPERACIONES = 3;
    private CuentaBancaria cuenta;
    private int idCliente;

    public Cliente(CuentaBancaria cuenta, int idCliente) {
        this.cuenta = cuenta;
        this.idCliente = idCliente;
    }

    @Override
    public void run() {
        for (int i = 0; i < NUM_OPERACIONES; i++) {
            cuenta.operar(idCliente);
        }
        System.out.println("Cliente " + idCliente + " ha terminado todas sus operaciones.");
    }
}

public class SimulacionBancoSinSemaforos {
    public static void main(String[] args) {
        CuentaBancaria cuenta = new CuentaBancaria(1000);

        for (int i = 1; i <= 5; i++) {
            new Cliente(cuenta, i).start();
        }
    }
}
