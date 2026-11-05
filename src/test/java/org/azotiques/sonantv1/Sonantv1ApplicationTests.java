package org.azotiques.sonantv1;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class Sonantv1ApplicationTests {

    @Test
    void verifiesModuleStructure() {
        ApplicationModules
                .of(Sonantv1Application.class)
                .verify();
    }

}
