/*
 * package com.mongodb.demo.controller;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.mongodb.demo.entity.Employee; import
 * com.mongodb.demo.entity.MyBean; import
 * com.mongodb.demo.repository.EmployeeRepository; import
 * com.mongodb.demo.service.MyStatusChecker;
 * 
 * @RestController public class EmployeeController {
 * 
 * @Autowired private EmployeeRepository employeeRepository;
 * 
 * @Autowired private MyBean myBean;
 * 
 * @Autowired private MyStatusChecker myStatusChecker;
 * 
 * @GetMapping("/save") public String saveEmployee() throws InterruptedException
 * {
 * 
 * 
 * System.out.println("starting ");
 * myStatusChecker.startCheckingStatus("1",1500l);
 * 
 * System.out.println("done");
 * 
 * 
 * return "Employee is saved successfully"; }
 * 
 * @GetMapping("/id/{id}") public Optional<Employee>
 * getEmployee(@PathVariable("id") long employeeId) {
 * 
 * return employeeRepository.findById(employeeId); }
 * 
 * @GetMapping("/details") public List<Employee> getAllEmployee() {
 * 
 * 
 * return employeeRepository.findAll(); }
 * 
 * @DeleteMapping("/delete/{id}") public String
 * deleteEmployee(@PathVariable("id") long employeeId) {
 * employeeRepository.deleteById(employeeId); return
 * "The requested employee id " + employeeId + " is deleted successfully"; } }
 */