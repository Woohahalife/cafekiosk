package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test
    void add() {
        CafeKiosk kiosk = new CafeKiosk();
        kiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + kiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료 : " + kiosk.getBeverages().get(0).getName());
    }
}