package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.time.Duration;

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
        String opcion;

        do {
            System.out.println();
            System.out.println("===== MENU =====");
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar Salida");
            System.out.println("3. Reporte de ocupacion");
            System.out.println("4. Reporte financiero");
            System.out.println("0. Salir");
            opcion = app.validarDatos("Elija una opción", 1);

            switch (opcion) {
                case "1":
                    app.registrarEntrada();
                    break;
                case "2":
                    app.registrarSalida();
                    break;
                case "3":
                    app.reporteOcupacion();
                    break;
                case "4":
                    app.reporteFinanciero();
                    break;
                case "0":
                    System.out.println("Hasta pronto");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (!opcion.equals("0"));

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

        placa[celda] = validarDatos("Ingrese la placa del Vehiculo", 2);
        fechaIngreso[celda] = leerFechaHora("Ingrese la fecha y hora de ingreso");
        tipoVehiculo[celda] = tipo;

        celdasOcupadas++;
        celdasLibres--;
        System.out.println("El vehiculo quedo registrado en la celda " + (celda + 1));
    }


    public void registrarSalida() {
        int indice = buscarCeldaOcupada();
        if (indice == -1) {
            return;
        }

        String tipo = tipoVehiculo[indice];
        System.out.println("Vehiculo encontrado en la celda " + (indice + 1) + " tipo: " + tipo);

        LocalDateTime salida;
        while (true) {
            salida = leerFechaHora("Ingrese la fecha y hora de salida");

            if (!salida.isBefore(fechaIngreso[indice])) {
                break;
            }
            System.out.println("Error: la salida no puede ser anterior al ingreso ("
                    + fechaIngreso[indice].format(formateador) + ")");
        }

        long valor = liquidar(indice, salida, tipo);

        if (tipo.equals("carro")) {
            recaudoCarros += valor;
        } else {
            recaudoMotos += valor;
        }
        recaudoTotal += valor;

        tipoVehiculo[indice] = null;
        fechaIngreso[indice] = null;
        placa[indice] = null;
        celdasOcupadas--;
        celdasLibres++;
    }


    public long contarDiasCompletos(LocalDateTime ingreso, LocalDateTime salida) {
        return ChronoUnit.DAYS.between(ingreso, salida);
    }

    public long calcularHorasRestantes(LocalDateTime cursor, LocalDateTime salida) {
        long minutosRestantes = Duration.between(cursor, salida).toMinutes();
        return (minutosRestantes + 59) / 60;
    }

    public long liquidar(int indice, LocalDateTime salida, String tipo){
        LocalDateTime ingreso = fechaIngreso[indice];

        diasACobrar = contarDiasCompletos(ingreso, salida);
        LocalDateTime cursor = ingreso.plusDays(diasACobrar);
        horasACobrar = calcularHorasRestantes(cursor, salida);

        if (horasACobrar >= UMBRAL_HORAS_DIA) {
            diasACobrar++;
            horasACobrar = 0;
        }

        int tarifaDia;
        int tarifaHora;
        if (tipo.equals("moto")) {
            tarifaDia = TARIFA_DIA_MOTO;
            tarifaHora = TARIFA_HORA_MOTO;
        } else {
            tarifaDia = TARIFA_DIA_CARRO;
            tarifaHora = TARIFA_HORA_CARRO;
        }

        valorLiquidado = diasACobrar * tarifaDia + horasACobrar * tarifaHora;

        System.out.println();
        System.out.println("===== FACTURA =====");
        System.out.println("Celda: " + (indice + 1));
        System.out.println("Placa: " + placa[indice]);
        System.out.println("Tipo: " + tipo);
        System.out.println("Ingreso: " + ingreso.format(formateador));
        System.out.println("Salida: " + salida.format(formateador));
        if (diasACobrar > 0) {
            System.out.println(diasACobrar + " dia(s) x $" + tarifaDia + " = $" + (diasACobrar * tarifaDia));
        }
        if (horasACobrar > 0) {
            System.out.println(horasACobrar + " hora(s) x $" + tarifaHora + " = $" + (horasACobrar * tarifaHora));
        }
        System.out.println("TOTAL A PAGAR: $" + valorLiquidado);

        return valorLiquidado;
    }

    public void reporteOcupacion() {
        System.out.println();
        System.out.println("===== REPORTE DE OCUPACION =====");
        System.out.println("Celdas ocupadas: " + celdasOcupadas);
        System.out.println("Celdas libres: " + celdasLibres);
    }

    public void reporteFinanciero() {
        System.out.println();
        System.out.println("===== REPORTE FINANCIERO =====");
        System.out.println("Recaudo carros: $" + recaudoCarros);
        System.out.println("Recaudo motos: $" + recaudoMotos);
        System.out.println("Recaudo total: $" + recaudoTotal);
    }

    public int buscarCeldaOcupada() {
        String dato = validarDatos("Ingrese el numero de la celda o la placa del vehiculo", 2);

        if (dato.matches("\\d{1,9}")) {
            int celda = Integer.parseInt(dato);
            if (celda < 1 || celda > CAPACIDAD_TOTAL) {
                System.out.println("Celda fuera de rango (1 a " + CAPACIDAD_TOTAL + ")");
                return -1;
            }
            if (tipoVehiculo[celda - 1] == null) {
                System.out.println("La celda " + celda + " esta libre");
                return -1;
            }
            return celda - 1;
        }

        for (int i = 0; i < CAPACIDAD_TOTAL; i++) {
            if (placa[i] != null && placa[i].equalsIgnoreCase(dato)) {
                return i;
            }
        }
        System.out.println("Placa no encontrada");
        return -1;
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

    public String validarDatos(String mensaje, int tipo){
        while(true){
            System.out.print(mensaje + ": ");
            String dato = SCANNER.nextLine().trim();

            if(dato.isEmpty()){
                System.out.println("Error: Vuelva a ingresar el dato solicitado");
            }else if(tipo == 1 && !dato.matches("\\d{1,9}")){
                System.out.println("Error: Solo se permiten numeros");
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