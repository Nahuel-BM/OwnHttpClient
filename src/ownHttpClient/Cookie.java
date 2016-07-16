package ownHttpClient;

import java.io.Serializable;

/**
 * Esta clase define objetos que representan cookies del navegador.
 *
 * @author Nahuel Bustamante Murua
 * @version 0.0.01
 *
 */
public class Cookie implements Comparable<Object>, Serializable {

    private String Name, Value;

    /**
     * Constructor para la implementacion de cookies
     *
     */
    public Cookie() {
    }

    /**
     * Constructor para la implementacion de cookies
     *
     * @param Cookie El parámetro Cookie es una cadena que contiene una cookie.
     * Tiene el formato "clave=valor".
     */
    public Cookie(String Cookie) {
        Separar(Cookie);
    }

    /**
     * Método que devuelve el nombre de la clave de esta cookie.
     *
     * @return El nombre de la clave del objeto Cookie.
     */
    public String getName() {
        return Name;
    }

    /**
     * Método que setea el nombre de la clave de esta cookie.
     *
     * @param Name El parámetro Name es el nombre de la clave de esta Cookie.
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * Método que devuelve el valor actual asociado a la Cookie.
     *
     * @return El valor del objeto Cookie.
     */
    public String getValue() {
        return Value;
    }

    /**
     * Método que setea el valor de esta cookie.
     *
     * @param Value El parametro Value es el valor que almacena la cookie.
     */
    public void setValue(String Value) {
        this.Value = Value;
    }

    private void Separar(String data) {

        int finValor;
        int inicioClave = 0;
        int finClave = data.indexOf("=");
        int inicioValor = data.indexOf("=");
        try {
            finValor = data.indexOf(";");
            if (finValor == -1) {
                finValor = data.length();
            }
        } catch (Exception e) {
            finValor = data.length();
        }

        this.Name = data.substring(inicioClave, finClave);
        this.Value = data.substring(inicioValor, finValor);

    }

    @Override
    public int compareTo(Object Cookie) {

        Cookie CookNueva = (Cookie) Cookie;
        String claveLLega = "" + CookNueva.getName();
        String claveLocal = "" + this.getName();

        return (claveLocal.compareTo(claveLLega));

    }

    @Override
    public String toString() {
        return "Name:" + Name + "\tValue:" + Value;
    }
}
