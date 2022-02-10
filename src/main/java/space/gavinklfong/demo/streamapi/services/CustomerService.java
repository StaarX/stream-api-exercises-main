package space.gavinklfong.demo.streamapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo repo;

    public ResponseEntity insert(Customer input){
        if (input!=null){
            //Business validations
            if (input.getTier()==null || input.getTier()>2 || input.getTier()<0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer can only be tier 0,1 or 2");

            Customer customer=repo.save(input);
            if (customer!=null){
                return ResponseEntity.status(HttpStatus.OK).body(customer);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer could not be saved "+customer);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer info is wrong");
    }
    public ResponseEntity update(Customer input){
        if (input.getId()==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        if (!repo.existsById(input.getId())) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer does not exist");
        //Business validations
        if (input.getTier()==null || input.getTier()>2 || input.getTier()<0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer can only be tier 0,1 or 2");

        Customer updated=repo.save(input);
        if (updated!=null) return ResponseEntity.status(HttpStatus.OK).body(updated);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer could not be updated");
    }
    public ResponseEntity delete(Long id){
        if (id==null || id<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
            if (!repo.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer does not exist");
            repo.deleteById(id);
            if (repo.existsById(id)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer could not be deleted");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Customer with ID: "+id + "deleted");
        }
    }
    public ResponseEntity get(Long id){
        if (id==null || id<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        }else{
            Optional<Customer> customer = repo.findById(id);

            if (!customer.isPresent()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer does not exist");
            }
            return ResponseEntity.status(HttpStatus.OK).body(customer);
        }
    }
    public List<Customer> getAll(){
        return repo.findAll();
    }

}
