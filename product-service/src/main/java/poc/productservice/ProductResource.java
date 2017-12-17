package poc.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/products")
public class ProductResource {

    private static final Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Value("${discountPercent}")
    private double discountPercent;

    @Value("${active}")
    private boolean active;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getAll() {
        if (!active) {
            log.info("Product list not available");
            return new ArrayList<>();
        }

        // Just return an in-memory list:
        List<Product> products = Arrays.asList(
                new Product("100", "Bread", 2.5),
                new Product("101", "Milk", 3.5),
                new Product("102", "Greek Salad", 8.5),
                new Product("103", "Water", 0.49)
        );

        List<Product> resultProducts = new ArrayList<>();
        for (Product product : products) {
            resultProducts.add(new Product(product.getId(), product.getName(), product.getPrice() * discountPercent / 100));
        }
        log.info("Product list retrieved");
        return resultProducts;
    }
}
