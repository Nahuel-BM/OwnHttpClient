package ownHttpClient;

import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Esta clase es una implementacion de un cliente HTTP/HTTPS con manejo de
 * cookies de forma transparente.
 *
 * @author Nahuel Bustamante Murua
 * @version 0.0.01
 *
 */
public class OwnHttpClient implements Serializable {

    private URL url;
    private String UltimaRedireccion;
    private String metodoEnvio;
    private final String agent = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:11.0) Gecko/20100101 Firefox/11.0";
    private transient String Response = "";
    private transient String Request;
    private ManejadorCookies ManejadorCookies = new ManejadorCookies();
    private boolean debug = true;

    /**
     * Default Constructor.
     */
    public OwnHttpClient() {
        this.ManejadorCookies = new ManejadorCookies();
        this.metodoEnvio = "GET";
    }

    /**
     * Este metodo se utiliza para añadir campos de peticion tipo POST.
     *
     * @param propiedad Nombre del campo.
     * @param valor Valor del campo.
     */
    public void add(String propiedad, String valor) {

        if (Request == null) {
            Request = "";
        }

        try {
            if (Request.length() > 0) {
                Request += "&" + URLEncoder.encode(propiedad, "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8");
            } else {
                Request += URLEncoder.encode(propiedad, "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8");
            }
        } catch (Exception e) {
            addLog("Error en add");
            addLog("LLega: " + propiedad + "=" + valor);
            addLog(e.getLocalizedMessage());
        }
    }

    /**
     * Metodo encargado de enviar la peticion al servidor, ya sea tipo POST,
     * GET, http o https.
     */
    public void submit() {

        if (this.url != null) {

            this.Response = "";
            if (this.Request == null) {
                this.Request = "";
            }
            try {

                if (this.url.getProtocol().equalsIgnoreCase("http")) {
                    this.manejarRedireccion(false);
                } else {
                    this.manejarRedireccion(true);
                }

            } catch (Exception e) {
                addLog("Error en envio..");
                addLog(e.getLocalizedMessage());
            }
        }
    }

    private void manejarRedireccion(boolean isHttps) throws IOException {
        String Lugar = null;
        HttpURLConnection Conexion;

        if (isHttps) {
            Conexion = (HttpsURLConnection) this.url.openConnection();
        } else {
            Conexion = (HttpURLConnection) this.url.openConnection();
        }

        Conexion.setInstanceFollowRedirects(false);
        Conexion.addRequestProperty("User-Agent", this.agent);
        Conexion.addRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*");
        Conexion.addRequestProperty("Accept-Language", "es-AR");

        if (this.ManejadorCookies.getCantCookies() >= 1) {
            Conexion.addRequestProperty("Cookie", this.ManejadorCookies.getCookiesString());
        }

        if ("post".equalsIgnoreCase(this.metodoEnvio) && (this.Request != null || !this.Request.equals(""))) {
            Conexion.setDoOutput(true);
            try (OutputStreamWriter wr = new OutputStreamWriter(Conexion.getOutputStream())) {
                wr.write(this.Request);
            }

        } else {
            Conexion.setRequestMethod("GET");
        }

        BufferedReader rd = new BufferedReader(new InputStreamReader(Conexion.getInputStream()));
        String linea;

        while ((linea = rd.readLine()) != null) {
            this.Response += linea + "\n";
        }
        
        rd.close();

        for (int i = 0; i < Conexion.getHeaderFields().size(); i++) {

            if ("Set-Cookie".equals(Conexion.getHeaderFieldKey(i))) {
                this.ManejadorCookies.addCookie(Conexion.getHeaderField(i));
            }
            if ("Location".equals(Conexion.getHeaderFieldKey(i))) {
                Lugar = Conexion.getHeaderField(i);

            }

        }

        if (Lugar != null && Lugar.contains("http")) {
            try {
                this.url = new URL(Lugar);
                this.UltimaRedireccion = Lugar;
                this.Request = "";
                submit();

            } catch (MalformedURLException e) {
                this.addLog(e.getLocalizedMessage());

            }
        } else {

        }

        this.Request = null;
    }

    /**
     * Metodo que retorna el User-Agent del cliente http/https.
     *
     * @return Retorna el User-Agent del cliente http / https.
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Metodo que retorna el metodo usado por el cliente http / https.
     *
     * @return Retorna el metodo de envio (POST o GET).
     */
    public String getMetodoEnvio() {
        return metodoEnvio;
    }

    /**
     * Metodo que retorna la URL actual.
     *
     * @return Url Actual.
     */
    public String getUrl() {
        return url.toString();
    }

    /**
     * Metodo que setea la url actual.
     *
     * @param url Direccion para realizar la peticion.
     */
    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            this.addLog(ex.getLocalizedMessage());
        }
    }

    /**
     * Metodo que retorna la ultima redireccion.
     *
     * @return Ultima redireccion.
     */
    public String getUltimaRedireccion() {
        return UltimaRedireccion;
    }

    /**
     * Metodo que retorna el objeto ManejadorCookies actual.
     *
     * @return objeto ManejadorCookies actual.
     */
    public ManejadorCookies getManejadorCookies() {
        return ManejadorCookies;
    }

    private void addLog(String Log) {
        if (debug) {
            String sFichero = "httpClient.err";
            Calendar calendario = Calendar.getInstance();
            int hora, minutos, segundos;
            try {
                hora = calendario.get(Calendar.HOUR_OF_DAY);
                minutos = calendario.get(Calendar.MINUTE);
                segundos = calendario.get(Calendar.SECOND);

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(sFichero, true))) {
                    bw.write("[" + hora + ":" + minutos + ":" + segundos + "]\t" + Log);
                    bw.newLine();
                }
            } catch (IOException ioe) {
                addLog(ioe.getLocalizedMessage());

            }
        }
    }

    /**
     * Metodo que retorna el contenido POST.
     *
     * @return Contenido POST.
     */
    public String getRequest() {
        return Request;
    }

    /**
     * Metodo que retorna la respuesta del servidor.
     *
     * @return String con la respuesta del servidor.
     */
    public String getResponse() {
        return Response;
    }

    /**
     * Metodo que setea el objeto ManejadorCookies.
     *
     * @param ManejadorCookies
     */
    public void setManejadorCookies(ManejadorCookies ManejadorCookies) {
        this.ManejadorCookies = ManejadorCookies;
    }

    /**
     * Metodo que setea el metodo de envio (GET o POST).
     *
     * @param metodoEnvio String metodoEnvio ("POST" o "GET").
     */
    public void setMetodoEnvio(String metodoEnvio) {
        this.metodoEnvio = metodoEnvio;
    }

    /**
     * Metodo que retorna si el objeto esta en modo debug.
     *
     * @return boolean que representa el estado de debug.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Metodo que setea el valor debug.
     *
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

}
