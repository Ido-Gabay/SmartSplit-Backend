package com.project.SmartSplit.service;

import com.project.SmartSplit.dto.PaymentDTO;
import com.project.SmartSplit.entity.Group;
import com.project.SmartSplit.entity.Payment;
import com.project.SmartSplit.repository.GroupRepository;
import com.project.SmartSplit.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GroupRepository groupRepository;

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPrice(payment.getPrice());
        dto.setPlace(payment.getPlace());
        dto.setPaySuccess(payment.isPaySuccess()); // Use the existing setter method
        if (payment.getGroup() != null) {
            dto.setGroupId(payment.getGroup().getId());
        }
        return dto;
    }


    private Payment mapToEntity(PaymentDTO dto, Group group) {
        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setPrice(dto.getPrice());
        payment.setPlace(dto.getPlace());
        payment.setGroup(group);
        return payment;
    }

    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public PaymentDTO getPaymentById(Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        return paymentOptional.map(this::mapToDTO).orElse(null);
    }

    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO, Group group) {
        Payment payment = mapToEntity(paymentDTO, group);
        int paymentAmount = payment.getPrice();

        if (paymentAmount <= group.getMoneyAmount()) {
            payment.setPaySuccess(true);
            int updatedMoneyAmount = group.getMoneyAmount() - paymentAmount;
            group.setMoneyAmount(updatedMoneyAmount);
            groupRepository.save(group);
            paymentRepository.save(payment);

            // Fetch the updated payment from the database
            Payment updatedPayment = paymentRepository.findById(payment.getId()).orElse(null);

            if (updatedPayment != null && updatedPayment.isPaySuccess()) {
                paymentDTO.setGroupId(group.getId());
                return mapToDTO(updatedPayment);
            } else {
                System.out.println("Payment creation failed");
                throw new RuntimeException("Payment creation failed");
            }
        } else {
            payment.setPaySuccess(false);
            System.out.println("Not enough money in the group");
            throw new IllegalArgumentException("Not enough money in the group");
        }
    }


    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentDTO.getId());
        if (optionalPayment.isPresent()) {
            Payment paymentToUpdate = optionalPayment.get();
            paymentToUpdate.setPrice(paymentDTO.getPrice());
            paymentToUpdate.setPlace(paymentDTO.getPlace());
            paymentToUpdate.setPaySuccess(paymentDTO.isPaySuccess());
            Payment updatedPayment = paymentRepository.save(paymentToUpdate);
            return mapToDTO(updatedPayment);
        }
        return null;
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public List<PaymentDTO> getPaymentsByGroupId(Long groupId) {
        List<Payment> payments = paymentRepository.findByGroupId(groupId);
        return payments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
