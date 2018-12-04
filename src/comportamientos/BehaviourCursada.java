package agentes.comportamientos;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class MiBehaviour extends Behaviour {

    private boolean termine = false;
    private int event = 1;
    private String currentAgent;
    private String senderName;

    @Override
    public void action() {

        ACLMessage msgReceived = myAgent.receive(MessageTemplate.MatchAll());

        if (msgReceived == null) {
            block();
        } else {
            currentAgent = myAgent.getName().split("@")[0];
            senderName = msgReceived.getSender().getName().split("@")[0];

            if (msgReceived.getPerformative() == ACLMessage.PROPOSE) {
                System.out.printf("%s --> recibió PROPOSE de %s\n", currentAgent, senderName);

                if (Math.random() > 0.5d) {
                    ACLMessage msgSend = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    msgSend.addReceiver(msgReceived.getSender());
                    myAgent.send(msgSend);
                    System.out.println(currentAgent + " --> envía ACCEPT_PROPOSAL");
                } else {
                    ACLMessage msgSend = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                    msgSend.addReceiver(msgReceived.getSender());
                    myAgent.send(msgSend);
                    System.out.println(currentAgent + " --> envía REJECT_PROPOSAL");
                }
            }

            if (msgReceived.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                System.out.println(currentAgent + " --> recibió ACCEPT_PROPOSAL");
                termine = true;
                event = 0;
            }

            if (msgReceived.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
                System.out.println(currentAgent + " --> recibió REJECT_PROPOSAL");

                ACLMessage msgSend = new ACLMessage(ACLMessage.PROPOSE);
                msgSend.addReceiver(msgReceived.getSender());
                myAgent.send(msgSend);
                System.out.println(currentAgent + " --> envía PROPOSE");

                event = 1;
            }
        }
    }

    @Override
    public boolean done() {
        return termine;
    }

    @Override
    public int onEnd() {
        System.out.println(currentAgent + " --> finalizó la ejecución");
        return event;
    }

    @Override
    public void reset() {
        termine = false;
    }
}
