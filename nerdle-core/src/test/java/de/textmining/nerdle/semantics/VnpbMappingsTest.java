package de.textmining.nerdle.semantics;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class VnpbMappingsTest {

    private static VnpbMappings vnpbMappings;

    @BeforeClass
    public static void setup() {
        vnpbMappings = VnpbMappings.getInstance();
    }

    @Test
    public void test(){
        System.out.println(vnpbMappings.getSynonyms("snore.01"));        
    }
    
    @Test
    public void testMappings() {
        // same verbnet class 11.1-1
        assertTrue(vnpbMappings.getSynonyms("FedEx.01").contains("UPS.01"));

        // same propbank roleset should not be in the sysnonyms list
        assertFalse(vnpbMappings.getSynonyms("FedEx.01").contains("FedEx.01"));

        // snore.01 => verbnet class 40.1.1
        assertTrue(vnpbMappings.getSynonyms("snore.01").contains("belch.01"));
        assertTrue(vnpbMappings.getSynonyms("belch.01").contains("snore.01"));

        // snore.01 => verbnet class 40.2
        assertTrue(vnpbMappings.getSynonyms("snore.01").contains("beam.02"));
        assertTrue(vnpbMappings.getSynonyms("beam.02").contains("snore.01"));
    }

}
