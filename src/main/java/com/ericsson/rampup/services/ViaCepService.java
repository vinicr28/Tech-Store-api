package com.ericsson.rampup.services;

import com.ericsson.rampup.dto.AddressDTO;
import com.ericsson.rampup.dto.ViaCepDTO;
import com.ericsson.rampup.entities.Address;
import com.google.gson.Gson;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ViaCepService {

    @Autowired
    private AddressService addressService;

    public Address getViaCep(String cep, int numero, Long customerId) throws IOException, ParseException {

        ViaCepDTO viaCepDTO = null;

        HttpGet request = new HttpGet("https://viacep.com.br/ws/"+ cep +"/json/");

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().disableRedirectHandling().build();
            CloseableHttpResponse response = httpClient.execute(request)){

            HttpEntity entity = response.getEntity();

            if(entity != null){
                String result = EntityUtils.toString(entity);

                Gson gson = new Gson();

               viaCepDTO = gson.fromJson(result, ViaCepDTO.class);
               viaCepDTO.setNumero(numero);
               viaCepDTO.setCustomerId(customerId);
            }

        }

        AddressDTO addressDTO = fromDtoToDto(viaCepDTO);
        addressDTO.setZipCode(Integer.parseInt(cep));
        return addressService.insert(addressDTO);
    }

    public AddressDTO fromDtoToDto(ViaCepDTO objDTO){
        return new AddressDTO(objDTO);
    }

}
