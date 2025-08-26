package serveasy.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import serveasy.model.Mesa;

public class MesaTest {

    @Test
    public void testValorGasto() {
        Mesa mesa = new Mesa(1, 101, false, 0.0f, false);

        // Testa valor inicial
        assertEquals(0.0f, mesa.getValorGasto());

        // Atualiza valor gasto
        mesa.setValorGasto(150.50f);
        assertEquals(150.50f, mesa.getValorGasto());
    }

    @Test
    public void testOcupada() {
        Mesa mesa = new Mesa(1, 101, false, 0.0f, false);

        // Testa valor inicial
        assertFalse(mesa.isOcupada());

        // Altera para ocupada
        mesa.setOcupada(true);
        assertTrue(mesa.isOcupada());
    }
}
