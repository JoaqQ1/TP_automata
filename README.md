# AFND a AFD mínimo

Trabajo Práctico Integrador de la materia **Fundamentos Teoricos de la Informática**.

- **Fecha de entrega:** 28 de septiembre
- **Modalidad:** individual o en grupos de hasta dos integrantes
- **Integrantes:** Joaquín Quiroga
- **Contacto:** quirogajuaco7@gmail.com

## Objetivo

El trabajo propone procesar la descripción de un autómata finito, convertir un AFND en un AFD equivalente, minimizar el AFD resultante y permitir validar cadenas. La entrega completa también contempla un informe técnico y casos de prueba.

## Estado actual

El proyecto está implementado en Java 17 y actualmente incluye:

- Un modelo de autómata que representa estados, alfabeto y transiciones a conjuntos de destinos.
- Evaluación de cadenas para autómatas no deterministas, incluidas transiciones épsilon.
- Lectura de descripciones en JSON y XML mediante Jackson.
- Pruebas unitarias del modelo y de la lectura de archivos.

La conversión AFND→AFD, la minimización y la escritura del autómata resultante todavía no están implementadas. La aplicación de consola carga un autómata y evalúa la cadena fija `abaaa`; todavía no permite ingresar una cadena por argumento ni valida automáticamente equivalencia o reducción de estados.

## Requisitos

- JDK 17 o posterior
- Apache Maven

## Compilar y probar

Desde la raíz del proyecto:

```bash
mvn test
```

Para compilar sin ejecutar las pruebas:

```bash
mvn compile
```

La clase de entrada es `ar.edu.unpsjb.informatica.automata.Main`. Puede ejecutarse desde el IDE. Acepta una ruta de entrada opcional como primer argumento; si no se proporciona, usa `input.automaton.path` de `src/main/resources/config.properties` y, si esa configuración no está disponible, `data/input/automata.json`.

## Formato de entrada

El archivo describe el alfabeto, los estados, el estado inicial, los estados de aceptación y las transiciones. Para cada transición, `to` contiene uno o más destinos. El formato JSON de ejemplo (`data/input/automata.json`) es:

```json
{
	"alphabet": ["a", "b"],
	"states": ["q0", "q1"],
	"initialState": "q0",
	"acceptingStates": ["q1"],
	"transitions": [
		{ "from": "q0", "symbol": "a", "to": ["q0", "q1"] },
		{ "from": "q1", "symbol": "b", "to": ["q0"] }
	]
}
```

También se incluye un ejemplo XML en `data/input/automata.xml`. La extensión `.json` o `.xml` determina el formato utilizado al leer el archivo. Las transiciones épsilon se representan en el modelo con el símbolo `ε`.

## Estructura del proyecto

- `automata/model`: representación del autómata y sus estados.
- `io`: lectura de archivos, conversión entre DTO y datos de entrada, y manejo de errores.
- `data/input`: autómatas de ejemplo.
- `data/output`: reservado para resultados; actualmente no se genera una salida minimizada.
- `src/test`: pruebas automatizadas.

## Trabajo pendiente para completar la consigna

1. Implementar la construcción por subconjuntos, incluyendo el tratamiento de cierres-ε.
2. Implementar un algoritmo de minimización para el AFD obtenido.
3. Serializar el autómata mínimo en JSON o XML.
4. Verificar equivalencia del lenguaje y comparar la cantidad de estados antes y después.
5. Incorporar casos de prueba pequeños y medianos, resultados comparativos y el informe técnico solicitado.

## Informe técnico

El documento PDF de entrega debe incluir portada, introducción y fundamentos teóricos; metodología, pseudocódigo o diagramas y decisiones de diseño; casos de prueba explicados; comparación de estados y transiciones; verificación de equivalencia; conclusiones y referencias bibliográficas.
