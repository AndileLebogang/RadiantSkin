package ac.za.mycput.factory;

import ac.za.mycput.util.Helper;
/*
* Name: Siphokazi Malingatshoni
* Student number: 222868708
*
 */
public class AddressFactory {

    public static Address createAddress(Long addressId, String street, String city,
                                        String province, String postalCode,
                                        String country, Customer customer){

        if (!Helper.isValidId(addressId)||
           Helper.isNullEmpty(street)||
           Helper.isNullEmpty(city)||
           Helper.isNullEmpty(province)||
           Helper.isNullEmpty(postalCode)||
           Helper.isNullEmpty(country)||
           customer ==null ) {
            return null;
        }
            return new Address.Builder()
                .setAddressId(addressId)
                .setStreet(street)
                .setCity(city)
                .setProvince(province)
                .setPostalCode(postalCode)
                .setCountry(country)
                .build();


    }

}


