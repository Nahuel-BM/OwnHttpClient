package ownHttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Esta clase es un manejador de objetos Cookie. Contiene una lista de Cookies y
 * define metodos para el manejo de las mismas.
 *
 * @author Nahuel Bustamante Murua
 * @version 0.0.01
 */
public class ManejadorCookies implements Serializable {

    private List<Cookie> Cookies = new ArrayList<>();

    /**
     * Constructor default.
     */
    public ManejadorCookies() {
    }

    /**
     * Metodo que devuelve la cadena armada lista para enviar al servidor.
     *
     * @return Cadena que contiene pares clave=valor de Cookies lista para
     * enviar al servidor.
     */
    public String getCookiesString() {
        String aux = "";
        Cookie Galleta;
        Iterator it = Cookies.iterator();
        int ulti = 0;

        while (it.hasNext()) {
            Galleta = (Cookie) it.next();
            if (ulti + 1 == Cookies.size()) {
                aux += Galleta.getName() + Galleta.getValue();
            } else {
                ulti++;
                aux += Galleta.getName() + Galleta.getValue() + "; ";
            }
        }

        return aux;

    }

    /**
     * Metodo que crea cookies en el contenedor.
     *
     * @param Cookie Parametro Cookie, cadena enviada por el servidor.
     */
    public void addCookie(String Cookie) {
        Cookie laNueva = new Cookie(Cookie);

        if (!Cookies.isEmpty()) {

            try {

                if (!existe(laNueva)) {
                    Cookies.add(laNueva);
                }

                if (existe(laNueva)) {
                    Reemplazar(laNueva);
                }
            } catch (Exception e) {
                System.out.println("Error.. en contenedor " + e.getMessage());
            }
        }

        if (Cookies.isEmpty()) {
            Cookies.add(laNueva);
        }

    }

    /**
     * Metodo que crea una lista de Cookie a partir de una cadena con el formato
     * nombre1=valor1,nombre2=valor2;.
     *
     * @param listOfCookies cadena con el formato nombre1=valor1,nombre2=valor2;
     */
    public void addListOfCookies(String listOfCookies) {
        String[] ArrayOfCookies = listOfCookies.split(",");
        for (String ArrayOfCookie : ArrayOfCookies) {
            this.addCookie(ArrayOfCookie);
        }
    }

    /**
     * Metodo que retorna la cantidad de Cookies actuales.
     *
     * @return Cantidad actual de Cookies.
     */
    public int getCantCookies() {
        return Cookies.size();
    }

    private boolean existe(Cookie cook) {
        boolean aux = false;
        Cookie Galleta;
        Iterator it = Cookies.iterator();

        while (it.hasNext()) {
            Galleta = (Cookie) it.next();
            if (Galleta.getName().equals(cook.getName())) {
                aux = true;
            }
        }
        return aux;
    }

    private void Reemplazar(Cookie Nueva) {
        try {
            Cookie Galleta;
            Iterator it = Cookies.iterator();

            while (it.hasNext()) {
                Galleta = (Cookie) it.next();
                if (Galleta.getName().equals(Nueva.getName())) {
                    it.remove();
                }
            }
            Cookies.add(Nueva);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Metodo que retorna la lista completa de Cookies en un objeto del tipo
     * List.
     *
     * @return Lista de Cookies en objeto del tipo List.
     */
    public List<Cookie> getCookies() {
        return Cookies;
    }

    /**
     * Metodo que setea la lista de Cookie.
     *
     * @param Cookies El parametro Cookies contiene una lista de Cookie.
     */
    public void setCookies(ArrayList<Cookie> Cookies) {
        this.Cookies = Cookies;
    }

    @Override
    public String toString() {
        return "Cookies=" + Cookies;
    }
}
