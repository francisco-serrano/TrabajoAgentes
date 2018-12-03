package agentes.comportamientos;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MiBehaviour extends Behaviour {

    private boolean termine = false;
    private int event = 1;

    @Override
    public void action() {
        System.out.printf("El agente %s está activo\n", myAgent.getAID().getName());

        ACLMessage msgReceived = myAgent.receive(MessageTemplate.MatchAll());
        if (msgReceived != null) {

            if (msgReceived.getPerformative() == ACLMessage.PROPOSE) {
                System.out.println(myAgent.getName() + ": recibió PROPOSE");

                if (Math.random() > 0.5d) {
                    ACLMessage msgSend = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    msgSend.addReceiver(msgReceived.getSender());
                    myAgent.send(msgSend);
                    System.out.println(myAgent.getName() + ": envía ACCEPT");
                } else {
                    ACLMessage msgSend = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    msgSend.addReceiver(msgReceived.getSender());
                    myAgent.send(msgSend);
                    System.out.println(myAgent.getName() + ": envía REJECT");
                }
            }

            if (msgReceived.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                System.out.println(myAgent.getName() + ": recibió ACCEPT");
                termine = true;
                event = 0;
            }

            if (msgReceived.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                System.out.println(myAgent.getName() + ": recibió REJECT");

                ACLMessage msgSend = new ACLMessage(ACLMessage.PROPOSE);
                msgSend.addReceiver(msgReceived.getSender());
                myAgent.send(msgSend);
                System.out.println(myAgent.getName() + ": envía PROPOSE");

                event = 1;
            }

        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return termine;
    }

    @Override
    public int onEnd() {
        System.out.println(myAgent.getName() + ": finalizó la ejecución");
        return event;
    }

    @Override
    public void reset() {
        termine = false;
    }
}
