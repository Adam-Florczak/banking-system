package banking.system.exchangerate;

import banking.system.common.Currency;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CurrencyApi {

    private final String API_KEY = "e81a4ece541e45318b896a7697c2e673";



    public BigDecimal getRatio(String from, String to) throws IOException {


        String address = "https://raw.githubusercontent.com/maciejkrolpl/temp_curr_api/master/json.html";
        String findKey = from.concat("_").concat(to);


        RestTemplate restTemplate = new RestTemplate();

        String s = restTemplate.getForObject(address, String.class);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, BigDecimal> map = mapper.readValue(s, new TypeReference<Map<String, BigDecimal>>() {
        });


        return map.get(findKey);


    }

}
