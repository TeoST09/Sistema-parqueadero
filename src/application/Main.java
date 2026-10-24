package application;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {

    static int CAPACIDAD_TOTAL_MOTOS = 20;
    static int CAPACIDAD_TOTAL_CARROS = 10;
    static int CAPACIDAD_TOTAL = CAPACIDAD_TOTAL_MOTOS + CAPACIDAD_TOTAL_CARROS;
    static int TARIFA_HORA_CARRO = 4000;
    static int TARIFA_DIA_CARRO = 30000;
    static int TARIFA_HORA_MOTO = 2000;
    static int TARIFA_DIA_MOTO = 15000;
    static int UMBRAL_HORAS_DIA = 8;
    static int HORA_APERTURA = 8;
    static int HORA_CIERRE = 22;

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
        Main app = new Main();
        System.out.println("Bienvenido al sistema de parqueadero ");
    }
}
