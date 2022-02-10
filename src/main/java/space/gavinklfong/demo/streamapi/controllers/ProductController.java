package space.gavinklfong.demo.streamapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.services.ProductService;

import java.util.List;

@Controller
@RequestMapping("/product")
@ResponseBody
public class ProductController {

    @Autowired
    ProductService service;

    @RequestMapping("/")
    public String home(){
        return "Hi this is the product's page";
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity deleteProduct(@RequestParam(name="id",defaultValue = "-1")String id){
        if (id==null || id.isBlank()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        return service.deleteProduct(Long.parseLong(id));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity updateProduct(@RequestBody Product input){
        return service.updateProduct(input);
    }

    @PostMapping("/insertProduct")
    public ResponseEntity insertProduct(@RequestBody Product input){
        return service.insertProduct(input);
    }

    @GetMapping("/product")
    public ResponseEntity getProduct(@RequestParam(name="id", defaultValue = "-1")String id){
        if (id==null || id.isBlank()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        return service.getProduct(Long.parseLong(id));
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return service.getProducts();
    }
}
