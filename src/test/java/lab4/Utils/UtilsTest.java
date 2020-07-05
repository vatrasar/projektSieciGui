package lab4.Utils;

import lab4.Dane;
import lab4.debug.Debug;
import lab4.debug.DebugSingSol;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @org.junit.jupiter.api.Test
    void convertBinaryForLong() {
        assert 5==Utils.convertBinaryForLong("101");
        assert 21==Utils.convertBinaryForLong("10101");
    }
}