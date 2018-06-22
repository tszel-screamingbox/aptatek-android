package com.aptatek.aptatek.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

// TODO english comments?

/**
 * @test.layer melyik retegebe tartozik az arhitekturanak
 * @test.feature melyik featurehoz tartozik a test
 * @test.type teszt tipusa
 */
public class ExampleTest {

    /**
     * mit initelsz, es miert initeled
     * @throws Exception milyen exceptionok dobodhatnak, honnan, miert
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * par szoban, hogy mit fog csinalni a teszt
     * @test.input adatok, melyeken a test dolgozni fog
     * @test.expected az adatokon torteno operacio eredmenye
     */
    @Test
    public void test1(){

    }

    /**
     * ket szam osszeadasanak tesztelese
     * @test.input a = 1 , b = 2
     * @test.expected teszt vegeredmenye 3
     */
    @Test
    public void test2(){

    }
}
