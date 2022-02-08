package space.gavinklfong.demo.streamapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
@ResponseBody
public class ProductController {

    @Autowired
    ProductRepo repo;

    @RequestMapping("/")
    public String home(){
        return "Hi this is the product's page";
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity deleteProduct(@RequestParam(name="id",defaultValue = "-1")String id){
        Long longID= Long.parseLong(id);

        if (id==null || id.isEmpty() || longID<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
           if (!repo.existsById(longID)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
            repo.deleteById(longID);
            if (repo.existsById(longID)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be deleted");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Product with ID: "+id + "deleted");
        }
    }

    @PostMapping("/updateProduct")
    public ResponseEntity updateProduct(@RequestBody Product input){
        if (input.getId()==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        if (!repo.existsById(input.getId())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
        Product updated=repo.save(input);
        if (updated!=null) return ResponseEntity.status(HttpStatus.OK).body(updated);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be updated");
    }

    @PutMapping("/insertProduct")
    public ResponseEntity insertProduct(@RequestBody Product input){
    if (input!=null){
        Product product=repo.save(input);
        if (product!=null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be saved "+product);
    }
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product info is wrong");

    }

    @GetMapping("/product")
    public ResponseEntity getProduct(@RequestParam(name="id", defaultValue = "-1")String id){
        if (id==null || id.isEmpty() || Long.parseLong(id)<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
            Optional<Product> product = repo.findById(Long.parseLong(id));

            if (!product.isPresent()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
            }
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return repo.findAll();
    }
}
