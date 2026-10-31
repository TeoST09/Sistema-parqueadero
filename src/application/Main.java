
package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    static Main app = new Main();

    static final int CAPACIDAD_TOTAL_MOTOS = 20;
    static final int CAPACIDAD_TOTAL_CARROS = 10;
    static final int CAPACIDAD_TOTAL = CAPACIDAD_TOTAL_MOTOS + CAPACIDAD_TOTAL_CARROS;
    static final int TARIFA_HORA_CARRO = 4000;
    static final int TARIFA_DIA_CARRO = 30000;
    static final int TARIFA_HORA_MOTO = 2000;
    static final int TARIFA_DIA_MOTO = 15000;
    static final int UMBRAL_HORAS_DIA = 8;
    static final int HORA_APERTURA = 8;
    static final int HORA_CIERRE = 22;

    static int celdasOcupadas = 0;
    static int celdasLibres = CAPACIDAD_TOTAL;

    static String[] tipoVehiculo = new String[CAPACIDAD_TOTAL];
    static LocalDateTime[] fechaIngreso = new LocalDateTime[CAPACIDAD_TOTAL];
    static String[] placa = new String[CAPACIDAD_TOTAL];

    static long diasACobrar = 0;
    static long horasACobrar = 0;
    static long valorLiquidado = 0;

    static long recaudoCarros = 0;
    static long recaudoMotos = 0;
    static long recaudoTotal = 0;

    static Scanner SCANNER = new Scanner(System.in);
    static DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        System.out.println("Bienvenido al sistema de parqueadero");
        int opcion;

        do {
            System.out.println();
            System.out.println("===== MENU =====");
            System.out.println("1. Registrar entrada");
            System.out.println("0. Salir");
            System.out.print("Elija una opcion: ");
            opcion = SCANNER.nextInt();
            SCANNER.nextLine();

            switch (opcion) {
                case 1:
                    app.registrarEntrada();
                    break;
                case 0:
                    System.out.println("Hasta pronto");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opcion != 0);

        SCANNER.close();
    }

    public void registrarEntrada() {
        String tipo = validarTipoVehiculo();
        int celda = celdaDisponible();

        if (celda == -1) {
            System.out.println("El parqueadero esta lleno");
            return;
        }
        if (!celdasVehiculo(tipo)) {
            System.out.println("El parqueadero esta lleno para " + tipo);
            return;
        }

        placa[celda] = validarDatos("Ingrese la placa del Vehiculo");
        fechaIngreso[celda] = leerFechaHora("Ingrese la fecha y hora de ingreso");
        tipoVehiculo[celda] = tipo;

        celdasOcupadas++;
        celdasLibres--;
        System.out.println("El vehiculo quedo registrado en la celda " + (celda + 1));
    }

    public int celdaDisponible() {
        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (tipoVehiculo[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public String validarTipoVehiculo() {
        while (true) {
            System.out.print("Escriba el tipo de vehiculo (Carro/Moto): ");
            String dato = SCANNER.nextLine().trim();

            if (dato.equalsIgnoreCase("moto")) {
                return "moto";
            } else if (dato.equalsIgnoreCase("carro")) {
                return "carro";
            }
            System.out.println("Ingrese un tipo valido");
        }
    }

    public String validarDatos(String mensaje){
        while(true){
            System.out.print(mensaje + ": ");
            String dato = SCANNER.nextLine().trim();
            if(dato.isEmpty()){
                System.out.println("Ingrese una placa valida");
            }else{
                return dato;
            }
        }
    }

    public Boolean celdasVehiculo(String tipo){
        int contador = 0;
        for(int i = 0; i < CAPACIDAD_TOTAL; i++)
            if (tipo.equals("moto")) {
                if ("moto".equals(tipoVehiculo[i])) {
                    contador++;
                }
            } else {
                if ("carro".equals(tipoVehiculo[i])) contador++;
            }
        return comprobarCeldasVehiculos(tipo, contador);
    }


    public Boolean comprobarCeldasVehiculos(String tipo,int contador){
        if(tipo.equals("moto")){
            if(contador >= CAPACIDAD_TOTAL_MOTOS){
                return false;
            }else{
                return true;
            }
        }else{
            if(contador >= CAPACIDAD_TOTAL_CARROS){
                return false;
            }else{
                return true;
            }
        }
    }

    public boolean esFechaHoraValida(String texto){
        try {
            LocalDateTime fecha = LocalDateTime.parse(texto, formateador);

            if (!fecha.format(formateador).equals(texto)) {
                return false;
            }

            int hora = fecha.getHour();
            if (hora < HORA_APERTURA || hora > HORA_CIERRE) {
                return false;
            }
            if (hora == HORA_CIERRE && fecha.getMinute() > 0) {
                return false;
            }

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (yyyy-MM-dd HH:mm): ");
            String entrada = SCANNER.nextLine().trim();

            if (esFechaHoraValida(entrada)) {
                return LocalDateTime.parse(entrada, formateador);
            }
            System.out.println("Fecha u hora invalidas. Use el formato indicado, una fecha que exista y el horario 08:00-22:00.");
        }
    }

}