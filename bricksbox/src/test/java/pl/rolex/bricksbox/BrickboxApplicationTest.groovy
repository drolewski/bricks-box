package pl.rolex.bricksbox

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BrickboxApplicationTest extends Specification {

    def "SpringBoot application context started"() {
        given:
        def two = 2

        when:
        def result = two * 2

        then:
        result == 4
    }
}
