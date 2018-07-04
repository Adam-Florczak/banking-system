//package banking.system.exchangerate;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.Map;
//import java.util.Set;
//
//class CurrencyApi {
//
//    public BigDecimal getRatio(String currency) throws IOException {
//
//        String address = "https://free.currencyconverterapi.com/api/v5/convert?q=" + currency + "_PLN&compact=ultra";
//        RestTemplate restTemplate = new RestTemplate();
//
//        String s = restTemplate.getForObject(address, String.class);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        Map<String, BigDecimal> map;
//        BigDecimal ratio = BigDecimal.ZERO;
//        map = mapper.readValue(s, new TypeReference<Map<String, BigDecimal>>() {
//        });
//
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            ratio = map.get(key);
//        }
//
//        return ratio;
//
//    }
//
//}
