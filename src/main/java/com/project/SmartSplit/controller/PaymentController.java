package com.project.SmartSplit.controller;

import com.project.SmartSplit.dto.PaymentDTO;
import com.project.SmartSplit.repository.GroupRepository;
import com.project.SmartSplit.service.PaymentService;
import com.project.SmartSplit.entity.Group;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final GroupRepository groupRepository;

    public PaymentController(PaymentService paymentService, GroupRepository groupRepository) {
        this.paymentService = paymentService;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentDTO getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping("/{groupId}")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO, @PathVariable Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);

        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            try {
                PaymentDTO createdPayment = paymentService.createPayment(paymentDTO, group);
                return ResponseEntity.ok().body(createdPayment);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment creation failed");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
    }

    @PutMapping("/{id}")
    public PaymentDTO updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        paymentDTO.setId(id);
        return paymentService.updatePayment(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByGroupId(@PathVariable Long groupId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByGroupId(groupId);
        return ResponseEntity.ok(payments);
    }
}
