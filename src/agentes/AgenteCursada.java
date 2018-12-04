package agentes;

import agentes.comportamientos.MiBehaviour;
import jade.core.Agent;

public class MiAgente extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new MiBehaviour());
    }
}
