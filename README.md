Diseña un programa en Java que simule una panadería con varios clientes y un panadero.

El panadero prepara panes y los coloca en una bandeja con capacidad máxima de 5 panes.

Los clientes compran panes, tomando uno cada vez de la bandeja.

Si la bandeja está vacía, los clientes deben esperar hasta que el panadero produzca más.

Si la bandeja está llena, el panadero debe esperar a que los clientes tomen panes antes de continuar.

El programa debe utilizar threads para representar al panadero y a los clientes, y debe emplear synchronization (por ejemplo, wait() y notifyAll()) para coordinar correctamente el acceso a la bandeja.
