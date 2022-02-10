package space.gavinklfong.demo.streamapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo repo;

    public ResponseEntity deleteProduct(Long id){

        if (id==null || id<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
            if (!repo.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
            repo.deleteById(id);
            if (repo.existsById(id)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be deleted");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Product with ID: "+id + "deleted");
        }
    }
    public ResponseEntity updateProduct(Product product){
        if (product.getId()==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        if (!repo.existsById(product.getId())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
        Product updated=repo.save(product);
        if (updated!=null) return ResponseEntity.status(HttpStatus.OK).body(updated);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be updated");
    }
    public ResponseEntity insertProduct(Product input){
        if (input!=null){
            Product product=repo.save(input);
            if (product!=null){
                return ResponseEntity.status(HttpStatus.OK).body(product);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product could not be saved "+product);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product info is wrong");

    }
    public ResponseEntity getProduct(Long id){
        if (id==null || id<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
            Optional<Product> product = repo.findById(id);

            if (!product.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product does not exist");
            }
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
    }
    public List<Product> getProducts(){
        return repo.findAll();
    }
}
