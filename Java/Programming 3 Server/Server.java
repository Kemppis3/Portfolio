package com.server;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

//Main class
public class Server {

    static MessageDatabase db = new MessageDatabase();

    //Constructor
    private Server() {
    }
    
    //Creates SSL context for the server
    private static SSLContext serverSSLContext(String file, String password) throws Exception {

        char[] passphrase = password.toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(file), passphrase);
     
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
     
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
     
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return ssl;
    }

    //Main function of the project
    public static void main(String[] args) throws Exception, FileNotFoundException {
        //create the http server to port 8001 with default logger
        try{
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
        SSLContext sslContext = serverSSLContext(args[0], args[1]);
        server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
            params.getClientAddress();
            SSLContext c = getSSLContext();
            SSLParameters sslparams = c.getDefaultSSLParameters();
            params.setSSLParameters(sslparams);
            }
        });
        
        
        HttpContext context = server.createContext("/warning", new MessageHandler(db));
        UserAuthenticator userAuthenticator = new UserAuthenticator(db);
        context.setAuthenticator(userAuthenticator);
        server.createContext("/registration", new RegistrationHandler(userAuthenticator));
        server.setExecutor(Executors.newCachedThreadPool()); 
        server.start(); 
    } 
    catch (FileNotFoundException f) {
        System.out.println("Certificate not found!");
        f.printStackTrace();
    }
    catch (Exception e){
        e.printStackTrace();
    }
    }
}