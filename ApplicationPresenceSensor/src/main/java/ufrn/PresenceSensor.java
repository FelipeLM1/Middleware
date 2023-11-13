package ufrn;

import ufrn.annotations.mqtt.PublishMapping;
import ufrn.start.MiddlewareApplication;

import java.io.IOException;
import java.util.Random;


/**
 * Representa o sensor de presença de um ambiente.
 * Periodicamente envia informações sobre presença ou não de pessoas
 * no ambiente para o Broker.
 *  */
public class PresenceSensor {
    /** Identificação deste nó. */
    private static final String IDENTIFICACAO = "PresenceSensor";

    /** Controla as requisições feitas ao servidor (<i>broker</i>). */
    //private static Requestor requestor = null;

    public static void main(String[] args) {

        MiddlewareApplication.run("src/main/resources/applicationMQTT.properties");

        /*
        try {
            System.out.println("Conectando-se...");

            requestor = new Requestor(IDENTIFICACAO);

            int previousPresence = obterPresenca();
            int presence = previousPresence;

            //publicação inicial
            requestor.publicar(ObjectIDs.TOPICO_PRESENCA, Integer.toString(presence), true);

            while (true) {
                Thread.sleep(1000);

                presence = obterPresenca();

                //Só publica o valor atual da presença se for diferente da leitura anterior
                if (presence != previousPresence)
                    requestor.publicar(ObjectIDs.TOPICO_PRESENCA, Integer.toString(presence), true);

                previousPresence = presence;
            }

        } catch (Exception e) {
            System.out.println("Erro! Desconectando...");
            e.printStackTrace();

            if (requestor != null)
                requestor.encerrar();
        }
        */
    }

    /** Gera valores entre 0 e 1 */
    private static int obterPresenca() {
        Random rn = new Random();
        return rn.nextInt(2);
    }

    @PublishMapping("/PRESENCE")
    private static void publicarPresence(String valor){

    }
}






