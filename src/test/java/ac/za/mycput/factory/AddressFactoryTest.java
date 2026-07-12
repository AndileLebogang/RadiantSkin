package ac.za.mycput.factory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
/*
*Name: Siphokazi Malingatshoni
* Student Number: 222868708
 */
public class AddressFactoryTest {

    @Test
    void testcreateAddress(){

        Address address = AddressFactory.createAddress(
                1L,
                "12 Main Road",
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                customer
        );
        assertNotNull(address);
        assertEquals("12 Main Road",address.getStreet());
        assertEquals("12 Main Road",address.getCity());

    }
    @Test
    void testcreateAddressWithNullStreet(){

        Customer customer = new Customer("C002","Siphokazi",null);

        Address address = AddressFactory.createAdddress(
                1L,
                null,
                "Cape Town",
                "Western Cape",
                "8000",
                "South Africa",
                customer
        );
        assertNull(address);
    }
     @Test
    void testcreateAddressWithEmptyCity(){

         Customer customer = new Customer("C002","Siphokazi",null);

         Address address = AddressFactory.createAdddress(
                 1L,
                 null,
                 "Cape Town",
                 "",
                 "8000",
                 "South Africa",
                 customer
         );
     }
}
