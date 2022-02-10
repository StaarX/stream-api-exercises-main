package space.gavinklfong.demo.streamapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.services.CustomerService;

import java.util.List;

@Controller
@RequestMapping("/customer")
@ResponseBody
public class CustomerController {
    @Autowired
    CustomerService service;

    @RequestMapping("/")
    public String home(){
        return "Hi this is the customer's page";
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteCustomer(@RequestParam(name="id",defaultValue = "-1")String id){
        if (id==null || id.isBlank()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        return service.delete(Long.parseLong(id));
    }

    @PutMapping("/update")
    public ResponseEntity updateCustomer(@RequestBody Customer input){
        return service.update(input);
    }

    @PostMapping("/insert")
    public ResponseEntity insertCustomer(@RequestBody Customer input){
        return service.insert(input);
    }

    @GetMapping("/customer")
    public ResponseEntity getCustomer(@RequestParam(name="id", defaultValue = "-1")String id){
        if (id==null || id.isBlank()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID, please check");
        return service.get(Long.parseLong(id));
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers(){
        return service.getAll();
    }
}
