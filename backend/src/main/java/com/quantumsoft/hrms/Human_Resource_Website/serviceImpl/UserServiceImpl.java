package com.quantumsoft.hrms.Human_Resource_Website.serviceImpl;

import com.quantumsoft.hrms.Human_Resource_Website.entity.Otp;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Role;
import com.quantumsoft.hrms.Human_Resource_Website.enums.Status;
import com.quantumsoft.hrms.Human_Resource_Website.entity.User;
import com.quantumsoft.hrms.Human_Resource_Website.exception.ResourceNotFoundException;
import com.quantumsoft.hrms.Human_Resource_Website.repository.AuditLogRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.OtpRepository;
import com.quantumsoft.hrms.Human_Resource_Website.repository.UserRepository;
import com.quantumsoft.hrms.Human_Resource_Website.security.JwtTokenUtil;
import com.quantumsoft.hrms.Human_Resource_Website.service.AuditLogService;
import com.quantumsoft.hrms.Human_Resource_Website.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.quantumsoft.hrms.Human_Resource_Website.enums.Role.ADMIN;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;




     @Override
        public void createUser (User user){
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Email already exists.");
            }
            String rawPassword = "Pass@123";
            user.setCreatedAt(LocalDateTime.now());
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setStatus(Status.ACTIVE);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

         sendCredentialsEmail(user.getEmail(), user.getUsername(), rawPassword);

     }

    private void sendCredentialsEmail(String toEmail, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your HRMS Account Details");
        message.setText("Dear " + username + ",\n\n"
                + "Your HRMS account has been created successfully.\n\n"
                + "Username: " + username + "\n"
                + "Password: " + password + "\n\n"
                + "Please log in and change your password after first login.\n\n"
                + "Regards,\nHRMS Team");

        mailSender.send(message);
    }

    @Override
    public String login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsernameCaseSensitive(username);

        // case 1: username not found
        if (userOpt.isEmpty()) {
            if (!passwordEncoder.matches(password, "dummyHashValue")) {
                // case 3: both wrong
                throw new RuntimeException(" username and password are incorrect");
            }
            throw new RuntimeException("Username is incorrect");
        }

        User user = userOpt.get();

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Account is INACTIVE");
        }

        // case 2: password wrong
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        if (!user.getRole().equals(ADMIN)) {
            boolean hasChangedPassword = auditLogRepository.existsByUsernameAndAction(username, "PASSWORD_CHANGE");
            if (!hasChangedPassword) {
                throw new RuntimeException("You must reset your password before first login.");
            }
        }

        userRepository.updateLoginTimestamps(user.getUserID(), LocalDateTime.now(), LocalDateTime.now());
        auditLogService.logEvent(user.getUsername(), "LOGIN");

        Map<String, Object> claims = Map.of("role", user.getRole().name());
        return jwtTokenUtil.generateToken(user.getUsername(), claims);
    }


    @Override
    public void sendOtp (String email){
        User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Optional<Otp> existingOtpOpt = otpRepository.findByEmail(email);
        if (existingOtpOpt.isPresent()) {
            Otp existingOtp = existingOtpOpt.get();
            if (existingOtp.getExpiry().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("An active OTP already exists. Please wait for 5 minutes before requesting a new one.");
            } else {
                otpRepository.delete(existingOtp);  // Clean up expired OTP
            }
        }

        String otp = generateOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        Otp otpRecord = new Otp();
        otpRecord.setOtp(otp);
        otpRecord.setExpiry(expiry);
        otpRecord.setEmail(email);
        otpRepository.save(otpRecord);
        sendOtpEmail(email, otp);

       auditLogService.logEvent(user.getUsername(), "OTP_SENT");
    }

     @Override
        public void resetPassword (String email, String otp, String newPassword){

         if (!newPassword.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,16}$")) {
             throw new IllegalArgumentException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character, and be 8–16 characters long");
         }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            Otp otpRecord = otpRepository.findByEmailAndOtp(email, otp)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid OTP."));

            if (otpRecord.getExpiry().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("OTP has expired.");
            }

         String trimmedPassword = newPassword.trim();

            user.setPassword(passwordEncoder.encode(trimmedPassword));
            userRepository.save(user);

            otpRepository.delete(otpRecord);

            auditLogService.logEvent(user.getUsername(), "PASSWORD_CHANGE");
        }

    @Override
    public User roleChangeByEmail(String email, Role newRole) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    private String generateOtp () {
            Random random = new Random();
            int otpNumber = 100000 + random.nextInt(900000);
            return String.valueOf(otpNumber);
    }

    private void sendOtpEmail (String toEmail, String otp){

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your HRMS OTP Code");
            message.setText("Your OTP is: " + otp + "\nIt is valid for 5 minutes.");

            mailSender.send(message);
    }

    @Override
        public List<User> getUsersByRole (Role role){
            return userRepository.findByRole(role);
    }

    @Override
        public void updateStatus (Long userID, Status status){
        if (userID == 1L) {
            throw new IllegalArgumentException("Cannot update status for Admin user.");
        }// add in front end
            User user = userRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User not found with userId: " + userID));
            user.setStatus(status);
            userRepository.save(user);

    }



}