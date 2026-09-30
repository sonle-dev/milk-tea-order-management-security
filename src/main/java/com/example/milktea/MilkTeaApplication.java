package com.example.milktea;

import com.example.milktea.entity.Product;
import com.example.milktea.entity.User;
import com.example.milktea.repository.ProductRepository;
import com.example.milktea.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class MilkTeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilkTeaApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      ProductRepository productRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Tạo Khách hàng (ROLE_CUSTOMER)
            if (userRepository.findByEmail("customer@milktea.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("customer@milktea.com")
                        .password(passwordEncoder.encode("customer123"))
                        .phone("0912345678")
                        .role("ROLE_CUSTOMER")
                        .build());
            }

            // 2. Tạo Nhân viên (ROLE_STAFF)
            if (userRepository.findByEmail("staff@milktea.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("staff@milktea.com")
                        .password(passwordEncoder.encode("staff123"))
                        .phone("0987654321")
                        .role("ROLE_STAFF")
                        .build());
            }

            // 3. Tạo Quản trị viên (ROLE_ADMIN)
            if (userRepository.findByEmail("admin@milktea.com").isEmpty()) {
                userRepository.save(User.builder()
                        .email("admin@milktea.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("0900000000")
                        .role("ROLE_ADMIN")
                        .build());
            }

            // 4. Tạo sẵn 2 sản phẩm có ID: 1 và ID: 2
            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        Product.builder()
                                .name("Trà Sữa Trân Châu Đường Đen")
                                .description("Vị ngọt thanh từ sữa tươi và đường đen")
                                .price(new BigDecimal("35000"))
                                .size("L")
                                .toppings("Trân châu đường đen")
                                .build(),
                        Product.builder()
                                .name("Trà Oolong Kem Phô Mai")
                                .description("Trà đậm vị hòa quyện lớp kem béo ngậy")
                                .price(new BigDecimal("42000"))
                                .size("M")
                                .toppings("Kem cheese")
                                .build()
                ));
            }
        };
    }
}