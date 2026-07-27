package ac.za.mycput.controller;

/*
//Name: Siphokazi Malingatshoni
// Student number: 222868708
 */

import ac.za.mycput.domain.Address;
import ac.za.mycput.factory.AddressFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
class AddressControllerTest {

    @Autowired
    private TestRestTemplate restTemplate ;

    private Address address;
    private String baseUrl;

    @BeforeEach
    void setUp(){
        address = AddressFactory.createAddress (1L,"Street","Cape Town","Western Cape","8001","South Africa");
        baseUrl = "/address";
    }
   @Test
    void create (){

        ResponseEntity<Address>response =
                restTemplate.postForEntity(baseUrl + "/create", address, Address.class);

        assertNotNull(response);
        assertNotNull(response.getBody);
        assertEquals(200,response.getStatusCodeValue());

    }
     @Test
      void read(){

         ResponseEntity<Address>createResponse =
                 restTemplate.postForEntity(baseUrl + "/create", address, Address.class);

         Long id = createResponse.getBody().getId();

         ResponseEntity<Address>response =
                 restTemplate.postForEntity(baseUrl + "/read", id, Address.class);

         assertNotNull(response);
         assertNotNull(response.getBody());

     }

      @Test
       void update(){

        restTemplate.postForEntity(baseUrl + "/create", address, Address.class);

          address.setCity("Johannesburg");

          restTemplate.put(baseUrl + "/update",address );

          ResponseEntity<Address>response =
                  restTemplate.postForEntity(baseUrl + "/read", address.getAddressId(), Address.class);

          assertEquals("Johannesburg", response.getBody(),getCity());
      }

      @Test
       void delete(){

          ResponseEntity<Address>createResponse =
                  restTemplate.postForEntity(baseUrl + "/create", address, Address.class);

          Long id = createResponse.getBody().getId();

          restTemplate.delete(baseUrl + "/delete"+ id);
          ResponseEntity<Address>response =
                  restTemplate.postForEntity(baseUrl + "/read", id, Address.class);

          assertNull(response.getBody());
      }
      @Test
      void getAll(){
          restTemplate.postForEntity(baseUrl + "/create", address, Address.class);

          ResponseEntity<Address[]>response =
                  restTemplate.postForEntity(baseUrl + "/all", Address[].class);

          assertNotNull(response);
          assertTrue(Arrays.asList(response.getBody()).size() > 0);
      }
}
