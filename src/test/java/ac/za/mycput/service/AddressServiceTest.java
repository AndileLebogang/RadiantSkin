package ac.za.mycput.service;

import ac.za.mycput.domain.Address;
import ac.za.mycput.factory.AddressFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class AddressServiceTest {

   @Test
   void create(){
       Address address = AddressFactory.createAddress(1L, "Street","City","1234", null);
       assertNotNull(address);
   }




}
