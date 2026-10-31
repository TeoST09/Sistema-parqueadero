<div align="center">

# Sistema de Parqueadero

**Aplicación de consola en Java para controlar el ingreso, la liquidación y la salida de carros y motos.**

<br>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![Markdown](https://img.shields.io/badge/Markdown-000000?style=for-the-badge&logo=markdown&logoColor=white)

![Interfaz](https://img.shields.io/badge/Interfaz-Consola-4D4D4D?style=flat-square)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Una_sola_clase-2E7D32?style=flat-square)
![Estructuras](https://img.shields.io/badge/Estructuras-Arreglos_paralelos-1565C0?style=flat-square)
![Estado](https://img.shields.io/badge/Estado-Completo-success?style=flat-square)

</div>

---

## Contenido

- [Descripción](#-descripción)
- [Funcionalidades](#-funcionalidades)
- [Reglas de negocio](#-reglas-de-negocio)
- [Tecnologías](#-tecnologías)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Cómo ejecutarlo](#-cómo-ejecutarlo)
- [Ejemplo de uso](#-ejemplo-de-uso)
- [Historial de commits](#-historial-de-commits)
- [Autor](#-autor)

---

## Descripción

Sistema de gestión para un parqueadero de un solo nivel. Cada vehículo se asigna a una celda disponible, se liquida al momento de salir según el tiempo de permanencia y el programa lleva el recaudo acumulado por tipo de vehículo.

La aplicación está implementada en **una sola clase** y representa las 30 celdas mediante **arreglos paralelos**. En cada posición se almacenan el tipo de vehículo, la placa y la fecha y hora de ingreso; si `tipoVehiculo` contiene `null` en esa posición significa que la celda está libre.

## Funcionalidades

| Opción | Función | Descripción |
|:------:|---------|-------------|
| `1` | **Registrar entrada** | Asigna la primera celda libre, pide tipo, placa y fecha/hora de ingreso, y controla el cupo por tipo de vehículo. |
| `2` | **Registrar salida** | Busca el vehículo por número de celda o por placa, valida la fecha de salida, liquida, suma al recaudo y libera la celda. |
| `3` | **Reporte de ocupación** | Muestra la cantidad de celdas ocupadas y libres. |
| `4` | **Reporte financiero** | Muestra el recaudo de carros, el de motos y el total general. |
| `0` | **Salir** | Cierra el programa y el `Scanner`. |

Todas las entradas se validan y se vuelven a solicitar cuando no son correctas: opción del menú, tipo de vehículo, placa, número de celda y fecha/hora.

## Reglas de negocio

### Capacidad

| Tipo | Celdas |
|------|:------:|
| Carros | 10 |
| Motos | 20 |
| **Total** | **30** |

### Tarifas

| Tipo | Por hora | Por día |
|------|---------:|--------:|
| Carro | $4.000 | $30.000 |
| Moto | $2.000 | $15.000 |

### Cálculo del valor de la estadía

1. Se cuentan los **días completos** (bloques de 24 horas) entre el ingreso y la salida.
2. Con lo que sobra se calculan las **horas restantes**, redondeando hacia arriba cualquier fracción: 61 minutos se cobran como 2 horas.
3. Si las horas restantes alcanzan las **8 horas**, se cobra un día adicional y las horas quedan en cero.
4. El total es `días × tarifa diaria + horas × tarifa por hora`.

### Fechas y horario

- Formato de fecha y hora: `yyyy-MM-dd HH:mm`.
- Horario de atención: de **08:00 a 22:00**.
- Se rechazan las fechas incorrectas que no esten en el formato.
- La salida no puede ser anterior al ingreso.

## Tecnologías

| Herramienta | Uso |
|-------------|-----|
| **Java** | Lenguaje del proyecto. |
| **`java.time`** | `LocalDateTime`, `DateTimeFormatter`, `Duration` y `ChronoUnit` para fechas y cálculo del tiempo. |
| **`Scanner`** | Única instancia para toda la entrada por consola. |
| **IntelliJ IDEA** | Entorno de desarrollo. |
| **Git y GitHub** | Control de versiones con commits granulares. |

## Estructura del proyecto

```text
Sistema-parqueadero/
├── src/
│   └── application/
│       └── Main.java
├── .gitignore
└── README.md
```

## Cómo ejecutarlo

**Requisitos:** tener instalado un JDK de Java.

```bash
# 1. Clonar el repositorio
git clone https://github.com/TeoST09/Sistema-parqueadero.git
cd Sistema-parqueadero

# 2. Compilar
javac -d out src/application/Main.java

# 3. Ejecutar
java -cp out application.Main
```

## Ejemplo de uso

```text
Bienvenido al sistema de parqueadero

===== MENU =====
1. Registrar entrada
2. Registrar Salida
3. Reporte de ocupacion
4. Reporte financiero
0. Salir
Elija una opción: 1
Escriba el tipo de vehiculo (Carro/Moto): carro
Ingrese la placa del Vehiculo: ABC123
Ingrese la fecha y hora de ingreso (yyyy-MM-dd HH:mm): 2026-10-24 21:00
El vehiculo quedo registrado en la celda 1
```

```text
===== MENU =====
Elija una opción: 2
Ingrese el numero de la celda o la placa del vehiculo: 1
Vehiculo encontrado en la celda 1 tipo: carro
Ingrese la fecha y hora de salida (yyyy-MM-dd HH:mm): 2026-10-25 22:00

===== FACTURA =====
Celda: 1
Placa: ABC123
Tipo: carro
Ingreso: 2026-10-24 21:00
Salida: 2026-10-25 22:00
1 dia(s) x $30000 = $30000
1 hora(s) x $4000 = $4000
TOTAL A PAGAR: $34000
```

## Autor

Desarrollado por [**TeoST09**](https://github.com/TeoST09).
