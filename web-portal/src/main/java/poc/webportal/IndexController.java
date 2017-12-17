package poc.webportal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @CrossOrigin
    @RequestMapping("/")
    public String index(Model model) {
        log.info("Index page requested");
        ServiceInstance instance = discoveryClient.getInstances("product-service").stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No available 'product-service' instance found."));

        ResponseEntity<List<Product>> productEntities = restTemplate.exchange(instance.getUri().toString() + "/products",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
        List<Product> products = new ArrayList<>(productEntities.getBody());

        model.addAttribute("products", products);

        return "index";
    }
}
