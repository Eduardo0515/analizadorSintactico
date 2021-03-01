/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsintactico.analisisnorecursivo;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Hugo Ruiz
 */
public class AnalisisNoRecursivo {

    private ArrayList lexemas;
    private String[][] tabla;
    private Stack pila;
    private String descripcionError = "";
    private FilaColumnaTabla filCol = new FilaColumnaTabla();

    public AnalisisNoRecursivo(ArrayList lexemas, String[][] tabla) {
        this.lexemas = lexemas;
        this.tabla = tabla;
        this.pila = new Stack();
    }

    private void inicializarPila() {
        pila.clear();
        mostrarEvolucionPila(pila);
        pila.push("$");
        mostrarEvolucionPila(pila);
        pila.push("GRAMATICAGENERAL");
    }

    public void metodoPredictivoNoRecursivo() {
        inicializarPila();
        String datoPila;
        String datoApuntado;
        int fila;
        int columna;
        int apuntado = 0;
        boolean error = false;

        do {
            datoPila = pila.lastElement().toString();
            datoApuntado = lexemas.get(apuntado).toString();
            mostrarEvolucionPila(pila);
            System.out.println(datoApuntado + " " + datoPila + " *************");
            if (filCol.isTerminal(datoPila)) {
                if (datoPila.equals("ε")) {
                    pila.pop();
                } else if (datoPila.equals(datoApuntado)) {
                    pila.pop();
                    apuntado++;
                } else {
                    System.out.println("Error 1");
                    descripcionError = "El elemento '" + datoApuntado + "' no se ha encontrado";
                    error = true;
                }
            } else {
                if (filCol.getNoTerminals().containsKey(datoPila)
                        && filCol.getTerminals().containsKey(datoApuntado)) {
                    fila = filCol.getNoTerminals().get(datoPila);
                    columna = filCol.getTerminals().get(datoApuntado);
                    if (!"null".equals(tabla[fila][columna])) {
                        pila.pop();
                        insertarProduccion(tabla[fila][columna], datoApuntado, datoPila);
                    } else {
                        System.out.println("aAAAA");
                        descripcionError = "La secuencia es incorrecta";
                        error = true;
                    }
                } else {
                    System.out.println("Dato en tabla inexistentes");
                    descripcionError = "El elemento no se reconoce";
                    error = true;
                }
            }
        } while (!pila.isEmpty() && apuntado < lexemas.size() && error == false);
        if (!error) {
            System.out.println(pila);
            pila.pop();
            mostrarEvolucionPila(pila);
        }
        evaluarPila(pila);
    }

    public void insertarProduccion(String produccion, String datoApuntado, String datoPila) {
        String[] opciones = produccion.split("\\|");
        String[] elementos;
        String encontrado = "";
        if (produccion.equals("&||")) {
            if (datoApuntado.equals("&")) {
                pila.push("&");
            } else {
                pila.push("|");
            }
        } else {
            if (opciones.length > 1) {
                for (String opcion : opciones) {
                    if (datoPila.equals("CONTENIDO")) {
                        encontrado = verificarTipoContenido(datoApuntado);
                    } else if (opcion.split(" ")[0].equals(datoApuntado) || opcion.split(" ")[0].equals(datoPila)) {
                        encontrado = opcion;
                    } else if (opcion.split(" ")[0].equals("ε")) {
                        encontrado = opcion;
                    }
                }
            } else {
                encontrado = opciones[0];
            }
            System.out.println(datoApuntado + " " + datoPila + " ECO " + encontrado);
            elementos = encontrado.split(" ");
            for (int i = elementos.length - 1; i >= 0; i--) {
                pila.push(elementos[i]);
            }
        }
    }

    public void evaluarPila(Stack pila) {
        if (pila.isEmpty()) {
            System.out.println("Correcto");
            //txtResultado.setText("La cadena es correcta");
        } else {
            System.out.println("Incorrecto");
            //txtResultado.setText("La cadena es incorrecta\n" + descripcionError);
        }
    }

    public void mostrarEvolucionPila(Stack pila) {
        //evolucionPila += pila.toString() + "\n";
        System.out.println(pila);
        //contenidoPila.setText(evolucionPila);
    }

    public String verificarTipoContenido(String elemento) {
        if (elemento.equals("}")) {
            return "ε";
        } else if (elemento.equals("int") || elemento.equals("strg")
                || elemento.equals("bool") || elemento.equals("flt")) {
            return "DECLARACIONASIG CONTENIDO";
        } else if (elemento.equals("Identificador")) {
            return "INVOCACIONFUNCION CONTENIDO";
        } else if (elemento.equals("while")) {
            return "SENTENCIAWHILE CONTENIDO";
        } else if (elemento.equals("if")) {
            return "SENTENCIAIF CONTENIDO";
        } else if (elemento.equals("return")) {
            return "ε";
        } else if (elemento.equals("inp")) {
            return "ENTRADA CONTENIDO";
        } else if (elemento.equals("out")) {
            return "IMPRIMIR CONTENIDO";
        } else {
            return "";
        }
    }

}